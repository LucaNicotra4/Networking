import java.net.*;
import java.io.*;

public class OriginalChatClient {
    public static final void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: Client Server_IP");
            return;
        }
        Socket socket = null;
        try {
            InetAddress server = InetAddress.getByName(args[0]);
            socket = new Socket(server, ChatServer.PORT);
            BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sockOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            while(true) {
                System.out.println("enter a message to send:");
                sockOut.println(keyIn.readLine());
                sockOut.flush();
                System.out.println(sockIn.readLine());
            }

        } catch (UnknownHostException uhe) {
            System.out.println("Sorry, I couldn't find the server " + args[0]);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }

    }
}