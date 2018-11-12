package com.wvkia.tools.kiwi.shardingjdbc_integration.model.domain;

import lombok.Data;

import java.util.Date;
@Data
public class OrderDo {
    private String id;

    private String name;

    private Integer userId;

    private String desc;

    private Date loginTime;

}