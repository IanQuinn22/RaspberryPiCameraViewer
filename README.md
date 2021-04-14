# RaspberryPiCameraViewer
This app views the Raspberry Pi video stream. If you are running this on your own Raspberry Pi, make sure to change the value of the IP address to that of your Pi.

## Environment
This application is designed to run on Android devices.

## Installation/Running
This application is meant to run on Android Devices. Thus, in order to install the application, you need to install Android Studio. Once Android Studio is installed, you can install the application on your device. You will need to give the application the INTERNET permission in order for the application to run correctly.

## Parameters
In order for the applicatio to run as intended, you will need to know the IP address and port number of the Raspberry Pi from which you are streaming video. You must then modify the following parameters:
* raspi_ip: IP address of Raspberry Pi
* raspi_portnum: Port number of the Raspberry Pi

## Important Functions/Methods
* Client.connectToServer: This function attempts to connect to the server (Raspberry Pi). It takes the IP address and port number as parameters. This function is automatically called when the client thread runs.
* Cient.sendMessage: This function attempts to send a message to the Raspberry Pi from the client application. This function is called when the user presses the "Send" button.

## Bottlenecks/Bugs
This project does not have any known bottlenecks or bugs.

## Future Improvements
The main goal for the future of this project should be remote control of the camera from the client application. The application already has a set of directional pad buttons. In the future, implementers can use a camera that can be controlled remotely by the application. The directional pad can then be updated such that the camera can move according to the user's desires.
