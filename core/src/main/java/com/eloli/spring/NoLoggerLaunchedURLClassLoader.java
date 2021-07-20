package com.eloli.spring;

import org.springframework.boot.loader.LaunchedURLClassLoader;
import org.springframework.boot.loader.archive.Archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class NoLoggerLaunchedURLClassLoader extends LaunchedURLClassLoader {
    private static final int BUFFER_SIZE = 4096;
    public static final Collection<String> replaceLaunched = new ArrayList<>();
    static {
        replaceLaunched.add("org.springframework.boot.SpringBootBanner");
        replaceLaunched.add("org.springframework.boot.logging.DelegatingLoggingSystemFactory");
        replaceLaunched.add("org.springframework.boot.logging.NoInitializeLoggingSystem");
    }
    public static final Collection<String> loggerPrefix = new ArrayList<>();
    static {
        loggerPrefix.add("ch.qos.logback");
    }

    public NoLoggerLaunchedURLClassLoader(final boolean exploded, final Archive rootArchive, final URL[] urls, final ClassLoader parent) throws NoSuchMethodException {
        super(exploded, rootArchive, urls, parent);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        Class<?> c = null;
        for (String prefix : replaceLaunched) {
            if (name.equals(prefix)) {
                c = loadClassInLoaderClassLoader(name);
                break;
            }
        }
        if(c == null) {
            for (String prefix : loggerPrefix) {
                if (name.startsWith(prefix)) {
                    c = getParent().loadClass(name);
                    break;
                }
            }
        }
        if(c != null){
            if(resolve) {
                resolveClass(c);
            }
            return c;
        }
        return super.loadClass(name, resolve);
    }

    private Class<?> loadClassInLoaderClassLoader(final String name) throws ClassNotFoundException {
        String internalName = name.replace('.', '/') + ".class";
        InputStream inputStream = NoLoggerLaunchedURLClassLoader.class.getClassLoader().getResourceAsStream(internalName);
        if (inputStream == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                byte[] bytes = outputStream.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            }
            finally {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
        }
    }
}
