package com.sh.thermostat.actuator.pi.util;

import com.pi4j.wiringpi.Gpio;
import com.sh.thermostat.actuator.service.DateTimeService;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class Dht11 {

    @Builder
    @Getter
    public static class Dht11DTO {
        private final double temperature;
        private final double humidity;
    }

    private static final int MAXTIMINGS = 84;
    private final DateTimeService dateTimeService;
    private final int[] dht11_dat = {0, 0, 0, 0, 0};
    private final Integer gpioPin;

    private LocalDateTime lastReadingTimestamp;


    public Dht11(@Value("${raspberry.temperature.gpio:8}") Integer ioPin, DateTimeService dateTimeService) {
        this.gpioPin = ioPin;
        this.dateTimeService = dateTimeService;
        lastReadingTimestamp = this.dateTimeService.nowLocalDateTime();
        if (Raspberry.isPi()) {
            if (Gpio.wiringPiSetup() == -1) {
                log.error("Cannot init WiringPi.\nCannot read temperature otherwise.\nApplication is stoped!");
                System.exit(-1);
            }
        } else {
            log.info("DHT11 Sensor not initialized. Not a RASPBERRY platform");
        }
    }

    public Dht11DTO getSensorTemperatureAndHumidity() {
        LocalDateTime now = this.dateTimeService.nowLocalDateTime();
        if (Math.abs(this.lastReadingTimestamp.until(now, ChronoUnit.SECONDS)) < 3) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.lastReadingTimestamp = this.dateTimeService.nowLocalDateTime();
        return getTemperatureAndHumidity(this.gpioPin);
    }

    private Dht11DTO getTemperatureAndHumidity(int pin) {
        int laststate = Gpio.HIGH;
        int j = 0;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.delay(500);
        Gpio.digitalWrite(pin, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.pinMode(pin, Gpio.INPUT);

        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pin) == laststate) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            laststate = Gpio.digitalRead(pin);

            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if (i >= 4 && i % 2 == 0) {
                /* shove each bit into the storage bytes */
                dht11_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last byte
        double c = Double.MIN_VALUE;
        double h = Double.MIN_VALUE;
        if (j >= 40 && checkParity()) {
            h = (double) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0]; // for DHT11
            }
            c = dht11_dat[2] + dht11_dat[3] * 0.1d;
            if (c > 125) {
                c = dht11_dat[2]; // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            log.debug("Humidity = " + h + " Temperature = " + c);
        } else {
            log.debug("Temperature/Humidity data invalid, skip");
        }

        return Dht11DTO.builder()
                .temperature(c)
                .humidity(h)
                .build();
    }

    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }

    public static void main(String[] ars) throws Exception {
        final Dht11 dht = new Dht11(25, new DateTimeService());
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            dht.getTemperatureAndHumidity(25);
        }
        System.out.println("Done!!");
    }

}
