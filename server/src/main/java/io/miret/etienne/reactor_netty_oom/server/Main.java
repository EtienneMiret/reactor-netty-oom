package io.miret.etienne.reactor_netty_oom.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;

public class Main {

  private static final Logger logger = LogManager.getLogger (Main.class);

  public static void main (String[] args) throws Exception {
    var portNumber = Integer.parseInt (args[0]);
    var errorProbability = Double.parseDouble (args[1]);
    try (var serverSocket = new ServerSocket (portNumber)) {
      logger.info ("Server listening on port {} with a {} error probability.",
          portNumber, errorProbability);
      while (true) {
        var socket = serverSocket.accept ();
        var handler = new Handler (socket, errorProbability);
        var thread = new Thread (handler::run);
        thread.start ();
      }
    }
  }

}
