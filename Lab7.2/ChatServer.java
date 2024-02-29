import java.net.*;
import java.io.*;
import java.util.concurrent.*;


public class ChatServer {
     public static final int PORT = 2048;

     public static final void main(String[] args) {
          ExecutorService executioner = Executors.newFixedThreadPool(1024);

          try (ServerSocket ss = new ServerSocket(PORT)) {
               while (true) {
                    executioner.submit(new Handler(ss.accept()));
               }
          } catch (IOException ioe) {
               ioe.printStackTrace(System.err);
          }
     }

     private static class Handler implements Runnable {
          private Socket socket;
          
          Handler(Socket socket) {
               this.socket = socket;
          }
         
          public void run() {
               BufferedReader in = null;
               PrintWriter out = null;
               String name = "Robbie";
               String clientName = "";
               try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.println(name);
                    out.flush();
                    clientName = in.readLine();
                    
                    while (true) {
                         String input = null;
                         input = in.readLine();
                         while (!input.equals("-1")) {
                              out.println("Received from " + clientName + ": " + input);
                              input = in.readLine();
                         }
                         out.println("-1");
                         out.flush();
                    }
                  
               } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
               } finally {
                    try {
                         in.close();
                    } catch (Exception e) {
                    }
                   
                    try {
                         out.close();
                    } catch(Exception e) {
                    }
                   
                    try {
                         socket.close();
                    }catch(Exception e) {}
               }
             
          }
     }
}