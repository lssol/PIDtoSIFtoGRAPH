package de.bioquant.cytoscape.pidfileconverter.FileReader;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import de.bioquant.cytoscape.pidfileconverter.Exceptions.FileParsingException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.InvalidIdException;
import de.bioquant.cytoscape.pidfileconverter.Exceptions.NoValidManagerSetException;
import de.bioquant.cytoscape.pidfileconverter.Model.CompMolMember;
import de.bioquant.cytoscape.pidfileconverter.Model.MoleculeNode;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.MoleculeNodeManager;
import de.bioquant.cytoscape.pidfileconverter.NodeManager.NodeManagerImpl;

public final class PidFileReader implements FileReader {

	private static PidFileReader instance= null;
	NodeManagerImpl manager;
	PidFileHandler handler;
	
	private PidFileReader() throws NoValidManagerSetException{
		manager=NodeManagerImpl.getInstance();
		handler=new PidFileHandler(manager,manager,manager,manager,manager);
		
	}
	
	public static PidFileReader getInstance()
	{
		if (instance==null)
			try {
				instance = new PidFileReader();
			} catch (NoValidManagerSetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return instance;
	}
	
	@Override
	public void read(String path) throws NoValidManagerSetException,
			FileParsingException{
		SAXParserFactory factory = SAXParserFactory.newInstance();
	    try {
			SAXParser saxParser = factory.newSAXParser();
			
			
			saxParser.parse(path, handler);
			connectComplexesAndFamilies(manager,this.handler);
			manager.connectIntCompsToMolecules(manager, manager);
			manager.connectInteractionsToInteractionComponents(manager, manager);
		} catch (ParserConfigurationException e) {
			throw new FileParsingException("Failed to read PID file!",e);
		} catch (SAXException e) {
			throw new FileParsingException("Failed to read PID file! "+e.getMessage(),e);
		} catch (IOException e) {
			throw new FileParsingException("Failed to read PID file!",e);
		} catch (InvalidIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void connectComplexesAndFamilies(MoleculeNodeManager manager,PidFileHandler handler) throws InvalidIdException {
		Map<String, Collection<CompMolMember>> complMembers=handler.getComplexComponents();
		connectMultipleMolecules(manager, complMembers);
		
		Map<String, Collection<CompMolMember>> familyMembers=handler.getFamilyMembers();
		connectMultipleMolecules(manager, familyMembers);
		
	}
	
	private void connectMultipleMolecules(MoleculeNodeManager manager, Map<String, Collection<CompMolMember>> complMembers) throws InvalidIdException {
		for (Entry<String, Collection<CompMolMember>>complex:complMembers.entrySet()){
			String complexID=complex.getKey();
			for (CompMolMember member:complMembers.get(complexID)){
				MoleculeNode memMolecule=new MoleculeNode(MoleculeNode.PREFIX+member.getPid());
				member.setMolecule(manager.getEqualMoleculeNodeInManager(memMolecule));				
			}
		}
	}

}
