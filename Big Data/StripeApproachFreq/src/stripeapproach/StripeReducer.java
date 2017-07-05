package stripeapproach;

import java.io.IOException;
import java.util.Map.Entry;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class StripeReducer extends
		Reducer<Text, MapWritableToString, Text, MapWritableToString> {
	@Override
	public void reduce(Text key, Iterable<MapWritableToString> values,
			Context context) throws IOException, InterruptedException {

		MapWritableToString tempMap = new MapWritableToString();
		for (MapWritableToString eachMap : values) {
			for (Entry<Writable, Writable> entry : eachMap.entrySet()) {
				Text currentKey = (Text) entry.getKey();
				DoubleWritable currValue = (DoubleWritable) entry.getValue();
				// total += currValue.get();
				if (tempMap.containsKey(currentKey)) {
					DoubleWritable oldVal = (DoubleWritable) tempMap.get(currentKey);
					double newVal = oldVal.get() + currValue.get();
					tempMap.put(currentKey, new DoubleWritable(newVal));
				} else {
					tempMap.put(currentKey, currValue);
				}
			}
		}
		
		
		double total = 0;
		for (Entry<Writable, Writable> item : tempMap.entrySet()) {
			DoubleWritable value = (DoubleWritable) item.getValue();
			total = total + value.get();
		}

		MapWritableToString resultMap = new MapWritableToString();
		for (Entry<Writable, Writable> entry : tempMap.entrySet()) {
			Text k = (Text) entry.getKey();
			DoubleWritable v = (DoubleWritable) entry.getValue();
			double newVal = ((double) v.get() / total);
			resultMap.put(k, new DoubleWritable(newVal));
		}
		context.write(key, resultMap);
	}
}







