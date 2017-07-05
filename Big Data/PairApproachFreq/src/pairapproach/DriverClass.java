package pairapproach;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DriverClass {

	public static void main(String[] args) throws IOException,InterruptedException,ClassNotFoundException {
        Job job = Job.getInstance(new Configuration());
        
        job.setJarByClass(DriverClass.class);
        job.setJobName("DriverClass");
        job.setPartitionerClass(PartitionClass.class);
        job.setNumReduceTasks(2);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(PairApproachMapper.class);
        job.setReducerClass(PairApproachReducer.class);

        job.setOutputKeyClass(Pair.class);
        job.setOutputValueClass(IntWritable.class);
        
        job.waitForCompletion(true);

    }
}
