package tree;


import java.util.ArrayList;
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
		System.out.println("Node Informations:");
		for(Node node : this.tree.values()){
			node.printNodeInformation();
		}
		System.out.println("==================");
	}
	
	public ArrayList<Node> getNodeIndexByName(String nodeName){
		ArrayList<Node> nodes=new ArrayList<Node>();
		for(int index=0;index<this.tree.size();index++){
			if(this.tree.get(index).getNodeName().equals(nodeName)){
				nodes.add(this.tree.get(index));
			}
		}
		return nodes;
	}
}
