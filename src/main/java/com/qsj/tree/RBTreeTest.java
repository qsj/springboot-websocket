package com.qsj.tree;

public class RBTreeTest {
    private static final int a[] = { 10, 40, 30, 60, 90, 70, 20, 50, 80 };
    private static final boolean mDebugInsert = true;
    private static final boolean mDebugDelete = true;

    public static void main(String[] args) {
        int i, ilen = a.length;
        RBTree<Integer> tree = new RBTree<>();

        System.out.println("primary data is：" + a);

        for (i = 0; i < ilen; i++) {
            tree.insert(a[i]);
            if (mDebugInsert) {
                System.out.println("add data:" + a[i]);
                tree.print();
                System.out.printf("\n");
            }
        }

        tree.preOrder();
        tree.inOrder();
        tree.postOrder();
        tree.maxValue();
        tree.minValue();

        tree.print();

        if (mDebugDelete) {
            for (i = 0; i < ilen; i++) {
                tree.remove(a[i]);
                System.out.println("remove data:" + a[i]);
                tree.print();
                System.out.printf("\n");
            }
        }
    }
}