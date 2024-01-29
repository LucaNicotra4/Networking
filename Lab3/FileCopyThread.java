import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileCopyThread extends Thread{
     String input;
     String output;

     public FileCopyThread(String inputName, String outputName){
          input = (inputName);
          output = (outputName);
     }

     @Override
     public void run(){
          try{
               FileReader fr = new FileReader(input);
               FileWriter fw = new FileWriter(output);
               BufferedReader br = new BufferedReader(fr);
               BufferedWriter bw = new BufferedWriter(fw);

               String line;
               System.out.println("Starting copy from " + input + " to " + output);
               while((line = br.readLine()) != null){
                    bw.write(line + "\n");
               }

               bw.flush();
               br.close();
               bw.close();
          }catch (Exception e){
               e.printStackTrace();
          }finally{
               System.out.println("Copy from " + input + " to " + output + " done");
          }
          
     }
}