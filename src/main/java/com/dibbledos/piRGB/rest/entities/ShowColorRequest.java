
package com.dibbledos.piRGB.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowColorRequest {

    private Color color = new Color();
    private Boolean fade = false;
    private SoundProperties soundSensitivity = new SoundProperties();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean getFade() {
        return fade;
    }

    public void setFade(Boolean fade) {
        this.fade = fade;
    }

    public SoundProperties getSoundSensitivity() {
        return soundSensitivity;
    }

    public void setSoundSensitivity(SoundProperties soundSensitivity) {
        this.soundSensitivity = soundSensitivity;
    }
}
