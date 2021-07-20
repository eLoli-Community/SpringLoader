package com.eloli.spring.spigot;

import com.eloli.spring.BootLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.loader.jar.JarFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class SpigotCore extends JavaPlugin {
    private static JavaPlugin container;
    public SpigotCore(){
        super();
        SpigotCore.container = this;
        getLogger().info("SpringLoader constructing.");
        Path path = new File("./spring").toPath();
        try {
            if(path.toFile().exists() && path.toFile().isFile()){
                Files.delete(path);
            }
            if(!path.toFile().exists()){
                Files.createDirectories(path);
            }
            Files.list(path)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".jar"))
                    .forEach(file -> {
                        getLogger().info("Loading "+file+".");
                        try {
                            new BootLoader(
                                    new JarFile(file),
                                    SpigotCore.class.getClassLoader()
                            ).launch(new String[0]);
                            getLogger().info(file+" loaded.");
                        } catch (Exception e) {
                            getLogger().log(Level.WARNING, "Failed to load "+file+".",e);
                        }
                    });
        }catch (IOException e){
            getLogger().log(Level.WARNING, "Failed to load SpringLoader.", e);
        }
        getLogger().info("SpringLoader completely all tasks. Any further errors don't report to author.");
    }
}
