package irrcyn.internal.View;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**


/**
 * This class is responsible for the Actions and the methods they call.
 * @author Hadi Kang
 *
 */
@SuppressWarnings("serial")
public class Controller extends JFrame implements ActionListener{
	
	// boolean to set if convertbutton is pressed
	private boolean isConverted = false;
	private MainFrame mainframe;
	private AffymetrixView affymetrixview;
	private IlluminaView illuminaview;
	private String inputfilepath;
	private static String inputbarcode1;
	private static String inputbarcode2;
	private static String targetSIFpath;
	private static String targetfilteredSIFpath;
	private String targetsubgraphedSIFpath;
	private static String targetNODE_TYPEpath;
	private static String targetUNIPROTpath;
	private static String targetMODIFICATIONSpath;
	private static String targetPREFERRED_SYMBOLpath;
	private static String targetPREFERRED_SYMBOL_EXTpath;
	private static String targetPIDpath;
	private static String targetID_PREFpath;
	private static String targetIDCytoUniProtFilepath;
	private static String targetUniqueUniProtFilepath;
	private static String targetUniProtToGeneIDMapFilepath;
	private static String targetGeneIDtoAffymetrixMapFilepath;
	private File curFile = null; 
	private File barcode1File = null;
	private File barcode2File = null;
	private File genesourceFile = null;
	private File genetargetFile = null;
	private File sigmolsourceFile = null;
	private File sigmoltargetFile = null;
	
	// the file name of the VIZMAP property file
	private static final String VIZMAP_PROPS_FILE_NAME = "netView.props";
	// the file concatenation of the (filtered_absent_proteins)
	private static final String ABSENT_PROTEINS_CONCATENATION = "(filtered_absent_proteins)";
	private static final String SUBGRAPHED = "(subgraphed)";
	/**
	 * The constructor for the mainframe controller
	 * @param mainframe
	 */
	public Controller(MainFrame mainframe)
	{
		this.mainframe = mainframe;
	}
	
	/**
	 * The constructor for the affymetrixview controller
	 * @param affymetrixview
	 */
	public Controller(AffymetrixView affymetrixview)
	{

	}
	
	/**
	 * The constructor for the illuminaview controller
	 * @param illuminaview
	 */
	public Controller(IlluminaView illuminaview)
	{
		this.illuminaview = illuminaview;
	}

	/**
	 * This method receives a set of ActionEvents denoted in the viewframes (mainframe, affy and illumina views).
	 * The ActionEvents then define what sort of action is to be carried out
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
