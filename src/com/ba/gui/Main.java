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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.nio.file.StandardCopyOption; 
import java.util.ResourceBundle; 

import org.icepdf.ri.common.SwingController; 
import org.icepdf.ri.common.SwingViewBuilder; 
import org.icepdf.ri.util.PropertiesManager; 

/** Copyright: Ba
 */
public class Main implements ActionListener{
	public Path destination, source;
	public URI pfad;
	public String aktuellesFile, PfadStringBS;
	public JFrame fr;
	public JMenuItem Oeffnen;
	public JMenuItem Drucken;
	static boolean running = false;
	static SwingController controller = new SwingController();

	public static void main(String ... args) {
		running = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {new Main().createGui(controller);}
		});
	}
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public transient int width = gd.getDisplayMode().getWidth();
	public transient int widthnormal = 900;
	public transient int height = 600;
	void createGui(SwingController controller){
		aktuellesFile = new String();
		fr = new JFrame("Betriebsanweisungen erstellen");
		fr.setExtendedState(Frame.MAXIMIZED_BOTH);
		fr.setSize(widthnormal,height); //Mindestgröße des Jframes
		fr.setResizable(true);
		fr.setDefaultCloseOperation(3);
		fr.setLocationRelativeTo(new JFrame());

		{
			JMenuBar mb = new JMenuBar();
			JMenu BA = new JMenu("BA");
			JMenu Datei = new JMenu("Datei");
			JMenu Help = new JMenu("Hilfe");
			mb.add(BA);
			mb.add(Datei);
			mb.add(Help);
			Oeffnen = new JMenuItem("Öffnen");
			Oeffnen.addActionListener(this);
			Drucken = new JMenuItem("Drucken");
			Drucken.addActionListener(this);
			JMenuItem Speichern = new JMenuItem("Speichern");
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

		//PDFViewer
		PropertiesManager properties = new PropertiesManager( 
				System.getProperties(), 
				ResourceBundle 
				.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE)); 
		properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.0"); 
		properties.set(PropertiesManager.PROPERTY_VIEWPREF_HIDETOOLBAR, "false"); 

		// nur für Event-Handling notwendig 
		// controller.setIsEmbeddedComponent(true); 
		SwingViewBuilder builder = new SwingViewBuilder(controller, properties); 
		JPanel viewerPanel = builder.buildViewerPanel(); 
		pLeft.add(viewerPanel); 
	}
	public void actionPerformed(ActionEvent e)
	{
		//FileChooser + PDF-Datei kopiert und geöffnet (übergeben mit controller)
		if(e.getActionCommand() == Oeffnen.getActionCommand()){
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new filterpdf());
			int dialog = fc.showDialog(fc, "Öffnen");
			File pdf = fc.getSelectedFile();//Datei in temp gespeichert
			if(dialog == 0){
				aktuellesFile = fc.getCurrentDirectory().toString() + "\\" + pdf.getName();//Pfad der PDF als String gespeichert
			}
			PfadStringBS = aktuellesFile.replace("\\", "\\\\");//Pfad der PDF für eclipse (mit doppelten Backslash)
			File temp = pdf;//PDF Datei als temp gespeichert (s.Files.copy...)
			String destinationtemp = "C:\\Users\\%Userprofile%\\AppData\\Local\\Temp\\" + pdf.getName();//Pfad für die temporär erzeugte Datei
			source = Paths.get(PfadStringBS);//Quellenpfad der PDF
			destination = Paths.get(destinationtemp);//Zielpfad der temporären PDF
			try {
				Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
			if (aktuellesFile == null) {
				System.err.println("Die Datei ist beschädigt oder hat nicht die Endung .pdf!");
			} 
			controller.openDocument(destinationtemp);
		}
	}
}
