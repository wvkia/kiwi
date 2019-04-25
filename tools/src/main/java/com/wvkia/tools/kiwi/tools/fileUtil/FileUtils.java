package com.wvkia.tools.kiwi.tools.fileUtil;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @author wukai
 * @date 2019/1/15
 */
public class FileUtils {

    public static void writeStrings(String fileName, List<String> stringList) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fileWriter = new FileWriter(file)){
            for (String s : stringList) {
                fileWriter.write(s);
                fileWriter.write("\n");

            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLine(String fileName) {
        File file = new File(fileName);
        List<String> list = Lists.newArrayList();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String string = bufferedReader.readLine();
            if (StringUtils.isNotEmpty(string)) {
                list.add(string);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("读取文件错误");
        }
        return list;
    }
}
