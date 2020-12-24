package com.sh.thermostat.actuator.service.weather;

import com.sh.thermostat.actuator.service.LogService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class OutsideTemperatureService {

    private static final long FIFTEEN_MINUTES_IN_MILLISECONDS = 900000;
    private static final String UNITS = "metric";
    private static final String CITY = "Apahida";

    private final String apiKey;
    private final OpenWeatherMapClient openWeatherMapClient;
    private final LogService logService;

    @Getter
    private double lastOutsideTemperature = -100;

    @Scheduled(fixedRate = FIFTEEN_MINUTES_IN_MILLISECONDS)
    public void requestWeather() {
        JSONObject weatherReport = openWeatherMapClient.getWeatherReport(apiKey, UNITS, CITY);
        lastOutsideTemperature = weatherReport.optJSONObject("main").optDouble("temp");
        logService.logOutsideTemperature(lastOutsideTemperature);
    }

}
