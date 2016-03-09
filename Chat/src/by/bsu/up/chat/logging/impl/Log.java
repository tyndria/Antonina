package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.*;

public class Log implements Logger {

    private static final String TEMPLATE = "[%s] %s";

    private String tag;

    private String src;

    private Log(Class<?> cls) {
        src = cls.getSimpleName() + "log.txt";
        tag = String.format(TEMPLATE, cls.getName(), "%s");
    }

    @Override
    public void info(String message) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(src, true), "UTF-8")){
            writer.write(String.format(tag, message));
        } catch (IOException e1) {
            System.out.println(e1.toString());
        }
        System.out.println(String.format(tag, message));
    }

    @Override
    public void error(String message, Throwable e) {
        System.err.println(String.format(tag, message));
        e.printStackTrace(System.err);
    }

    public static Log create(Class<?> cls) {
        return new Log(cls);
    }
}
