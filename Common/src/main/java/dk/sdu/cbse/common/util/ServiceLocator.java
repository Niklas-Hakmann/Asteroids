package dk.sdu.cbse.common.util;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public enum ServiceLocator {

    INSTANCE;

    private static final Map<Class<?>, ServiceLoader<?>> serviceCache = new HashMap<>();
    private final ModuleLayer moduleLayer;

    ServiceLocator() {
        this.moduleLayer = initializeModuleLayer();
    }

    private ModuleLayer initializeModuleLayer() {
        try {
            Path pluginPath = Paths.get("plugins");
            ModuleFinder finder = ModuleFinder.of(pluginPath);

            List<String> moduleNames = finder.findAll().stream()
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .collect(Collectors.toList());

            Configuration config = ModuleLayer.boot()
                    .configuration()
                    .resolve(finder, ModuleFinder.of(), moduleNames);

            return ModuleLayer.boot()
                    .defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize module layer", e);
        }
    }

    public ModuleLayer getModuleLayer() {
        return moduleLayer;
    }

    public <T> List<T> locateAll(Class<T> service) {
        ServiceLoader<T> loader = (ServiceLoader<T>) serviceCache.computeIfAbsent(
                service, s -> ServiceLoader.load(moduleLayer, s)
        );

        List<T> results = new ArrayList<>();
        try {
            loader.forEach(results::add);
        } catch (ServiceConfigurationError e) {
            e.printStackTrace();
        }
        return results;
    }
}