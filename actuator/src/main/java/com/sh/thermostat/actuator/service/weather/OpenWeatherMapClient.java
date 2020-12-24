package com.sh.thermostat.actuator.service.weather;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenWeatherMapClient {

    RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://api.openweathermap.org/data/2.5")
            .build();

    public JSONObject getWeatherReport(String appId, String units, String city) {
        return new JSONObject(restTemplate.getForObject("/weather?appId=" + appId
                + "&units=" + units
                + "&q=" + city, String.class));
    }


}
