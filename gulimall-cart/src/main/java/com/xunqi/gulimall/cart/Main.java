package com.xunqi.gulimall.cart;



public class Main {

    public static void main(String[] args) {

        LinkedNode<Integer> integerLinkedNode = new LinkedNode<>(1,new LinkedNode<>(86,new LinkedNode<>(87,new LinkedNode<>(87,new LinkedNode<>(87,new LinkedNode<>(87,new LinkedNode<>(87,new LinkedNode<>(88,null))))))));
        LinkedNode<Integer> integerLinkedNode1 = new LinkedNode<>(2,new LinkedNode<>(3,new LinkedNode<>(4,new LinkedNode<>(5,new LinkedNode<>(6,new LinkedNode<>(7,new LinkedNode<>(8,new LinkedNode<>(9,new LinkedNode<>(10,null)))))))));
        LinkedNode<Integer> integerLinkedNode2 = mergeListNode(integerLinkedNode, integerLinkedNode1);

        LinkedNode<Integer> next = null;
            next = integerLinkedNode2;
        while (true){
            if (next == null){
                break;
            }
            System.out.println(next.value());
            next = next.next();
        }
    }

    public static LinkedNode<Integer> mergeListNode(LinkedNode<Integer> list1, LinkedNode<Integer> list2){
        Integer oldValue = null;
        LinkedNode<Integer> mergeNode = null;
        LinkedNode<Integer> mergeNode1 = null;
        LinkedNode<Integer> lastNode = null;

        while(true){
            if(mergeNode  == null){
                mergeNode = list1;
                mergeNode1 = mergeNode;
            }

            else if ((mergeNode = mergeNode.next()) == null){
                break;
            }

            Integer v1 = mergeNode.value();
            if(oldValue == null)	{
                oldValue = v1;
            }

            LinkedNode<Integer> lastNode1 = lastNode;
            if(lastNode  == null){
                lastNode = list2;
            }
            else if ((lastNode = lastNode.next()) == null){
                break;
            }

            if(lastNode.value() > v1){
                LinkedNode<Integer> next = mergeNode.next();
                if(next.value() >= lastNode.value()){
                    while (lastNode != null) {
                        if (next.value() > lastNode.value()) {
                            mergeNode.linkNext(new LinkedNode<>(next.value(), next));
                            mergeNode.link(lastNode.value());

                            lastNode = lastNode.next();
                        }else break;
                    }
                }else {
                    lastNode = lastNode1;
                    continue;
                }
            }
            else if(lastNode.value() <= v1){
                mergeNode.linkNext(new LinkedNode<>(lastNode.value(),mergeNode.next()));
            }

        }

        return mergeNode1;

    }






}
