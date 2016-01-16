package irrcyn.View;



import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import irrcyn.internal.MyTaskObserver;
import irrcyn.internal.parser.ParserTask;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.FileReader.FileReader;
import de.bioquant.cytoscape.pidfileconverter.FileReader.PidFileReader;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ExtPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.FileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.IdWithPreferredSymbolWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.ModificationsWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.NodeTypeAttributeForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PidForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.PreferredSymbolForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.SifFileWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.UniprotIdForUniprotWithModWriter;
import de.bioquant.cytoscape.pidfileconverter.FileWriter.MemberExpansion.SifFileExpandMolWriter;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;

import static java.lang.Thread.sleep;

/**


/**
 * This class is responsible for the Actions and the methods they call.
 * @author Hadi Kang
 *
 */
@SuppressWarnings("serial")
public class Controller extends JFrame implements ActionListener{

	private MainFrame mainframe;
	private String inputfilepath;

	private static String inputbarcode1;
	private static String inputbarcode2;
	private static String targetSIFpath;
	private static String targetCSVpath;
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

	// Cytoscape
	private TaskManager tm;
	private LoadNetworkFileTaskFactory ldn;
	private LoadTableFileTaskFactory ldt;
	private LoadVizmapFileTaskFactory lds;
	private TaskIterator itr;
	private ParserTask parserTask;


	// the output directory
	private String dir;

	private String outputMessage;

	/**
	 * The constructor for the mainframe controller
	 * @param mainframe
	 * @param tm
	 * @param ldn
	 * @param ldt
	 * @param lds
	 */
	public Controller(MainFrame mainframe, TaskManager tm, LoadNetworkFileTaskFactory ldn, LoadTableFileTaskFactory ldt, LoadVizmapFileTaskFactory lds)
	{
		this.mainframe = mainframe;
		this.tm = tm;
		this.ldn=ldn;
		this.ldt=ldt;
		this.lds = lds;
	}

