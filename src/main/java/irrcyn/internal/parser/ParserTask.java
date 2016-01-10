package irrcyn.internal.parser;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.io.*;
import java.util.List;


/**
 * Created by seti on 09/01/16.
 */
public class ParserTask extends AbstractTask{
    /**
     * List of the files we need to convert
     */
    private String[] aParser;
    File f;

    private Nodes nodes;
    private String infoLabel;

    // For the buffer
    BufferedReader br = null;
    String line = "";
    String split = "=";

    String targetCSV;

    public ParserTask(String dir, String... aParser) throws IOException {
        nodes = new Nodes();
        this.aParser = aParser;
        this.targetCSV = dir;
        f = new File (targetCSV);
        f.createNewFile();
    }

    public void run(TaskMonitor taskMonitor) throws Exception {
        Node currentNode;
        int index = 0;

        // Lecture
        for (String directory:aParser){
            try {
                br = new BufferedReader(new FileReader(directory));

                line = br.readLine();
                infoLabel = line.split(split)[0].trim(); // Le label courant (=fichier courant)

                while ((line = br.readLine()) != null) {
                    currentNode = nodes.tryToAdd(line.split(split)[0].trim()); // On ajoute le noeud ou on le récupère
                    currentNode.addAttribute(infoLabel,line.split(split)[1].trim()); // On ajoute l'attribut au noeud
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Ecriture
        try
        {
            PrintWriter pw = new PrintWriter (new BufferedWriter (new FileWriter (f)));

            // 1st line
            pw.print("name");
            List<String> attributes = nodes.getAttributes();
            for(String attribute:attributes){
                pw.print("," + attribute);
            }

            pw.print('\n');


            // The next lines
            for (Node node:nodes)
            {
                pw.print (node.getName());
                for(String attribute:attributes){
                    if(node.getAttributes().containsKey(attribute)){
                        pw.print("," + node.getAttributes().get(attribute));
                    }
                    else{
                        pw.print("");
                    }
                }
                pw.print('\n');
            }

            pw.close();
        }
        catch (IOException exception)
        {
            System.err.println ("Erreur lors de l'ecriture du CSV : " + exception.toString());
        }

    }
    public String getTargetCSV(){
        File f = new File (targetCSV);
        //return targetCSV;
        return f.getAbsolutePath();
    }
}
