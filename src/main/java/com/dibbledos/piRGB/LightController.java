package com.dibbledos.piRGB;

import com.dibbledos.piRGB.lightSystems.LightSystem;
import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.soundSensitivity.MicReader;

import java.util.List;

//TODO after getting this under test, a lot of cleanup can happen in here
public class LightController {
    private final LightSystem lightSystem;
    protected Color currentColor;
    private final double fadeSteps = 25.0; // double to force decimals when used with ints. 25 because higher numbers causes lots of flickering due to rounding in divisions
    protected boolean shouldContinueSequence = false;
    private Thread sequenceThread = new Thread();
    private static MicReader micReader;
    private static Thread micThread;
    protected static boolean soundSensitive = false;

    public LightController(LightSystem lightSystem, MicReader micReader) {
        this.lightSystem = lightSystem;
        lightSystem.init();
        this.micReader = micReader;
        currentColor = new Color(0, 0, 0, 0);
    }

    public void showColor(Color requestedColor, boolean soundSensitive) {
        System.out.println("Request to set color to " + requestedColor);
        resetLights();
        this.soundSensitive = soundSensitive;
        showColorImpl(requestedColor);
        startMicProcessing();
    }

    private void showColorImpl(Color requestedColor) {
        int blue = adjustForMagnitude(requestedColor.getBluePercent(), requestedColor.getMagnitude());
        int green = adjustForMagnitude(requestedColor.getGreenPercent(), requestedColor.getMagnitude());
        int red = adjustForMagnitude(requestedColor.getRedPercent(), requestedColor.getMagnitude());

        System.out.println(String.format("Writing pin values red: %d green %d blue %d", red, green, blue));

        setPinValue(ColorPin.BLUE, blue);
        setPinValue(ColorPin.GREEN, green);
        setPinValue(ColorPin.RED, red);
        currentColor = requestedColor;

        System.out.println("Current color values are " + currentColor);
    }

    public void off() {
        resetLights();
        System.out.println("Turning all LEDs off");

        setPinValue(ColorPin.BLUE, 0);
        setPinValue(ColorPin.GREEN, 0);
        setPinValue(ColorPin.RED, 0);
        currentColor = new Color();
    }

    public void fadeTo(Color requestedColor, boolean soundSensitive) {
        resetLights();
        this.soundSensitive = soundSensitive;
        fadeToImpl(requestedColor);
        startMicProcessing();
    }

    private void fadeToImpl(Color requestedColor) {
        System.out.println(String.format("Fading from color %s to color %s", currentColor, requestedColor));
        int startingRed = currentColor.get255Red();
        int startingGreen = currentColor.get255Green();
        int startingBlue = currentColor.get255Blue();
        int startingMagnitude = currentColor.getMagnitude();

        int redChange = requestedColor.get255Red() - startingRed;
        int greenChange = requestedColor.get255Green() - startingGreen;
        int blueChange = requestedColor.get255Blue() - startingBlue;
        int magnitudeChange = requestedColor.getMagnitude() - startingMagnitude;

        double redStep = redChange / fadeSteps;
        double greenStep = greenChange / fadeSteps;
        double blueStep = blueChange / fadeSteps;
        double magnitudeStep = magnitudeChange / fadeSteps;

        System.out.println(String.format("Fade step values: Red %f Green %f Blue %f Magnitude %f", redStep, greenStep, blueStep, magnitudeStep));

        for (int i = 1; i <= fadeSteps; i++) {
            Color fadeStepColor = new Color(
                    (int) Math.round(startingRed + (redStep * i)),
                    (int) Math.round(startingGreen + (greenStep * i)),
                    (int) Math.round(startingBlue + (blueStep * i)),
                    (int) Math.round(startingMagnitude + (magnitudeStep * i))
            );
            showColorImpl(fadeStepColor);
            currentColor.setMagnitude(fadeStepColor.getMagnitude());
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void fadeOff() {
        resetLights();
        System.out.println("Fading off");
        int magnitude = currentColor.getMagnitude();
        for (int i = magnitude; i >= 0; i--) {
            // fade LED by decrementing magnitude
            showColorImpl(new Color(currentColor.get255Red(), currentColor.get255Green(), currentColor.get255Blue(), i));
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void fadeSequence(List<Color> colors, int showInterval, Boolean soundSensitive) {
        resetLights();
        shouldContinueSequence = true;
        this.soundSensitive = soundSensitive;
        sequenceThread = new Thread(() -> {
            while (shouldContinueSequence) {
                for (Color color : colors) {
                    if (shouldContinueSequence) {
                        fadeToImpl(color);
                        try {
                            Thread.sleep(showInterval * 1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            System.out.println("Sequence is stopping");
        });
        sequenceThread.start();
        startMicProcessing();
    }

    public void showSequence(List<Color> colors, int showInterval, Boolean soundSensitive) {
        resetLights();
        this.soundSensitive = soundSensitive;
        shouldContinueSequence = true;
        sequenceThread = new Thread(() -> {
            while (shouldContinueSequence) {
                for (Color color : colors) {
                    if (shouldContinueSequence) {
                        showColorImpl(color);
                        try {
                            Thread.sleep(showInterval * 1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            System.out.println("Sequence is stopping");
        });
        sequenceThread.start();
        startMicProcessing();
    }

    //Utility methods
    private void setPinValue(ColorPin pin, int value) {
        lightSystem.setPinPercentage(pin, value);
    }

    protected int adjustForMagnitude(double colorValue, double magnitude) {
        return (int) Math.round(colorValue * (magnitude / 100.0));
    }

    protected void resetLights() {
        shouldContinueSequence = false;
        soundSensitive = false;
        try {
            System.out.println("Waiting for existing sequence to stop");
            sequenceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stopped sequence thread");
    }

    protected void startMicProcessing() {
        micThread = new Thread(() -> {
            boolean isBase = false;
            while (soundSensitive) {
                double soundLevel = micReader.getLevelScalingNumber();
                int blue, green, red;
                final int baseBrightness = 10;
                final double baseSoundThreshold = 0.70;

                if(soundLevel <= baseSoundThreshold){
                    blue = adjustForMagnitude(currentColor.getBluePercent(), baseBrightness);
                    green = adjustForMagnitude(currentColor.getGreenPercent(), baseBrightness);
                    red = adjustForMagnitude(currentColor.getRedPercent(), baseBrightness);
                    if(!isBase){
                        lightSystem.setPinPercentage(ColorPin.BLUE, blue);
                        lightSystem.setPinPercentage(ColorPin.GREEN, green);
                        lightSystem.setPinPercentage(ColorPin.RED, red);
                    }
                    isBase = true;
                }else{
                    double amountAboveThreshold = soundLevel - baseSoundThreshold;
                    double multiplier = 3;
                    blue = adjustForMagnitude(currentColor.getBluePercent(), currentColor.getMagnitude() * (amountAboveThreshold * multiplier));
                    green = adjustForMagnitude(currentColor.getGreenPercent(), currentColor.getMagnitude() * (amountAboveThreshold * multiplier));
                    red = adjustForMagnitude(currentColor.getRedPercent(), currentColor.getMagnitude() * (amountAboveThreshold * multiplier));
                    isBase = false;
                    lightSystem.setPinPercentage(ColorPin.BLUE, blue);
                    lightSystem.setPinPercentage(ColorPin.GREEN, green);
                    lightSystem.setPinPercentage(ColorPin.RED, red);
                }
            }
        });
        if(micReader.isMicPresent()) {
            micThread.start();
        }
    }
}