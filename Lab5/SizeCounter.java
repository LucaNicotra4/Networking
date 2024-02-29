import java.io.File;

public class SizeCounter extends Thread{
     File file;
     File[] fileList;
     private long time;
     private long totalSize;

     public SizeCounter(File file) throws IllegalArgumentException{
          this.file = file;
          if(!this.file.isDirectory()){
               System.out.println(file.getAbsolutePath() + " is invalid");
               throw new IllegalArgumentException();
          }
          totalSize = 0;
     }

     public void run(){
          if(file != null){
               File[] fileList =  file.listFiles();
               long startTime = System.nanoTime();
               for(File tempFile : fileList){
                    if(!tempFile.isDirectory()){
                         totalSize += tempFile.length();
                    }else{
                         SizeCounter sc = new SizeCounter(tempFile);
                         sc.start();
                    }
               }
               long endTime = System.nanoTime();
               time = endTime - startTime;
          }
          System.out.println("\n" + "File: " + file.getAbsolutePath() + "\n" +
           "Time Elapsed: " + time + "\n" + "Total Space: " + (totalSize/1E3) + " KB\n");
     }

}