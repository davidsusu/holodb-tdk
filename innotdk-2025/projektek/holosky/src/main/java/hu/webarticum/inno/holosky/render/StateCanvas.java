package hu.webarticum.inno.holosky.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.HierarchyEvent;

import javax.swing.JPanel;

import hu.webarticum.inno.holosky.render.ViewState.ViewStateListener;

public class StateCanvas extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ViewState viewState;
    private final ViewStateListener viewStateListener;
    
    private boolean showingState = false;
    
    public StateCanvas(ViewState viewState) {
        setPreferredSize(new Dimension(400, 400));
        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        this.viewState = viewState;
        this.viewStateListener = v -> repaint();
        addHierarchyListener(this::onHierarchyChanged);
    }

    private void onHierarchyChanged(HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == 0) {
            return;
        }
        
        boolean showing = isShowing();
        if (showing == showingState) {
            return;
        }
        
        if (showing) {
            viewState.addListener(viewStateListener);
        } else {
            viewState.removeListener(viewStateListener);
        }
        showingState = showing;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString(String.format("Azimuth: %.2f", viewState.getAzimuthDeg()), 10, 25);
        g2.drawString(String.format("Elevation: %.2f", viewState.getElevationDeg()), 10, 50);
        g2.drawString(String.format("Zoom: %.2f", viewState.getZoom()), 10, 75);
    }
    
}
