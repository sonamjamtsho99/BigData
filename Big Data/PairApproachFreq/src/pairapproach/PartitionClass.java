package pairapproach;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class PartitionClass extends Partitioner<Pair, IntWritable>{

	@Override
	public int getPartition(Pair key, IntWritable in, int numReducer) {
		// TODO Auto-generated method stub
		return key.getTerm().hashCode() % numReducer;
	}

}
