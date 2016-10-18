package irassignment;
//This Class is used for Query Optimization
public class Optimal implements Comparable<Optimal> {
	String term;
	int size;
	public Optimal() {
		this.term = "";
		this.size = 0;
	}
	public Optimal(String term, int size) {
		this.term = term;
		this.size = size;
	}
	@Override
	public int compareTo(Optimal o) {
		int comparedsize = o.size;
		if (this.size > comparedsize) {
			return 1;
		} else if(this.size == comparedsize){
			return 0;
		}
		else {
			return -1;
		}
	}
}
