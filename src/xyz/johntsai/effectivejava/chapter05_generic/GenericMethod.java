package xyz.johntsai.effectivejava.chapter05_generic;

import java.util.*;

/**
 * Created by JohnTsai on 16/3/30.
 * 优先考虑泛型方法
 * Page:113
 * Chapter:05
 */
public class GenericMethod {
    /**
     * 使用原始类型
     * 返回两个集合的联合
     * @param s1
     * @param s2
     * @return
     *
     * 可编译通过,但会得到警告
     * ➜  chapter05_generic  javac -Xlint:unchecked GenericMethod.java
     * GenericMethod.java:21: 警告: [unchecked] 对作为原始类型HashSet的成员的HashSet(Collection<? extends E>)的调用未经过检查
     * Set result = new HashSet(s1);
     *  ^
     * 其中, E是类型变量:
     * E扩展已在类 HashSet中声明的Object
     * GenericMethod.java:22: 警告: [unchecked] 对作为原始类型Set的成员的addAll(Collection<? extends E>)的调用未经过检查
     * result.addAll(s2);
     * ^
     * 其中, E是类型变量:
     * E扩展已在接口 Set中声明的Object
     */
    public static Set union(Set s1,Set s2){
        Set result = new HashSet(s1);
        result.addAll(s2);
        return result;
    }

    /**
     * 使用泛型方法
     * 返回两个集合的联合
     * @param s1
     * @param s2
     * @param <E>
     * @return
     *
     * 局限:两个参数和返回的结果的类型必须全部相同
     * 解决方法:使用有限制的通配符
     */
    public static <E> Set<E> unionGeneric(Set<E> s1,Set<E> s2){
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    public static <K,V> Map<K,V> newHashMap(){
        return new HashMap<K,V>();
    }

    public static void main(String[] args) {
        Set<String> asiaCountries = new HashSet<>(
                Arrays.asList("China","India","Japan")
        );
        Set<String> americaCountries = new HashSet<>(
                Arrays.asList("Canada","USA","Mexico")
        );
        //类型推导 unionGeneric方法并没有特意去指定类型 而该方法的方法名是 public static <E> Set<E> unionGeneric
        Set<String> countriesUnion = unionGeneric(asiaCountries,americaCountries);
        System.out.println(countriesUnion);

        //JDK1.7之前
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        //JDK1.7之后 Diamond
        Map<String,List<String>> anagrams = new HashMap<>();
        //使用泛型静态工厂方法 原理:类型推导
        Map<String,List<String>> factorymap = newHashMap();

        Collections.reverseOrder();

        String[]strings = {"aa","bb","cc"};
        UnaryFunction<String> sameString = identityFunction();
        for(String string:strings){
            System.out.println(sameString.apply(string));
        }

    }

    public interface UnaryFunction<T>{
        T apply(T arg);
    }
    private static UnaryFunction<Object> IDENTITY_FUNCTION =
            new UnaryFunction<Object>() {
                @Override
                public Object apply(Object arg) {
                    return arg;
                }
            };

    @SuppressWarnings("unchecked")
    public static <T> UnaryFunction<T> identityFunction(){
        return (UnaryFunction<T>) IDENTITY_FUNCTION;
    }

    /**
     * 每次都要创建一个,很浪费,而且它是无状态的.
     * 泛型被具体化了,每个类型都需要一个恒等函数,但是它们被擦除后,就只需要一个泛型单例了.
     * @param <T>
     * @return
     */
    public static <T> UnaryFunction<T> identityFunction2(){
        return new
                UnaryFunction<T>() {
                    @Override
                    public T apply(T arg) {
                        return arg;
                    }
                };
    }

    public interface Comparable<T>{
        int compareto(T t);
    }

    /**
     * 递归类型限制
     * 通过包含该类型参数本身的表达式来限制类型参数是允许的.
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Comparable<T>> T max(List<T> list){
        Iterator<T> i = list.iterator();
        T result = i.next();
        while (i.hasNext()){
            T t = i.next();
            if(t.compareto(result)>0)
                result = t;
        }
        return result;
    }
}
