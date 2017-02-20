package com.ba.gui;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/** Copyright: Ba
**/
public class Main {
	static boolean running = false;
	public static void main(String ... args) {
		running = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {new Main().createGui();}
		});
	}
	public transient float resolution = 1.45f;
	public transient int height = 600;
	void createGui() {
		JFrame fr = new JFrame("BA");
		fr.setSize(new Dimension((int) (height*resolution), height));
		fr.setDefaultCloseOperation(3);
		fr.setResizable(false);
		fr.setLocationRelativeTo(new JFrame());
		{
			JMenuBar mb = new JMenuBar();
			mb.add(new JMenu("BA") {});
			mb.add(new JMenu("Datei") {});
			mb.add(new JMenu("Help") {});
			fr.setJMenuBar(mb);
		}
		
		JPanel pLeft = new JPanel();
		pLeft.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		JPanel pRight = new JPanel();
		pRight.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, pLeft, pRight);
		splitPane.setDividerLocation((int) (resolution*height/2.0));
		fr.add(splitPane);
		
		
		fr.setVisible(running);
		System.out.println("Works :D: " + running);
}
	
}
