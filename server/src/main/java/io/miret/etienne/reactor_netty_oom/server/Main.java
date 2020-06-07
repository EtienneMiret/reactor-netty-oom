package io.miret.etienne.reactor_netty_oom.server;

import java.net.ServerSocket;

public class Main {

  public static void main (String[] args) throws Exception {
    var portNumber = Integer.parseInt (args[0]);
    try (var serverSocket = new ServerSocket (portNumber)) {
      while (true) {
        var socket = serverSocket.accept ();
        var handler = new Handler (socket);
        var thread = new Thread (handler::run);
        thread.start ();
      }
    }
  }

}
