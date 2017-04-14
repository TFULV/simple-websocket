Simple WebSocket
=====

Simple **Java WebSocket** Maven packet for **Java 6** and newer!

The package is ready to compile and run the server.

Server port `8080`, which you can connect to via http://websocket.org/echo.html

### *There are server commands*
 - **help** _Display a list of commands_
 - **print** `message` _Send a message to all_
 - **private** `session-id` `message` _Send a message to a specific client_
 - **exit** _Shutdown the server_

### *Structure*
In the project only 2 classes

Main class that runs the server and processes console commands

Listeners class of the client when connecting, receiving a message and disconnecting
