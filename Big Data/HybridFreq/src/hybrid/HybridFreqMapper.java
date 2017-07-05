package hybrid;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HybridFreqMapper extends Mapper<LongWritable, Text, Pair, DoubleWritable> {
    private DoubleWritable one = new DoubleWritable(1);
    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

    	String[] item = value.toString().split("\\s+");
    	for(int i=0; i< item.length; i++ ){
    		String temp= item[i].replaceAll("\\W+","");
    		if(item[i].equals("")){
                continue;
            }
    		
    		int j = i+1;
    		while(j< item.length && !temp.equals(item[j])){
    			 context.write(new Pair(temp,item[j]), one);
    			 //context.write(new Pair(temp,"*"), new IntWritable(1));
    			 j++;
    		}		
    	}    	
        
    }
}