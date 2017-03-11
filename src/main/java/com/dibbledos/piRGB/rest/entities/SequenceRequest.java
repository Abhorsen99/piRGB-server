
package com.dibbledos.piRGB.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceRequest {

    private Boolean fade = false;
    private Integer interval = 1;
    private List<Color> sequence = new ArrayList<>();
    private SoundProperties soundSensitivity = new SoundProperties();

    public Boolean getFade() {
        return fade;
    }

    public void setFade(Boolean fade) {
        this.fade = fade;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<Color> getSequence() {
        return sequence;
    }

    public void setSequence(List<Color> sequence) {
        this.sequence = sequence;
    }

    public SoundProperties getSoundSensitivity() {
        return soundSensitivity;
    }

    public void setSoundSensitivity(SoundProperties soundSensitivity) {
        this.soundSensitivity = soundSensitivity;
    }
}
