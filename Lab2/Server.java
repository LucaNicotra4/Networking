import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static final void main (String[] args) {
        ServerSocket sSocket = null;
        Socket socket = null;
        try {
        sSocket = new ServerSocket(2021);
        socket = sSocket.accept();
        System.out.println("Connected on " + socket.getLocalPort());
        DataInputStream Dis = new DataInputStream(new BufferedInputStream(socket.getInputStream())) ;
        System.out.println(Dis.readLine()); 
         

        } catch(IOException ioe){
            ioe.printStackTrace(System.err);
        } finally{
            try {
                sSocket.close();
            } catch (Exception e){}
            try {
                socket.close();
            } catch(Exception e){
            
            }
            
        }
        
    }
}
