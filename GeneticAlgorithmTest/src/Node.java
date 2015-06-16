
public class Node {
	boolean leaf;
	String data;
	Node left;
	Node right;
    public Node()
    {
        left = null;
        right = null;
        data = "";
        leaf = false;
    }
    public Node(boolean inleaf, String val){
    	leaf = inleaf;
    	data = val;
    	
    }
}
