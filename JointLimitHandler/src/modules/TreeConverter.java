package modules;

import java.util.ArrayList;
import java.util.Stack;

import tree.Channel;
import tree.Node;
import tree.NodeKey;
import tree.Tree;

public class TreeConverter {
	
	public TreeConverter(){}
	
	private ArrayList<Channel> makeCopy(ArrayList<Channel> src){
		
		ArrayList<Channel> cpy=new ArrayList<Channel>();
		
		for(Channel currentVal : src){
			cpy.add(currentVal);
		}
		
		return cpy;
	}
	
	public Tree convert(ArrayList<String> bvhHeader){
		
		Tree tree=new Tree();
		Stack<Integer> stack=new Stack<Integer>();
		
		int currentNodeIndex=0;
		NodeKey currentNodeKey=null;
		
		int currentChannelIndex=0;
		ArrayList<Channel> currentChannels=new ArrayList<Channel>();
		
		for(String line : bvhHeader){
			
			String[] currentLine = line.split("\\s+");
			
			/*
			for(String frag : currentLine){
				System.out.println(frag);
			}
			System.out.println();
			*/
			
			// Root/Joint case.
			if(line.contains("ROOT") || line.contains("JOINT")){
				if(line.contains("JOINT")){
					currentNodeKey=new NodeKey(currentNodeIndex,currentLine[2]);
				}
				else{
					currentNodeKey=new NodeKey(currentNodeIndex,currentLine[1]);
				}
			}
			// Channel case.
			else if(line.contains("CHANNELS")){
				int numOfChannels=Integer.parseInt(currentLine[2]);
				for(int index=0;index<numOfChannels;index++){
					Channel channel=new Channel(currentChannelIndex,currentLine[3+index],0.0);
					currentChannels.add(channel);
					currentChannelIndex++;
				}
				Node node=new Node(currentNodeKey,this.makeCopy(currentChannels));
				tree.putNode(node);
				currentNodeIndex++;
				currentChannels.clear();
			}
			// End case, add a node without channels.
			else if(line.contains("End Site")){
				currentNodeKey=new NodeKey(currentNodeIndex,"End Site");
				Node node=new Node(currentNodeKey,this.makeCopy(currentChannels));
				tree.putNode(node);
				currentNodeIndex++;
			}
			// '{' case, push the stack.
			else if(line.contains("{")){
				stack.push(currentNodeIndex);
			}
			// '}' case, pop the stack and link the node.
			else if(line.contains("}")){
					Integer childNodeIndex=stack.pop();
					if(stack.isEmpty()==false){
						Integer parentNodeIndex=stack.pop();
						stack.push(parentNodeIndex);
						Node parentNode=tree.getNodebyIndex(parentNodeIndex);
						parentNode.addChildByIndex(childNodeIndex);
						tree.putNode(parentNode);
					}
			}
		}
		
		return tree;
	}
}

















