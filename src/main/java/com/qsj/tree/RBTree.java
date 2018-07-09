package com.qsj.tree;

import org.apache.catalina.valves.CrawlerSessionManagerValve;

public class RBTree<T extends Comparable<T>> {
    private RBNode<T> root;
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public class RBNode<T extends Comparable<T>> {
        boolean color;
        T key;
        RBNode<T> left;
        RBNode<T> right;
        RBNode<T> parent;

        public RBNode(T key, boolean color, RBNode<T> parent, RBNode<T> left, RBNode<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T getKey() {
            return key;
        }

        public String toString() {
            return "" + key + (this.color == RED ? "R" : "B");
        }
    }

    public RBTree() {
        root = null;
    }

    public RBNode<T> parentOf(RBNode<T> node) {
        return node != null ? node.parent : null;
    }

    public void setParent(RBNode<T> node, RBNode<T> parent) {
        if (node != null) {
            node.parent = parent;
        }
    }

    public boolean colorOf(RBNode<T> node) {
        return node != null ? node.color : BLACK;
    }

    public boolean isRed(RBNode<T> node) {
        return (node != null) && (node.color == RED) ? true : false;
    }

    public boolean isBlack(RBNode<T> node) {
        return !isRed(node);
    }

    public void setRed(RBNode<T> node) {
        if (node != null) {
            node.color = RED;
        }
    }

    public void setBlack(RBNode<T> node) {
        if (node != null) {
            node.color = BLACK;
        }
    }

    public void setColor(RBNode<T> node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(RBNode<T> tree) {
        if (tree != null) {
            System.out.println(tree.key + "--->");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(RBNode<T> tree) {
        if (tree != null) {
            preOrder(tree.left);
            System.out.println(tree.key + "--->");
            preOrder(tree.right);
        }
    }

    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(RBNode<T> tree) {
        if (tree != null) {
            preOrder(tree.left);
            preOrder(tree.right);
            System.out.println(tree.key + "--->");
        }
    }

    public RBNode<T> search(T key) {
        return search(root, key);
    }

    private RBNode<T> search(RBNode<T> x, T key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return x;
            }
        }
        return x;
    }

    public RBNode<T> searchByRecursion(RBNode<T> x, T key) {
        if (x == null) {
            return x;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return searchByRecursion(x.left, key);
        } else if (cmp > 0) {
            return searchByRecursion(x.right, key);
        } else {
            return x;
        }
    }

    public T minValue() {
        RBNode<T> node = minNode(root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    private RBNode<T> minNode(RBNode<T> tree) {
        if (tree == null) {
            return null;
        }
        while (tree.left != null) {
            tree = tree.left;
        }
        return tree;
    }

    public T maxValue() {
        RBNode<T> node = maxNode(root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    private RBNode<T> maxNode(RBNode<T> tree) {
        if (tree == null) {
            return null;
        }
        while (tree.right != null) {
            tree = tree.right;
        }
        return tree;
    }

    /**
     * 查找节点x的后继节点，即大于节点x的最小节点
     */
    public RBNode<T> successor(RBNode<T> x) {
        if (x.right != null) {
            return minNode(x.right);
        }
        RBNode<T> p = x.parent;
        while ((p != null) && (x == p.right)) {
            x = p;
            p = x.parent;
        }
        return p;
    }

    /**
     * 查找节点x的前驱节点，即小于节点x的最大节点
     */
    public RBNode<T> predecessor(RBNode<T> x) {
        if (x.left != null) {
            return maxNode(x.left);
        }
        RBNode<T> p = x.parent;
        while ((p != null) && (x == p.left)) {
            x = p;
            p = x.parent;
        }
        return p;
    }

    /**
     * 对红黑树节点进行左旋操作
     * 
     * 左旋示意图 p p / / x y / \ / \ lx y ----> x ry / \ / \ ly ry lx ly
     * 
     * 左旋分为三个步骤： 1、将y的左子节点赋给x的右子节点，并将x赋给y的左子节点的父节点（y左子节点非空时）
     * 2、将x的父节点p（非空时）赋给y的父节点，同时更新p的子节点为y（左或右） 3、将y的左子节点设为x，将x的父节点设为y
     */
    private void leftRotate(RBNode<T> x) {
        RBNode<T> y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else {
            if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }

        y.left = x;
        x.parent = y;
    }

    /**
     * 对红黑树节点y进行右旋操作 右旋示意图，对接点y进行右旋 p p / / y x / \ / \ x ry ----> lx y / \ / \ lx
     * rx rx ry
     * 
     * 右旋分为三个步骤 1、将x的右子节点赋给y的左子节点，并将y赋给x右子节点的父节点（x右子节点非空时）
     * 2、将y的父节点p（非空时）赋给x的父节点，同时更新p的子节点为x（左或右） 3、将x的右子节点设为y，将y的父节点设为x
     */
    private void rightRotate(RBNode<T> y) {
        RBNode<T> x = y.left;
        y.left = x.right;

        if (x.right != null) {
            x.right.parent = y;
        }

        x.parent = y.parent;

        if (y.parent == null) {
            this.root = x;
        } else {
            if (y == y.parent.right) {
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }
        }

        x.right = y;
        y.parent = x;
    }

    public void insert(T key) {
        RBNode<T> node = new RBNode<T>(key, RED, null, null, null);
        if (node != null) {
            insert(node);
        }
    }

    private void insert(RBNode<T> node) {
        RBNode<T> current = null;
        RBNode<T> x = this.root;

        while (x != null) {
            current = x;
            int cmp = node.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = current;

        if (current != null) {
            int cmp = node.key.compareTo(current.key);
            if (cmp < 0) {
                current.left = node;
            } else {
                current.right = node;
            }
        } else {
            this.root = node;
        }

        insertFixup(node);
    }

    private void insertFixup(RBNode<T> node) {
        RBNode<T> parent, gparent;

        while (((parent = parentOf(node)) != null) && isRed(parent)) {
            gparent = parentOf(parent);

            if (parent == gparent.left) {
                RBNode<T> uncle = gparent.right;
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                if (node == parent.right) {
                    leftRotate(parent);
                    RBNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }

                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else {
                RBNode<T> uncle = gparent.left;
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                if (node == parent.left) {
                    rightRotate(parent);
                    RBNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }

                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }
        setBlack(this.root);
    }

    public void remove(T key) {
        RBNode<T> node;
        if ((node = search(root, key)) != null) {
            remove(node);
        }
    }

    private void remove(RBNode<T> node) {
        RBNode<T> child, parent;
        boolean color;

        if ((node.left != null) && (node.right != null)) {
            RBNode<T> replace = node;
            replace = replace.right;
            while (replace.left != null) {
                replace = replace.left;
            }
            if (parentOf(node) != null) {
                if (node == parentOf(node).left) {
                    parentOf(node).left = replace;
                } else {
                    parentOf(node).right = replace;
                }
            } else {
                this.root = replace;
            }

            child = replace.right;
            parent = parentOf(replace);
            color = colorOf(replace);
            if (parent == node) {
                parent = replace;
            } else {
                if (child != null) {
                    setParent(child, parent);
                }
                parent.left = child;
                replace.right = node.right;
                setParent(node.right, replace);
            }
            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;

            if (color == BLACK) {
                removeFixup(child, parent);
            }
            node = null;
            return;
        }
    }

    private void removeFixup(RBNode<T> node, RBNode<T> parent) {
        RBNode<T> other;

        while ((node == null || isBlack(node)) && (node != this.root)) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left == null || isBlack(other.left)) && (other.right == null || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    if (other.right == null || isBlack(other.right)) {
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }

                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {
                other = parent.left;
            }

            if (isRed(other)) {
                setBlack(other);
                setRed(parent);
                rightRotate(parent);
                other = parent.left;
            }

            if ((other.left == null || isBlack(other.left)) && (other.right == null || isBlack(other.right))) {
                setRed(other);
                node = parent;
                parent = parentOf(node);
            } else {
                if (other.left == null || isBlack(other.left)) {
                    setBlack(other.right);
                    setRed(other);
                    leftRotate(other);
                    other = parent.left;
                }

                setColor(other, colorOf(parent));
                setBlack(parent);
                setBlack(other.left);
                rightRotate(parent);
                node = this.root;
                break;
            }
        }
        if (node != null) {
            setBlack(node);
        }
    }

    public void clear() {
        destory(root);
        root = null;
    }

    private void destory(RBNode<T> tree) {
        if (tree == null) {
            return;
        }
        if (tree.left != null) {
            destory(tree.left);
        }
        if (tree.right != null) {
            destory(tree.right);
        }
        tree = null;
    }

    public void print() {
        if (root != null) {
            print(root, root.key, 0);
        }
    }

    private void print(RBNode<T> tree, T key, int direction) {
        if (tree != null) {
            if (0 == direction) {
                System.out.printf("%2d(B) is root \n", tree.key);
            } else {
                System.out.printf("%2d(%s) is %2d's %6s child \n", tree.key, isRed(tree) ? "R" : "B", key,
                        direction == 1 ? "right" : "left");
            }
            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }

}