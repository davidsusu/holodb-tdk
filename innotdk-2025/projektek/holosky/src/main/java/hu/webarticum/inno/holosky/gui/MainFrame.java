package hu.webarticum.inno.holosky.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hu.webarticum.inno.holosky.render.MainCanvas;
import hu.webarticum.inno.holosky.render.StateCanvas;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    public MainFrame() {
        super("HoloSky");
        initLayout();
    }
    
    private void initLayout() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        MainCanvas mainCanvas = new MainCanvas();
        add(mainCanvas, BorderLayout.CENTER);
        mainCanvas.requestFocusInWindow();

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLUE);
        add(rightPanel, BorderLayout.LINE_END);
        
        JPanel rightUpperPanel = new JPanel();
        rightUpperPanel.setLayout(new BoxLayout(rightUpperPanel, BoxLayout.PAGE_AXIS));
        rightUpperPanel.setPreferredSize(new Dimension(400, 400));
        rightUpperPanel.setOpaque(false);
        rightPanel.add(rightUpperPanel, BorderLayout.PAGE_START);
        
        JLabel rightLabel = new JLabel("RIGHT");
        rightLabel.setOpaque(false);
        rightLabel.setForeground(Color.WHITE);
        rightUpperPanel.add(rightLabel);

        rightUpperPanel.add(createCheckBox("Galaxies", mainCanvas.isGalaxiesEnabled(), mainCanvas::setGalaxiesEnabled));
        rightUpperPanel.add(createCheckBox("Nebulas", mainCanvas.isNebulasEnabled(), mainCanvas::setNebulasEnabled));
        rightUpperPanel.add(createCheckBox("Stars", mainCanvas.isStarsEnabled(), mainCanvas::setStarsEnabled));
        rightUpperPanel.add(createCheckBox("Planets", mainCanvas.isPlanetsEnabled(), mainCanvas::setPlanetsEnabled));
        rightUpperPanel.add(createCheckBox("Moon", mainCanvas.isMoonEnabled(), mainCanvas::setMoonEnabled));
        rightUpperPanel.add(createCheckBox("Ground", mainCanvas.isGroundEnabled(), mainCanvas::setGroundEnabled));

        JPanel rightBottomPanel = new JPanel(new BorderLayout());
        rightBottomPanel.setPreferredSize(new Dimension(400, 400));
        rightPanel.add(rightBottomPanel, BorderLayout.PAGE_END);
        
        StateCanvas stateCanvas = new StateCanvas(mainCanvas.getSharedViewState());
        rightBottomPanel.add(stateCanvas, BorderLayout.CENTER);
        
        pack();
    }
    
    private JCheckBox createCheckBox(String text, boolean selected, Consumer<Boolean> changeCallback) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setOpaque(false);
        checkBox.setForeground(Color.WHITE);
        checkBox.setSelected(selected);
        checkBox.addChangeListener(e -> changeCallback.accept(checkBox.isSelected()));
        return checkBox;
    }
}
