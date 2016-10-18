package irassignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class CSE535Assignment {
	
	public static void getTopK(LinkedList<Index> I3,int n){ 		//Function to get Top K terms
		Collections.sort(I3,Collections.reverseOrder());          //Sort the Index according to size
		System.out.println("FUNCTION: getTopK "+n);
		System.out.print("Result:");
		for(int i =0;i<n;i++){									//Displays Top K terms
			System.out.print(" "+I3.get(i).term);
			if(i!=n-1)
				System.out.print(",");
			else
				System.out.print("\n");
		}
	}
	public static void getPostings(LinkedList<Index> I1,LinkedList<Index> I2, String queryTerm){	//Function to get PostingList for a given queryTerm
		System.out.println("FUNCTION: getPostings "+queryTerm);
		for(Index i1:I1){								 
			if(i1.term.equals(queryTerm)){				//Checks If the term in Index is equal to queryTerm
				System.out.print("Ordered by doc IDs:"); 
				for(int i=0;i<i1.size;i++){				//Displays Posting List based on increasing document ID
					System.out.print(" "+i1.plist.get(i).docID);
					if(i!=i1.size-1)
						System.out.print(",");
					else
						System.out.print("\n");
				}
				System.out.print("Ordered by TF:");		 
				for(Index i2:I2){						//// Displays Posting List based on Decreasing TF
					if(i2.term.equals(queryTerm)){
						for(int j=0;j<i2.size;j++){
							System.out.print(" "+i2.plist.get(j).docID);
							if(j!=i2.size-1)
								System.out.print(",");
							else
								System.out.print("\n");
						}
					}
				}
				return;
			}
				
		}
		System.out.println("term not found");
	}
	public static LinkedList<String> sortQueryInc(LinkedList<String> a,LinkedList<Index> I1){//This Function uses Linked List defined in class Optimal that sorts the entire QueryList based on the postings size in Increasing Order
		LinkedList<Optimal> Op = new LinkedList<Optimal>();
		for(String s: a){
			for(Index i1:I1){
				if(i1.term.equals(s)){		//Check if the term is equal to queryTerm
					Optimal temp = new Optimal(i1.term,i1.size);
					Op.add(temp);
					break;
				}
			}
		}
		Collections.sort(Op);			//Sorts the List using compareTo method in Optimal Class
		LinkedList<String> b=new LinkedList<String>();
		for(Optimal o:Op){
			//System.out.println(o.term);
			b.add(o.term);				//Adds the term to QueryList which has to be returned
		}
		return b;
	}
	public static ArrayList<Integer> TaaTAnd(LinkedList<Index> I4,ArrayList<Integer> a1,String queryTerm,long counter[],int flag){
		ArrayList<Integer> a = new ArrayList<Integer>();	//Defining final ArrayList which will be used be used to return
		if(flag==0){										// If function is called for the first time just add Incoming DocIds to final List.
			for(Index i1:I4){								 
				if(i1.term.equals(queryTerm)){				//Checks If the term in Index is equal to queryTerm
					for(int i=0;i<i1.size;i++){
						a.add(i1.plist.get(i).docID);		//If equal add docID to new List
					}
				}
			}
		}
			else{
				for(int i=0; i<a1.size();i++){
					for(Index i2:I4){
						if(i2.term.equals(queryTerm)){
							for(int j=0;j<i2.size;j++){
								counter[0]++;
								if(a1.get(i) == i2.plist.get(j).docID){			// Compares docID with old list and query term's posting list
									a.add(i2.plist.get(j).docID);
									break;
								}
							}
						}
					}
				}
			}
		return a;										// Return Intermediate or Final Result List
	}
	public static ArrayList<Integer> TaaTOr(LinkedList<Index> I4,ArrayList<Integer> a1,String queryTerm,long counter2[],int flag){
		ArrayList<Integer> a = new ArrayList<Integer>();
		if(flag==0){										// If function is called for the first time just add Incoming DocIds to final List.
			for(Index i1:I4){								 
				if(i1.term.equals(queryTerm)){				//Checks If the term in Index is equal to queryTerm
					for(int i=0;i<i1.size;i++){
						a.add(i1.plist.get(i).docID);		//If equal add docID to new List
					}
				}
			}
		}
		else{
			for(Index i2:I4){
				if(i2.term.equals(queryTerm)){
					for(int j=0;j<i2.size;j++){
						a.add(i2.plist.get(j).docID);
					}
				}
			}
			for(int i=0; i<a1.size();i++){
				for(Index i3:I4){
					if(i3.term.equals(queryTerm)){
						for(int k=0;k<i3.size;k++){
							counter2[0]++;
							if(a1.get(i) == i3.plist.get(k).docID){			// Compares docID with old list and query term's posting list
							a1.remove(i);									//If equal remove docID from Old List
							i--;											//Decrease the counter as removing docID shifts other terms to left
							break;
							}
						}
					}
				}
			}
			if(a1!=null)									// Checks if old Result Set had terms. Adds them to new Result Set.
				a.addAll(a1);
		}
		return a;										// Return Intermediate or Final Result List
	}
	public static ArrayList<Integer> DaaTAnd(LinkedList<Index> I1,LinkedList<String> queryList, long counter3[]){
		
		int flag=0;
		for(String s:queryList){
			for(Index i1:I1){
				if(i1.term.equals(s)){ //Checks for existence of queryTerms in Index
				flag=1;
				break;
			}
		}
		}
		ArrayList<Integer> a = new ArrayList<Integer>();// Final List defined which is going to be returned
		if(flag!=0){
			int index = queryList.size();
			int check = 1;
			ArrayList<LinkedList<Integer>> postingsList = new ArrayList<LinkedList<Integer>>();// ArrayList of LinkedLists of type integer containing only document ID
			@SuppressWarnings("unchecked")
			Iterator<Integer>[] iter = new Iterator[index];//Iterator Array containing Iterators for each ArrayList above
			Integer[] docIds = new Integer[index];		// Array Of docIds to hold each Iterated PostingLists
			
			
			for(int i=0;i<index;i++){//Assigning each index of the above to each term in the query list respectively
				
				for(Index i2:I1){
					if(i2.term.equals(queryList.get(i))){
						LinkedList<Integer> p = new LinkedList<Integer>();
						for(int j=0;j<i2.size;j++){
							p.add(i2.plist.get(j).docID);
							
						}
						postingsList.add(p);
						iter[i]=postingsList.get(i).iterator();
						docIds[i]=iter[i].next();
						}
				}
			}
			while(check==1){							//This checks all of the postingsList have items to be traversed. 
				int max=-1,i;
				for (i=0;i<index-1;i++){				//Finds the max docID of all terms in PostingsList being currently pointed to
					counter3[0]++;
					if(docIds[i].equals(docIds[i+1]))
						continue;
					else{
						if(docIds[i]>docIds[i+1])
							max=docIds[i];
						else
							max=docIds[i+1];
					}
						
				}
				if(max==-1){							// This check determines if all docIDs were equal
					a.add(docIds[i]);				// adds term to Final List which has to be returned
					for(i=0;i<index;i++){
						if(iter[i].hasNext())
							docIds[i]=iter[i].next();
						else{
							check=0;					// One postings list has no more postings.
							break;
						}
					}
				}	
				else{									// Case for docIDs not equal
					for(i=0;i<index;i++){
						while((docIds[i]<max)&&(check==1)){
							counter3[0]++;
							if(iter[i].hasNext())		
								docIds[i]=iter[i].next();
							else
								check=0;				// One postings list has no more postings.
						}
						if(check==0)
							break;
					}
				}	
			}
		}	
		return a;
	}	
	public static LinkedList<Index> copy(LinkedList<Index> L1){ //This Function Copies an Index
		LinkedList<Index> L2=new LinkedList<Index>();
		for(Index t1:L1){
			Index t2 = new Index();
			t2.term=t1.term;
			t2.size=t1.size;
			for(Posting p1:t1.plist){
				Posting p2 = new Posting();
				p2.docID=p1.docID;
				p2.tF=p1.tF;
				t2.plist.add(p2);
			}
			L2.add(t2);
		}
		return L2;
	}
	public static ArrayList<Integer> DaaTOr(LinkedList<Index> I1,LinkedList<String> queryTerms, long counter4[]){
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(String s:queryTerms){		// This block checks for existence of query terms in the Index. If it exists add to Arraylist.
			for(Index i1:I1){
				counter4[0]++;
				if(i1.term.equals(s)){ //Checks for existence of terms in Index
					for(int i=0;i<i1.size;i++){
						a.add(i1.plist.get(i).docID);
					}
				}
			}
		}
		return a;
	}
	
	public static void main(String[] args)throws IOException {
		String sLine;
		LinkedList<Index> I1 = new LinkedList<Index>();
		LinkedList<Index> I2 = new LinkedList<Index>();
		BufferedReader br = new BufferedReader(new FileReader(args[0]));		//Reading Input File
		while ((sLine = br.readLine()) != null) {
			String s1[] = sLine.split("\\\\c");
			String s2[] = s1[1].split("\\\\m");
			Index temp1 = new Index(s1[0],Integer.parseInt(s2[0]));
			Index temp2 = new Index(s1[0],Integer.parseInt(s2[0]));
			s2[1]=s2[1].substring(1, s2[1].length()-1);
			String s3[] = s2[1].split(", ");
			for (int i=0; i < s3.length; i++) {
				String s4[]=s3[i].split("/");
				temp1.addList(Integer.parseInt(s4[0]),Integer.parseInt(s4[1]));
				temp2.addSortedList(Integer.parseInt(s4[0]),Integer.parseInt(s4[1]));
			}
			I1.add(temp1);				//Adding to Index list which is sorted according to docId
			I2.add(temp2);				//Adding to Index list which is sorted according to TermFrequency
		}
		br.close();
		
		File outfile = new File(args[1]);
		FileOutputStream fos = new FileOutputStream(outfile);
		PrintStream print = new PrintStream(fos);				//Writing the output to LogFile
		System.setOut(print);
		//display(I3);
		getTopK(copy(I2),Integer.parseInt(args[2]));	//Get Top K Terms.
		
		br=new BufferedReader(new FileReader(args[3]));
		sLine="";
		while((sLine=br.readLine())!=null){				// Loop runs for each line in the query file 
			String s5[]=sLine.split(" ");
			ArrayList<Integer> taatAnd = null;
			int flag =0;
			long counter1[]={0,0};
			long startTime,endTime;
			for(int i=0; i < s5.length; i++){
				getPostings(I1, I2, s5[i]);					//getting postings for each term
			}
			//TaaTAnd Starts here
			System.out.print("FUNCTION: termAtATimeQueryAnd");
			LinkedList<String> queryTerms = new LinkedList<String>();
			for (int i=0;i<s5.length;i++){
				queryTerms.add(s5[i]);
				System.out.print(" "+s5[i]);
				if(i!=s5.length-1)
					System.out.print(",");
				else
					System.out.print("\n");
			}
			
			for(int i=0; i<s5.length; i++){
				startTime=System.currentTimeMillis();
				taatAnd=TaaTAnd(copy(I2),taatAnd,s5[i],counter1,flag);//TaaTAnd- QueryNot Optimized
				endTime=System.currentTimeMillis();
				counter1[1]=counter1[1]+endTime-startTime;
				flag=1;
				if(taatAnd.isEmpty())
					break;
			}
			System.out.println(taatAnd.size()+" documents are found");
			System.out.println(counter1[0]+" comparisons are made");
			System.out.println(((double)counter1[1]/1000)+" seconds are used");
			queryTerms=sortQueryInc(queryTerms,copy(I2));					//Optimizing Query Terms
			flag=0;counter1[0]=0;taatAnd=null;
			for(String s:queryTerms){
				taatAnd=TaaTAnd(copy(I2),taatAnd, s,counter1, flag);//TaaTAnd-query Optimized
				flag=1;
				if(taatAnd.isEmpty())
					break;
			}
			System.out.println(counter1[0]+" comparisons are made with optimization");
			if(taatAnd.isEmpty())
				System.out.println("Result: terms not found");
			else{
				Collections.sort(taatAnd);			//Sorting the output based on increasing docID
				System.out.print("Result:");
				for(int i=0;i<taatAnd.size();i++){
					System.out.print(" "+taatAnd.get(i));
					if(i!=taatAnd.size()-1)
						System.out.print(",");
					else
						System.out.print("\n");
				}
			}
						
			//TaaTOr Starts here
			ArrayList<Integer> taatOr = null;
			flag=0;
			long counter2[]={0,0};
			System.out.print("FUNCTION: termAtATimeQueryOr");
			for (int i=0;i<s5.length;i++){
				System.out.print(" "+s5[i]);
				if(i!=s5.length-1)
					System.out.print(",");
				else
					System.out.print("\n");
			}
			for(int i=0; i<s5.length; i++){
				startTime=System.currentTimeMillis();
				taatOr=TaaTOr(copy(I2),taatOr,s5[i],counter2,flag);//TaaTOr- Query Not Optimized
				endTime=System.currentTimeMillis();
				counter2[1]=counter2[1]+endTime-startTime;
				flag=1;
				if(taatOr.isEmpty())
					break;
			}
			System.out.println(taatOr.size()+" documents are found");
			System.out.println(counter2[0]+" comparisons are made");
			System.out.println(((double)counter2[1]/1000)+" seconds are used");
			for(String s:queryTerms){
				taatOr=TaaTOr(copy(I2),taatOr, s, counter2, flag);//TaaTOr-Query Optimized
				flag=1;
			}
			System.out.println(counter2[0]+" comparisons are made with optimization");
			if(taatOr.isEmpty())
				System.out.println("Result: terms not found");
			else{
				Collections.sort(taatOr);		//Sorting the output based on increasing docID
				System.out.print("Result:");
				for(int i=0;i<taatOr.size();i++){
					System.out.print(" "+taatOr.get(i));
					if(i!=taatOr.size()-1)
						System.out.print(",");
					else
						System.out.print("\n");
				}
			}
						
			//DaaTAND Starts here
			ArrayList<Integer> daatAnd = null;
			long counter3[]={0,0};
			System.out.print("FUNCTION: docAtATimeQueryAnd");
			for (int i=0;i<s5.length;i++){
				System.out.print(" "+s5[i]);
				if(i!=s5.length-1)
					System.out.print(",");
				else
					System.out.print("\n");
			}			
			startTime=System.currentTimeMillis();
			daatAnd=DaaTAnd(I1,queryTerms,counter3);// Calling DaaTAnd Function
			endTime=System.currentTimeMillis();
			counter3[1]=endTime-startTime;
			System.out.println(daatAnd.size()+" documents are found");
			System.out.println(counter3[0]+" comparisons are made");
			System.out.println(((double)counter3[1]/1000)+" seconds are used");
			
			if(daatAnd.isEmpty())
				System.out.println("Result: terms not found");
			else{
				Collections.sort(daatAnd);			//Sorting the output based on increasing docID
				System.out.print("Result:");
				for(int i=0;i<daatAnd.size();i++){
					System.out.print(" "+daatAnd.get(i));
					if(i!=daatAnd.size()-1)
						System.out.print(",");
					else
						System.out.print("\n");
				}
			}
			
			//DaaT Or Starts here
			ArrayList<Integer> daatOr = null;
			long counter4[]={0,0};
			System.out.print("FUNCTION: docAtATimeQueryOr");
			for (int i=0;i<s5.length;i++){
				System.out.print(" "+s5[i]);
				if(i!=s5.length-1)
					System.out.print(",");
				else
					System.out.print("\n");
			}			
			startTime=System.currentTimeMillis();
			daatOr=DaaTOr(copy(I1),queryTerms, counter4);//Calling DaaTOr Function
			endTime=System.currentTimeMillis();
			counter4[1]+=endTime-startTime;
			System.out.println(daatOr.size()+" documents are found");
			System.out.println(counter4[0]+" comparisons are made");
			System.out.println(((double)counter4[1]/1000)+" seconds are used");
			if(daatOr.isEmpty())
				System.out.println("Result: terms not found");
			else{
				Collections.sort(daatOr);			//Sorting the output based on increasing docID
				System.out.print("Result:");
				for(int i=0;i<daatOr.size();i++){
					System.out.print(" "+daatOr.get(i));
					if(i!=daatOr.size()-1)
						System.out.print(",");
					else
						System.out.print("\n");
				}
			}
		}
		br.close();
	}
}
