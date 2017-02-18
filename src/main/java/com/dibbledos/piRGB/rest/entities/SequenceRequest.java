
package com.dibbledos.piRGB.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceRequest {

    private Boolean fade;
    private Integer interval;
    private List<Color> sequence = null;

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

}
