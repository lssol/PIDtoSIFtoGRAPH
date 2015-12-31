package irrcyn.internal;

import irrcyn.View.Controller;
import irrcyn.View.MainFrame;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskManager;

import java.awt.event.ActionEvent;


/**
 * Creates a new menu item under Apps menu section.
 *
 */
public class MenuAction extends AbstractCyAction {
	private Controller controller;
	private MainFrame mainframe;
	private TaskFactory factory;
	private TaskManager tm;
	private LoadNetworkFileTaskFactory ldn;
	private LoadTableFileTaskFactory ldt;

	public MenuAction(CyApplicationManager cyApplicationManager, final String menuTitle , TaskManager tm, LoadNetworkFileTaskFactory ldn, LoadTableFileTaskFactory ldt) {
		super(menuTitle, cyApplicationManager, null, null);
		setPreferredMenu("Apps");
		tm = tm;
		ldn=ldn;
		ldt=ldt;
	}

	public void actionPerformed(ActionEvent e) {

		try {
			mainframe = new MainFrame(controller, tm, ldn, ldt);
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
