
package com.dibbledos.piRGB.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OffRequest {

    private Boolean fade = false;

    public Boolean getFade() {
        return fade;
    }

    public void setFade(Boolean fade) {
        this.fade = fade;
    }

}
