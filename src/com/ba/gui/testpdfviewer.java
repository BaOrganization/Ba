package com.ba.gui;

import java.io.File; 
import java.io.IOException; 
import java.io.InputStream; 
import java.net.URL; 
import java.nio.file.Files; 
import java.nio.file.Paths; 
import java.nio.file.StandardCopyOption; 
import java.util.ResourceBundle; 

import javax.swing.JFrame; 
import javax.swing.JPanel; 

import org.icepdf.ri.common.SwingController; 
import org.icepdf.ri.common.SwingViewBuilder; 
import org.icepdf.ri.util.PropertiesManager; 

public class testpdfviewer { 

    public testpdfviewer() { 
        String pdfPath = loadPDF("http://www.javabeginners.de/Test.pdf"); 
        if (pdfPath == null) { 
                    System.err 
                            .println("Datei kann nicht geladen werden oder ist keine PDF-Datei."); 
                    System.exit(1); 
                } 
        SwingController controller = new SwingController(); 
        createGUI(controller); 
        controller.openDocument(pdfPath); 
    } 

    public static void main(String[] args) { 
        new testpdfviewer(); 
    } 

    public String loadPDF(String adresse) { 
        if (!adresse.toLowerCase().endsWith("pdf")) 
                    return null; 
                String fileName = adresse.substring(adresse.lastIndexOf("/") + 1, 
                        adresse.lastIndexOf(".")); 
                String suffix = adresse.substring(adresse.lastIndexOf("."), 
                        adresse.length()); 
        File temp = null; 
        try (InputStream in = new URL(adresse).openStream()) { 
            temp = File.createTempFile(fileName, suffix); 
            temp.deleteOnExit(); 
            Files.copy(in, Paths.get(temp.toURI()), 
                    StandardCopyOption.REPLACE_EXISTING); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return temp.getAbsolutePath(); 
    } 

   public static void createGUI(SwingController controller) { 
        JFrame frame = new JFrame(); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setLocationRelativeTo(null); 
        frame.setTitle("PDF anzeigen"); 
        frame.setVisible(true); 

        PropertiesManager properties = new PropertiesManager( 
                System.getProperties(), 
                ResourceBundle 
                        .getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE)); 
        properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.5"); 
        properties.set(PropertiesManager.PROPERTY_VIEWPREF_HIDETOOLBAR, "true"); 

        // nur für Event-Handling notwendig 
        // controller.setIsEmbeddedComponent(true); 
        SwingViewBuilder builder = new SwingViewBuilder(controller, properties); 
        JPanel viewerPanel = builder.buildViewerPanel(); 
        frame.getContentPane().add(viewerPanel); 
    } 
} 
