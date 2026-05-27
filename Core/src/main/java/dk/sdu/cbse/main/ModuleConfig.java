package dk.sdu.cbse.main;

import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.common.services.IPointService;
import dk.sdu.cbse.common.util.ServiceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModuleConfig {

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return ServiceLocator.INSTANCE.locateAll(IGamePluginService.class);
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServices() {
        return ServiceLocator.INSTANCE.locateAll(IEntityProcessingService.class);
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return ServiceLocator.INSTANCE.locateAll(IPostEntityProcessingService.class);
    }

    @Bean
    public List<IPointService> pointServices() {
        return ServiceLocator.INSTANCE.locateAll(IPointService.class);
    }

    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServices,
                     List<IPostEntityProcessingService> postEntityProcessingServices,
                     List<IPointService> pointServices) {
        return new Game(gamePluginServices, entityProcessingServices, postEntityProcessingServices, pointServices);
    }
}
