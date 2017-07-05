package pairapproach;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Pair implements Writable, WritableComparable<Pair> {

    private Text term;
    private Text neighbor;

    public Pair(Text term, Text neighbor) {
        this.term = term;
        this.neighbor = neighbor;
    }

    public Pair(String term, String neighbor) {
        this(new Text(term),new Text(neighbor));
    }

    public Pair() {
        this.term = new Text();
        this.neighbor = new Text();
    }

    @Override
    public int compareTo(Pair other) {
        int returnVal = this.term.compareTo(other.getTerm());
        if(returnVal != 0){
            return returnVal;
        }
        if(this.neighbor.toString().equals("*")){
            return -1;
        }else if(other.getNeighbor().toString().equals("*")){
            return 1;
        }
        return this.neighbor.compareTo(other.getNeighbor());
    }

    @Override
    public void write(DataOutput out) throws IOException {
        term.write(out);
        neighbor.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        term.readFields(in);
        neighbor.readFields(in);
    }

    @Override
    public String toString() {
        return "("+term+","+neighbor+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair termPair = (Pair) o;

        if (neighbor != null ? !neighbor.equals(termPair.neighbor) : termPair.neighbor != null) return false;
        if (term != null ? !term.equals(termPair.term) : termPair.term != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
		int result = 1;
        result = prime * result + (term != null ? term.hashCode() : 0);
        result = prime * result + (neighbor != null ? neighbor.hashCode() : 0);
        return result;
    }

    public void setTerm(String term){
        this.term.set(term);
    }
    
    public void setNeighbor(String neighbor){
        this.neighbor.set(neighbor);
    }

    public Text getTerm() {
        return term;
    }

    public Text getNeighbor() {
        return neighbor;
    }
}
