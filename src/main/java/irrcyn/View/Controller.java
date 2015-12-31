package irrcyn.View;



import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetwork;

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
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;
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
	private String inputfilepath;
	private static String inputbarcode1;
	private static String inputbarcode2;
	private static String targetSIFpath;
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
	 * This method receives a set of ActionEvents denoted in the viewframes (mainframe, affy and illumina views).
	 * The ActionEvents then define what sort of action is to be carried out
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	/*	String command = e.getActionCommand();

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
			//checks if the user has typed a file path into the input textfield. if not, alert!
			if (mainframe.getInputTextfieldText().equals("")) {
				JOptionPane
						.showMessageDialog(new JFrame(),
								"Please enter a file using the browse button above",
								"Warning", JOptionPane.WARNING_MESSAGE);
			} else if (!mainframe.getInputTextfieldText().equals("")) {
				// do a read of the inputtextfield to determine file
				String filetobeconverted = mainframe.getInputTextfieldText().trim();
				inputfilepath = filetobeconverted;
				// set this filepath in the File object
				curFile = new File(inputfilepath);

				//-------------------------------------------------------
				if (curFile.getAbsolutePath().endsWith("xml")) {
					// targetSIFpath is set here by default
					String[] temporarypath = curFile.getAbsolutePath().split(".xml");
					setTargetSIFpath(temporarypath[0].concat(".sif"));
				}
				if (curFile.getAbsolutePath().endsWith("sif")) {
					// targetSIFpath is set here by default
					setTargetSIFpath(curFile.getAbsolutePath());
				}

				// create the splashframe
				SplashFrame sp = new SplashFrame();
				sp.setTitle("Please Wait a Moment...");
				sp.setLocation(100, 100);
				sp.setSize(500, 100);
				sp.setResizable(false);
				sp.setVisible(true);
				try {
					//setfocus on this frame
					sp.requestFocus();
					// converting a file which is displayed in the inputtextfield JTextfield field in mainframe
					convertFile(filetobeconverted);
					isConverted = true;
				} catch (NullPointerException exp) {
					JOptionPane
							.showMessageDialog(new JFrame(),
									"Please enter a file using the browse button above",
									"Warning", JOptionPane.WARNING_MESSAGE);
					exp.printStackTrace();
				}
				try {
					//set focus on this frame
					sp.requestFocus();
					// draw the network graph of the SIF file
					drawGraphFromSIF(getTargetSIFpath());
					//set focus on this frame again
					sp.requestFocus();
					// load the NODE_TYPE .NA file
					loadNodeAttributeFile(getTargetNODE_TYPEpath());
					// load the UNIPROT .NA file
					loadNodeAttributeFile(getTargetUNIPROTpath());
					// load the MODIFICATIONS .NA file
					loadNodeAttributeFile(getTargetMODIFICATIONSpath());
					// load the PREFERRED_SYMBOL .NA file
					loadNodeAttributeFile(getTargetPREFERRED_SYMBOLpath());
					// load the PREFERRED_SYMBOL_EXT .NA file
					loadNodeAttributeFile(getTargetPREFERRED_SYMBOL_EXTpath());
//					// load the PREFERRED_SYMBOL_EXT .NA file
//					loadNodeAttributeFileFromGraph(getTargetPREFERRED_SYMBOL_EXTpath());
					// load the PID .NA filecreateNetworkTaskFactory
					loadNodeAttributeFile(getTargetPIDpath());
					// load the ID_PREF .NA file
					loadNodeAttributeFile(getTargetID_PREFpath());
					// change the title of the splash frame
					sp.setTitle("Network loaded, now loading visualisation...");
					// load the VIZMAP props file
					mapVisually(VIZMAP_PROPS_FILE_NAME);

					// change the title of the splash frame
					sp.setTitle("Visualisation loaded, this window closes automatically.");

					// displaying a successful conversion message
					String filepath = getTargetSIFpath();
					JOptionPane
							.showMessageDialog(new JFrame(),
									"Conversion successful! Files converted are located in the directory:"
											+ "\n" + filepath,
									"Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exp) {
					JOptionPane
							.showMessageDialog(new JFrame(),
									"The graph cannot be read!",
									"Warning", JOptionPane.WARNING_MESSAGE);
					exp.printStackTrace();
				} finally {
					mainframe.requestFocus();
					// delete the SplashFrame
					sp.dispose();
				}
			}

		}*/
	}

