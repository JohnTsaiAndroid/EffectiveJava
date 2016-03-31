package xyz.johntsai.effectivejava.chapter05_generic;

import java.util.*;


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


        //类型推导规则很复杂
        Set<Integer> integerSet = new HashSet<>();
        Set<Double> doubleSet = new HashSet<>();
        //通过显示的类型参数告诉编译器使用哪种类型
        Set<Number> numberSet = BoundedWildCardType.<Number>union(integerSet,doubleSet);
    }
    /**
     * s1和s2都是生产者,根据PECS,应该是</? extends E>
     * @param s1
     * @param s2
     * @param <E>
     * @return
     */
    public static <E> Set<E> union(Set<? extends E> s1,Set<? extends E> s2){
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
    //原方法
//    public static <T extends Comparable<T>> T max(List<T> list){return null;}

    /**
     * 对于参数list,它产生T实例,是生产者,故List<T>---->List<? extends T>
     * 对于类型参数T(Comparable<T>) 它消费T实例并产生表示顺序关系的整值 故Comparable<T>--->Comparable<? super T>
     *
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Comparable<? super T>> T max(List<? extends T> list){
        Iterator<? extends T> iterator = list.iterator();
        T max = iterator.next();
        while(iterator.hasNext()){
            T next = iterator.next();
            if(next.compareTo(max)>0)
                max = next;
        }
        return max;
    }

    //before
//    public static void swap(List<?> list,int i ,int j){
//        list.set(i,list.set(j,list.get(i)));
//    }
    //after
    public static void swap(List<?> list,int i,int j){
        swapHelper(list,i,j);
    }

    private static<E> void swapHelper(List<E> list, int i, int j) {
        list.set(i,list.set(j,list.get(i)));
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
