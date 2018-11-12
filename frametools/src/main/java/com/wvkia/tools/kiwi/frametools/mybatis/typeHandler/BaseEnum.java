package com.wvkia.tools.kiwi.frametools.mybatis.typeHandler;

public interface BaseEnum<T extends Enum<?>,K> {
    K getCode();
}
