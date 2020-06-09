This is a sample app to try to reproduce a memory leak with Spring Framework’s
WebClient in case of network errors.
It consists of three modules:
 - server: a web server that always replies with a 200 code and a random
   content, but may sometime abruptly close the TCP connection.
 - reactor-netty-client: a pure Reactor netty client that loop querying the
   above server.
 - spring-client: a client that loop querying the above server using Spring’s
   WebClient.

## Usage

Launch the server,
listening on port 7890,
with a 0.015 probability of aborting any TCP connection:

    $ ./gradlew server:run --args "7890 0.015"
    
Launch the Spring client,
connecting to a server on localhost port 7890:

    $ ./gradlew spring-client:run --args http://localhost:7890/
