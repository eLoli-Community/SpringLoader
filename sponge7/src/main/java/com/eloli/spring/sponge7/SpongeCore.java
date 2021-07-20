package com.eloli.spring.sponge7;

import com.eloli.spring.BootLoader;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.springframework.boot.loader.jar.JarFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "spring-loader")
public class SpongeCore {
    private static PluginContainer container;

    @Inject
    public SpongeCore(PluginContainer container){
        SpongeCore.container = container;
        container.getLogger().info("SpringLoader constructing.");
        Path path = Sponge.getGame().getGameDirectory().resolve("spring");;
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
                        container.getLogger().info("Loading "+file+".");
                        try {
                            new BootLoader(
                                    new JarFile(file),
                                    SpongeCore.class.getClassLoader()
                            ).launch(new String[0]);
                            container.getLogger().info(file+" loaded.");
                        } catch (Exception e) {
                            container.getLogger().warn("Failed to load "+file+".",e);
                        }
                    });
        }catch (IOException e){
            container.getLogger().warn("Failed to load SpringLoader.",e);
        }
        container.getLogger().info("SpringLoader completely all tasks. Any further errors don't report to author.");
    }

    public static PluginContainer container() {
        return container;
    }
}
