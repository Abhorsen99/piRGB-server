package com.dibbledos.piRGB;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

// Vigorous inspiration taken from http://proteo.me.uk/2009/10/sound-level-monitoring-in-java/
public class MicReader {
    private final int SAMPLES_PER_SECOND = 32000;
    private final int AVERAGES_CALCULATED_PER_SECOND = 5;
    private byte[] sample = new byte[SAMPLES_PER_SECOND / AVERAGES_CALCULATED_PER_SECOND];
    private TargetDataLine microphone;

    public MicReader() throws LineUnavailableException {
        init();
    }

    private void init() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLES_PER_SECOND, 16, 1, true, false);
        microphone = AudioSystem.getTargetDataLine(format);

        microphone.open();
        microphone.start();
    }

    public double getCurrentLevelPercent()  {
        microphone.read(sample, 0, sample.length);
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
