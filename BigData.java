/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigdata;
import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 *
 * @author Lenovo-pc
 */
public class BigData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      URL u;
      InputStream is = null;
      BufferedReader dis;
      String s;

      
      try {
	    File file = new File("wikipedia.keys");
	    FileReader fileReader = new FileReader(file);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedWriter writer = new BufferedWriter(new FileWriter("wikipedia.data"));
            File fileout =new File("wikipedia.data");
	    String line;
	    while ((line = bufferedReader.readLine()) != null  && fileout.length() <= 11 * 1000000 ) {
		 System.out.println(line);
                 try {

                   u = new URL("https://en.wikipedia.org/wiki/"+line+"?action=raw");
                   
                   is = u.openStream();         // throws an IOException
                   dis = new BufferedReader(new InputStreamReader(is));
                   while ((s = dis.readLine()) != null ) {
                     if ( Objects.equals("==References==", s ) == true || Objects.equals("== References ==", s ) == true )
                     {
                        System.out.println("byyyyyyyyyyyyeeeeeeeeeeeeeee");
                        break;
                     }
                     writer.append(s);
                     writer.append("\n");
        
                     //System.out.println(s);
                   }
 
     

               } catch (MalformedURLException mue) {

                System.out.println("Ouch - a MalformedURLException happened.");
                mue.printStackTrace();
                System.exit(1);

             } catch (IOException ioe) {

               System.out.println("Oops- an IOException happened.");
               ioe.printStackTrace();
               System.exit(1);

            } finally {

               try {
               is.close();
              } catch (IOException ioe) {
                // just going to ignore this one
              }

      } // end of 'finally' clause */
	    }
             writer.close();
	    fileReader.close();
        } 
      catch (IOException e) {
	    e.printStackTrace();
	}
    
      FileInputStream instream = null;
      FileOutputStream outstream = null;
 
    	try{
    	    File infile =new File("wikipedia.data");
    	    File outfile =new File("raw.data");
            
            outstream = new FileOutputStream(outfile);
            
            for ( int i = 0 ; i < 26 ; i++) {
    	    instream = new FileInputStream(infile);
    	    
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    /*copying the contents from input stream to
    	     * output stream using read and write methods
    	     */
    	    while ((length = instream.read(buffer)) > 0){
    	    	outstream.write(buffer, 0, length);
    	    }

    	    //Closing the input/output file streams
            }
    	    instream.close();
    	    outstream.close();

    	    System.out.println("File copied successfully!!");
 
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	 }

    }
    
}
