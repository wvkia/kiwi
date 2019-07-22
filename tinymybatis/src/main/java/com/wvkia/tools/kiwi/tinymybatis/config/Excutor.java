package com.wvkia.tools.kiwi.tinymybatis.config;

public interface Excutor {
    public <T> T query(String statement, Object parameter);
}
