package irrcyn.internal;

import irrcyn.internal.View.Controller;
import irrcyn.internal.View.MainFrame;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

import java.awt.event.ActionEvent;


/**
 * Creates a new menu item under Apps menu section.
 *
 */
public class MenuAction extends AbstractCyAction {
	private Controller controller;
	private MainFrame mainframe;

	public MenuAction(CyApplicationManager cyApplicationManager, final String menuTitle) {
		
		super(menuTitle, cyApplicationManager, null, null);
		setPreferredMenu("Apps");
		
	}

	public void actionPerformed(ActionEvent e) {

		try {
			mainframe = new MainFrame(controller);
			mainframe.setTitle("PIDtoSIFtoGRAPH");
			mainframe.setLocation(10, 10);
			mainframe.setSize(800, 400);
			mainframe.setResizable(false);
			mainframe.setVisible(true);
			mainframe.pack();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		
	}
}
