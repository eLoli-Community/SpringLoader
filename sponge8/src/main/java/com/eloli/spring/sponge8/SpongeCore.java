package com.eloli.spring.sponge8;

import com.eloli.spring.BootLoader;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;
import org.springframework.boot.loader.jar.JarFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin("spring-loader")
public class SpongeCore {
    private static PluginContainer container;
    @Inject
    public SpongeCore(final PluginContainer container){
        SpongeCore.container = container;
        container.logger().info("SpringLoader constructing.");
        Path path = Sponge.game().gameDirectory().resolve("spring");
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
                        container.logger().info("Loading "+file+".");
                        try {
                            new BootLoader(
                                    new JarFile(file),
                                    SpongeCore.class.getClassLoader()
                            ).launch(new String[0]);
                            container.logger().info(file+" loaded.");
                        } catch (Exception e) {
                            container.logger().warn("Failed to load "+file+".",e);
                        }
                    });
        }catch (IOException e){
            container.logger().warn("Failed to load SpringLoader.",e);
        }
        container.logger().info("SpringLoader completely all tasks. Any further errors don't report to author.");
    }

    public static PluginContainer container(){
        if(container == null){
            throw new IllegalStateException("SpringLoader still not loaded.");
        }
        return container;
    }
}