	/**
	 * This method receives a set of ActionEvents denoted in the viewframes (mainframe, affy and illumina views).
	 * The ActionEvents then define what sort of action is to be carried out
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		// if user clicks browse button
		if (command.equals("Browse")) {
			browseInputFile();
		}

		// if user clicks output button
		if (command.equals("Output")) {
			browseOutputFilePath();
		}
		// if user runs the convert button
		if (command.equals("Convert")) {
			// do a read of the inputtextfield to determine file
			inputfilepath = mainframe.getInputTextfieldText().trim();;
			// set this filepath in the File object
			curFile = new File(inputfilepath);

			try {
				convertFile();
			} catch (Exception exp) {
				JOptionPane
						.showMessageDialog(new JFrame(),
								"Problem when trying to generate the SIF : \n" + exp.toString(),
								"Warning", JOptionPane.WARNING_MESSAGE);
				exp.printStackTrace();
			}
			try {
				parserTask = new ParserTask(getTargetCSVpath(),getTargetPIDpath(), getTargetNODE_TYPEpath(), getTargetUNIPROTpath(), getTargetMODIFICATIONSpath(),
						getTargetPREFERRED_SYMBOLpath(), getTargetPREFERRED_SYMBOL_EXTpath(), getTargetPREFERRED_SYMBOL_EXTpath(), getTargetID_PREFpath());

				File SIFFile = new File(getTargetSIFpath());
				MyTaskObserver myTaskObserver = new MyTaskObserver(mainframe, tm, ldt, parserTask);

				itr = ldn.createTaskIterator(SIFFile);
				itr.append(parserTask);
				tm.execute(itr, myTaskObserver);
			} catch (Exception exp) {
				JOptionPane
						.showMessageDialog(new JFrame(),
								"The graph cannot be read! : " + exp.toString(),
								"Warning", JOptionPane.WARNING_MESSAGE);
			} finally {
				mainframe.requestFocus();
			}


		}
	}

	/**
	 * This method gets the filepath from the input file text area and then converts that xml file into SIF files
	 */
	private void convertFile() throws Exception {
		// Inform the user that we're working
		mainframe.isLoading();
		outputMessage = "Starting conversion...";

		// ---------- Setting the output
		// *** Creating the dir

		// if the outputfiletextfield is empty, output file folder is same as input file's
		if(mainframe.getOutputTextfieldText().trim().equals("")) {
			String[] temporarypath = null;
			temporarypath = curFile.getAbsolutePath().split(".xml");
			dir = temporarypath[0];

			outputMessage += '\n' + "No output fuilled, same directory as input will be used";
		}
		else{
			try {
				File f = new File(mainframe.getOutputTextfieldText());

				// if the selected is a directory
				if(f.isDirectory())
				{
					// split the file name
					String[] temporarypath = curFile.getName().split(".xml");

					dir = f.getAbsolutePath() + File.separator + temporarypath[0];

				}
				else if(f.getAbsolutePath().endsWith("sif"))
				{
					String[] temporarypath = f.getAbsolutePath().split(".sif");
					dir = temporarypath[0];
				}
				else if(f.getAbsolutePath().endsWith("csv"))
				{
					String[] temporarypath = f.getAbsolutePath().split(".csv");
					dir = temporarypath[0];
				}
				else{
					throw new Exception("Invalid output : directory, SIF file or CSV file expected");
				}
			} catch (NullPointerException nullExp){
				outputMessage += '\n' + "No output fuilled, same directory as input will be used";
			}
		}


		// *** Setting the targets
		setTargetSIFpath(dir.concat(".sif"));
		setTargetCSVpath(dir.concat(".csv"));
		// set the target node type NA path
		setTargetNODE_TYPEpath(dir.concat(".NODE_TYPE.NA"));
		// set the UNIPROT NA path
		setTargetUNIPROTpath(dir.concat(".UNIPROT.NA"));
		// set the MODIFICATIONS NA path
		setTargetMODIFICATIONSpath(dir.concat(".MODIFICATIONS.NA"));
		// set the PREFERRED_SYMBOL NA path
		setTargetPREFERRED_SYMBOLpath(dir.concat(".PREFERRED_SYMBOL.NA"));
		// set the PREFERRED_SYMBOL_EXT NA path
		setTargetPREFERRED_SYMBOL_EXTpath(dir.concat(".PREFERRED_SYMBOL_EXT.NA"));
		// set the PID NA path
		setTargetPIDpath(dir.concat(".PID.NA"));
		// set the ID_PREF NA path
		setTargetID_PREFpath(dir.concat(".ID_PREF.NA"));
		// set the IDCytoUniProtFile path
		setTargetIDCytoUniProtFilepath(dir.concat(".IDCytoToUniProt.NA"));
		// set the UniqueUniProtFile path
		setTargetUniqueUniProtFilepath(dir.concat(".UNIQUEUNIPROT.NA"));
		// set the UniProt to GeneID map file path
		setTargetUniProtToGeneIDMapFilepath(dir.concat(".UPToGeneIDMap.NA"));
		// set the GeneID to Affymetrix map file path
		setTargetGeneIDtoAffymetrixMapFilepath(dir.concat(".GeneIDToAffyMap.NA"));

		// For the user
		outputMessage += '\n' + "Target for conversion : " + dir;
		mainframe.setTextToOutputText(dir);

		// ----------------- Converting
		if(inputfilepath.endsWith("xml"))
		{
			NodeManagerImpl manager = NodeManagerImpl.getInstance();
			FileReader reader = PidFileReader.getInstance();
			try
			{
				reader.read(inputfilepath);
			}
			catch (NoValidManagerSetException e1)
			{
				JOptionPane
						.showMessageDialog(new JFrame(),
								"Program error, please contact hadi.kang@bioquant.uni-heidelberg.de for assistance.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}
			catch (FileParsingException e1)
			{
				JOptionPane
						.showMessageDialog(new JFrame(),
								"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
			}

			FileWriter writer = SifFileWriter.getInstance();
			try
			{
				writer.write(getTargetSIFpath(), manager);
				if (mainframe.isExpandChecked())
				{
					writer = SifFileExpandMolWriter.getInstance();
					writer.write(getTargetSIFpath(), manager);
				}
			}
			catch (FileNotFoundException e)
			{
				JOptionPane
						.showMessageDialog(new JFrame(),
								"File write error",
								"Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}

			FileWriter nWriter = NodeTypeAttributeForUniprotWithModWriter.getInstance();
			try
			{
				nWriter.write(getTargetNODE_TYPEpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter uWriter = UniprotIdForUniprotWithModWriter.getInstance();
			try
			{
				uWriter.write(getTargetUNIPROTpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter modiWriter = ModificationsWriter.getInstance();
			try
			{
				modiWriter.write(getTargetMODIFICATIONSpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter pWriter = PreferredSymbolForUniprotWithModWriter.getInstance();
			try
			{
				pWriter.write(getTargetPREFERRED_SYMBOLpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			pWriter = ExtPreferredSymbolWriter.getInstance();
			try
			{
				pWriter.write(getTargetPREFERRED_SYMBOL_EXTpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter pidWriter = PidForUniprotWithModWriter.getInstance();
			try
			{
				pidWriter.write(getTargetPIDpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			FileWriter prefIdWriter = IdWithPreferredSymbolWriter.getInstance();
			try
			{
				prefIdWriter.write(getTargetID_PREFpath(), manager);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		}
		else // not an xml
		{
			throw new Exception("Invalid input : .xml expected");
		}
	}

	/**
	 * This method opens a file chooser dialog box and sets the input text field string to the absolute path of the file
	 */
	private void browseInputFile()
	{
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose an XML file");
			FileNameExtensionFilter xmldata = new FileNameExtensionFilter("XML", "xml");
			fc.addChoosableFileFilter(xmldata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				curFile = fc.getSelectedFile();
				inputfilepath = curFile.getAbsolutePath();
				mainframe.setInputFileText(inputfilepath);
			}

		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a valid xml file downloaded from Pathway Interaction Database, or a converted SIF file.",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

	}

	/**
	 * This method selects the folder in which the output file should be placed.
	 * if there are same named files present, they will be overwrite!
	 */
	private void browseOutputFilePath()
	{
		String filedirectory = "";
		String filepath = "";
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //select directories or files
			fc.setDialogTitle("Please choose a directory to save the converted file(s)");

			FileNameExtensionFilter sifdata = new FileNameExtensionFilter("SIF", "sif");
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				mainframe.setOutputTextfieldText(fc.getSelectedFile().getAbsolutePath());
			}

		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Problem when trying to choose an output : " + e.toString(),
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}


	public String getTargetSIFpath()
	{
		return targetSIFpath;
	}

	public void setTargetSIFpath(String targetSIFpath)
	{
		this.targetSIFpath = targetSIFpath;
	}

	public String getTargetNODE_TYPEpath()
	{
		return targetNODE_TYPEpath;
	}

	public void setTargetNODE_TYPEpath(String targetNODE_TYPEpath)
	{
		this.targetNODE_TYPEpath = targetNODE_TYPEpath;
	}

	public String getTargetUNIPROTpath()
	{
		return targetUNIPROTpath;
	}

	public void setTargetUNIPROTpath(String targetUNIPROTpath)
	{
		this.targetUNIPROTpath = targetUNIPROTpath;
	}

	public String getTargetMODIFICATIONSpath()
	{
		return targetMODIFICATIONSpath;
	}

	public void setTargetMODIFICATIONSpath(String targetMODIFICATIONSpath)
	{
		this.targetMODIFICATIONSpath = targetMODIFICATIONSpath;
	}

	public String getTargetPREFERRED_SYMBOLpath()
	{
		return targetPREFERRED_SYMBOLpath;
	}

	public void setTargetPREFERRED_SYMBOLpath(String targetPREFERRED_SYMBOLpath)
	{
		this.targetPREFERRED_SYMBOLpath = targetPREFERRED_SYMBOLpath;
	}

	public String getTargetPREFERRED_SYMBOL_EXTpath()
	{
		return targetPREFERRED_SYMBOL_EXTpath;
	}

	public void setTargetPREFERRED_SYMBOL_EXTpath(String targetPREFERRED_SYMBOL_EXTpath)
	{
		this.targetPREFERRED_SYMBOL_EXTpath = targetPREFERRED_SYMBOL_EXTpath;
	}

	public String getTargetPIDpath()
	{
		return targetPIDpath;
	}

	public void setTargetPIDpath(String targetPIDpath)
	{
		this.targetPIDpath = targetPIDpath;
	}

	public String getTargetID_PREFpath()
	{
		return targetID_PREFpath;
	}

	public void setTargetID_PREFpath(String targetID_PREFpath)
	{
		this.targetID_PREFpath = targetID_PREFpath;
	}

	public String getTargetIDCytoUniProtFilepath()
	{
		return targetIDCytoUniProtFilepath;
	}

	public void setTargetIDCytoUniProtFilepath(String targetIDCytoUniProtFilepath)
	{
		this.targetIDCytoUniProtFilepath = targetIDCytoUniProtFilepath;
	}

	public String getTargetUniqueUniProtFilepath()
	{
		return targetUniqueUniProtFilepath;
	}

	public void setTargetUniqueUniProtFilepath(String targetUniqueUniProtFilepath)
	{
		this.targetUniqueUniProtFilepath = targetUniqueUniProtFilepath;
	}

	public String getTargetUniProtToGeneIDMapFilepath()
	{
		return targetUniProtToGeneIDMapFilepath;
	}

	public void setTargetUniProtToGeneIDMapFilepath(String targetUniProtToGeneIDMapFilepath)
	{
		this.targetUniProtToGeneIDMapFilepath = targetUniProtToGeneIDMapFilepath;
	}

	public String getTargetGeneIDtoAffymetrixMapFilepath()
	{
		return targetGeneIDtoAffymetrixMapFilepath;
	}

	public void setTargetGeneIDtoAffymetrixMapFilepath(String targetGeneIDtoAffymetrixMapFilepath)
	{
		this.targetGeneIDtoAffymetrixMapFilepath = targetGeneIDtoAffymetrixMapFilepath;
	}

	public String getInputbarcode1()
	{
		return inputbarcode1;
	}

	public void setInputbarcode1(String inputbarcode1)
	{
		this.inputbarcode1 = inputbarcode1;
	}

	public String getInputbarcode2()
	{
		return inputbarcode2;
	}

	public void setInputbarcode2(String inputbarcode2)
	{
		this.inputbarcode2 = inputbarcode2;
	}


	public void setTargetCSVpath(String targetCSVpath) {
		this.targetCSVpath = targetCSVpath;
	}

	public String getTargetCSVpath() {
		return targetCSVpath;
	}
}