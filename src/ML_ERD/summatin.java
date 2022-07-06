package ML_ERD;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class summatin extends JFrame {
	ImagePanel pp = new ImagePanel();
	
	public summatin() {
		setBounds(0,0,300,300);
		setLayout(null);
		add(pp);
		pp.setBounds(0,0,200,200);
		setVisible(true);
	}
		
	class ImagePanel extends JPanel{		
		//Image img = null;// new ImageIcon(getClass().getResource("C://Image/WellTrjat.jpg"));		
		ImagePanel(){
			setLayout(null);
			//Toolkit tk = Toolkit.getDefaultToolkit();
			//img = tk.getImage("WellTrjat.jpg");	
			
		}
		
		public void paint(Graphics g){		
			super.paint(g);
			g.drawImage(MainDriver.img, 50,50, this);
				}			
		}
	}
