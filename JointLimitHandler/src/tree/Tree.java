package tree;


import java.util.HashMap;
import java.util.Map;

public class Tree {
	
	private Map<Integer,Node> tree;
	
	public Tree(){
		this.tree=new HashMap<Integer,Node>();
	}
	
	public void putNode(Node node){
		this.tree.put(node.getNodeIndex(),node);
	}
	
	public Node getNodebyIndex(Integer nodeIndex){
		return this.tree.get(nodeIndex);
	}
	
	public void printAllNodeInformation(){
		for(Node node : this.tree.values()){
			node.printNodeInformation();
		}
	}
}
