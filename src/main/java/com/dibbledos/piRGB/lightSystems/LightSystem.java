package com.dibbledos.piRGB.lightSystems;

import com.dibbledos.piRGB.ColorPin;

public interface LightSystem {
    void init();

    void setPinPercentage(ColorPin pin, int value);
}
