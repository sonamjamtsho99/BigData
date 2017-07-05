package average;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class Average {

	public static class AverageMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {
			

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//StringTokenizer tokens = new StringTokenizer(value.toString());
			try {
				String[] myArray = value.toString().split("\\s+");
				String userID = myArray[0];
				String time = myArray[myArray.length - 1];
				Integer num = Integer.parseInt(time);
				context.write(new Text(userID), new IntWritable(num));
			} catch (Exception e) {

			}
		}
	}

	public static class AverageReducer extends
			Reducer<Text, IntWritable, Text, DoubleWritable> {

		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			double sum = 0;
			double count = 0;
			for (IntWritable val : values) {
				count++;
				sum += val.get();
			}
			context.write(key, new DoubleWritable(sum / count));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");

		job.setJarByClass(Average.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(AverageMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setReducerClass(AverageReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}