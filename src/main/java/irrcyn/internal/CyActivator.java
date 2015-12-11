package irrcyn.internal;

import irrcyn.internal.View.Controller;
import irrcyn.internal.View.MainFrame;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

import java.util.Properties;

public class CyActivator extends AbstractCyActivator {
	private Controller controller;
	private MainFrame mainframe;
	@Override
	public void start(BundleContext bc) throws Exception {
		
		CyApplicationManager cyApplicationManager = getService(bc, CyApplicationManager.class);
		
		MenuAction action = new MenuAction(cyApplicationManager, "PIDtoSIFtoGRAPH");
		
		Properties properties = new Properties();

//   Register it as a service:
		registerService(bc,action,CyAction.class, new Properties());
	}

}
