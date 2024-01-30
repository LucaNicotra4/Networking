package Assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class to read given log file from system
 * @author Luca Nicotra
 */
public class Driver{
     public static final void main(String[] s){
          //Declare readers
          File file;
          FileReader fr;
          BufferedReader br;
          ArrayList<String> logs;
          //Multiple file options
          String path = "/Users/lsnicotra/Library/Logs/DiscRecording.log";
          String path2 = "/Users/lsnicotra/Library/Logs/zoominstall.log";
          String path3 = "/Users/lsnicotra/Library/Logs/fsck_hfs.log";
          try{
               //Instantiate Readers
               file = new File(path3);
               fr = new FileReader(file);
               br = new BufferedReader(fr);
               logs = new ArrayList<String>();

               //Read file line by line
               String line = br.readLine();
               while(line != null){
                    logs.add(line);
                    line = br.readLine();
               }

               //Print out last 10 lines
               int count = 0;
               while(count < 10 && (logs.size() - count) > 0){
                    System.out.print("Log " + (logs.size() - count) + ": ");
                    System.out.println(logs.get(logs.size() - count - 1));
                    count++;
               }
          }catch(Exception e){
               e.printStackTrace();
          }
     
     }
}