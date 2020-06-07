package io.miret.etienne.reactor_netty_oom.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

public class Client {

  private static final Logger logger = LoggerFactory.getLogger (Client.class);

  private final HttpClient nettyClient;

  public Client (String host, int port) {
    var tcpClient = TcpClient.create ()
        .host (host)
        .port (port)
        .doOnConnected (connection -> connection.addHandler (new ReadTimeoutHandler (5, TimeUnit.SECONDS)));
    this.nettyClient = HttpClient.from (tcpClient);
  }

  public Mono<Void> get (int i) {
    logger.info ("Querying {}.", i);
    return nettyClient.get ()
        .uri ("/" + i)
        .responseContent ()
        .asByteArray ()
        .then ()
        .onErrorResume (Exception.class, e -> {
          logger.error ("Error on get {}: {}.", i, e.getMessage ());
          return Mono.empty ();
        });
  }

}
