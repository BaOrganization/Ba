package com.ba.gui;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

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
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public transient int width = gd.getDisplayMode().getWidth();
	public transient float resolution = 1.45f;
	public transient int height = 600;
	void createGui() {
		JFrame fr = new JFrame("Betriebsanweisungen erstellen");
		fr.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		fr.setResizable(true);//a.comment 1:lieber true, wir müssten aber anpassen, wenn man es kleiner zieht
		fr.setDefaultCloseOperation(3);
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
		splitPane.setDividerLocation((int) (width/2.0));
		fr.add(splitPane);
		
		
		fr.setVisible(running);
		System.out.println("Works :D: " + running);
}
	
}
