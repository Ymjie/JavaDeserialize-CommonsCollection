package com.melll;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Unserialize {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        unsserialize("ser.bin");
    }
    public static Object unsserialize(String Filename) throws IOException,ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(Filename)));
        return ois.readObject();
    }
}