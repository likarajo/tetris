package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Setup extends JPanel {

	private static final long serialVersionUID = 1L;

	public static int M = 10; // scoring factor
	public static int N = 20; // number of rows required for each Level
	public static float S = 0.1f; // speed factor
	public static int W = 10; // Width of playArea (no. of squares)
	public static int H = 20; // Height of playArea (no. of squares)
	public static int squareSize = 10;

	public Setup() {

		this.setLayout(new GridLayout(7, 2));

		this.add(new JLabel("Scoring Factor", SwingConstants.CENTER));

		JSlider scoringFactorSlider = new JSlider();
		scoringFactorSlider.setMinimum(1);
		scoringFactorSlider.setMaximum(10);
		scoringFactorSlider.setMinorTickSpacing(1);
		scoringFactorSlider.setMajorTickSpacing(1);
		scoringFactorSlider.setPaintTicks(true);
		scoringFactorSlider.setPaintLabels(true);
		scoringFactorSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if ((JSlider) e.getSource() == scoringFactorSlider) {
					M = scoringFactorSlider.getValue();
				}
			}
		});
		this.add(scoringFactorSlider);

		this.add(new JLabel("Rows required in each level", SwingConstants.CENTER));

		JSlider rowsReqSlider = new JSlider();
		rowsReqSlider.setMinimum(20);
		rowsReqSlider.setMaximum(50);
		rowsReqSlider.setMinorTickSpacing(1);
		rowsReqSlider.setMajorTickSpacing(10);
		rowsReqSlider.setPaintTicks(true);
		rowsReqSlider.setPaintLabels(true);
		rowsReqSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if ((JSlider) e.getSource() == rowsReqSlider) {
					N = rowsReqSlider.getValue();
				}
			}
		});
		this.add(rowsReqSlider);

		this.add(new JLabel("Speed Factor", SwingConstants.CENTER));

		JSlider speedFactorSlider = new JSlider();
		speedFactorSlider.setMinimum(1);
		speedFactorSlider.setMaximum(10);
		speedFactorSlider.setMinorTickSpacing(1);
		speedFactorSlider.setMajorTickSpacing(1);
		speedFactorSlider.setPaintTicks(true);
		speedFactorSlider.setPaintLabels(true);
		speedFactorSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if ((JSlider) e.getSource() == speedFactorSlider) {
					S = (float) (speedFactorSlider.getValue() / 10.0);
				}
			}
		});
		this.add(speedFactorSlider);

		this.add(new JLabel("Play Area Width", SwingConstants.CENTER));

		JSlider widthSlider = new JSlider();
		widthSlider.setMinimum(10);
		widthSlider.setMaximum(20);
		widthSlider.setMinorTickSpacing(2);
		widthSlider.setMajorTickSpacing(2);
		widthSlider.setPaintTicks(true);
		widthSlider.setPaintLabels(true);
		widthSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if ((JSlider) e.getSource() == widthSlider) {
					W = widthSlider.getValue();
					H = W * 2;
				}
			}
		});
		this.add(widthSlider);

		this.add(new JLabel("Square Size", SwingConstants.CENTER));

		JSlider squareSizeSlider = new JSlider();
		squareSizeSlider.setMinimum(10);
		squareSizeSlider.setMaximum(30);
		squareSizeSlider.setMinorTickSpacing(1);
		squareSizeSlider.setMajorTickSpacing(10);
		squareSizeSlider.setPaintTicks(true);
		squareSizeSlider.setPaintLabels(true);
		squareSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if ((JSlider) e.getSource() == squareSizeSlider) {
					squareSize = squareSizeSlider.getValue();
				}
			}
		});
		this.add(squareSizeSlider);

		JButton startButton = new JButton("Start game");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Game();
				startButton.setEnabled(false);
			}
		});
		this.add(startButton);

	}
}
