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
          LinkedList<File> path = new LinkedList<File>(); //Used to keep track of filepath
          path.add(roots[0]);

          //If there aren't any roots, kill thread
          if (rootFiles == null) {
              return;
          }

          //Print out all files underneath main root
          int count = 0;
          System.out.println("\u001b[31mCurrent Directory: " + path.peek() + "\u001b[37m");
          for (File file : rootFiles) {
               if(count == 5){
                    System.out.println();
                    count = 0;
               } 
               if (file.isDirectory()) {
                    System.out.print(file.getName() + "     ");
               } else if(true){
                    System.out.print(file.getName() + "     ");
               }
               count++;
          }
          System.out.println("\n");

          //Prompting user for first directory of choice
          File requestedFile = null;
          while(requestedFile == null){
               String input = KeyboardReader.readLine("Please choose a directory: ");
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
          String input = null;
          while(input == null){
               System.out.println("1. Enter the name of one of the subdirectories");
               System.out.println("2. Go back one level ");
               System.out.println("3. Execute one of the executable files");
               System.out.println("4. Quit");
               input = KeyboardReader.readLine("Please enter a command");

               // switch (input) {
               //      case ():

               //           break;
               //      case():

               //           break;
               //      case():

               //           break;
               //      case():

               //           break;
               //      default:

               //           break;
               // }
          }
          
     }
}

// TODO: List executables. Account for user input. Complete the switch statement. Figure out a way to get java to run commands
// ProcessBuilder class for operating system commands