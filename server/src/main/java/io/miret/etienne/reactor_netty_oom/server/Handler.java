package io.miret.etienne.reactor_netty_oom.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

public class Handler {

  private static final Logger logger = LogManager.getLogger (Handler.class);

  private final Socket socket;

  private final Random random = new Random ();

  public Handler (Socket socket) {
    this.socket = socket;
  }

  public void run () {
    try (socket) {
      var writer = new OutputStreamWriter (socket.getOutputStream ());
      writer.write ("HTTP/1.1 200 OK\nContent-Type: application/octet-stream\nContent-Length: 1048576\n\n");
      writer.flush ();
      int size = random.nextInt (1048576);
      logger.info ("Request received. Sending up to {} bytes.", size);
      byte[] buffer = new byte[1024];
      for (int i = 0; i < size / 1024; i++) {
        random.nextBytes (buffer);
        socket.getOutputStream ().write (buffer);
      }
      random.nextBytes (buffer);
      socket.setSoLinger (true, 0);
      socket.getOutputStream ().write (buffer, 0, size % 1024);
      socket.getOutputStream ().flush ();
    } catch (Exception e) {
      logger.error ("Error while handling request.", e);
    }
  }

}
