package irassignment;
import java.util.Collections;
import java.util.LinkedList;

public class Index implements Comparable<Index> {
	String term;
	int size;
	LinkedList<Posting> plist = new LinkedList<Posting>();
	public Index() {
		this.term = "";
		this.size = 0;
	}
	public Index(String term, int size) {
		this.term = term;
		this.size = size;
	}
	public void addList(int docID, int tF)
	{
		Posting p1 = new Posting(docID,tF);
		this.plist.add(p1);
	}
	public void addSortedList(int docID, int tF){
		Posting p1 = new Posting(docID,tF);
		this.plist.add(p1);
		Collections.sort(plist,Collections.reverseOrder());
		
	}
	@Override
	public int compareTo(Index o) {
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
