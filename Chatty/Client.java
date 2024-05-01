import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Client side of remote chat program
 * To be used with Server class
 * 
 * By Luca Nicotra
 * @LucaNicotra4
 */
public class Client {
     public static void main(String[] args){
          final int PORT = 4069;
          System.out.println("Enter hostname: ");
          String hostName = KeyboardReader.readLine();
          Socket socket = null;

          try{
               socket = new Socket(hostName, PORT);
               System.out.println("Connected");
          }catch(IOException ioe){
               ioe.printStackTrace();
          }

          ClientThread thread = new ClientThread(socket);
          thread.start();
     }

     private static class ClientThread extends Thread{
          Socket socket;
          String clientName;

          public ClientThread(Socket socket){
               this.socket = socket;
               this.clientName = null;
          }

          public void run(){
               BufferedReader reader = null;
               PrintWriter writer = null;

               try{
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

                    //Get client name
                    boolean accepted = false;
                    do{
                         clientName = KeyboardReader.readLine("What is your name? ");
                         writer.println(clientName);
                         writer.flush();
                         String input = reader.readLine();
                         if(input.equals("Valid clientName")){
                              accepted = true;
                         }else{
                              System.out.println("Name already in use");
                              System.out.println("What is your name? ");
                         }
                         System.out.println("Valid name");

                    }while(!accepted);

                    //Loop for repeatedly prompting for new messages to send and receive
                    boolean canRun = true;
                    while(canRun){
                         //Determine who to send to
                         accepted = false;
                         do{
                              String input = KeyboardReader.readLine("Who would you like to send to? ");
                              writer.println(input);
                              writer.flush();
                              input = reader.readLine();
                              if(input.equals("Valid receiverName")){
                                   accepted = true;
                              }else{
                                   System.out.println("Receiver DNE");
                              }
                         }while(!accepted);

                         //Determine message to send
                         String input = KeyboardReader.readLine("What would you like to say? ");
                         writer.println(clientName + ": " + input);
                         writer.flush();

                         //Print received
                         input = reader.readLine();
                         while(input != null && !input.equals("--END--")){
                              System.out.println(input);
                              input = reader.readLine();
                         }
                         System.out.println("--END--\n");
                    }
               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }
     }
}
