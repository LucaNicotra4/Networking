import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.charset.*;

public class WebServer {
    public static final void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("Accepting connections on port " + serverSocket.getLocalPort());
            ExecutorService pool = Executors.newFixedThreadPool(1000);
            while (true) { 
                try {
                    pool.execute(new HttpHandler(serverSocket.accept()));
                } catch (IOException ioe) {
                    System.err.println("Error Accepting Connection");
                    ioe.printStackTrace(System.err); 
                }
            }
        } catch (IOException ioe) {    
            ioe.printStackTrace(System.err); 
        }
    }
}

class HttpHandler implements Runnable {
    private Socket socket;

    HttpHandler(Socket socket) {
        this.socket = socket;
    }

    public final void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

            ArrayList<String> requestLineHeader = new ArrayList<>();
            //Keeps receiving user input until null or new line
            for(String input = in.readLine(); input != null && input.length() > 0; input = in.readLine()) {
                requestLineHeader.add(input);
            }

            System.out.println();
            System.out.println("Processing Connection");
            String getLine = null;
            for(String requestLine : requestLineHeader) {
                System.out.println(requestLine);
                if(requestLine.startsWith("GET")) {
                    getLine = requestLine;
                }
            }

            String[] parts = getLine.split(" ");
            System.out.println("Parts: " + parts[0]);
            /*If there is a requested file, give requested file, else give index
            index is default file
            changed to 0*/
            String fileName = (parts[1].length() > 1)? parts[1].substring(0) : "default";
            System.out.println("The requested file: " + fileName);


            // byte[] file = "<html><body><p>Hello World</p><body></html>\n\n".getBytes(Charset.forName("UTF-8"));
            byte[] file = null;
            String headerResponse = "200 OK";

            //try to put the requested file into byte array
            try {
                File documents = new File("documents");
                File[] allFiles = documents.listFiles();

                String html = "<html><body>";
                if(fileName.equals("default")){
                    for(File tempfile : allFiles){
                        String path = tempfile.getPath();
                        String name = tempfile.getName();
                        System.out.println(path + " / " + name);
                        String temp = "<a href=\"" + path + "\">" + name + "</a><br>";
                        html = html.concat(temp);
                    }
                    html = html.concat("</body></html>");
                    file = html.getBytes(Charset.forName("UTF-8"));
                }else {
                    for(File tempFile : allFiles){
                        if( ("/documents/" + tempFile.getName()).equals(fileName) ){
                            System.out.println("In if statement");
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));
                            html = "";
                            String line = br.readLine();
                            while(line != null){
                                html = html.concat(line);
                                line = br.readLine();
                            }
                            file = html.getBytes(Charset.forName("UTF-8"));
                            br.close();
                        }
                    }
                }

            } catch(Exception nsfe) {
                System.err.println("FILE NOT FOUND: " + fileName);
                headerResponse = "404 NOT FOUND";
                file = new byte[0];
            }

            //Determine filetype
            String mimeType = "text/html";
            if(fileName.endsWith("png")) {
                mimeType = "image/png";
            }
            
            String header = "HTTP/1.1 " + headerResponse + "\n"
                + "Server: BCWeb 1.0\n"
                + "Keep-Alive: timeout=15, max=100\n"
                + "Content-length: " + file.length + "\n"
                + "Content-type: " + mimeType + "; charset=UTF-8\n\n";
            byte[] headerArr = header.getBytes(Charset.forName("US-ASCII"));
            out.write(headerArr);
            out.flush();
            out.write(file);
            out.flush();

        } catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        } finally {
            try {
                socket.close();
            } catch(Exception e) {}
        }
    }
}