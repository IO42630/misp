### MISP Reverse

#### What it is...
* JAR / adapter / reverse proxy.
* connects to MISP forward server.




#### Overview
* `Reverse`
    * Core
    * Starts Threads
    * Coordinates
    * Launch from here
        * as `main()`
        * or as `Runnable` from somewhere else.
* `CheckSupply`
    * Runnable
    * Asks *forward* about supply.
* `Journey`
    * Runnable
    * Journey fo a `Ride` through the App.
* `Tools`
    * Performs the actual request. 

### Build
* maven clean package
* Dockerfile -> build