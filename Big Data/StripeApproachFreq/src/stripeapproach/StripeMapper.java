package stripeapproach;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public  class StripeMapper extends Mapper<LongWritable, Text, Text, MapWritableToString> {
		@Override 
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		 String[] terms = value.toString().split("\\s+");
			for(int i = 0; i< terms.length-1; i++){
				if(terms[i].trim().length()>0){
					String temp= terms[i];
					MapWritableToString neighMap = new MapWritableToString();
					
					int j = i+1;
					while(j< terms.length && !temp.equals(terms[j])){
						Text word = new Text(terms[j]);
						if(neighMap.containsKey(word)){
							DoubleWritable intvalue = (DoubleWritable) neighMap.get(word);
							double count = intvalue.get();
							count += 1;
							neighMap.put(word, new DoubleWritable(count));
						}
						else{
							neighMap.put(word, new DoubleWritable(1.0));
						}
						j++;
					}
				    context.write(new Text(temp), neighMap);
				}
			}
		}
}
