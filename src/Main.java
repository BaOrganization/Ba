import java.awt.Dimension;

import javax.swing.JFrame;
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
		fr.setVisible(running);
	}
}
