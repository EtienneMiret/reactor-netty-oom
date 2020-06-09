package io.miret.etienne.reactor_netty_oom.reactor_netty_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger (Main.class);

  private static final int BATCH_SIZE = 50;

  public static void main (String[] args) {
    var host = args[0];
    var port = Integer.parseInt (args[1]);
    logger.info ("Connecting to {}:{}.", host, port);
    var client = new Client (host, port);
    for (int i = 0; true; i++) {
      var index = i * BATCH_SIZE;
      logger.info ("Making {}th batch of {} requests.", i, BATCH_SIZE);
      Flux.<Integer, Integer>generate (() -> index, (state, sink) -> {
        if (state < index + 50) {
          sink.next (state);
        } else {
          sink.complete ();
        }
        return state + 1;
      })
          .flatMap (client::get)
          .then ()
          .block ();
    }
  }

}
