package com.dibbledos.piRGB.lightSystems;

public class LightSystemProvider {

    public LightSystem getLightSystem(){
        if(isWindowsOS()){
            return new WindowedDebugLightSystem();
        }else{
            return new PiLightSystem();
        }
    }

    private boolean isWindowsOS(){
        String os =  System.getProperty("os.name");
        System.out.println("os.name: " + os);
        return os.contains("Windows");
    }
}
