package com.ba.gui;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/** Copyright: Ba
 */
public class Main implements ActionListener{
	public File temp;
	public JLabel aktuellesFile;
	public JMenuItem Oeffnen;
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
	void createGui(){
		aktuellesFile = new JLabel("Keine Datei ausgewählt");
		aktuellesFile.setBounds(50,220,300,30);
		JFrame fr = new JFrame("Betriebsanweisungen erstellen");
		fr.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		fr.setResizable(true);//a.comment 1:lieber true, wir müssten aber anpassen, wenn man es kleiner zieht
		fr.setDefaultCloseOperation(3);
		fr.setLocationRelativeTo(new JFrame());

		{
			JMenuBar mb = new JMenuBar();
			JMenu BA = new JMenu("BA");
			JMenu Datei = new JMenu("Datei");
			JMenu Help = new JMenu("Help");
			mb.add(BA);
			mb.add(Datei);
			mb.add(Help);
			Oeffnen = new JMenuItem("Öffnen");
			Oeffnen.addActionListener(this);
			JMenuItem Speichern = new JMenuItem("Speichern");
			Datei.add(Oeffnen);
			Datei.add(Speichern);

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

	}
	public void actionPerformed(ActionEvent e)
	{
		//FileChooser
		if(e.getActionCommand() == Oeffnen.getActionCommand()){
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new filterpdf());
			int dialog = fc.showDialog(fc, "Öffnen");
			if(dialog == 0){
				temp = fc.getSelectedFile();//a.2: Datei in temp gespeichert. Pdf-Opener?
				aktuellesFile.setText(fc.getCurrentDirectory().toString() + "/" + temp.getName());
				//Pfad der pdf im Label aktuellesFile gespeichert
			}
		}
	}
}
