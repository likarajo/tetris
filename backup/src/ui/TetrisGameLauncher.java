package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TetrisGameLauncher extends JFrame {

	private JPanel contentPane;
	private static TetrisGameLauncher tetrisGameLauncher;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					tetrisGameLauncher = new TetrisGameLauncher();
					tetrisGameLauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TetrisGameLauncher() {
		setTitle("Tetris Game Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 310, 315);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblScoreMultiplier = new JLabel("Score Multiplier: ");
		lblScoreMultiplier.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblLinesPerLevel = new JLabel("Lines Per Level: ");
		lblLinesPerLevel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblGameSpeed = new JLabel("Game Speed: ");
		lblGameSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblGameWidth = new JLabel("Game Width: ");
		lblGameWidth.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblGameHeight = new JLabel("Game Height: ");
		lblGameHeight.setHorizontalAlignment(SwingConstants.CENTER);
		
		JSlider sliderLinesPerLevel = new JSlider();
		sliderLinesPerLevel.setValue(20);
		sliderLinesPerLevel.setMaximum(50);
		sliderLinesPerLevel.setMinimum(20);
		sliderLinesPerLevel.setMinorTickSpacing(1);
		sliderLinesPerLevel.setMajorTickSpacing(5);
		sliderLinesPerLevel.setPaintTicks(true);
		sliderLinesPerLevel.setPaintLabels(true);
		sliderLinesPerLevel.setSnapToTicks(true);
		
		JSlider sliderGameSpeed = new JSlider();
		sliderGameSpeed.setToolTipText("In Milliseconds.");
		sliderGameSpeed.setMinorTickSpacing(20);
		sliderGameSpeed.setMinimum(100);
		sliderGameSpeed.setMajorTickSpacing(200);
		sliderGameSpeed.setMaximum(1000);
		sliderGameSpeed.setValue(500);
		sliderGameSpeed.setPaintTicks(true);
		sliderGameSpeed.setPaintLabels(true);
		sliderGameSpeed.setSnapToTicks(true);
		
		JSlider sliderGameWidth = new JSlider();
		sliderGameWidth.setValue(10);
		sliderGameWidth.setMajorTickSpacing(10);
		sliderGameWidth.setMinorTickSpacing(5);
		sliderGameWidth.setMinimum(5);
		sliderGameWidth.setPaintTicks(true);
		sliderGameWidth.setPaintLabels(true);
		sliderGameWidth.setSnapToTicks(true);
		
		JSlider sliderGameHeight = new JSlider();
		sliderGameHeight.setValue(20);
		sliderGameHeight.setMinorTickSpacing(5);
		sliderGameHeight.setMajorTickSpacing(10);
		sliderGameHeight.setMinimum(15);
		sliderGameHeight.setPaintTicks(true);
		sliderGameHeight.setPaintLabels(true);
		sliderGameHeight.setSnapToTicks(true);
		
		JSlider sliderScoreMultiplier = new JSlider();
		sliderScoreMultiplier.setValue(1);
		sliderScoreMultiplier.setMinimum(1);
		sliderScoreMultiplier.setMaximum(10);
		sliderScoreMultiplier.setMinorTickSpacing(1);
		sliderScoreMultiplier.setMajorTickSpacing(1);
		sliderScoreMultiplier.setPaintTicks(true);
		sliderScoreMultiplier.setToolTipText("");
		sliderScoreMultiplier.setPaintLabels(true);
		sliderScoreMultiplier.setSnapToTicks(true);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int scoreMultiplier = sliderScoreMultiplier.getValue();
				int linesPerLevel = sliderLinesPerLevel.getValue();
				long updateInterval = sliderGameSpeed.getValue();
				int playAreaWidth = sliderGameWidth.getValue();
				int playAreaHeight = sliderGameHeight.getValue();
				
				TetrisGameUI ui = new TetrisGameUI(playAreaWidth, playAreaHeight,
						updateInterval, linesPerLevel, scoreMultiplier);
				ui.setVisible(true);
				
				tetrisGameLauncher.setVisible(false);
				tetrisGameLauncher.dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblLinesPerLevel, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(lblScoreMultiplier, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(lblGameSpeed, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(lblGameWidth, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
								.addComponent(lblGameHeight, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(sliderGameHeight, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
								.addComponent(sliderGameWidth, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
								.addComponent(sliderGameSpeed, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
								.addComponent(sliderLinesPerLevel, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
								.addComponent(sliderScoreMultiplier, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
							.addGap(4))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnStartGame, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addComponent(lblScoreMultiplier, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addComponent(sliderScoreMultiplier, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLinesPerLevel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sliderLinesPerLevel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGameSpeed, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sliderGameSpeed, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGameWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sliderGameWidth, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGameHeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(sliderGameHeight, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnStartGame)
					.addGap(0))
		);
		contentPane.setLayout(gl_contentPane);
	}
}