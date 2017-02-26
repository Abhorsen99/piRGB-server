package com.dibbledos.piRGB.soundSensitivity;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Microphone {
    private TargetDataLine line;

    public Microphone(){ }

    public void setFormat(AudioFormat format) throws LineUnavailableException {
        line = AudioSystem.getTargetDataLine(format);
    }

    public void open() throws LineUnavailableException {
        line.open();
    }

    public void start(){
        line.start();
    }

    public byte[] read(int offset, int length){
        byte[] output = new byte[length];
        line.read(output, offset, length);
        return output;
    }
}
