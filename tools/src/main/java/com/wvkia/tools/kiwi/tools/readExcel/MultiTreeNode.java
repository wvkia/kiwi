package com.wvkia.tools.kiwi.tools.readExcel;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author wukai
 * @date 2019/1/10
 */
@Data
public class MultiTreeNode<T>implements TreeNodeTaskInterface{
    public static final String DEFAULT_ID = "-1";
    private String id;
    private String parentId;
    private T data;

    private List<MultiTreeNode<T>> children;

    public MultiTreeNode(String id,String parentId,T data) {
        this.id = id;
        this.parentId = parentId;
        this.data = data;
        this.children = Lists.newArrayList();
    }

    @Override
    public void doSomething() {

    }
}
