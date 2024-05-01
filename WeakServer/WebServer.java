import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
            System.out.println("END OF REQUEST");

            String[] parts = getLine.split(" ");
            System.out.println("Parts: " + parts[0]);
            /*If there is a requested file, give requested file, else give index
            index is default file
            changed to 0*/
            String requestedFileName = (parts[1].length() > 1)? parts[1].substring(0) : "site/index.html";
            System.out.println("\u001b[32mThe requested file: " + requestedFileName + "\u001b[0m");


            // byte[] file = "<html><body><p>Hello World</p><body></html>\n\n".getBytes(Charset.forName("UTF-8"));
            byte[] file = null;
            String headerResponse = "200 OK";

            //try to put the requested file into byte array
            try {
                
                File site = new File("/Users/lsnicotra/Desktop/TenBroek/Networking/AssignmentsLabs/WeakServer/site");
                File requestedFile = findFile(requestedFileName, site);
                if(requestedFile == null) throw new FileNotFoundException();
                System.out.println(requestedFile);

                //Read file if found
                if(!requestedFileName.endsWith("jpg")){
                    
                        String html = "";
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(requestedFile)));
                        String line = br.readLine();
                        while(line != null){
                            html = html.concat(line + "\n");
                            line = br.readLine();
                        }
                        file = html.getBytes(Charset.forName("UTF-8"));
                        br.close();
                    
                }else{
                    BufferedImage image = ImageIO.read(requestedFile);
                    ImageIO.write(image, "jpg", out);
                }

            } catch(Exception nsfe) {
                System.err.println("FILE NOT FOUND: " + requestedFileName);
                headerResponse = "404 NOT FOUND";
                file = new byte[0];
            }

            //Determine filetype
            String mimeType = "text/html";
            if(requestedFileName.endsWith("png")) {
                mimeType = "image/png";
            }else if(requestedFileName.endsWith("jpg") || requestedFileName.endsWith("jpeg")){
                mimeType = "image/jpeg";
            }else if(requestedFileName.endsWith("js")){
                mimeType = "text/javascript";
            }else if(requestedFileName.endsWith("css")){
                mimeType = "text/css";
            }else if(requestedFileName.endsWith("woff")){
                mimeType = "font/woff";
            }else if(requestedFileName.endsWith("woff2")){
                mimeType = "font/woff2";
            }else if(requestedFileName.endsWith("eot")){
                mimeType = "application/vnd.ms-fontobject";
            }else if(requestedFileName.endsWith("svg")){
                mimeType = "image/svg+xml";
            }else if(requestedFileName.endsWith("ttf")){
                mimeType = "font/ttf";
            }else if(requestedFileName.endsWith("otf")){
                mimeType = "font/otf";
            }else if(requestedFileName.endsWith("bin")){
                mimeType = "application/octet-stream";
            }else if(requestedFileName.endsWith("otf")){
                mimeType = "font/otf";
            }
            
            int fileLength = (file == null)? 0 : file.length;
            String header = "HTTP/1.1 " + headerResponse + "\n"
                + "Server: BCWeb 1.0\n"
                + "Keep-Alive: timeout=15, max=100\n"
                + "Content-length: " + fileLength + "\n"
                + "Content-type: " + mimeType + "; charset=UTF-8\n\n";
            byte[] headerArr = header.getBytes(Charset.forName("US-ASCII"));
            out.write(headerArr);
            out.flush();
            if(file != null) out.write(file);
            out.flush();

        } catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        } finally {
            try {
                socket.close();
            } catch(Exception e) {}
        }
    }

    private File findFile(String path, File directory){
        File[] allFiles = directory.listFiles();
        String[] pathList = path.split("/"); //split by each directory
        String request = pathList[1];
        
        //cut off version for font packages
        if(request.endsWith("?v=4.6.3")){
            System.out.println("Altering");
            String[] temp = request.split("\\?");
            System.out.println("sofdsf");
            request = temp[0];
            System.out.println("Altered: " + request);
        }
        
        //Removing current directory for recursion
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i < pathList.length; i++) {
            sb.append(pathList[i]);
            sb.append("/");
        }
        String currentPath = sb.toString();
        //System.out.println("Current Path: " + currentPath);

        //Search for request in current directory
        for(File file : allFiles){
            System.out.println("\u001b[31mChecking: " + request + " and " + file.getName() + "\u001b[0m");
            if(request.equals(file.getName())){ //if match is found
                if(file.isDirectory()){ //if file is a directory
                    return findFile(currentPath, file);
                }else{
                    System.out.println("\u001b[32mFILE FOUND\u001b[0m");
                    return file;
                }
            }
        }
        return null; //if no match is found
    }

}