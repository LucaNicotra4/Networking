import java.io.File;

public class Driver {
     public static final void main(String[] s){
          File[] filesFromRootDirectory = File.listRoots()[0].listFiles();
          //[0].listFiles()
          
          for(File tempFile : filesFromRootDirectory){
               SizeCounter sizeCounter = new SizeCounter(tempFile);
               sizeCounter.start();
          }
          
     }
}