//	/**
//	 * This method creates a network graph from the string 's',
//	 * which should be a path to a Cytoscape readable file, e.g. SIF
//	 * @param s the path of the file to be read.
//	 */
//	private void drawGraphFromSIF(String s)
//	{
//		File file = new File(s);
//		tm.execute(ldn.createTaskIterator(file));
//		Cytoscape.getCurrentNetworkView().redrawGraph(false, true);
//	}
//
//	/**
//	 * This method loads the attribute NA files
//	 * @param s the path of the NA file to be loaded
//	 */
//	private void loadNodeAttributeFile(String s)
//	{
//		Cytoscape.loadAttributes(new String[] { s },
//				new String[] {});
//		Cytoscape.getCurrentNetworkView().updateView();
//		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);
//	}
//
//	/**
//	 * This method loads the attribute NA files
//	 * @param s the path of the NA file to be loaded
//	 */
//	private void loadNodeAttributeFileFromGraph(String s)
//	{
//		Cytoscape.loadAttributes(new String[] { s },
//				new String[] {  });
//		Cytoscape.getCurrentNetworkView().updateView();
//		Cytoscape.firePropertyChange(Cytoscape.ATTRIBUTES_CHANGED, null, null);
//
//		//TODO: big construction site here, to find out how to import network file from table
//		ImportAttributeTableTask task = new ImportAttributeTableTask(new DefaultAttributeTableReader(null, null,
//				0, null, true), s);
//		task.run();
//		//http://chianti.ucsd.edu/svn/cytoscape/trunk/coreplugins/TableImport/src/main/java/edu/ucsd/bioeng/coreplugin/tableImport/ui/ImportTextTableDialog.java
////		AttributeMappingParameters mapping;
////		mapping = new AttributeMappingParameters(objType, del,
////				 listDelimiter, keyInFile,
////				 mappingAttribute, aliasList,
////				 attributeNames, attributeTypes,
////				 listDataTypes, importFlag, caseSensitive);
//		Cytoscape.firePropertyChange(Cytoscape.NEW_ATTRS_LOADED, null, null);
//
////		try {
////			ImportTextTableDialog ittd = new ImportTextTableDialog(Cytoscape.getDesktop(), true, 1);
////			ittd.pack();
////			ittd.setLocationRelativeTo(Cytoscape.getDesktop());
////			ittd.setVisible(true);
////
////		} catch (JAXBException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//	}
//
//
//	/**
//	 * This method loads the vizmap property file and redraws the graph
//	 * Also does the layout hierarchically and rotates the graph 180 degrees
//	 * @param s the name of the vizmap property file
//	 */
//	private void mapVisually(String s)
//	{
//		//load the vizmap file
//		Cytoscape.firePropertyChange(Cytoscape.VIZMAP_LOADED, null, s);
//		VisualStyle vs = Cytoscape.getVisualMappingManager().getCalculatorCatalog().getVisualStyle("netView");
//		Cytoscape.getCurrentNetworkView().setVisualStyle(vs.getName()); // not strictly necessary
//
//		// actually apply the visual style
//		Cytoscape.getVisualMappingManager().setVisualStyle(vs);
//		Cytoscape.getCurrentNetworkView().redrawGraph(true,true);
//
//		// TODO: should ask user whether he wants this? set the layout as hierarchical
//		CyLayouts.getLayout("hierarchical").doLayout();
//		if(Cytoscape.getCurrentNetwork().getNodeCount()<1000)
//		{
//			// TODO: sometimes rotating the graph causes some crash errors. thus only rotate if hierarchichal layout!
//			//rotateGraph();
//		}
//	}

	/**
	 * This method gets the filepath from the input file text area and then converts that xml file into SIF files
	 * @param filepath
	 */
	private void convertFile(String filepath)
	{

		// if the outputfiletextfield is empty, output file folder is same as input file's
		if(mainframe.getOutputTextfieldText().trim().equals(""))
		{
			String[] temporarypath = null;
			// if a file with xml ending
			if(curFile.getAbsolutePath().endsWith("xml"))
			{
				// set the SIF file path
				temporarypath = curFile.getAbsolutePath().split(".xml");
				setTargetSIFpath(temporarypath[0].concat(".sif"));
			}
			if(curFile.getAbsolutePath().endsWith("sif"))
			{
				temporarypath = curFile.getAbsolutePath().split(".sif");
				setTargetSIFpath(curFile.getAbsolutePath());
				// for loading of pre-filtered SIF files
				if(curFile.getAbsolutePath().contains(getAbsentProteinsConcatenation()))
				{
					temporarypath[0] = temporarypath[0].replace(getAbsentProteinsConcatenation(), "");
				}
				// for loading of pre-subgraphed SIF files
				if(curFile.getAbsolutePath().contains(getSubgraphed()))
				{
					temporarypath[0] = temporarypath[0].replace(getSubgraphed(), "");
				}
			}
			// set the target node type NA path
			setTargetNODE_TYPEpath(temporarypath[0].concat(".NODE_TYPE.NA"));
			// set the UNIPROT NA path
			setTargetUNIPROTpath(temporarypath[0].concat(".UNIPROT.NA"));
			// set the MODIFICATIONS NA path
			setTargetMODIFICATIONSpath(temporarypath[0].concat(".MODIFICATIONS.NA"));
			// set the PREFERRED_SYMBOL NA path
			setTargetPREFERRED_SYMBOLpath(temporarypath[0].concat(".PREFERRED_SYMBOL.NA"));
			// set the PREFERRED_SYMBOL_EXT NA path
			setTargetPREFERRED_SYMBOL_EXTpath(temporarypath[0].concat(".PREFERRED_SYMBOL_EXT.NA"));
			// set the PID NA path
			setTargetPIDpath(temporarypath[0].concat(".PID.NA"));
			// set the ID_PREF NA path
			setTargetID_PREFpath(temporarypath[0].concat(".ID_PREF.NA"));
			// set the IDCytoUniProtFile path
			setTargetIDCytoUniProtFilepath(temporarypath[0].concat(".IDCytoToUniProt.NA"));
			// set the UniqueUniProtFile path
			setTargetUniqueUniProtFilepath(temporarypath[0].concat(".UNIQUEUNIPROT.NA"));
			// set the UniProt to GeneID map file path
			setTargetUniProtToGeneIDMapFilepath(temporarypath[0].concat(".UPToGeneIDMap.NA"));
			// set the GeneID to Affymetrix map file path
			setTargetGeneIDtoAffymetrixMapFilepath(temporarypath[0].concat(".GeneIDToAffyMap.NA"));
		}
		this.inputfilepath = filepath;
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
		else if(inputfilepath.endsWith("sif"))
		{

		}
		else // not an xml or SIF file
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"File parse error. Make sure you have selected a valid xml file downloaded from Protein Interaction Database.",
							"Warning", JOptionPane.WARNING_MESSAGE);

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
			FileNameExtensionFilter sifdata = new FileNameExtensionFilter("SIF", "xml");
			fc.addChoosableFileFilter(xmldata);
			fc.addChoosableFileFilter(sifdata);

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				curFile = fc.getSelectedFile();
				if(curFile.getAbsolutePath().endsWith("xml"))
				{
					inputfilepath = curFile.getAbsolutePath();
					mainframe.setInputFileText(inputfilepath);

					// targetSIFpath is set here by default
					String[] temporarypath = curFile.getAbsolutePath().split(".xml");
					setTargetSIFpath(temporarypath[0].concat(".sif"));
				}
				if(curFile.getAbsolutePath().endsWith("sif"))
				{
					inputfilepath = curFile.getAbsolutePath();
					mainframe.setInputFileText(inputfilepath);
				}
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
				// if the selected is a directory
				if(fc.getSelectedFile().isDirectory())
				{
					// get the absolute path of the directory in which the file lies
					filepath = fc.getSelectedFile().getAbsolutePath();
					// split the file name
					String[] temporarypath = curFile.getName().split(".xml");

					// set the target SIF path
					setTargetSIFpath(filepath.concat("\\").concat(temporarypath[0].concat(".sif")));

					// set the target node type NA path
					setTargetNODE_TYPEpath(filepath.concat("\\").concat(temporarypath[0].concat(".NODE_TYPE.NA")));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filepath.concat("\\").concat(temporarypath[0].concat(".UNIPROT.NA")));

					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filepath.concat("\\").concat(temporarypath[0].concat(".MODIFICATIONS.NA")));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filepath.concat("\\").concat(temporarypath[0].concat(".PREFERRED_SYMBOL.NA")));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filepath.concat("\\").concat(temporarypath[0].concat(".PREFERRED_SYMBOL_EXT.NA")));

					// set the target PID NA path
					setTargetPIDpath(filepath.concat("\\").concat(temporarypath[0].concat(".PID.NA")));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filepath.concat("\\").concat(temporarypath[0].concat(".ID_PREF.NA")));

					// set the IDCytoUniProtFile path
					setTargetIDCytoUniProtFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".IDCytoToUniProt.NA")));

					// set the UniqueUniProtFile path
					setTargetUniqueUniProtFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UNIQUEUNIPROT.NA")));

					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap.NA")));

					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filepath.concat("\\").concat(temporarypath[0].concat(".GeneIDToAffyMap.NA")));

				}
				else // if the selected is a file
				{
					// get the directory path
					filedirectory = fc.getCurrentDirectory().getAbsolutePath();
					// split the current file into one without .xml
					String[] temporarypath = curFile.getName().split(".xml");

					// set the target SIF path
					setTargetSIFpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".sif"));

					// set the target node type NA path
					setTargetNODE_TYPEpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".NODE_TYPE.NA"));

					// set the target uniprot NA path
					setTargetUNIPROTpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".UNIPROT.NA"));

					// set the target MODIFICATIONS NA path
					setTargetMODIFICATIONSpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".MODIFICATIONS.NA"));

					// set the target PREFERRED_SYMBOLpath NA path
					setTargetPREFERRED_SYMBOLpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PREFERRED_SYMBOL.NA"));

					// set the target PREFERRED_SYMBOL_EXTpath NA path
					setTargetPREFERRED_SYMBOL_EXTpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PREFERRED_SYMBOL_EXT.NA"));

					// set the target PID NA path
					setTargetPIDpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".PID.NA"));

					// set the target ID_PREF NA path
					setTargetID_PREFpath(filedirectory.concat("\\").concat(temporarypath[0]).concat(".ID_PREF.NA"));

					// set the IDCytoUniProtFile path
					setTargetIDCytoUniProtFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".IDCytoToUniProt.NA")));

					// set the UniqueUniProtFile path
					setTargetUniqueUniProtFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UNIQUEUNIPROT.NA")));

					// set the UniProt to GeneID map file path
					setTargetUniProtToGeneIDMapFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".UPToGeneIDMap.NA")));

					// set the GeneID to Affymetrix map file path
					setTargetGeneIDtoAffymetrixMapFilepath(filedirectory.concat("\\").concat(temporarypath[0].concat(".GeneIDToAffyMap.NA")));
				}
				mainframe.setOutputTextfieldText(targetSIFpath);
			}

		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a valid xml file downloaded from Protein Interaction Database.",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}


	/**
	 * This method opens a browsing window so the user can select a gene source file
	 */
	private void browseGeneSource()
	{
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				genesourceFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setGenesourcetextfieldText(genesourceFile.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a Text file",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a gene target file
	 */
	private void browseGeneTarget()
	{
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				genetargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setGenetargettextfieldText(genetargetFile.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a Text file",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a sigmol source file
	 */
	private void browseSigmolSource()
	{
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				sigmolsourceFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setSigmolsourcetextfieldText(sigmolsourceFile.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a Text file",
							"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method opens a browsing window so the user can select a s file
	 */
	private void browseSigmolTarget()
	{
		try
		{
			JFileChooser fc = new JFileChooser(".");
			fc.setDialogTitle("Please choose a text file");

			int returnVal = fc.showOpenDialog(this); // shows the dialog of the file browser
			// get name und path
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				sigmoltargetFile = fc.getSelectedFile();
				// put the absolute path in the textfield
				mainframe.setSigmoltargettextfieldText(sigmoltargetFile.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(new JFrame(),
							"Please select a Text file",
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

	public static String getAbsentProteinsConcatenation() {
		return ABSENT_PROTEINS_CONCATENATION;
	}

	public static String getSubgraphed() {
		return SUBGRAPHED;
	}
}