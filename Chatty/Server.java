import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;

public class Server extends Thread{
     public static void main(String[] args){
          final int PORT = 4069;
          ArrayList<String> clientList = new ArrayList<String>(); //Name key, for String message
          HashMap<String, String> messageMap = new HashMap<String, String>();
          ExecutorService pool = Executors.newFixedThreadPool(1000); //ThreadPool
          ServerSocket serverSocket = null;

          try{
               serverSocket = new ServerSocket(PORT);
               while(true){
                    pool.execute(new ClientConnectionHandler(serverSocket.accept(), clientList, messageMap));
               }
          }catch(IOException ioe){
               ioe.printStackTrace();
          }finally{
               try{
                    serverSocket.close();
               }catch(Exception e){}
          }
          
     }

     //While handle client input and send client messages from shared array
     private static class ClientConnectionHandler implements Runnable{
          private Socket socket;
          ArrayList<String> clientList;
          HashMap<String, String> messageMap;
          String clientName;
          String receiverName;

          public ClientConnectionHandler(Socket socket, ArrayList<String> clientList, HashMap<String, String> messageMap){
               this.socket = socket;
               this.clientList = clientList;
               this.messageMap = messageMap;
               this.clientName = null;
               this.receiverName = null;
          }

          public void run(){
               BufferedReader reader = null;
               PrintWriter writer = null;
               try{
                    //Establish connections
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

                    //Get client name
                    System.out.println("Waiting for client name");
                    do{
                         //Client asks: What is your name?
                         while(clientName == null){
                              clientName = reader.readLine();
                         }

                         //Check for repeated clientName
                         if(clientList.contains(clientName)){
                              writer.println("clientName in use");
                         }else{
                              writer.println("Valid clientName");
                         }
                         writer.flush();
                    } while(clientList.contains(clientName));
                    clientList.add(clientName);

                    while(socket != null){
                         //Determine who to send to
                         System.out.println("Waiting to receive receiverName");
                         do{
                              //Client asks: Who would you like to send to?
                              receiverName = reader.readLine();
                              if(receiverName != null){
                                   if(clientList.contains(receiverName)){
                                        writer.println("Valid receiverName");
                                   }else{
                                        writer.println("Receiver name DNE");
                                        receiverName = null;
                                   }
                              }
                              writer.flush();
                         }while(receiverName == null);
                         
                         //Determine contents of message
                         System.out.println("Determining message contents");
                         String message = null;
                         //Client asks: what would you like to say?
                         message = reader.readLine();
                         while(message == null){
                              message = reader.readLine();
                         }
                         
                         //"Send" message
                         messageMap.put(receiverName, message);

                         //Send out received messages to client
                         writer.println("--Received--");
                         if(messageMap.containsKey(clientName)){
                              writer.println(messageMap.get(clientName));
                              messageMap.remove(clientName);
                         }
                         writer.println("--END--");
                         writer.flush();
                    }
               }catch(IOException ioe){
                    ioe.printStackTrace();
               }finally{
                    try{
                         reader.close();
                         writer.close();
                         socket.close();
                    }catch(Exception e){}
               }
          }//end of run
     }
}
