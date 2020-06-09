package io.miret.etienne.reactor_netty_oom.spring_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger (Main.class);

  private static final int BATCH_SIZE = 50;

  public static void main (String[] args) {
    var url = args[0];
    logger.info ("Getting {}.", url);
    var client = new Client (url);
    for (int i = 0; true; i++) {
      logger.info ("Making {}th batch of {} requests.", i, BATCH_SIZE);
      var index = i * BATCH_SIZE;
      Flux.<Integer, Integer>generate (() -> index, (state, sink) -> {
        if (state < index + BATCH_SIZE) {
          sink.next (state);
        } else {
          sink.complete ();
        }
        return state + 1;
      })
          .flatMap (client::get)
          .collectList ()
          .then ()
          .block ();
    }
  }

}
