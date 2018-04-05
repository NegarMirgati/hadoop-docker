/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.Objects;


/* from Hdfs wrtier */
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.ToolRunner;
/**/


public class BigData {

    /**
     * @param args the command line arguments
     */
    static class HdfsWriter extends Configured implements Tool {

    public static final String FS_PARAM_NAME = "fs.defaultFS";

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("HdfsWriter [local input path] [hdfs output path]");
            return 1;
        }

        String localInputPath = args[0];
        Path outputPath = new Path(args[1]);

        Configuration conf = getConf();
        System.out.println("configured filesystem = " + conf.get(FS_PARAM_NAME));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            System.err.println("output path exists");
            return 1;
        }
        OutputStream os = fs.create(outputPath);
        InputStream is = new BufferedInputStream(new FileInputStream(localInputPath));
        IOUtils.copyBytes(is, os, conf);
        return 0;
    }
}
    public static void main(String[] args) throws Exception {
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
		 //System.out.println(line);
                 try {

                   u = new URL("https://en.wikipedia.org/wiki/"+line+"?action=raw");
                   
                   is = u.openStream();         // throws an IOException
                   dis = new BufferedReader(new InputStreamReader(is));
                   while ((s = dis.readLine()) != null ) {
                     if ( Objects.equals("==References==", s ) == true || Objects.equals("== References ==", s ) == true )
                     {
                        //System.out.println("byyyyyyyyyyyyeeeeeeeeeeeeeee");
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
          String[] my_args = {"raw.data", "/user/training/raw_copyyy.data"}; 
          HdfsWriter hdfs_obj = new HdfsWriter();
          int returnCode = ToolRunner.run(hdfs_obj, my_args);
          System.exit(returnCode);
 
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	 }
 
         
    }
     
}
