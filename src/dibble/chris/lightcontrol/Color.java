package dibble.chris.lightcontrol;

public class Color {
    @Override
    public String toString() {
        return String.format("Red: %d Green: %d Blue: %d Magnitude: %d", red255, green255, blue255, magnitude);
    }

    private int red255;
    private int green255;
    private int blue255;
    private int magnitude;

    public Color(int red255, int green255, int blue255, int magnitude) {
        this.red255 = red255;
        this.green255 = green255;
        this.blue255 = blue255;
        this.magnitude = magnitude;
    }

    public int get255Red() {
        return red255;
    }

    public int get255Green() {
        return green255;
    }

    public int get255Blue() {
        return blue255;
    }

    public void set255Red(int red255Value) {
        red255 = red255Value;
    }

    public void set255Green(int green255Value) {
        green255 = green255Value;
    }

    public void set255Blue(int blue255Value) {
        blue255 = blue255Value;
    }

    public void setPercentRed(int percentRed) {
        int value = convertPinPercentageTo255(percentRed);
        red255 = value;
    }

    public void setPercentGreen(int percentGreen) {
        green255 = convertPinPercentageTo255(percentGreen);
    }

    public void setPercentBlue(int percentBlue) {
        blue255 = convertPinPercentageTo255(percentBlue);
    }

    public int getRedPercent() {
        return convertToPinPercentage(red255);
    }

    public int getGreenPercent() {
        return convertToPinPercentage(green255);
    }

    public int getBluePercent() {
        return convertToPinPercentage(blue255);
    }

    public int getMagnitude(){
        return magnitude;
    }

    public void setMagnitude(int magnitude){
        this.magnitude = magnitude;
    }

    private int convertToPinPercentage(int colorValue) {
        return (int)Math.round((colorValue / 255.0) * 100);
    }

    private int convertPinPercentageTo255(int pinPercent) {
        return  (int)Math.round((pinPercent / 100.0) * 255.0);
    }
}
