import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.io.*;
//192.168.248.251
public class ChatClient{
     public static Semaphore semaphore;
     public static final void main(String[] args) {
          Scanner scanner = new Scanner(System.in);
          System.out.println("Usage: Client Server_IP");
          System.out.print("Enter name: ");
          String name = scanner.nextLine();
          System.out.print("Enter ip: ");
          String ip = scanner.nextLine();
          semaphore = new Semaphore(1);          
          
          Socket socket = null;
          try {
               //Connect to Server
               InetAddress server = InetAddress.getByName(ip);
               socket = new Socket(server, ChatServer.PORT);
               System.out.println("Connected");
               //Semaphore sem = new Semaphore(1);

               outputThread ot = new outputThread(socket, name);
               ot.start();

               try{
                    Thread.sleep(100);
               }catch(InterruptedException ie){}
               
               inputThread it = new inputThread(socket);
               it.start();

               while(true){}

          } catch (UnknownHostException uhe) {
               System.out.println("Sorry, I couldn't find the server " + ip);
          } catch (IOException ioe) {
               ioe.printStackTrace(System.err);
          } finally {
               try {
                    socket.close();
               } catch (Exception e) {}
               scanner.close();
          }
     }

     private static class outputThread extends Thread{
          BufferedReader keyIn;
          PrintWriter sockOut;

          public outputThread(Socket socket, String name){

               try{
                    keyIn = new BufferedReader(new InputStreamReader(System.in));
                    sockOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                    //Send name and confirm connection
                    sockOut.println(name);
                    sockOut.flush();

               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }

          public void run(){
               while(true){
                    if(semaphore.availablePermits() > 0){
                         //System.out.println("Output Acquiring Semaphore..." + semaphore.availablePermits());
                         try{
                              semaphore.acquire();
                         }catch(InterruptedException ie){}
                         System.out.println("Output Acquired: " + semaphore.availablePermits());
                         
                         try{

                              System.out.println("enter a message to send:");
                              sockOut.println(keyIn.readLine());
                              sockOut.println("-1");
                              sockOut.flush();

                         }catch(IOException ioe){
                              ioe.printStackTrace();
                         }finally{
                              semaphore.release();
                              System.out.println("Output Released Semaphore: " + semaphore.availablePermits());
                              try{
                                   Thread.sleep(500);
                              }catch(InterruptedException ie){}
                         }
                    }
               }
          }
          
     }

     private static class inputThread extends Thread{
          private BufferedReader sockIn;
          private String serverName;

          public inputThread(Socket socket){

               try{
                    sockIn = null;
                    sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    serverName = sockIn.readLine();
               }catch(IOException ioe){
                    ioe.printStackTrace();
               }
          }

          public void run(){
               while(true){
                    try{
                         Thread.sleep(100);
                    }catch(InterruptedException ie){}
                    if(semaphore.availablePermits() > 0){
                         //System.out.println("Input Acquiring Semaphore..." + semaphore.availablePermits());
                         try{
                              semaphore.acquire();
                         }catch(InterruptedException ie){}
                         System.out.println("Input Acquired: " + semaphore.availablePermits());

                         try{
                              String input = null;
                              input = sockIn.readLine();
                              while (input != null && !input.equals("-1")) {
                                   System.out.println(serverName + ": " + input);
                                   input = sockIn.readLine();
                              }
                         }catch(IOException ioe){
                              ioe.printStackTrace();
                         }finally{
                              semaphore.release();
                              System.out.println("Input Released Semaphore: " + semaphore.availablePermits());
                              try{
                                   Thread.sleep(500);
                              }catch(InterruptedException ie){}  
                         }
                    }
               }
          }
     }
}