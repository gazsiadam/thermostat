package com.sh.thermostat.actuator.pi;

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.sh.thermostat.actuator.pi.util.Dht11;
import com.sh.thermostat.actuator.pi.util.Raspberry;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class PiController {

    private final GpioController controller;
    private final GpioPinDigitalOutput heatingPin;
    private final Dht11 dht11;

    public PiController(Dht11 dht11) throws PlatformAlreadyAssignedException {
        if (Raspberry.isPi()) {
            PlatformManager.setPlatform(Platform.RASPBERRYPI);
            log.info("Set RASPBERRY mode");
        } else {
            log.warn("Not a raspberry, set SIMULATED mode");
            PlatformManager.setPlatform(Platform.SIMULATED);
        }
        this.dht11 = dht11;
        controller = GpioFactory.getInstance();
        heatingPin = controller.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.HIGH);
    }

    public void stopHeating() {
        heatingPin.high();
    }

    public void startHeating() {
        heatingPin.low();
    }

    public double getSensorTemperature() {
        return dht11.getSensorTemperatureAndHumidity().getTemperature();
    }

    public boolean isHeatingOn() {
        PinState state = heatingPin.getState();
        if (state != null) {
            return state.isLow();
        }
        return false;
    }

}
