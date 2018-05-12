Flight Data Sources
====================

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

Tracking Data
----------------------

### Astrix Cat 62
[https://www.eurocontrol.int/sites/default/files/content/documents/nm/asterix/archives/asterix-cat062-system-track-data-part9-v1.12-092010.pdf](https://www.eurocontrol.int/sites/default/files/content/documents/nm/asterix/archives/asterix-cat062-system-track-data-part9-v1.12-092010.pdf)


### Flight Radar 24 and any other ADS-B service
I can't find any ads-b service which provides their data for free. Flightradar 24 would require reverese engineering their website javascript/html to figure out how to obtain live flight data, but I've tried for at least an hour but it's a bit crazy.

### Flight simulator
I can record my own flights, and also record flight plan, and intentions such as vectoring to simulate pretty closely what we would be getting out of the system.

This is the option I propose we try first, it's the easiest and simplest. I'll record a few flights.

### Vatsim
Vatsim provides live data but it has a 5 min refresh rate. This does include flight plan information though which is very cool.

Use euroscope or vrc to record an atc session to get the realtime data.

### IVAO
??

Nav Data
------------------------

In order to implement intent tracking etc for vor and ils intercepts, it may be necessary to use a navigation data database. There is one included with x-plane which one could use if we cannot obtain a commercial grade one.