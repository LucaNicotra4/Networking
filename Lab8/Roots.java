import java.io.*;
import java.util.LinkedList;

public class Roots extends Thread {
     //private File root;
     //private File[] files;

     public Roots (File roots) {
          //this.root = roots;
     }

     @Override
     public void run() {
          File[] roots = File.listRoots(); //Starting point, original root
          File[] rootFiles = roots[0].listFiles(); //All files under original root
          File root = roots[0]; // "/" directory
          LinkedList<File> path = new LinkedList<File>(); //Used to keep track of filepath
          path.add(roots[0]);

          //If there aren't any roots, kill thread
          if (rootFiles == null) {
              return;
          }

          //Print out all files underneath main root
          listSubs(root);

          //Prompting user for first directory of choice
          File requestedFile = null;
          while(requestedFile == null){
               String input = KeyboardReader.readLine("Please choose a directory: ");
               System.out.println();
               for(File file : rootFiles){
                    //if the requested directory is found
                    if(file.getName().equals(input)){
                         requestedFile = file;
                         path.add(file);
                    }
               }
               if(requestedFile == null) System.out.println("Invalid directory");
          }

          //Start prompting for user options
          boolean canRun = true;
          while(canRun){
               //List subdirectories and files
               listSubs(path.getLast());

               //Prompt user to command
               String input = null;
               while(input == null){
               System.out.println("1. Enter the name of one of the subdirectories <cd FILENAME>");
               System.out.println("2. Go back one level <rt>");
               System.out.println("3. Execute one of the executable files <ex FILENAME>");
               System.out.println("4. Quit <qt>");
               input = KeyboardReader.readLine("Please enter a command");
               }

               String command = input.substring(0, 2);
               String fileName = null;
               if(input.length() > 2) fileName = input.substring(3);
               switch (command) {
                    //Change to requested directory
                    case("cd"): 
                         //check for requested file in current directory
                         for(File tempFile : path.getLast().listFiles()){
                              if(tempFile.getName().equals(fileName)){
                                   //Check if file is a directory, warn if not
                                   if(tempFile.isDirectory()){
                                        path.add(tempFile);
                                   }else{
                                        System.out.println("File is not a directory");
                                   }
                                   break;
                              }
                         }
                         System.out.println("Could not find file");
                         break;
                    //Go up a file in the directory
                    case("rt"):
                         //Return back up path if not at base root '/'
                         if(path.getLast() != root) path.removeLast();
                         break;
                    //Execute an executable file
                    case("ex"):
                         for(File tempFile : path.getLast().listFiles()){
                              if(tempFile.getName().equals(fileName)){
                                   //Check if file is a directory, warn if not
                                   if(tempFile.canExecute()){
                                        //Execute file

                                        ProcessBuilder builder = new ProcessBuilder("sh", "-c", "ls"); 
                                        builder.command(tempFile.getAbsolutePath());
                                        Process process = null;
                                        try{
                                             process = builder.start();
                                             
                                             //Read output list
                                             BufferedReader reader = 
                                                  new BufferedReader(
                                                       new InputStreamReader(
                                                            process.getInputStream()));
                                             String line = reader.readLine(); 
                                             while (line != null) { 
                                                 System.out.println(line);
                                                 line = reader.readLine();
                                             } 

                                             //Exiting
                                             int exitCode = process.waitFor(); 
                                             System.out.println("\nExited with error code : " + exitCode);
                                        }catch(IOException ioe){
                                             ioe.printStackTrace();
                                        }catch(InterruptedException ie){}
                                   }else{
                                        System.out.println("File is not executable");
                                   }
                                   break;
                              }
                         }
                         break;
                    //Quit program
                    case("qt"):
                         canRun = false;
                         break;
                    default:
                         System.out.println("Invalid command");
                         break;
               }//end of switch statement
               
          }
               
          
     }

     public void listSubs(File file){
          if(!file.isDirectory()) return;

          int count = 0;
          File[] subs = file.listFiles();
          System.out.println("\u001b[31mCURRENT DIRECTORY: " + file.getName() + "\u001b[37m");
          for (File tempFile : subs) {
               if(count == 5){
                    System.out.println();
                    count = 0;
               } 
               if (tempFile.isDirectory()) { //Directories listed in red
                    System.out.print("\u001b[31m" + tempFile.getName() + "     \u001b[37m");
               } else if(tempFile.canExecute()){ //Executables listed in green
                    System.out.print("\u001b[32m" + tempFile.getName() + "     \u001b[37m");
               } else { //Everything else in white
                    System.out.println(tempFile.getName());
               }
               count++;
          }
          System.out.println("\n");
     }
}

// TODO: List executables. Account for user input. Complete the switch statement. Figure out a way to get java to run commands
// ProcessBuilder class for operating system commands