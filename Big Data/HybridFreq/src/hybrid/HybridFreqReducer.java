package hybrid;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class HybridFreqReducer extends
		Reducer<Pair, DoubleWritable, Text, MapWritableToString> {
	private String prevKey;
	private MapWritableToString neighborCount;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		prevKey = " ";
		neighborCount = new MapWritableToString();
	}

	public void reduce(Pair term, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
		String t = term.getTerm().toString();
		if (!t.equals(prevKey) && prevKey != " ") {
			double total = 0;
			for (Writable writableValue : neighborCount.values()) {
				DoubleWritable doubleWritable = (DoubleWritable) writableValue;
				total += doubleWritable.get();

			}

			MapWritableToString relativeFreq = new MapWritableToString();
			for (MapWritableToString.Entry<Writable, Writable> entry : neighborCount.entrySet()) {
				DoubleWritable doubleValue = (DoubleWritable) entry.getValue();
				doubleValue.set(doubleValue.get() / total);
				relativeFreq.put(entry.getKey(), doubleValue);
			}

			neighborCount = new MapWritableToString();

			context.write(new Text(prevKey), relativeFreq);

		}

		double sum = 0;
		for (DoubleWritable value : values) {
			sum += value.get();
		}
		
		neighborCount.put(new Text(term.getNeighbor()), new DoubleWritable(sum));
		prevKey = term.getTerm().toString();
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		double sum = 0;
		for (Writable value : neighborCount.values()) {
			sum += ((DoubleWritable) value).get();
		}

		MapWritableToString relativeFreq = new MapWritableToString();
		for (MapWritableToString.Entry<Writable, Writable> entry : neighborCount.entrySet()) {
			DoubleWritable doubleValue = (DoubleWritable) entry.getValue();
			doubleValue.set(doubleValue.get() / sum);
			relativeFreq.put(entry.getKey(), doubleValue);
		}

		context.write(new Text(prevKey), relativeFreq);
	}
}
