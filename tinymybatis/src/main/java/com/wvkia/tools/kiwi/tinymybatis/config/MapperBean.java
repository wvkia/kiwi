package com.wvkia.tools.kiwi.tinymybatis.config;

import java.util.List;

/**
 * 保存一个mapper文件的bean,即一个接口的所有方法解析
 */
public class MapperBean {
    private String interfaceName;
    private List<Function> list;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}
