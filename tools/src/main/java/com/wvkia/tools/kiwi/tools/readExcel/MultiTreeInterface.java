package com.wvkia.tools.kiwi.tools.readExcel;

/**
 * @author wukai
 * @date 2019/1/10
 * 多叉树
 */
public interface MultiTreeInterface<T> {
    /**
     * 构建一颗有根节点的数
     * @return
     */
    MultiTree<T> build();

    /**
     * 添加一个节点
     * @param data
     */
    void add(MultiTreeNode<T> data);

    /**
     * 获取节点
     * @param id
     * @return
     */
    MultiTreeNode getById(String id);

    /**
     * 前序遍历
     */
    void preScan(MultiTreeNode<T> root);
}
