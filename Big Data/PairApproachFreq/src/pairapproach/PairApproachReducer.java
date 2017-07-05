package pairapproach;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PairApproachReducer extends Reducer<Pair, IntWritable, Pair, DoubleWritable> {
    private Text prevTerm = new Text("NOT_SET");
    private Text star = new Text("*");
    private int total=0;
    
    @Override
    protected void reduce(Pair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {       
    	int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        
        if (key.getNeighbor().equals(star)) {
	        if (key.getTerm().equals(prevTerm)) {
	        	total+=sum;
	        } else {
	        	prevTerm.set(key.getTerm());
	            total=0;
	            total+=sum;
	        }
	    } else {
	        context.write(key, new DoubleWritable((double) sum / total));
	    }
    }
}