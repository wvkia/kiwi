package com.wvkia.tools.kiwi.tools.recordlog;

import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wukai
 * 日志解析工具类
 */
public class ParseLogUtils {

    /**
     * 用于list显示分割，例如A，B，C
     */
    private static final String SPLIT = ",";
    /**
     * 表示指示值，例如 A：B
     */
    private static final String SEPARATOR = ":";

    private ParseLogUtils(){}


    /**
     * 解析数据统一入口
     * @param value
     * @return
     */
    public static String parseObjectValue(Object value) {

        return ParseSingle.parseValue(value);
    }

    public static String parseObjectValue(Object before, Object after) {
        return ParseDouble.parseValue(before, after);
    }

    /**
     * 获取一个类的可用fields类型
     * @param clazz
     * @return
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        Set<Field> fieldSet = new HashSet<>();
        fieldSet.addAll(Lists.newArrayList(clazz.getDeclaredFields()));
        fieldSet.addAll(Lists.newArrayList(clazz.getFields()));
        return Lists.newArrayList(fieldSet);
    }

    /**
     * 域字段上是否含有LogFiled注解
     * @param field
     * @return
     */
    private static boolean isAnnotatedByLogField(Field field) {
        boolean isLogFieldAnno = false;
        if (null == field) {
            return isLogFieldAnno;
        }

        LogField logField = field.getAnnotation(LogField.class);
        if (null != logField) {
            isLogFieldAnno=true;
        }
        return isLogFieldAnno;
    }

