package com.dibbledos.piRGB.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundProperties {
    private boolean enabled = false;
    private int threshold = 70;

    public SoundProperties(boolean enabled, int threshold) {
        this.enabled = enabled;
        this.threshold = threshold;
    }

    public SoundProperties() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SoundProperties that = (SoundProperties) o;

        return new EqualsBuilder()
                .append(enabled, that.enabled)
                .append(threshold, that.threshold)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(enabled)
                .append(threshold)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SoundProperties{" +
                "enabled=" + enabled +
                ", threshold=" + threshold +
                '}';
    }
}
