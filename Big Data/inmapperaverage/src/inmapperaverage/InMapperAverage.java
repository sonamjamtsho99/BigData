package inmapperaverage;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
public class InMapperAverage {
	public static class Map extends Mapper<LongWritable, Text, Text, Pair> {
		private HashMap<String, Pair> pairSum;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			pairSum = new HashMap<>();
		}

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			try {
				String[] myArray = value.toString().split("\\s+");
				
				String userID = myArray[0];
				String time = myArray[myArray.length - 1];
				Integer num = Integer.parseInt(time);
				
				Pair sumCount = pairSum.get(userID);
				
				if (sumCount == null) {
					sumCount = new Pair(num, 1);
				} else {
					sumCount.setSum(sumCount.getSum() + num);
					sumCount.setCount(sumCount.getCount() + 1);
				}
				pairSum.put(userID, sumCount);
			} catch (Exception e) {

			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			for (Entry<String, Pair> entry : pairSum.entrySet()) {
				context.write(new Text(entry.getKey()), entry.getValue());
			}
		}
	}

	public static class Reduce extends
			Reducer<Text, Pair, Text, DoubleWritable> {

		public void reduce(Text key, Iterable<Pair> values, Context context)
				throws IOException, InterruptedException {
			double sum = 0, count = 0;
			for (Pair val : values) {
				sum += val.getSum();
				count += val.getCount();
			}
			context.write(key, new DoubleWritable(sum / count));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "inmapperaverage");
		job.setJarByClass(InMapperAverage.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Pair.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		job.setMapperClass(InMapperAverage.Map.class);
		job.setReducerClass(InMapperAverage.Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}

}
