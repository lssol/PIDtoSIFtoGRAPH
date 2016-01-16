package irrcyn.internal;

import irrcyn.View.Controller;
import irrcyn.View.MainFrame;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskManager;
import org.omg.CORBA.OBJ_ADAPTER;

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
	private LoadVizmapFileTaskFactory lds;

	public MenuAction(CyApplicationManager cyApplicationManager, final String menuTitle, TaskManager tm, LoadNetworkFileTaskFactory ldn, LoadTableFileTaskFactory ldt, LoadVizmapFileTaskFactory lds) {
		super(menuTitle, cyApplicationManager, null, null);
		setPreferredMenu("Apps");
		this.tm = tm;
		this.ldn=ldn;
		this.ldt=ldt;
		this.lds=lds;
	}

	public void actionPerformed(ActionEvent e) {

		try {
			mainframe = new MainFrame(controller, tm, ldn, ldt, lds);
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
