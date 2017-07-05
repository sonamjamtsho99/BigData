package pairapproach;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PairApproachMapper extends Mapper<LongWritable, Text, Pair, IntWritable> {
    private IntWritable one = new IntWritable(1);
    
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
    			 context.write(new Pair(temp,"*"), new IntWritable(1));
    			 j++;
    		}		
    	}    	
        
    }
}