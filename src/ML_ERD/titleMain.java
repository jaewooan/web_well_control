package ML_ERD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem; 
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JMenu;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import ML_ERD.MainMenu.detectTask;

public class titleMain extends JApplet implements ActionListener{	
	
	static final JButton btnStart = new JButton("START");
	static final Timer Timer1 = new Timer();	
	static MainMenu m1;
	int numberCount=0; 
	static summatin aa;
	JPanel panel = new JPanel();
	JPanel panel2 = new JPanel();
	int xSize = 800, ySize=600;
	 
	static URL base_Title;
	static URL base_SNU;
	static Image img_Title;
	static Image img_SNU;
	static Image img_SNUlogo;
	static MediaTracker tr_Title;
	static MediaTracker tr_SNU;
	ImagePanel imgPnlTitle;
	ImagePanel imgPnlSNU;
	
	int ImgSizeX=200;
	int ImgSizeY=60;
	//Dim passCount, Color As Integer
	//Dim pmoveTop(4) As Integer, pmoveLeft(4) As Integer
	
	public titleMain(){
		setBounds(0,0, xSize, ySize);		
		//final frameex m1 = new frameex();
		MainDriver.MainMenuOn=0;
		panel.setBackground(Color.WHITE);
		panel.setForeground(new Color(0, 0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblTitle1 = new JLabel("Drilling Simulation System");
		lblTitle1.setForeground(new Color(0, 0, 0));
		lblTitle1.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 50));
		lblTitle1.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle1.setBounds(0, 25, xSize, 50);
		panel.add(lblTitle1);
		
		JLabel lblTitle2 = new JLabel("for Onshore and Offshore wells");
		lblTitle2.setForeground(new Color(0, 0, 0));
		lblTitle2.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 30));
		lblTitle2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle2.setBounds(0, 75, xSize, 50);
		panel.add(lblTitle2);
		
		JLabel lblTitle3 = new JLabel("in Java Applet.");
		lblTitle3.setForeground(new Color(0, 0, 0));
		lblTitle3.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 30));
		lblTitle3.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle3.setBounds(0, 115, xSize, 50);
		panel.add(lblTitle3);
		
		panel.add(panel2);
		panel2.setBackground(Color.WHITE);
		panel2.setBounds(0,185,800,386);
		panel2.setLayout(null);
		
		JLabel LBLTitle4 = new JLabel("Press the button to start the program.");
		LBLTitle4.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 20));
		LBLTitle4.setHorizontalAlignment(SwingConstants.CENTER);
		LBLTitle4.setBounds(0, 310, xSize, 29);
		panel2.add(LBLTitle4);
		
		UIManager.put("OptionPane.okButtonText","Ok");
		UIManager.put("OptionPane.yesButtonText", "Yes");
		UIManager.put("OptionPane.noButtonText","No");
		UIManager.put("OptionPane.cancelButtonText", "Cancel");
		
		Timer1.schedule(new WorkTask(), 0, 500);
		btnStart.setFont(new Font("±¼¸²", Font.BOLD, 20));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numberCount++;
				initialize();
				//MainModule.DefaultData_test();    //Set Default variable
				//MainModule.DefaultData_VB();
				MainDriver.iCase=1;
				MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
				MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
				MainModule.DefaultData_case1_dr();
				
				MainModule.getGeometry();
				MainModule.setMDvd();
				//Timer1.cancel();
				if(numberCount==1 && MainDriver.MainMenuOn==0){
					m1=new MainMenu();
					m1.setVisible(true);
					m1.btnInputData.setEnabled(false);
					m1.btnSrtSim.setEnabled(false);
					m1.btnRunCase.setEnabled(false);
					MainDriver.MainMenuOn=1;
					if(m1.svTimerOn==0){
						m1.setVisibleTimer = new Timer();
						m1.setVisibleTimer.schedule(m1.new detectTask(), 0, 50);
						m1.svTimerOn=1;
					}
					btnStart.setEnabled(false);
				}
				else if(MainDriver.MainMenuOn==0){
					m1.dispose();
					m1=new MainMenu();
					m1.setVisible(true);
					m1.btnInputData.setEnabled(false);
					m1.btnSrtSim.setEnabled(false);
					m1.btnRunCase.setEnabled(false);
					MainDriver.MainMenuOn=1;
					if(m1.svTimerOn==0){
						m1.setVisibleTimer = new Timer();
						m1.setVisibleTimer.schedule(m1.new detectTask(), 0, 50);
						m1.svTimerOn=1;
						}
					btnStart.setEnabled(false);
					}
				else if(MainDriver.MainMenuOn==1) m1.setVisible(true);
				
			}
		});		
		
		btnStart.setBounds(xSize/2-55, 340, 110, 33);
		panel2.add(btnStart);		
		
		//Menu Setting
		JMenuBar mb = new JMenuBar();
		JMenuItem thelp = new JMenuItem("Help");
		thelp.setHorizontalAlignment(SwingConstants.LEFT);
		thelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				String helpContent = "  This program is carefully designed for easy use.  Use your common senses."+"\n" 
			                       + " Click once to activate menus and command buttons, unless otherwise specified."+"\n"
						           + " If you have any warning sign and ignore it, you can have unexpected results." +"\n"
			                       + " Hopefully, this simulator is good for well control analysis and education."+"\n"
						           + " Click the Continue menu or the Start button to start the program and then you will see Main Menu Screen." +"\n"
			                       + " If you have further questions or comments, please contact to the author.";
				JOptionPane.showMessageDialog(null, helpContent, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JMenuItem tver = new JMenuItem("Version");
		tver.setHorizontalAlignment(SwingConstants.LEFT);
		tver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String VersionC =  " The version of this program is 6.5 written using Java Development Kit(JDK) 6_45."+"\n"
								  +" It is a tool for Java programming and includes Java Runtime Environment(JRE)"+"\n"
								  +" which makes Java programs able to be activated in users¡¯ computers."+"\n"
								  +" In order to use this program, you should install Java Development Kit."+"\n"
								  +" The number 6_45 means the version of JDK. You can download Java Development Kit in"+"\n"
								  +"      http://www.oracle.com/technetwork/java/javase/downloads/index.html"+"\n"
								  +" and it is recommended to use later versions than JDK 6_45."+"\n"
								  +" Thank you.";
				JOptionPane.showMessageDialog(null, VersionC, "Version",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JMenuItem tAut = new JMenuItem("Author");
		tAut.setHorizontalAlignment(SwingConstants.LEFT);
		tAut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String AuthorC = " The author is a professor and consultant in the Department of Energy Resources Engineering"+"\n"
					     	   + "at Seoul National University. He has a PhD degree from Texas A&M University, "+"\n"
						       + "and MS and BS degrees from Seoul National University, Korea."+"\n"
						       + "For more Information, send an email to johnchoe@snu.ac.kr.";
				JOptionPane.showMessageDialog(null, AuthorC, "Author",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JMenuItem tcontin = new JMenuItem("Continue");
		tcontin.setHorizontalAlignment(SwingConstants.LEFT);
		tcontin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numberCount++;
				initialize();
				//MainModule.DefaultData_test();    //Set Default variable
				//MainModule.DefaultData_VB();
				MainDriver.iCase=1;
				MainDriver.iProblem[0] = 0;MainDriver.iProblem[1] = 0;MainDriver.iProblem[2] = 0;MainDriver.iProblem[3] = 0;
				MainDriver.iProblem_occured[0] = 0;MainDriver.iProblem_occured[1] = 0;MainDriver.iProblem_occured[2] = 0;MainDriver.iProblem_occured[3] = 0;
				MainModule.DefaultData_case1_dr();
				
				MainModule.getGeometry();
				MainModule.setMDvd();
				//Timer1.cancel();
				if(numberCount==1 && MainDriver.MainMenuOn==0){
					m1=new MainMenu();
					m1.setVisible(true);
					m1.btnInputData.setEnabled(false);
					m1.btnSrtSim.setEnabled(false);
					m1.btnRunCase.setEnabled(false);
					MainDriver.MainMenuOn=1;
					if(m1.svTimerOn==0){
						m1.setVisibleTimer = new Timer();
						m1.setVisibleTimer.schedule(m1.new detectTask(), 0, 50);
						m1.svTimerOn=1;
					}
					btnStart.setEnabled(false);
				}
				else if(MainDriver.MainMenuOn==0){
					m1.dispose();
					m1=new MainMenu();
					m1.setVisible(true);
					m1.btnInputData.setEnabled(false);
					m1.btnSrtSim.setEnabled(false);
					m1.btnRunCase.setEnabled(false);
					MainDriver.MainMenuOn=1;
					if(m1.svTimerOn==0){
						m1.setVisibleTimer = new Timer();
						m1.setVisibleTimer.schedule(m1.new detectTask(), 0, 50);
						m1.svTimerOn=1;
						}
					btnStart.setEnabled(false);
					}
				else if(MainDriver.MainMenuOn==1) m1.setVisible(true);	
			}
			
		});
		
		JMenu mnNewMenu = new JMenu("Menu");
		mnNewMenu.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		mb.add(mnNewMenu);
		
		mnNewMenu.add(thelp);
		mnNewMenu.add(tcontin);
		mnNewMenu.add(tver);
		mnNewMenu.add(tAut);		
		this.setJMenuBar(mb);		
		//MenuSetting END		
	}	
	//String h = "iHWDP :"+ Integer.toString(MainDriver.iHWDP)+ "i2 :"+ Integer.toString(i2) + "NWCS" + Integer.toString(MainDriver.NwcS);
    //JOptionPane.showMessageDialog(null, h);
	
			
	
	public void init(){		//20130120 ajw : image loading.
		MainDriver.tr=new MediaTracker(this);
		try{
			MainDriver.base = getDocumentBase();
			base_Title = getDocumentBase();
			base_SNU = getDocumentBase();
		}catch(Exception e){}
		MainDriver.img=getImage(MainDriver.base,"resource/WellTrjat.jpg");
		MainDriver.tr.addImage(MainDriver.img, 1);
		img_Title = getImage(base_Title, "resource/Title.jpg");
		imgPnlTitle = new ImagePanel(img_Title,xSize,300);
		imgPnlTitle.setBounds(0,0,800,300);
		panel2.add(imgPnlTitle);
		
		//ImgSizeX=240;
		//ImgSizeY=40;
		//img_SNUlogo = getImage(base_SNU, "resource/SNU.jpg");
		
		ImgSizeX=200;
		ImgSizeY=70;
		img_SNUlogo = getImage(base_SNU, "resource/snu2.jpg");
		
		imgPnlSNU = new ImagePanel(img_SNUlogo, ImgSizeX, ImgSizeY);
		imgPnlSNU.setBounds(xSize-ImgSizeX-2,386-ImgSizeY-2, ImgSizeX,ImgSizeY);
		
		img_SNU = getImage(base_SNU, "resource/symbol.jpg");
		//imgPnlSNU = new ImagePanel(img_SNU, 50, 50); 
		//imgPnlSNU.setBounds(xSize-52,386-52,50,50);
		panel2.add(imgPnlSNU);
		
		MainDriver.icon = new ImageIcon(img_SNU);
		
		try{
			MainDriver.tr.waitForAll();
			}catch(InterruptedException e){
				
			}
	}
	
	
	public void start(){
		
	}

	public void actionPerformed(ActionEvent event){
		
	}				
	
	public void stop(){
		
	}
	
	public void destroy(){
		
	}
	
	public void initialize(){
	//  Initialize all dimensional variables zero
	   MainDriver.iDone = 0;
	   double psia = 14.7;
	   for(int i=0; i<MainDriver.Npt; i++){
		   MainDriver.xTop[i] = 0;  MainDriver.xBot[i] = 0;   MainDriver.Ppump[i] = psia; MainDriver.rhoK[i] = 0;
		   MainDriver.Vpit[i] = 0;  MainDriver.VOLcir[i] = 0; MainDriver.Stroke[i] = 0; MainDriver.pxTop[i] = psia;
		   MainDriver.Pchk[i] = psia; MainDriver.Psp[i] = psia;   MainDriver.Pcsg[i] = psia;  MainDriver.TTsec[i] = 0;
		   MainDriver.Pb2p[i] = 0;  MainDriver.CHKopen[i] = 0; MainDriver.QmcfDay[i] = 0;
		   MainDriver.mudflow[i] = 0; MainDriver.gasflow[i] = 0; MainDriver.PmLine[i] = 0;
	   }
	   for(int i = 0; i< MainDriver.Ntot; i++){
		   MainDriver.Hgnd[i] = 0;  MainDriver.Pnd[i] = psia; MainDriver.Xnd[i] = 0;
		   MainDriver.pvtZb[i] = 0; MainDriver.volL[i] = 0;
	   }
	   
	   for(int i = 0; i < MainDriver.Nwc; i++){
		   MainDriver.TMD[i] = 0;   MainDriver.TVD[i] = 0;   MainDriver.Do2p[i] = 0; MainDriver.Di2p[i] = 0;
		   MainDriver.DiDS[i] = 0;  MainDriver.ang2p[i] = 0;  MainDriver.DiDS[i] = 0;  //A2p[i] = 0;
		   MainDriver.volDS[i] = 0; MainDriver.VOLann[i] = 0;
	   }
	   MainDriver.fileDat = " ";   //default file name before retrieving
	   }
	
	class WorkTask extends TimerTask{
		int i=0;
		public void run(){
			i++;
			if(i%2==0) titleMain.btnStart.setForeground(Color.RED);
			else titleMain.btnStart.setForeground(Color.BLUE);
			if(MainDriver.MainMenuOn==0) btnStart.setEnabled(true);
		}
	}

	class ImagePanel extends JPanel{
		Image img;
		int x=0;
		int y=0;
		ImagePanel(Image aa, int sizeX, int sizeY){
			setLayout(null);
			img = aa;
			x=sizeX;
			y=sizeY;
			}
		
		public void paint(Graphics g){
			super.paint(g);
			g.drawImage(img, 0, 0, x, y, this);
			}
		}
}
	

