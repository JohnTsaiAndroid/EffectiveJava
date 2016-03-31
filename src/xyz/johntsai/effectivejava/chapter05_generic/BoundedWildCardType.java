package xyz.johntsai.effectivejava.chapter05_generic;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;


/**
 * Created by JohnTsai on 16/3/31.
 * 利用有限制的通配符来提升API的灵活性
 * Page:117
 * Chapter:05
 */
public class BoundedWildCardType {
    /**
     * 具体使用哪种通配符类型:
     * 使用通配符PECS(Producer Extends,Consumer super)
     * ------------------
     * 参数化类型  通配符类型
     *  T生产者   extends
     *  T消费者   super
     * ------------------
     */
    public static void main(String[] args) {
        Stack<Number> stack= new Stack<>();
        Iterable<Integer> integers = Arrays.asList(1,2,3,4,5);
        Collection<Object> objectCollection = new LinkedList<>();
        //before
//        stack.pushAll(integers);//参数类型不对
//        stack.popAll(objectCollection);//参数类型不对
        //after
        stack.pushAll(integers);
        System.out.println(stack);
        stack.popAll(objectCollection);
        System.out.println(stack);

    }
}
/**
 * 栈的实现
 * @param <E>
 * API:
 *   public Stack();
 *   public void push(E e);
 *   public E pop();
 *   public boolean isEmpty();
 *
 * 新增API:
 *   before:
 *     public void pushAll(Iterable<E> i);
 *     public void popAll(Collection<E> c);
 *   after:
 *     使用有限制的通配符类型(bounded wildcard type)
 *    public void pushAll(Iterable</? extends E> i);
 *    public void popAll(Collection</? super E> c);
 *
 */

class Stack<E>{
    private E [] elements;
    private static final int INIT_CAPABILITY = 16;
    private int size = 0;
    @SuppressWarnings("unchecked")
    public Stack(){
        elements = (E[]) new Object [INIT_CAPABILITY];
    }
    public void push(E e){
        checkCapability();
        elements[size++]=e;
    }
    public E pop(){
        if(size==0)
            throw new RuntimeException("Empty Stack");
        E e = elements[--size];
        elements[size]=null;
        return e;
    }

    private void checkCapability() {
        if(size==elements.length)
            elements = Arrays.copyOf(elements,2*elements.length-1);
    }
    public boolean isEmpty(){
        return size==0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String start = super.toString();
        sb.append(start);
        for(int i = 0 ;i<size;i++){
            String s = " ["+elements[i]+"]";
            sb.append(s);
        }
        return sb.toString();
    }

    //before
//    public void pushAll(Iterable<E> i){
//        for(E e:i){
//            push(e);
//        }
//    }
//    public void popAll(Collection<E> c){
//        while (!isEmpty()){
//            c.add(pop());
//        }
//    }
    //after
    public void pushAll(Iterable<? extends E> i){
        for(E e:i){
            push(e);
        }
    }
    public void popAll(Collection<? super E> c){
        while(!isEmpty()){
            c.add(pop());
        }
    }

}
