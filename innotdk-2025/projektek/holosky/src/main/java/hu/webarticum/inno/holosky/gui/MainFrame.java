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

        rightUpperPanel.add(createCheckBox("Galaxies", mainCanvas.isGalaxiesEnabled(), s -> mainCanvas.setGalaxiesEnabled(s)));
        rightUpperPanel.add(createCheckBox("Nebulas", mainCanvas.isNebulasEnabled(), s -> mainCanvas.setNebulasEnabled(s)));
        rightUpperPanel.add(createCheckBox("Stars", mainCanvas.isStarsEnabled(), s -> mainCanvas.setStarsEnabled(s)));
        rightUpperPanel.add(createCheckBox("Planets", mainCanvas.isPlanetsEnabled(), s -> mainCanvas.setPlanetsEnabled(s)));
        rightUpperPanel.add(createCheckBox("Moon", mainCanvas.isMoonEnabled(), s -> mainCanvas.setMoonEnabled(s)));
        rightUpperPanel.add(createCheckBox("Ground", mainCanvas.isGroundEnabled(), s -> mainCanvas.setGroundEnabled(s)));

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setPreferredSize(new Dimension(400, 400));
        rightBottomPanel.setBackground(Color.DARK_GRAY);
        rightPanel.add(rightBottomPanel, BorderLayout.PAGE_END);
        
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
