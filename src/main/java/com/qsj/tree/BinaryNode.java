package com.qsj.tree;

import java.util.LinkedList;

public class BinaryNode{
    private Object data;
    private BinaryNode left;
    private BinaryNode right;

    public BinaryNode(){}

    public BinaryNode(Object data){
        this.data = data;
    }

    public BinaryNode(Object data,BinaryNode left,BinaryNode right){
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }

    public BinaryNode getLeft(){
        return left;
    }

    public void setLeft(BinaryNode left){
        this.left = left;
    }

    public BinaryNode getRight(){
        return right;
    }

    public void setRight(BinaryNode right){
        this.right = right;
    }

    public String toString(){
        return String.format("BinaryNode{data:%s,left:%s,right:%s}", data.toString(),left.toString(),right.toString());
    }

    public BinaryNode createNode(){
        BinaryNode node = new BinaryNode("1");
        BinaryNode left2 = new BinaryNode("2");
        BinaryNode left3 = new BinaryNode("3");
        BinaryNode left4 = new BinaryNode("4");
        BinaryNode left5 = new BinaryNode("5");
        BinaryNode left6 = new BinaryNode("6");
        node.setLeft(left2);
        left2.setLeft(left4);
        left2.setRight(left6);
        node.setRight(left3);
        left3.setRight(left5);
        return node;
    }

    /**
     * 二叉树的层序遍历 借助队列实现 使用队列先进先出的特性
     * 
     * 首先将根节点入队列 然后遍历队列
     * 首先将根节点打印出来，接着判断左节点是否为空，不为空则加入队列
     * 
     */
    public void levelInterator(BinaryNode node){
        LinkedList<BinaryNode> queue = new LinkedList<>();
        
        queue.offer(node);
        BinaryNode current;
        while(!queue.isEmpty()){
            current = queue.poll();

            System.out.println(current.data+"--->");
            if(current.getLeft()!=null){
                queue.offer(current.getLeft());
            }
            if(current.right!=null){
                queue.offer(current.right);
            }
        }
    }

    /**
     * 递归实现前序遍历
     */
    public void preOrder(BinaryNode node){
        if(node!=null){
            System.out.println(node.data+"--->");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }
    /**
     * 递归实现中序遍历
     */
    public void midOrder(BinaryNode node){
        if(node!=null){
            midOrder(node.getLeft());
            System.out.println(node.data+"--->");
            midOrder(node.getRight());
        }
    }
/**
 * 递归实现后序遍历
 */
    public void postOrder(BinaryNode node){
        if(node!=null){
            postOrder(node.getLeft());
            postOrder(node.getRight());
            System.out.println(node.data+"--->");
        }
    }

    public static void main(String[] args) {
        BinaryNode node = new BinaryNode().createNode();
        System.out.println("层序遍历算法");
        node.levelInterator(node);
        System.out.println("前序遍历递归");
        node.preOrder(node);
        System.out.println("中序遍历递归");
        node.midOrder(node);
        System.out.println("后序遍历递归");
        node.postOrder(node);

    }

}