    private static Object getValueByInstance(Field field,Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 是否是基础类型
     * @param object
     * @return
     */
    private static boolean isPrimitive(Object object) {
        Class<?> clazz = object.getClass();
        boolean isPrimitive = false;
        if (clazz.isPrimitive()) {
            isPrimitive = true;
        }
        if (Primitives.isWrapperType(clazz)) {
            isPrimitive = true;
        }
        if (clazz == String.class) {
            isPrimitive = true;
        }
        if (object instanceof Enum) {
            isPrimitive = true;
        }
        return isPrimitive;
    }



    /**
     * 是否是集合类型
     * @param object
     * @return
     */
    private static boolean isCollection(Object object) {
        boolean isCollection = false;

        if (object instanceof Collection) {
            isCollection = true;
        }

        return isCollection;
    }


    private static boolean isEqual(Object obj, Object after) {
        if (null == obj && null == after) {
            return true;
        }
        if ((null == obj && null != after) || (null != obj && null == after)) {
            return false;
        }
        if (obj.getClass() != after.getClass()) {
            return false;
        }
        if (isPrimitive(obj)) {
            return obj.equals(after);
        }
        if (isCollection(obj)) {
            Collection collectionObj = (Collection) obj;
            Collection collectionAfter = (Collection) after;
            return CollectionUtils.isEqualCollection(collectionObj, collectionAfter);
        }

        Class<?> clazz = obj.getClass();
        boolean isEqual = true;
        List<Field> fieldList = getAllFields(clazz);

        for (Field field : fieldList) {
            if (!isAnnotatedByLogField(field)) {
                continue;
            }
            field.setAccessible(true);
            Object objValue = getValueByInstance(field, obj);
            Object afterValue = getValueByInstance(field, after);
            if (!isEqual(objValue, afterValue)) {
                isEqual=false;
                break;
            }
        }
        return isEqual;

    }


    /**
     * 静态内部类
     * 解析单个对象LogField域
     */
    private static class ParseSingle{
        /**
         * 解析单个值引用
         * 名称:xx,性别:xx,列表:[1,2,3],
         * @param object
         * @return
         */
        private static String parseReference(Object object) {
            StringBuilder stb = new StringBuilder();
            if (null == object) {
                return stb.toString();
            }
            Class<?> clazz = object.getClass();
            List<Field> fieldList = getAllFields(clazz);

            for (Field field : fieldList) {
                if (isAnnotatedByLogField(field)) {
                    LogField logField = field.getAnnotation(LogField.class);

                    field.setAccessible(true);

                    //获取字段值
                    Object logValue = getValueByInstance( field, object);
                    String valueStr = parseValue(logValue);
                    if (StringUtils.isEmpty(valueStr)) {
                        continue;
                    }

                    stb.append(logField.value())
                            .append(SEPARATOR)
                            .append(valueStr)
                            .append(SPLIT);
                }
            }
            if (StringUtils.endsWith(stb.toString(), SPLIT)) {
                stb.deleteCharAt(StringUtils.lastIndexOf(stb.toString(), SPLIT));
            }
            return stb.toString();
        }
        /**
         * 解析集合值value
         * @param value
         * @return
         */
        private static List<String> parseCollection(Object value){
            Collection collection = null;
            if (isCollection(value)) {
                collection = (Collection) value;
            }
            if (null == collection) {
                return null;
            }
            List<String> objectList = Lists.newArrayList();
            for (Object o : collection) {
                String val = parseValue(o);
                objectList.add(val);
            }
            return objectList;
        }
        private static String parsePrimitive(Object object) {
            return String.valueOf(object);
        }
        /**
         *
         * @param value
         * @return
         */
        private static String parseValue(Object value) {
            StringBuilder stb = new StringBuilder();
            if (null == value) {
                return stb.toString();
            }

            //基础类型
            if (isPrimitive(value)) {
                return parsePrimitive(value);
            }
            //集合类型
            if (isCollection(value)) {
                List<String> collectionValues = parseCollection(value);
                if (CollectionUtils.isNotEmpty(collectionValues)) {
                    stb.append("[");
                    collectionValues.forEach(item ->{
                        stb.append("(").append(item).append(")").append(SPLIT);
                    });
                    if (StringUtils.endsWith(stb.toString(), SPLIT)) {
                        stb.deleteCharAt(StringUtils.lastIndexOf(stb.toString(), SPLIT));
                    }
                    stb.append("]");
                }
                return stb.toString();
            }

            //如果不是基础类型和集合类型，确定为引用对象
            return parseReference(value);
        }
    }

    private static class ParseDouble{
        /**
         * 解析对比值格式
         * 名称：修改前值：1 --> 修改后值：2，性别：修改前值：男 --> 修改后值：女，列表：修改前值：[名称(id)、名称(id)] --> 修改后值：
         * @param before
         * @param after
         * @return
         */
        public static String parseValue(Object before,Object after) {
            StringBuilder stb = new StringBuilder();
            if (isEqual(before,after)) {
                return stb.toString();
            }

            Class<?> clazz = before.getClass();
            List<Field> fieldList = getAllFields(clazz);

            for (Field field : fieldList) {
                if (isAnnotatedByLogField(field)) {
                    LogField logField = field.getAnnotation(LogField.class);

                    field.setAccessible(true);

                    //获取各个字段值
                    Object beforeValue = getValueByInstance(field, before);
                    Object afterValue = getValueByInstance(field, after);

                    //如果两个值相同，退出
                    if (isEqual(beforeValue, afterValue)) {
                        continue;
                    }


                    Object beforeLogValue = ParseSingle.parseValue(beforeValue);
                    Object afterLogValue = ParseSingle.parseValue(afterValue);
                    if (isEqual(beforeLogValue,afterLogValue)) {
                        continue;
                    }
                    if (StringUtils.isNotEmpty(stb.toString())) {
                        stb.append(SPLIT);
                    }

                    stb.append(logField.value())
                            .append(SEPARATOR)
                            .append("修改前值:")
                            .append(beforeLogValue)
                            .append("  -->  ")
                            .append("修改后:")
                            .append(afterLogValue)
                            .append("\n");
                }
            }
            return stb.toString();
        }
    }


    /**
     * 但愿测试
     * @param args
     */
    public static void main(String[] args) {


    }
}
