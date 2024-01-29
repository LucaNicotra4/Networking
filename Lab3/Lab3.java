/**
 * Lab3 for CSCI-4400-A
 * Creating threads to read and write
 * to and from .txt files
 * @author Luca Nicotra
 * @author John/Jack Vanderzanden
 */
public class Lab3{
     public static final void main(String[] s){
          FileCopyThread[] threads = new FileCopyThread[10];

          String inputName = "FileCopyThread.java";
          for(int i = 0; i < threads.length; i++){
               threads[i] = new FileCopyThread(inputName  + i + ".txt", inputName + i + "b.txt");
          }
          for(int i = 0; i < threads.length; i++){
               threads[i].start();
          }
          for(int i = 0; i < threads.length; i++){
               while(threads[i].isAlive()){}
          }
          System.out.println("All threads done");
     }
}