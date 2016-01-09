package irrcyn.internal.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seti on 09/01/16.
 */
public class Node {
    public static Integer nbNodes;
    private String name;
    private Map<String, String> attributes;

    public Node(String name) {
        this.name = name;
        attributes = new HashMap<String, String>();


        // Debuggage
        if(nbNodes == null)
            nbNodes = 0;
        else
            nbNodes++;

    }
    public void addAttribute(String attributeName, String attributeValue){
        attributes.put(attributeName, attributeValue);
    }
    public Map<String, String> getAttributes(){
        return attributes;
    }

    public String getName() {
        return name;
    }
}
