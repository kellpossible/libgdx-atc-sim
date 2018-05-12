# Settings

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

Configuration for the Libgdx ATC Simulator project is handled in the `ApplicationConfig` class.


This class makes use of the [_typesafe/config_](https://github.com/typesafehub/config) library from github.
We are storing the default configuration in [default_settings.json](../../assets/default_settings.json).
These values can be overridden using the [settings.json](../../assets/settings.json) file.

A breakdown of the settings.json file can be found below with descriptions of options, along with default options.

_Section author: Uros_

+ Projection-reference": [0.0, -37.668905,144.8307145],
Selects the geographical co-ordinate for the projection reference point.

## Prediction-service
_Section author: Uros_

### Prediction-engine
+ **Algorithm-type: "LMLEASTSQUARESV3"**
Selects the type of algorithm to use in the prediction engine.

+ **Interpolation-transition-time: 180.0**

+ **Java-worker-threads: 4**  
Selects the number of java worker threads that will run concurrently.

### Debug-data-feed-client
+ **Port-number: 6989 (Acceptible Port Range).** Selects the port number that Debug Data Feed Client connects on. This is set arbitarily to 6989 for testing purposes. This will connect to the Debug Data Feed Server.

+ **Server-ip: "localhost"**  
Selects the Server's IP address, in this case Local host. This is the Debug Data Feed Server by default.

### Server
+ **Port-number: 6789 (Acceptible Port Range).** Selects the port number that Prediction Feed Server runs on.



## Debug-data-feed
+ **Enabled: true (True/False).** Can be set to True or False, This will enable or disable the Debug Data Feed, This can be disabled when running a direct feed from other sources.

+ **Port-number": 6989 (Acceptible Port Range).** Selects the Port Number the Debug-Data Feed Server runs on

### Adsb-recording-scenario
+ "file": "assets/flight_data/YMML_26_05_2016/database.json"
Selects ADSB scenario that gets pushed through prediction service.

+ **"filter-for-planeID":[]**
This will filter for plane ID's and only show the planes filtered.

+ **"heading-velocity": false  (True/False).**

+ **speed": 10**
Sets the speed that the program runs at.

## Display
+ **use-msaa": true (True/False).** Enables Multisample anti-aliasing.

+ **msaa-samples": 8 (Integer Value)**. Selects number of Multisample anti-aliasing samples.

+ **prediction-display-method": "GRADIENT"**

+ **show-tracks-default": true (True/False).** This will either show or hide Aircraft Tracks. When disabled the tracks will dissapear.

+ **Prediction-feed-client port-number": 6789  (Acceptible Port Range).** Selects the port number the prediction feed client connects on.

+ **Prediction-feed-client server-ip": "localhost"**
Sets the Server IP the prediction feed client connects to.

+ **stippled-predictions": false (True/False).** This will show stippled vector lines on the visualization display if enabled.

+ **bonus-feature": false (True/False).** Enables a bonus feature that was discovered accidentally by Luke. This will show some flashy graphics, NOTE: Could be dangerous to ATC controller.


## Testing
+ **"run-accuracy-test": false (True/False).** Will run the accuracy tests if selected true.

+ **"save-csv": false (True/False).** Will save a CSV file of accuracy tests.

+ **"save-json": true (True/False).** Saves a JSON file for accuracy tests.     
