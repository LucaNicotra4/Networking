package Assignment1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;


public class Driver{
     public static final void main(String[] s){
          String workingPath = "/Users/lsnicotra/Desktop/example.txt";
          String realPath = "/Users/lsnicotra/Library/Logs/zoominstall.log";
          try{
               File file = new File(realPath);
               // FileInputStream fis = new FileInputStream(file);
               FileReader fr = new FileReader(file);
               BufferedReader br = new BufferedReader(fr);
               System.out.println(br.readLine());
          }catch(Exception e){
               e.printStackTrace();
          }
          
     }
}