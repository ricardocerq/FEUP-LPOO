package logic;
import java.util.Comparator;

/**
 * class representing a pair
 */
public class Pair<L extends Comparable<L>, R extends Comparable<R>> {
	/**
	 * to be used to determine entity printing order
	 */
	public class PairPositionComparator implements Comparator<Pair<L,R>>
	{
	    @Override
	    public int compare(Pair<L,R> p1, Pair<L,R> p2)
	    {
	    	if(p1 == null && p2 == null)
	    		return 0;
	    	if(p1 == null)
	    		return -1;
	    	if(p2==null)
	    		return 1;
	    	if(p1.first.compareTo(p2.first) < 0)
	    		return -1;
	    	if(p1.first.compareTo(p2.first) > 0)
	    		return 1;
	    	if(p1.second.compareTo(p2.second) < 0)
	    		return -1;
	    	if(p1.second.compareTo(p2.second) > 0)
	    		return 1;
	    	return 0;
	    }
	}
	public L first;
	
	public R second;
	
	/**
	 * constructor
	 * @param first first element of pair
	 * @param second second element of pair
	 */
	public Pair(L first, R second){
		this.first = first;
		this.second= second;
	}
	
	/**
	 * equals operator
	 * @param o object to check for equality
	 */
	public boolean equals(Object o){
		if(o == null || ! (o instanceof Pair<?,?>) )
			return false;
		@SuppressWarnings("unchecked")
		Pair<L,R> p2 = (Pair<L,R>) o;
		return (first.equals(p2.first)) && (second.equals(p2.second));
	}
	
	/**
	 * @return pair as a string
	 */
	public String toString(){
		return first.toString() + " " + second.toString();
	}
}




