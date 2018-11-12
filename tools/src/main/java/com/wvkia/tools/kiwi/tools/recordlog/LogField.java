package com.wvkia.tools.kiwi.tools.recordlog;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogField {
    String value();  //用于描述信息
}
