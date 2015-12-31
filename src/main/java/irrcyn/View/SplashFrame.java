package irrcyn.View;

import javax.swing.*;
import java.awt.*;

public class SplashFrame extends JFrame
{
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel("Please wait for processing. This window closes automatically.");
	
	public SplashFrame()
	{
		panel.add(label);
		getContentPane().add(panel);
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}
}
