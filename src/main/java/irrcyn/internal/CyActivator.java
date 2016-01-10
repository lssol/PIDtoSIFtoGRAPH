package irrcyn.internal;

import irrcyn.View.Controller;
import irrcyn.View.MainFrame;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;


import java.util.Properties;

public class CyActivator extends AbstractCyActivator {
	private Controller controller;
	private MainFrame mainframe;

	@Override
	public void start(BundleContext bc) throws Exception {
		// To insert the app into the Apps menu
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		// To execute the tasks
		TaskManager tm = getService(bc, SynchronousTaskManager.class);
		// To load the sif
		LoadNetworkFileTaskFactory ldn = getService(bc, LoadNetworkFileTaskFactory.class) ;
		// To load the NA attributes
		LoadTableFileTaskFactory ldt = getService(bc, LoadTableFileTaskFactory.class) ;

		
		MenuAction action = new MenuAction(cyApplicationManager, "PIDtoSIFtoGRAPH", tm, ldn, ldt);
//   Register it as a service:
		registerAllServices(bc, action, new Properties());
	}

}
