# piRGB-server
This is a program meant to run on a raspberry pi which controls a strand of RGB led lights that are connected via the GPIO pins. Control of the lights is accomplished via http posts with JSON payloads. 

This program opens a rest endpoint on localhost:8000 with three individual URI's to accept post:
 * /color/show
 * /off
 * /sequence/show
 
Each request requires a JSON payload to how to perform the requested action. 

The payload properties are as follows

fade - Boolean whether or not to fade to the next color. True fades; false snaps. <br>
color - Color object <br>
Red - RGB red value for the color. Values between 0-255<br>
Green - RGB green value for the color. Values between 0-255<br> 
Blue - RGB blue value for the color. Values between 0-255<br>
Magnitude - Percentage brightness for the color. Values 0-100<br>


Example payloads

/off
```
{
  "fade": true
}
```
/color/show
```
{
  "color": {
     "red": 255,
     "green": 35,
     "blue": 175,
     "magnitude": 100
  },
  "fade": true
}
```
