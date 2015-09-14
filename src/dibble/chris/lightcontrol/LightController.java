package dibble.chris.lightcontrol;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import java.util.ArrayList;

public class LightController {
    private static LightController instance;
    private Color currentColor;
    private final double fadeSteps = 25.0; // double to force decimals when used with ints. 25 because higher numbers causes lots of flickering due to rounding in divisions
    private boolean shouldContinueSequence = false;
    private Thread sequenceThread = new Thread();

    private LightController() {
        Gpio.wiringPiSetup();

        SoftPwm.softPwmCreate(ColorPin.BLUE.getValue(), 0, 100);
        SoftPwm.softPwmCreate(ColorPin.GREEN.getValue(), 0, 100);
        SoftPwm.softPwmCreate(ColorPin.RED.getValue(), 0, 100);
        currentColor = new Color(0, 0, 0, 0);
    }

    public static LightController getInstance() {
        if (instance == null) {
            instance = new LightController();
        }
        return instance;
    }

    public void showColor(Color requestedColor) {
        System.out.println("Request to set color to " + requestedColor);
        stopSequence();
        showColorImpl(requestedColor);
    }

    private void showColorImpl(Color requestedColor) {
        int blue = adjustForMagnitude(requestedColor.getBluePercent(), requestedColor.getMagnitude());
        int green = adjustForMagnitude(requestedColor.getGreenPercent(), requestedColor.getMagnitude());
        int red = adjustForMagnitude(requestedColor.getRedPercent(), requestedColor.getMagnitude());

        System.out.println(String.format("Writing pin values red: %d green %d blue %d", red, green, blue));

        setPinValue(ColorPin.BLUE, blue);
        setPinValue(ColorPin.GREEN, green);
        setPinValue(ColorPin.RED, red);
        currentColor.setMagnitude(requestedColor.getMagnitude());

        System.out.println("Current color values are " + currentColor);
    }

    public void off() {
        stopSequence();
        System.out.println("Turning all LEDs off");

        setPinValue(ColorPin.BLUE, 0);
        setPinValue(ColorPin.GREEN, 0);
        setPinValue(ColorPin.RED, 0);
    }

    public void fadeTo(Color requestedColor) {
        stopSequence();
        fadeToImpl(requestedColor);
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
        stopSequence();
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

    public void fadeSequence(ArrayList<Color> colors, int showInterval) {
        stopSequence();
        shouldContinueSequence = true;
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
    }

    public void showSequence(ArrayList<Color> colors, int showInterval) {
        stopSequence();
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
    }

    //Utility methods
    private void setPinValue(ColorPin pin, int value) {
        SoftPwm.softPwmWrite(pin.getValue(), value);
        if (pin == ColorPin.BLUE) {
            currentColor.setPercentBlue(value);
        } else if (pin == ColorPin.GREEN) {
            currentColor.setPercentGreen(value);
        } else if (pin == ColorPin.RED) {
            currentColor.setPercentRed(value);
        }
    }

    private int adjustForMagnitude(double colorValue, double magnitude) {
        return (int) Math.round(colorValue * (magnitude / 100.0));
    }

    private void stopSequence() {
        shouldContinueSequence = false;
        try {
            System.out.println("Waiting for existing sequence to stop");
            sequenceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped sequence thread");
    }
}
