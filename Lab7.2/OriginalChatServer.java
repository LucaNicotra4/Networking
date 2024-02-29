import java.net.*;
import java.io.*;
import java.util.concurrent.*;


public class OriginalChatServer {
    public static final int PORT = 2048;

    public static final void main(String[] args) {
        ExecutorService executioner = Executors.newFixedThreadPool(1024);

        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                executioner.submit(new Handler(ss.accept()));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }

    private static class Handler implements Runnable {
        private Socket socket;

        Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true) {
                    String input = null;
                    while ((input = in.readLine()) != null) {
                        out.println("Received: " + input);
                        out.flush();
                    }
                }

            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }

                try {
                    out.close();
                } catch(Exception e) {
                }

                try {
                    socket.close();
                }catch(Exception e) {}
            }

        }
    }
}