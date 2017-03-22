package com.dibbledos.piRGB.soundSensitivity;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

// Vigorous inspiration taken from http://proteo.me.uk/2009/10/sound-level-monitoring-in-java/
public class MicReader {
    protected final int SAMPLES_PER_SECOND = 32000;
    protected final int AVERAGES_CALCULATED_PER_SECOND = 5;
    private byte[] sample = new byte[SAMPLES_PER_SECOND / AVERAGES_CALCULATED_PER_SECOND];
    private Microphone mic;
    private Boolean micPresent = true;

    public MicReader(Microphone microphone) {
        try {
            mic = microphone;
            AudioFormat format = new AudioFormat(SAMPLES_PER_SECOND, 16, 1, true, false);
            mic.setFormat(format);

            mic.open();
            mic.start();
        }catch (LineUnavailableException | IllegalArgumentException e){
            micPresent = false;
            System.out.println("Mic unavailable " + e.getMessage());
        }
    }

    public Boolean isMicPresent(){
        return micPresent;
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
        return rawVolume == 0 ? 0 : Math.log10(rawVolume) * 22.147; //The max raw value is 32754. multiplying like this makes that value map to 100
    }

    /**
     * Theoretical max is 32754
     */
    private short getMaxValueFromInput(byte[] buffer){
        short max = 0;
            for (int p=0; p<buffer.length-1; p+=2) {
                short thisValue = (short) (buffer[p] + (buffer[p+1] << 8));
                if (thisValue>max) max=thisValue;
            }
            return max;
    }
}
