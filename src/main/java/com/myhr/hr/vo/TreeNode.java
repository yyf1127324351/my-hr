package com.myhr.hr.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
    private Long parentId;
    private Integer level; //级别
    private Integer hasChild;
    private Integer sortNumber;

    //菜单树使用
    private Integer type;//类型 0菜单 1按钮
    private String url; //菜单地址
    //部门树使用
    private String path;//部门路径

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
                        if (StringUtils.isNotBlank(state)) {
                            node2.setState(state);// 让父节点显示关闭
                        }
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
    public static void isChecked(List<TreeNode> list, List<Long> idList) {
        for (TreeNode node : list) {
            if(idList.contains(node.getId())) {
                node.setChecked(true);
            }else {
                node.setChecked(false);
            }
        }
    }

    //排序
    public static void sortTreeNode(List<TreeNode> list) {
        list.sort(comparing(TreeNode::getSortNumber));
        for (TreeNode node : list) {
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                sortTreeNode(node.getChildren());
            }
        }
    }
}
