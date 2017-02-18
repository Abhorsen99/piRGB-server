package com.dibbledos.piRGB.lightSystems;

import com.dibbledos.piRGB.ColorPin;
import com.dibbledos.piRGB.lightSystems.LightSystem;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

public class PiLightSystem implements LightSystem {
    @Override
    public void init() {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(ColorPin.BLUE.getValue(), 0, 100);
        SoftPwm.softPwmCreate(ColorPin.GREEN.getValue(), 0, 100);
        SoftPwm.softPwmCreate(ColorPin.RED.getValue(), 0, 100);
    }

    @Override
    public void setPinPercentage(ColorPin pin, int value) {
        SoftPwm.softPwmWrite(pin.getValue(), value);
    }
}
