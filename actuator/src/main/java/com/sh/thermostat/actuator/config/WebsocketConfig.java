package com.sh.thermostat.actuator.config;

import com.sh.thermostat.actuator.controller.UiWebsocketController;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;

@Configuration
public class WebsocketConfig extends WebSocketMessageBrokerConfigurationSupport {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(
                UiWebsocketController.TEMPERATURE_TOPIC,
                UiWebsocketController.ACTIVE_PROGRAM_TOPIC,
                UiWebsocketController.SYSTEM_HEALTH_TOPIC,
                UiWebsocketController.LOG_TOPIC
        );
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/data")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
