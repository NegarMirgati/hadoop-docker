import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.File;
import java.net.URI;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCount6_3 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

      private final static IntWritable one = new IntWritable(1);
    //public static final Log log = LogFactory.getLog(TokenizerMapper.class);
    private Text word = new Text();
    private HashSet<String> stopWords = new HashSet<String>();

    @Override
    public void setup(Context context) throws IOException, InterruptedException{ 
      Path pt = new Path("/user/sina/data1/stop-words.keys");
      FileSystem fs = FileSystem.get(new Configuration());
      /*FileReader fileReader = new FileReader("hdfs:/user/sina/extra/stop-words_copy.keys");*/
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fs.open(pt)));
      String line;
      while ((line = bufferedReader.readLine()) != null ) {
      stopWords.add(line); 
      System.out.println(line);
      } 

      /*fileReader.close();*/
      
}
  
    @Override
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        
        String value_s = word.toString();
        if(value_s.matches("^.*[^a-zA-Z ].*$"))
           continue;

        String lower_val = value_s.toLowerCase();
        Text final_text = new Text(lower_val);
        if(!stopWords.contains(lower_val)){
            context.write(final_text, one);
            System.out.println("ADDDDDDDDDDDDDDDDDDDDDDDD");
          }
      }
    }
  }


  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    //private int firstValue = 0;
    private IntWritable firstValue = new IntWritable(0); 
    private Text firstKey = new Text("");
    private IntWritable secondValue = new IntWritable(0);
    //private int secondValue = 0;
    private Text secondKey = new Text("");
    
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      //context.write(key, result);
      if ( result.get() >= firstValue.get() && result.get() > secondValue.get()) {
       secondValue.set(firstValue.get());
       secondKey.set(firstKey);
       firstValue.set(result.get());
       firstKey.set(key);
      }
      else if ( result.get() < firstValue.get() && result.get() >= secondValue.get() ) {
       secondValue.set(result.get()) ;
       secondKey.set(key);
      }
    }
    @Override
    public void cleanup(Context context) throws IOException,  InterruptedException{
    
    context.write(firstKey , firstValue);
    context.write(secondKey , secondValue);
    System.out.println("cleanup");
  }
 }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount6_3.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setNumReduceTasks(10);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
