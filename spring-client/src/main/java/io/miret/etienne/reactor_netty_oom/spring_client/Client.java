package io.miret.etienne.reactor_netty_oom.spring_client;

import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

public class Client {

  private static final Logger logger = LoggerFactory.getLogger (Client.class);

  private final WebClient webClient;

  public Client (String url) {
    var tcpClient = TcpClient.create ()
        .doOnConnected (connection -> connection.addHandler (new ReadTimeoutHandler (5, TimeUnit.SECONDS)));
    var connector = new ReactorClientHttpConnector (HttpClient.from (tcpClient));
    this.webClient = WebClient.builder ()
        .clientConnector (connector)
        .baseUrl (url)
        .build ();
  }

  public Mono<Integer> get (int i) {
    logger.info ("Getting {}.", i);
    return webClient.get ()
        .uri ("/{index}", i)
        .exchange ()
        .flatMap (response -> response.releaseBody ().thenReturn (i));
  }

}
