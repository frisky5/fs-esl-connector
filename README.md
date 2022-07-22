# Freeswitch mod_event_socket client

fs-esl-connector is a TCP client that connects to Freeswitch mod_event_socket module to receive events and send commands from and to Freeswitch.
The project is developed using Springboot as a base framework to further extend the capabilities of the client and using NettyIO library for TCP communication and socket processing.

Future planned enhancments
- Administrator REST API.
- Freeswitch Control over REST API for freeswitch api commands (blocking commands).
- Freeswitch Control over Websockets for freeswitch bgapi commands (non-blocking commands).
- Database configuration support along with application.properties file.
