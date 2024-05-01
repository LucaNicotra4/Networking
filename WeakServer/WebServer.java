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
                System.out.println("About to request");
                File requestedFile = findFile(requestedFileName, site);
                System.out.println("wait");
                if(requestedFile == null) throw new FileNotFoundException();
                System.out.println(requestedFile);

                //Read file if found
                String html = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(requestedFile)));
                String line = br.readLine();
                while(line != null){
                    html = html.concat(line);
                    line = br.readLine();
                }
                file = html.getBytes(Charset.forName("UTF-8"));
                br.close();

                // if(fileName.equals("index")){
                //     for(File tempfile : allFiles){
                //         String path = tempfile.getPath();
                //         String name = tempfile.getName();
                //         System.out.println(path + " / " + name);
                //         String temp = "<a href=\"" + path + "\">" + name + "</a><br>";
                //         html = html.concat(temp);
                //     }

                //     html = html.concat("</body></html>");
                //     file = html.getBytes(Charset.forName("UTF-8"));
                // }else {
                //     for(File tempFile : allFiles){
                //         if( ("/documents/" + tempFile.getName()).equals(fileName) ){
                //             System.out.println("In if statement");
                //             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));
                //             html = "";
                //             String line = br.readLine();
                //             while(line != null){
                //                 html = html.concat(line);
                //                 line = br.readLine();
                //             }
                //             file = html.getBytes(Charset.forName("UTF-8"));
                //             br.close();
                //         }
                //     }
                // }

            } catch(Exception nsfe) {
                System.err.println("FILE NOT FOUND: " + requestedFileName);
                headerResponse = "404 NOT FOUND";
                file = new byte[0];
            }

            //Determine filetype
            String mimeType = "text/html";
            if(requestedFileName.endsWith("png")) {
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

    private File findFile(String path, File directory){
        File[] allFiles = directory.listFiles();
        String[] pathList = path.split("/"); //split by each directory
        String request = pathList[1];
        for(String string : pathList) System.out.print(string + " : ");

        //Removing current directory for recursion
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i < pathList.length; i++) {
            sb.append(pathList[i]);
            sb.append("/");
        }
        String currentPath = sb.toString();
        System.out.println("Current Path: " + currentPath);

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

    // private File findFile(String requestedFileName){
    //     File site = new File("site");
    //     File[] allFiles = site.listFiles();

    //     //Find requested file in site folder
    //     String [] requestParts = requestedFileName.split("/");
    //     File requestedFile = null;
    //     Boolean found = false;
    //     //Search through each level of requestedFileName's path
    //     for(int i = 0; i < requestParts.length; i++){
    //         if(requestParts[i].equals("")) continue; //skip blanks
    //         //Checking each file in 'site' for a match to the partition of the requested File
    //         for(File tempFile : allFiles){
    //             System.out.println("\u001b[31mChecking: " + requestParts[i] + " and " + tempFile.getName() + "\u001b[0m");//\u001b[0m
    //             if(tempFile.getName().equals(requestParts[i])){ //if a match is found
    //                 if(i == (requestParts.length - 1)){ //if it is exact requested file (not higher up in the pathname)
    //                     found = true;
    //                     return tempFile;
    //                 } else if(tempFile.isDirectory()){ //if needing to go further into directory
    //                     return findFile();
    //                 }
    //                 i++;
    //                 continue;
    //             }
    //         }
    //     }
    //     return null;
    // }

}