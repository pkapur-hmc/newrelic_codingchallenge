### Pratyush Kapur Coding Challenge

## Steps to run the application
* `./gradlew build`
* `./gradlew shadowJar`
* `./java -jar ./build/libs/coding-challenge-shadow.jar` - the first time this is run it creates the server, every time after that it creates a client.


## Issues
* Logging currently displays in the Server's terminal window

## Tech:
* Java
* Gradle
* Apache Commons Logging


### Assumptions
* Leading zeros are included in the 9-digits


### Classes
#### Main.java
* Starts up the Server and Clients as necessary

#### Server.java
* Creates and handles a Server

#### Client.java
* Creates and handles a Client

#### DisplayedInfo.java
* Stores and updates information that is displayed every 10s
