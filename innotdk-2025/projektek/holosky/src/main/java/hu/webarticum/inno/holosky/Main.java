package hu.webarticum.inno.holosky;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import hu.webarticum.inno.holosky.gui.MainFrame;

public class Main {
    
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = graphicsEnvironment.getDefaultScreenDevice();
        screenDevice.setFullScreenWindow(mainFrame);
    }

}
