package com.ba.gui;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

//PdfViewer
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.nio.file.StandardCopyOption; 
import java.util.ResourceBundle; 

import org.icepdf.ri.common.SwingController; 
import org.icepdf.ri.common.SwingViewBuilder; 
import org.icepdf.ri.util.PropertiesManager; 
/**JavaDocs Dokumentation zu ICEPDF: 
http://res.icesoft.org/docs/icepdf/latest/viewer/index.html
http://res.icesoft.org/docs/icepdf/latest/core/index.html
http://www.icesoft.org/wiki/display/PDF/Customizing+the+Viewer*/

/** Copyright: Andreas Götz
 */
public class Main implements ActionListener{
	/**Deklaration*/
	public Path destination, source;//Pfade der BA-Dateien (temp + original)
	public String aktuellesFile, PfadStringBS, destinationtemp;//Bedeutung: s.u.
	public File temp, pdf;
	public JFrame fr;
	public JMenuItem Oeffnen, Drucken, Speichern;
	public boolean tempcreated = false;
	static boolean running = false;
	static SwingController controller = new SwingController();//Bibliothek IcePDF: Übergibt dem PDFViewer in createGUI(controller) die Datei
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();//Bildschirmgröße in Pixel gespeichert (->Divider in der Mitte)
	public transient int width = gd.getDisplayMode().getWidth();//width für divider setzen
	public transient int widthnormal = 900;//Fensterbreite
	public transient int height = 600;//Fensterhöhe


	public static void main(String ... args) {
		running = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {new Main().createGui(controller);}
		});
	}


	/**Konstruktor*/
	void createGui(SwingController controller){//controller übergibt die PDFDatei an den Viewer
		fr = new JFrame("Betriebsanweisungen erstellen");
		fr.setExtendedState(Frame.MAXIMIZED_BOTH);
		fr.setSize(widthnormal,height);//Mindestgröße des Jframes
		fr.setResizable(true);
		fr.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//wird seperat mit Bestätigung 
		//Bei Beendigung die Originaldatei durch die temp überschreiben(s.u.) + Neues Verhalten für das Schließen des Fensters
		fr.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
		
		/**MenuBar*/
		{
			JMenuBar mb = new JMenuBar();
			JMenu Datei = new JMenu("Datei");
			JMenu Help = new JMenu("Hilfe");
			mb.add(Datei);
			mb.add(Help);
			Oeffnen = new JMenuItem("Öffnen");
			Oeffnen.addActionListener(this);
			Drucken = new JMenuItem("Drucken");
			Drucken.addActionListener(this);
			Speichern = new JMenuItem("Speichern");
			Speichern.addActionListener(this);
			Datei.add(Oeffnen);
			Datei.add(Speichern);
			Datei.add(Drucken);
			fr.setJMenuBar(mb);
		}

		JPanel pLeft = new JPanel();
		pLeft.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		JPanel pRight = new JPanel();
		pRight.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, pLeft, pRight);
		splitPane.setDividerLocation((int) (width/2));
		fr.add(splitPane);
		fr.setVisible(running);

		/**PDFViewer GUI*/
		{
			PropertiesManager properties = new PropertiesManager( 
					System.getProperties(), 
					ResourceBundle 
					.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE)); 
			//Werte noch mit defaults in der doc vergleichen. Übereinstimmung => Rauslöschen!
			properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.0"); 
			properties.set(PropertiesManager.PROPERTY_VIEWPREF_HIDETOOLBAR, "false"); 
			properties.set(PropertiesManager.PROPERTY_SHOW_KEYBOARD_SHORTCUTS, "true");
			properties.set(PropertiesManager.PROPERTY_SHOW_STATUSBAR, "true");
			properties.set(PropertiesManager.PROPERTY_VIEWPREF_FITWINDOW, "true");
			properties.set(PropertiesManager.PROPERTY_VIEWPREF_HIDEMENUBAR, "false");
			properties.set(PropertiesManager.PROPERTY_VIEWPREF_HIDEMENUBAR, "false");
			properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
			properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, "false");
			System.getProperties().put("viewer.toolbar.pageFit.actualsize.tooltip", "true");
			// nur für Event-Handling notwendig 
			// controller.setIsEmbeddedComponent(true); 
			SwingViewBuilder builder = new SwingViewBuilder(controller, properties); 
			JPanel viewerPanel = builder.buildViewerPanel(); 
			pLeft.add(viewerPanel); 
		}
	}


	/**Aktionen der Buttons (JMenuItem,...)*/
	public void actionPerformed(ActionEvent e)
	{
		//FileChooser + PDF-Datei kopiert und geöffnet (übergeben mit controller)
		if(e.getActionCommand() == Oeffnen.getActionCommand()){
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new filterpdf());//Filtert die Pdf, seperate Klasse
			int dialog = fc.showDialog(fc, "Öffnen");
			pdf = fc.getSelectedFile();//Originaldatei in pdf gespeichert
			if(dialog == 0){//<-- a: dialog muss mir jemand mal erklären :D (Sinn davon)
				aktuellesFile = fc.getCurrentDirectory().toString() + "\\" + pdf.getName();//Pfad der PDF-Datei als String gespeichert
			}
			PfadStringBS = aktuellesFile.replace("\\", "\\\\");//Pfad der PDF-Datei für Eclipse (mit doppelten Backslash) als String
			temp = pdf;//PDF-Datei als temp gespeichert (s.Files.copy...)
			//destinationtemp = "C:\\Users\\%Userprofile%\\AppData\\Local\\Temp\\" + pdf.getName();//Pfad für die manuell temporär erzeugte PDF-Datei
			destinationtemp = "Z:\\Informatik Q11 und Q12\\BAWs\\" + pdf.getName();//Pfad für die manuell temporär erzeugte PDF-Datei
			source = Paths.get(PfadStringBS);//Quellenpfad der PDF-Datei
			destination = Paths.get(destinationtemp);//Zielpfad der temporären PDF-Datei
			try {
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} 
			catch (IOException e1) {
				e1.printStackTrace();
			}
			tempcreated = true;
			controller.openDocument(destinationtemp);//Übergibt den Dateipfad der temp Datei zum Öffnen an den PDFViewer
		}
	}
	/**Gibt einen OptionPane zum Beenden an mit Option speichern der BA oder nicht*/
	public void exit(){
		int result = JOptionPane.showConfirmDialog(null,
				"Sollen die Dateien gespeichert werden?",
				"Programm beenden",
				JOptionPane.YES_NO_OPTION);
		switch(result){
		case JOptionPane.YES_OPTION:
			//Speichert die Datei bei Existenz wieder am Originalpfad
			if(tempcreated){
				try {
					Files.copy(destination, source, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else{
		        JOptionPane.showMessageDialog(null, "Es waren keine Dateien geöffnet \n"
		        		+ "OK zum Beenden drücken", "Information", JOptionPane.INFORMATION_MESSAGE); 
			}
			System.exit(0);
			temp.delete();
		case JOptionPane.NO_OPTION:
			System.exit(0);
			temp.delete();
		}
	}
}