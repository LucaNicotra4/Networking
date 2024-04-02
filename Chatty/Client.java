import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
     public static void main(String[] args){
          final int PORT = 4069;
          Scanner scanner = new Scanner(System.in);
          System.out.println("Enter hostname: ");
          //String hostName = scanner.nextLine();
          String hostName = "10.30.73.221"; //Continue to update!
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
               Scanner scanner = null;
               String clientName = null;

               try{
                    
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    
                    //Sending name to server
                    do{
                         scanner = new Scanner(System.in);
                         System.out.println("Enter your name: ");
                         clientName = scanner.nextLine();
                         writer.println(clientName);
                         writer.flush();

                         String input = reader.readLine();
                         if(input.equals("clientName in use")){
                              clientName = null;
                              System.out.println("-- Name already in use --");
                         }else{
                              System.out.println("Name valid");
                         }
                    } while(clientName == null);

                    while(true){
                         //Send Message
                         String receiver = null;
                         do{
                              System.out.println("Who would you like to send to? ");
                              receiver = scanner.nextLine();
                              writer.println(receiver);
                              writer.flush();

                              String input = reader.readLine();
                              System.out.println("input" + input);
                              if(input.equals("Receiver DNE")){
                                   receiver = null;
                                   System.out.println("Invalid Receiver");
                              }
                         }while(receiver == null);

                         System.out.println("What would you like to say? ");
                         String message = scanner.nextLine();
                         writer.println(message);
                         writer.flush();
                         System.out.println("Message Sent"); //Good to here

                         //Receive Message
                         String input = reader.readLine();
                         if(input != null){
                              System.out.println("\nReceived: " + input + "\n");
                         }else{
                              System.out.println("\nNothing to Receive\n");
                         }
                    }

               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }
     }

     private static class IncomingMessageThread extends Thread{
          public IncomingMessageThread(){

          }

          public void run(){
               while(true){

               }
          }
     }
}
