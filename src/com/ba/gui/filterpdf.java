package com.ba.gui;

import java.io.File;

/** Copyright: Ba
 */

import javax.swing.filechooser.*; 
public class filterpdf extends FileFilter{
	public boolean accept(File f) {
		String Endung = KriegDateiEndung(f);
		if (Endung != null) {
			if (Endung.equals("pdf")){
				return true;
			} else {
				return false;
			}
		}else{

			return false;
		}
	}
	public String KriegDateiEndung(File f){
		String Endung = null;
		String DateiName = f.getName();
		int i = DateiName.lastIndexOf('.');

		if (i > 0 &&  i < DateiName.length() - 1) {
			Endung = DateiName.substring(i+1).toLowerCase();
		}
		return Endung;
	}
	public String getDescription() {
		return ".pdf-Dateien";
	}
}
