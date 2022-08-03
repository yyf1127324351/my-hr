package com.myhr.hr.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * 可用做 部门树，菜单树，字典树等转换
 * */

@Getter
@Setter
public class TreeNode {

    private Long id;
    private String name;
    private String code;
    private Integer type;//类型 0菜单 1按钮
    private Long parentId;
    private Integer level; //级别
    private String url;
    private Integer hasChild;
    private Integer sortNumber;


    //easyui tree 必需的属性
    private String text;
    private String state;//'open' 或 'closed'，默认：'open'
    private Boolean checked;
    private String iconCls;
    private List<TreeNode> children;


    public TreeNode() {
        super();
    }
    public TreeNode(Long id, String text) {
        super();
        this.id = id;
        this.name = text;
        this.text = text;
    }
    /**
     * 转化成easyui tree需要的树形数据（原始数据只是一个list，转化后list中的对象children属性有list）
     */
    public static List<TreeNode> convertToTreeList(List<TreeNode> nodeList,String state) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (TreeNode node1 : nodeList) {
            node1.setText(node1.getName());
            boolean mark = false;
            for (TreeNode node2 : nodeList) {
                if (node1.getParentId() != null && node1.getParentId().equals(node2.getId())) {
                    mark = true;
                    if (node2.getChildren() == null) {
                        node2.setChildren(new ArrayList<>());
                        node2.setText(node2.getName());
                        node2.setState(state);// 让父节点显示关闭
                    }
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark) {
                treeNodeList.add(node1);
            }
        }
        return treeNodeList;
    }

    //填充是否有权限
    public static void isChecked(List<TreeNode> menuList, List<Long> menuIdList) {
        for (TreeNode pf : menuList) {
            if(menuIdList.contains(pf.getId())) {
                pf.setChecked(true);
            }else {
                pf.setChecked(false);
            }
        }
    }

    //排序
    public static void sortTreeNode(List<TreeNode> menuList) {
        menuList.sort(comparing(TreeNode::getSortNumber));
        for (TreeNode treeNode : menuList) {
            if (CollectionUtils.isNotEmpty(treeNode.getChildren())) {
                sortTreeNode(treeNode.getChildren());
            }
        }
    }
}
