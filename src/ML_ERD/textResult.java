package ML_ERD;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class textResult extends JFrame {

	private JPanel contentPane;
	TextArea textArea = new TextArea("", 10000, 10000, TextArea.SCROLLBARS_BOTH);
	
	textResult(String title) {
		setTitle(title);
		setIconImage(MainDriver.icon.getImage());
		
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                e.getWindow().dispose();
                e.getWindow().setVisible(false);
                menuClose();
            }
        });		
		
		setBounds(100, 100, 750, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textArea.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
		textArea.setFont(new Font("COURIER", Font.PLAIN, 12));
		contentPane.add(textArea);
	}
	
	void menuClose(){
		MainDriver.MainMenuVisible=1;
		this.dispose();
		this.setVisible(false);
	}

}
