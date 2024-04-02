import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;

public class Server extends Thread{
     public static void main(String[] args){
          final int PORT = 4069;
          HashMap<String, Socket> clientMap = new HashMap<String, Socket>();
          ExecutorService pool = Executors.newFixedThreadPool(1000); //ThreadPool
          ServerSocket serverSocket = null;

          try{
               serverSocket = new ServerSocket(PORT);
               while(true){
                    pool.execute(new ClientConnectionHandler(serverSocket.accept(), clientMap));
               }
          }catch(IOException ioe){
               ioe.printStackTrace();
          }finally{
               try{
                    serverSocket.close();
               }catch(Exception e){}
          }
          
     }

     private static class ClientConnectionHandler implements Runnable{
          private Socket socket;
          HashMap<String, Socket> clientMap;
          String clientName;

          public ClientConnectionHandler(Socket socket, HashMap<String, Socket> clientMap){
               this.socket = socket;
               this.clientMap = clientMap;
          }

          public void run(){
               try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));                    
                    PrintWriter writer2 = null;

                    //Accounting for repeated clientName
                    do{
                         String input = null;
                         while(input == null){
                              input = reader.readLine();
                         }
                         clientName = input;
                         
                         //Account for repeated name
                         if(clientMap.containsKey(clientName)){
                              writer.println("clientName in use");
                         }else{
                              writer.println("all good");
                         }
                         writer.flush();
                    } while(clientMap.containsKey(clientName));

                    clientMap.put(clientName, socket);

                    while(socket != null){
                         /* Ask: Who would you like to send to?
                         Determining Receiver */
                         String receiver = null;
                         System.out.println("Waiting to receive");
                         do{
                              receiver = reader.readLine();
                              if(receiver != null && !clientMap.containsKey(receiver)){
                                   receiver = null;
                                   System.out.println("Receiver DNE");
                                   writer.println("Receiver DNE");
                                   writer.flush();
                              }else{
                                   writer.println("Valid receiver");
                                   writer.flush();
                              }
                         }while(receiver == null);
                         System.out.println("Receiver: " + receiver + " / Waiting to receive message");

                         /* Ask: What are the contents of the message?
                         Determining Message */
                         String message = null;
                         while(message == null){
                              message = reader.readLine();
                         }

                         //Sending message to receiver
                         System.out.println("Sending \"" + message + "\" to " + receiver);
                         Socket tempSocket = clientMap.get(receiver);
                         writer2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(tempSocket.getOutputStream())));
                         writer2.println(clientName + ": " + message);
                         writer2.flush();
                    }
                    System.out.println(clientName + " disconnected");
               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }//end of run
     }
}
