import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {
    public static void main(String[] args) {
       Socket socket = null;
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter URL here.");
       String url = sc.nextLine();
       
        try {
          socket = new Socket(InetAddress.getLocalHost(),2021);
          System.out.println("Connected on " + socket.getLocalPort());
          URL url2 = new URL(url);
          Scanner s2 = new Scanner(url2.openStream());
          StringBuffer sb = new StringBuffer();
          while(s2.hasNext()){
               sb.append(s2.next() + "\n");
          }
          String result = sb.toString();
          result = result.replaceAll("<[^>]*>", "");
          System.out.println("Result: " + result);

          Path path = Paths.get("/Users/lsnicotra/Desktop/TenBroek/Networking/Lab2/example.txt");

          try {
            Files.writeString(path, result, StandardCharsets.UTF_8);
          }
 
          // Catch block to handle the exception
          catch (IOException ex) {
              System.out.print("Invalid Path");
          }
          
        // PrintStream ps = new PrintStream( new BufferedOutputStream(socket.getOutputStream()));
        // PrintStream ps1 = new PrintStream( new BufferedOutputStream(socket.getOutputStream()));
        //  ps1.println(url);
        //  ps.println("hello World");
        //  ps.flush();
        } catch (IOException ioe){
          ioe.printStackTrace(System.err);
          System.out.println("Invalid URL");
        } finally {
            try {
                 socket.close();
            } catch (Exception e){}
           sc.close();
        }
    }
}