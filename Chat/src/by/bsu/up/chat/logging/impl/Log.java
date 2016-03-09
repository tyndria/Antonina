package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

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
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(src, true), "UTF-8")){
            writer.write(timestamp.toString() + ": " + String.format(tag, message));
            writer.write(System.lineSeparator());
        } catch (IOException e1) {
            System.out.println(e1.toString());
        }
        System.out.println(String.format(tag, message));
    }

    @Override
    public void error(String message, Throwable e) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(src, true), "UTF-8")){
            writer.write(timestamp.toString() + ": " + String.format(tag, message));
            writer.write(System.lineSeparator());
        } catch (IOException e1) {
            System.out.println(e1.toString());
        }
        System.err.println(String.format(tag, message));
        e.printStackTrace(System.err);
    }

    public static Log create(Class<?> cls) {
        return new Log(cls);
    }
}
