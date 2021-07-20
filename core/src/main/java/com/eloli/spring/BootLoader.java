package com.eloli.spring;

import org.springframework.boot.loader.JarLauncher;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.boot.loader.jar.JarFile;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.Slf4JLoggingSystem;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BootLoader extends JarLauncher {

    protected final ClassLoader parentClassLoader;
    public BootLoader(JarFile jarFile, ClassLoader parentClassLoader) throws IOException {
        super(new JarFileArchive(jarFile));
        this.parentClassLoader = parentClassLoader;
    }

    public void launch(String[] args) throws Exception {
        if (!isExploded()) {
            JarFile.registerUrlProtocolHandler();
        }
        ClassLoader classLoader = createClassLoader(getClassPathArchivesIterator());
        String launchClass = getMainClass();
        launch(args, launchClass, classLoader);
    }

    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new NoLoggerLaunchedURLClassLoader(isExploded(), getArchive(), urls, this.parentClassLoader);
    }

    protected void launch(String[] args, String launchClass, ClassLoader classLoader) throws Exception{
        CompletableFuture<Void> future = new CompletableFuture<>();
        Thread thread = new Thread(()->{
            try {
                createMainMethodRunner(launchClass, args, classLoader).run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, launchClass.substring(launchClass.lastIndexOf('.') + 1));
        thread.setContextClassLoader(classLoader);
        thread.start();
        try {
            future.get();
        } catch (ExecutionException e) {
            if(e.getCause() instanceof Exception){
                throw (Exception) e.getCause();
            }else{
                throw e;
            }
        }
    }
}
