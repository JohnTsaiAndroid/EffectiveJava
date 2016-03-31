package xyz.johntsai.effectivejava.chapter05_generic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JohnTsai on 16/3/31.
 * 优先考虑类型安全的异构容器
 * Page:123
 * Chapter:05
 */
public class HeterogeneousContainers {
    /*
     * Java1.5将类Class泛型化了
     * 如String.class属于Class<String>类型
     * Integer.class属于Class<Integer>类型
     * 当一个类的字面文字被用在方法中,来传达编译时
     * 和运行时的类型信息时,被称作type token
     */
    public class Favorites{
        private Map<Class<?>,Object> favorites = new HashMap<>();

        public <T> void putFavorates(Class<T> type,T instance){
            if(type==null)
                throw new NullPointerException("type is null");
            favorites.put(type,instance);
        }

        public <T> T getFavorates(Class<T> type){
//            return (T) favorites.get(type);
            return type.cast(favorites.get(type));
        }
    }

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("test");

        try {
            Method method = stringList.getClass().getMethod("add",Object.class);
            method.invoke(stringList,5);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(stringList);
    }

}
