package com.dibbledos.piRGB.lightSystems;

public class LightSystemProvider {

    public LightSystem getLightSystem(){
        if(isNotPi()){
            return new WindowedDebugLightSystem();
        }else{
            return new PiLightSystem();
        }
    }

    private boolean isNotPi(){
        String os =  System.getProperty("os.name");
        System.out.println("os.name: " + os);
        return os.contains("Windows") || os.contains("Mac");
    }
}
