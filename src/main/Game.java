package main;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import klassen.Configuration;

public class Game {

	public Game() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame mainFrame = new JFrame();
				final GLJPanel mainPanel = new LarryEngineKern();
				mainPanel.setPreferredSize(new Dimension(Configuration.holFensterBreite(),Configuration.holFensterHoehe()));
				final FPSAnimator mainAnimator = new FPSAnimator(mainPanel, Configuration.holFramerate());
				mainFrame.getContentPane().add(mainPanel);
				mainFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						new Thread() {
							@Override
							public void run() {
								if (mainAnimator.isStarted())
									mainAnimator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				mainFrame.setTitle(Configuration.holTitel());
				mainFrame.pack();
				mainFrame.setVisible(true);
				mainAnimator.start();
			}
		});
	}

	public static void main(String[] args) {
            Game game;
            game = new main.Game();
	}
}
