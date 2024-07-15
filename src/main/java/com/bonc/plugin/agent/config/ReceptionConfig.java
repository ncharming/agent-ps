package com.bonc.plugin.agent.config;

import com.bonc.plugin.agent.service.AgentReceptionService;
import com.bonc.plugin.agent.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/21
 */

@Configuration
public class ReceptionConfig {

    @Bean
    public Map<String, AgentReceptionService> idGenerator(BusinessServiceImpl businessService,
                                                          OrderCarServiceImpl orderCarService,
                                                          OutAndCardServiceImpl outAndCardService,
                                                          UtilServiceImpl utilServiceImpl,
                                                          KaoQinServiceImpl kaoQinService) {
        Map<String, AgentReceptionService> idGeneratorMap = new HashMap<>(8);
        idGeneratorMap.put("business", businessService);
        idGeneratorMap.put("orderCar", orderCarService);
        idGeneratorMap.put("outAndCard", outAndCardService);
        idGeneratorMap.put("util", utilServiceImpl);
        idGeneratorMap.put("kaoQin", kaoQinService);
        return idGeneratorMap;

    }
}
