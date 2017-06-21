package KaisUebungen;
import java.util.ArrayDeque;
/**
 *  Algo Uebungsblatt08 Nr.1
 */
import java.util.ArrayList;

public class Tree {
	
	// mit ArrayLists
	public static void tree(int n){
		ArrayList <String> queue= new ArrayList();
		queue.add("");
		queue=getQueue(queue, n);
		System.out.println(queue);
	}
	public static ArrayList<String> getQueue(ArrayList<String> queue, int n){
		ArrayList<String> erg = new ArrayList<>();
		while(!queue.isEmpty()){
			if(queue.get(0).length()==n){
				erg.add(queue.get(0));
			}
			else{
				for(int i=1; i<=n; i++){
					String s=queue.get(0);
					if(!s.contains(""+i)){
						queue.add(s+i);
					}
				}
			}
			queue.remove(0);
		}
		return erg;
	}
	
	// Mit Deque
	public static void treeDeque(int n){
		ArrayList<String> erg= new ArrayList<>();
		ArrayDeque<String> queue= new ArrayDeque<String>();
		queue.add("");
		erg=getQueue2(n, erg, queue);
		System.out.println(erg.toString());
	}
	public static ArrayList<String> getQueue2(int n, ArrayList<String> erg, ArrayDeque<String> queue){
		while(!queue.isEmpty()){
			if(queue.getFirst().length()!=n){
				for(int i=1; i<=n; i++){
					if(!queue.getFirst().contains(""+i)){
						queue.addLast(queue.getFirst()+i);
					}
				}
				queue.removeFirst();
			}else{
				erg.add(queue.removeFirst());
			}
		}
		return erg;
	}
	public static void main(String[] args) {
		tree(3);
		treeDeque(3);
		InOrder(3);
	}

	// nach InOrder
	public static void InOrder(int n){
		String s="";
		ArrayList<String> erg= new ArrayList();
		erg = treeInOrder(n, s, erg);
		System.out.println(erg.toString());
	}
	public static ArrayList<String> treeInOrder(int n, String s, ArrayList<String> erg){
		if(s.length()!=n){
			for(int z=1; z<=n; z++){
				if(!s.contains(""+z)){
				treeInOrder(n, s+z, erg);
				}
			}
		}else{
			erg.add(s);
			return erg;
		}
		return erg;
	}
}
