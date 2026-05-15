package com.xunqi.gulimall.cart;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public final class LinkedNode<T> {
    private  T value;
    private LinkedNode<T> next;

    public LinkedNode(T value, LinkedNode<T> next) {
        this.value = value;
        this.next = next;
    }

    public void linkNext(LinkedNode<T> n) {
            this.next = n;
    }

    public void link(T value) {
        this.value = value;
    }

    public LinkedNode<T> next() {
        return this.next;
    }

    public T value() {
        return this.value;
    }




    public static <ST> boolean contains(LinkedNode<ST> node, ST value) {
        while(node != null) {
            if (node.value() == value) {
                return true;
            }

            node = node.next();
        }

        return false;
    }
}
