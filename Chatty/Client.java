import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Client {
     public static void main(String[] args){
          final int PORT = 4069;
          Scanner scanner = new Scanner(System.in);
          System.out.println("Enter hostname: ");
          String hostName = scanner.nextLine();
          Socket socket = null;

          try{
               socket = new Socket(hostName, PORT);
               System.out.println("Connected");
          }catch(IOException ioe){
               ioe.printStackTrace();
          }

          ClientThread thread = new ClientThread(socket);
          thread.start();
          scanner.close();
     }

     private static class ClientThread extends Thread{
          Socket socket;

          public ClientThread(Socket socket){
               this.socket = socket;
          } 

          public void run(){
               BufferedReader reader = null;
               PrintWriter writer = null;
               String clientName = null;
               Scanner scanner = null;
               Queue<String> printQueue = new LinkedList<String>();

               try{
                    
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    scanner = new Scanner(System.in);

                    //Sending name to server
                    System.out.println("Who are you sending to? ");
                    while(!scanner.hasNextLine()){}
                    clientName = scanner.nextLine();
                    System.out.println("we here");
                    writer.println(clientName);
                    writer.flush(); 
                    do{

                         String input = reader.readLine();
                         while(input != null) {
                              if(input.startsWith("--INCOMING--")){
                                   printQueue.add(input.substring(12));
                              } else if(input.equals("clientName in use")){
                                   clientName = null;
                                   System.out.println("-- Name already in use --");
                              }else{
                                   System.out.println("Name valid");
                              }
                              input = reader.readLine();
                         }
                    } while(clientName == null);

                    while(true){
                         //Send Message
                         String receiver = null;
                         do{
                              receiver = KeyboardReader.readLine("Who would you like to send to?");
                              writer.println(receiver);
                              writer.flush();

                              String input = reader.readLine();
                              while(input != null){
                                   if(input.startsWith("--INCOMING--")) {
                                        printQueue.add(input.substring(12));
                                   } else if(input.equals("Receiver DNE")){
                                        receiver = null;
                                        System.out.println("Invalid Receiver");
                                   }
                                   input = reader.readLine();
                              }
                         }while(receiver == null);

                         String message = KeyboardReader.readLine("What would you like to say? ");
                         writer.println(message);
                         writer.flush();
                         System.out.println("Message Sent"); //Good to here

                         //Print received messages
                         System.out.println("\n--Received--");
                         for(String str : printQueue){
                              System.out.println(str);
                         }
                         printQueue.clear();
                         System.out.println();
                    }

               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }
     }

}
