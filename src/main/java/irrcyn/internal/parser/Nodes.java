package irrcyn.internal.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by seti on 09/01/16.
 */
public class Nodes extends ArrayList<Node> {

    public Nodes() {
        super();
    }

    private Node contains(String name){
        for(Node node:this){
            if(name.equals(node.getName())){
                return node;
            }
        }
        return null;
    }
    private Node addNode(String name){
        Node node = new Node(name);
        this.add(node);
        return node;
    }

    /**
     * Add a new node if it was not in the list, return the first occurence of the node otherwise (suposedely the only one)
     * @param name the name of the node we want to add
     * @return the first occurence of the node (suposedely the only one) or the node that was just added
     */
    public Node tryToAdd(String name){
        Node n = this.contains(name);
        if(n == null){
            return this.addNode(name);
        }
        else{
            return n;
        }
    }

    /**
     * Go over the nodes to find all the attributes they have
     * @return the list of the attributes carried by the nodes of the current list
     */
    public List<String> getAttributes(){
        List<String> attributes = new ArrayList<String>();
        Collection<String> nodeAttributes;

        for(Node node:this){
            nodeAttributes = node.getAttributes().keySet();
            attributes.removeAll(nodeAttributes);
            attributes.addAll(nodeAttributes);
        }
        return attributes;
    }
}
