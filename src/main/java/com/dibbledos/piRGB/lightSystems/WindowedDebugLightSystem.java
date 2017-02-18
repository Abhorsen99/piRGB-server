package com.dibbledos.piRGB.lightSystems;

import com.dibbledos.piRGB.ColorPin;

import javax.swing.*;
import java.awt.*;

public class WindowedDebugLightSystem implements LightSystem {
    private JPanel colorCube = new JPanel();
    private Dimension cubeSize = new Dimension(0,0);
    private JFrame frame = new JFrame("R: 0 G: 0 B: ");

    @Override
    public void init() {
        System.out.println("Light system init ");

        frame.getContentPane().setPreferredSize(new Dimension(400, 400));
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        colorCube.setBackground(Color.PINK);
        colorCube.setPreferredSize(cubeSize);
        frame.add(colorCube);

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void setPinPercentage(ColorPin pin, int value) {
        System.out.println(String.format("Setting pin %s to percent %s", pin, value));
        double scaleValue = 255 /100.0;
        Color currentColor = colorCube.getBackground();
        Color newColor = null;
        switch(pin){
            case BLUE: {
                double blue255 = value * scaleValue;
                newColor = new Color(currentColor.getRed(), currentColor.getGreen(), (int)blue255);
                break;
            }
            case GREEN: {
                double green255 = value * scaleValue;
                newColor = new Color(currentColor.getRed(), (int)green255, currentColor.getBlue());
                break;
            }
            case RED: {
                double red255 = value * scaleValue;
                newColor = new Color((int)red255, currentColor.getGreen(), currentColor.getBlue());
                break;
            }
        }
        colorCube.setBackground(newColor);
        resizeForMagnitude(newColor);
        frame.revalidate();
        frame.repaint();
    }

    private void resizeForMagnitude(Color color){
        setCubeSize(getBrightnessPercent(color));
    }

    private void setCubeSize(int percentSize){
        double onePercent = frame.getContentPane().getWidth() / 100.0;
        int newSize = (int)(percentSize * onePercent);
        System.out.println("New Size is " + newSize);
        cubeSize.setSize(newSize, newSize);
        colorCube.setPreferredSize(cubeSize);
        colorCube.setSize((int)cubeSize.getWidth(), (int)cubeSize.getHeight());
    }

    private int getBrightnessPercent(Color color){
        int blue = color.getBlue();
        int green = color.getGreen();
        int red = color.getRed();

        double averageBrightness = (blue + red + green) / 3.0;
        double brightnessPercentage = (averageBrightness / 255) * 100;
        System.out.println("Average brightness: " + averageBrightness + " Percentage: " + brightnessPercentage);
        return (int) brightnessPercentage;
    }
}
