package ML_ERD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class blowout extends JFrame {

	private JPanel contentPane;
	int TimerBOOn = 0;
	int FrameXsize = 300;
	int FrameYsize = 200;

	/**
	 * Launch the application.
	 */

	blowout() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				MainDriver.BlowOutOccurred=1;
				menuclose();
				MainDriver.iWshow = 0;
				}
			});
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("You should NOT see this BLOWOUT !");
		setIconImage(MainDriver.icon.getImage());
		setBounds(500, 500, FrameXsize, FrameYsize);
		contentPane = new JPanel();
		contentPane.setBackground(Color.RED);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// TimerBO.schedule(new BOTask(), 0, 1000);
		JButton btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainDriver.BlowOutOccurred=1;
				menuclose();
			}
		});
		btnReset.setBounds(FrameXsize / 2 - 45, FrameYsize / 2 + 10, 80, 20);
		contentPane.add(btnReset);

		JLabel lblBlowOut = new JLabel("Blow Out!");
		lblBlowOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlowOut.setFont(new Font("±¼¸²", Font.BOLD, 27));
		lblBlowOut.setForeground(Color.BLACK);
		lblBlowOut.setBounds(FrameXsize / 2 - 105, FrameYsize / 2 - 80, 200, 80);
		contentPane.add(lblBlowOut);
	}

	void menuclose() {		
		this.dispose();
		this.setVisible(false);
	}
	
}
