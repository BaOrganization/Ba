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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

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

/** Copyright: Ba
 */
public class Main implements ActionListener{
	/**Deklaration*/
	public Path destination, source;//Pfade der PDF-Dateien (temp + original)
	public String aktuellesFile;//Pfad als String, aktuellesFile z.B. zum Anzeigen
	public JFrame fr;
	public JMenuItem Oeffnen, Drucken, Speichern;
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
		fr.setDefaultCloseOperation(3);

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
		splitPane.setDividerLocation((int) (width/2.0));
		fr.add(splitPane);
		fr.setVisible(running);

		/**PDFViewer*/
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
			File pdf = fc.getSelectedFile();//Originaldatei in pdf gespeichert
			if(dialog == 0){//a: dialog muss mir jemand mal erklären :D (Sinn davon)
				aktuellesFile = fc.getCurrentDirectory().toString() + "\\" + pdf.getName();//Pfad der PDF-Datei als String gespeichert
			}
			String PfadStringBS = aktuellesFile.replace("\\", "\\\\");//Pfad der PDF-Datei für Eclipse (mit doppelten Backslash) als String
			File temp = pdf;//PDF-Datei als temp gespeichert (s.Files.copy...)
			String destinationtemp = "C:\\Users\\%Userprofile%\\AppData\\Local\\Temp\\" + pdf.getName();//Pfad für die manuell temporär erzeugte PDF-Datei
			source = Paths.get(PfadStringBS);//Quellenpfad der PDF-Datei
			destination = Paths.get(destinationtemp);//Zielpfad der temporären PDF-Datei
			try {
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} 
			catch (IOException e1) {
				e1.printStackTrace();
			}
			//Versuch, bei Beendigung die Originaldatei durch die temp zu überschreiben. Klappt nicht!
			fr.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					try {
						Files.copy(destination, source, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.getWindow().dispose();
				}
			});
			temp.deleteOnExit();
			if (aktuellesFile == null) {//gibt er auch nicht aus. Fehler!
				System.err.println("Die Datei ist beschädigt oder hat nicht die Endung .pdf!");
			} 
			controller.openDocument(destinationtemp);//Übergibt den Dateipfad der temp Datei zum Öffnen an den PDFViewer 
		}
	}
}