package com.ba.gui;

import java.io.File;

/** Copyright: Ba
 */

import javax.swing.filechooser.*; 
public class filterpdf extends FileFilter{
	public boolean accept(File f) {
		String Endung1 = KriegDateiEndungo(f);
		if(Endung1 == null){
			return true;
		}
		String Endung2 = KriegDateiEndungm(f);
			if (Endung2.equals("pdf")){
				return true;
			} else{ return false;
			}
		}
	public String KriegDateiEndungo(File f){
		String Endung = "error";
		String DateiName = f.getName();
		if(DateiName.contains(".")){
			Endung = "error";
		}
			else{ Endung = null;
			
			}
		
		return Endung;
	}
	public String KriegDateiEndungm(File f){
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
