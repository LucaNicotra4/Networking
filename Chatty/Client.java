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
          // Scanner scanner = new Scanner(System.in);
          System.out.println("Enter hostname: ");
          // String hostName = scanner.nextLine();
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
          // scanner.close();
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
               // Scanner scanner = null;

               try{
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    // scanner = new Scanner(System.in);
                    //BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));

                    //Get client name
                    boolean accepted = false;
                    // System.out.println("What is your name? ");
                    // // scanner.useDelimiter(System.lineSeparator());
                    // // clientName = scanner.nextLine();
                    // clientName = KeyboardReader.readLine();
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
                         while(!input.equals("--END--")){
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
