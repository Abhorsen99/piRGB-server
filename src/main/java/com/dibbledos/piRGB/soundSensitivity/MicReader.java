package com.dibbledos.piRGB.soundSensitivity;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

// Vigorous inspiration taken from http://proteo.me.uk/2009/10/sound-level-monitoring-in-java/
public class MicReader {
    private final int SAMPLES_PER_SECOND = 32000;
    private final int AVERAGES_CALCULATED_PER_SECOND = 5;
    private byte[] sample = new byte[SAMPLES_PER_SECOND / AVERAGES_CALCULATED_PER_SECOND];
    private Microphone mic;

    public MicReader() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLES_PER_SECOND, 16, 1, true, false);
        mic = new Microphone(format);

        mic.open();
        mic.start();
    }

    public double getCurrentLevelPercent()  {
        sample = mic.read(0, sample.length);
        int rawVolume = getMaxValueFromInput(sample);
        double adjustedVolume = applyLogAndScale(rawVolume);
        System.out.println(String.format("Raw: %s. Adjusted: %s", rawVolume, adjustedVolume));
        return adjustedVolume;
    }

    public double getLevelScalingNumber(){
        return getCurrentLevelPercent()/100;
    }

    private double applyLogAndScale(double rawVolume){
        return Math.log10(rawVolume) * 22.1545924031; //The max raw value is 32639. multiplying like this makes that value map to 100
    }

    /**
     * Theoretical max is 32639
     */
    private short getMaxValueFromInput(byte[] buffer){
        short max;
            max = (short) (buffer[0] + (buffer[1] << 8));
            for (int p=2;p<buffer.length-1;p+=2) {
                short thisValue = (short) (buffer[p] + (buffer[p+1] << 8));
                if (thisValue>max) max=thisValue;
            }
            return max;
    }
}
