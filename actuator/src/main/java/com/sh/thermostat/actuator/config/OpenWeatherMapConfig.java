package com.sh.thermostat.actuator.config;

import com.sh.thermostat.actuator.service.LogService;
import com.sh.thermostat.actuator.service.weather.OpenWeatherMapClient;
import com.sh.thermostat.actuator.service.weather.OutsideTemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("${openwathermap.enable:false} == true")
public class OpenWeatherMapConfig {

    private final OpenWeatherMapClient openWeatherMapClient;
    private final LogService logService;

    @Value("${openwathermap.api-key}")
    private String apiKey;

    @Bean
    public OutsideTemperatureService externalTemperatureDAO() {
        return new OutsideTemperatureService(apiKey, openWeatherMapClient, logService);
    }

}
