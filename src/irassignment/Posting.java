package irassignment;

public class Posting implements Comparable<Posting> {
	int docID,tF;

	public Posting() {
		this.docID = 0;
		this.tF = 0;
	}
	public Posting(int docID, int tF) {
		this.docID = docID;
		this.tF = tF;
	}
	@Override
	public int compareTo(Posting o) {
		int comparedtF = o.tF;
		if (this.tF > comparedtF) {
			return 1;
		} else if(this.tF == comparedtF){
			return 0;
		}
		else {
			return -1;
		}
	}
}
