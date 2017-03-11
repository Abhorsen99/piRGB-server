# piRGB-server
This is a program meant to run on a raspberry pi which controls a strand of RGB led lights that are connected via the GPIO pins. Control of the lights is accomplished via http posts with JSON payloads. 

This program opens a rest endpoint on localhost:8000 with three individual URI's to accept post:
 * /color/show
 * /off
 * /sequence/show
 
Each request requires a JSON payload to how to perform the requested action. 

The payload properties are as follows

fade - Boolean whether or not to fade to the next color. True fades; false snaps. Default false.<br>
soundSensitivity - Properties for controlling how the lights respond to sound <br>
enabled - Boolean whether or not to have lights react to sound. True increases light brightness during loud moments; false disables sound sensitivity. Default false.<br>
threshold - The percent of "loudness" that must be present before the lights will scale above base brightness. Default 70.<br>
color - Color object <br>
red - RGB red value for the color. Values between 0-255. Default 0.<br>
green - RGB green value for the color. Values between 0-255 Default 0.<br> 
blue - RGB blue value for the color. Values between 0-255 Default 0.<br>
magnitude - Percentage brightness for the color. Values 0-100 Default 0.<br>
interval - Interval (in seconds) to display each color in a sequence Default 0.<br>


Example payloads.

###/off###
```
{
  "fade": true
}
```
###/color/show###
```
{
  "color": {
     "red": 255,
     "green": 35,
     "blue": 175,
     "magnitude": 100
  },
  "fade": true,
  "soundSensitivity": {
  	  "enabled":false,
  	  "threshold":70
  }
}
```

###/sequence/show###
```
{
  "fade": false,
  "interval": 10,
   "soundSensitivity": {
  	  "enabled":true,
  	  "threshold":90
  },
  "sequence": [
     {
        "red": 255,
        "green": 35,
        "blue": 175,
        "magnitude": 100
     },
     {
        "red": 45,
        "green": 142,
        "blue": 235,
        "magnitude": 40
     }
   ]
}
```

# Installation
Prerequisites: Raspberry Pi with Java JRE 1.8+ isntalled. Sound sensitivity requires microphone connected to Pi

* copy build jar to pi (Bitvise or scp, for example) 
* elevate to root console (```sudo -i```) 
* edit the cron table to execute the jar on startup. 
  * Edit crontable with ``` crontab -e ``` 
  * add to file (Jar location assumed): ```@reboot sudo java -jar /home/pi/LightControl/LightControl.jar&```
