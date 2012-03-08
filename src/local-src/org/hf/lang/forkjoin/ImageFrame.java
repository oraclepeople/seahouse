package org.hf.lang.forkjoin;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

class ImageFrame extends JFrame {
	  public ImageFrame(String title, BufferedImage image) {
	    super(title);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(image.getWidth(), image.getHeight());
	    add(new ImagePanel(image));
	    setLocationByPlatform(true);
	    setVisible(true);
	  }    
	}
