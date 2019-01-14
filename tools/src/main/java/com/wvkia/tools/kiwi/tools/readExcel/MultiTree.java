package com.wvkia.tools.kiwi.tools.readExcel;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wukai
 * @date 2019/1/10
 */

public class MultiTree<T> implements MultiTreeInterface<T> {

    private MultiTreeNode<T> root;

    private MultiTree() {
    }

    @Override
    public MultiTree build() {
        this.root = new MultiTreeNode<>(MultiTreeNode.DEFAULT_ID, null,null);
        return this;
    }

    @Override
    public void add(MultiTreeNode node) {
        if (null == node) {
            throw new IllegalArgumentException("节点不能为空");
        }
        if (node.getParentId().equals(MultiTreeNode.DEFAULT_ID)) {
            root.getChildren().add(node);
        }else {
            MultiTreeNode record = getById(node.getParentId());
            record.getChildren().add(node);
        }
    }


    @Override
    public MultiTreeNode getById(String id) {
        return null;
    }

    private MultiTreeNode scan(MultiTreeNode<T> data, String id) {
        if (null == root || null == root.getChildren()) {
            return null;
        }
        for (MultiTreeNode node : data.getChildren()) {
            if (StringUtils.equals(node.getId(), id)) {
                return node;
            }
            MultiTreeNode sonNode = scan(node, id);
            if (null != sonNode) {
                return sonNode;
            }
        }
        return null;
    }



    @Override
    public void preScan(MultiTreeNode<T> root) {
        if (null == root) {
            return;
        }
        root.doSomething();

        if (CollectionUtils.isNotEmpty(root.getChildren())) {
            for (MultiTreeNode<T> node : root.getChildren()) {
                preScan(node);
            }
        }
    }

    /**
     * 获取从根节点到指定id节点的路径
     * @param id
     * @return
     */
    public List<String> getPathById(String id) {
        LinkedList<String> stack = new LinkedList<>();
        findPath(id, root, stack);
        return stack;
    }

    private boolean findPath(String id,MultiTreeNode<T> children, LinkedList<String> stack) {
        if (StringUtils.isEmpty(id) || null == children) {
            return false;
        }
        if (StringUtils.equals(children.getId(), id)) {
            stack.push(children.getId());
            return true;
        }
        if (CollectionUtils.isNotEmpty(children.getChildren())) {
            stack.push(children.getId());
            for (MultiTreeNode<T> node : children.getChildren()) {
                boolean exist = findPath(id, node, stack);
                if (exist) {
                    return true;
                }
            }
            stack.pop();
        }

        return false;
    }

    /**
     * 获取叶子节点
     * @return
     */
    public List<MultiTreeNode> getLeft() {
        if (null == root) {
            return Lists.newArrayList();
        }

        if (CollectionUtils.isNotEmpty(root.getChildren())) {
            List<MultiTreeNode> list = Lists.newArrayList();
            root.getChildren().stream().forEach(item ->{
                list.addAll(getLeft(item));
            });
            return list;
        }else {
            return Lists.newArrayList(root);
        }
    }

    private List<MultiTreeNode> getLeft(MultiTreeNode<T> record) {
        if (null == record) {
            return Lists.newArrayList();
        }
        if (CollectionUtils.isEmpty(record.getChildren())) {
            return Lists.newArrayList(record);
        }
        List<MultiTreeNode> lefts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(record.getChildren())) {
            record.getChildren().stream().forEach(item ->{
                lefts.addAll(getLeft(item));
            });
        }
        return lefts;
    }
}
