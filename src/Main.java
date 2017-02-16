import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

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
		fr.setLocationRelativeTo(new JFrame());
		{
			JMenuBar mb = new JMenuBar();
			mb.add(new JMenu("BA") {});
			mb.add(new JMenu("Datei") {});
			mb.add(new JMenu("Help") {});
			mb.add(new JMenu("Branch") {});
			
			fr.setJMenuBar(mb);
		}
		fr.setVisible(running);
	}
}
