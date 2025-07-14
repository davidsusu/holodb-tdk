package hu.webarticum.inno.holosky.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import hu.webarticum.inno.holosky.render.MainRenderer;
import hu.webarticum.inno.holosky.render.ViewState;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private FPSAnimator animator;

    private int lastMouseX = 0, lastMouseY = 0;
    private boolean dragging = false;

    public MainFrame() {
        super("HoloSky");
        initLayout();
        addListeners();
    }
    
    private void initLayout() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(400, 400));
        rightPanel.setBackground(Color.BLUE);
        add(rightPanel, BorderLayout.LINE_END);
        JLabel rightLabel = new JLabel("RIGHT");
        rightLabel.setOpaque(false);
        rightLabel.setForeground(Color.WHITE);
        rightPanel.add(rightLabel, BorderLayout.PAGE_START);

        
        
        
        
        
        
        
        // XXX
        
        ViewState viewState = new ViewState();
        
        GLCanvas glCanvas = createGlCanvas(viewState);
        add(glCanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glCanvas, 60);
        
        glCanvas.setFocusable(true);
        glCanvas.requestFocusInWindow();
        glCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: viewState.azimuthDeg -= 5; break;
                    case KeyEvent.VK_RIGHT: viewState.azimuthDeg += 5; break;
                    case KeyEvent.VK_UP: viewState.elevationDeg += 5; break;
                    case KeyEvent.VK_DOWN: viewState.elevationDeg -= 5; break;
                    case KeyEvent.VK_MINUS: viewState.zoom /= 1.5; break;
                    case KeyEvent.VK_PLUS: case KeyEvent.VK_PERIOD: viewState.zoom *= 1.5; break;
                    case KeyEvent.VK_SPACE: viewState.zoom = 1.0; break;
                    default: // nothing to do
                }
                viewState.normalize();
                glCanvas.repaint();
            }
        });
        
        glCanvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = true;
            }

            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                int dx = e.getX() - lastMouseX;
                int dy = e.getY() - lastMouseY;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                viewState.azimuthDeg -= dx * 0.3;
                viewState.elevationDeg += dy * 0.3;
                viewState.normalize();
                glCanvas.display();
            }
        });

        glCanvas.addMouseWheelListener(e -> {
            double delta = e.getWheelRotation();
            viewState.zoom *= Math.pow(1.1, delta);
            viewState.normalize();
            glCanvas.display();
        });

    }
    
    private GLCanvas createGlCanvas(ViewState viewState) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas glCanvas = new GLCanvas(capabilities);
        glCanvas.addGLEventListener(new MainRenderer(viewState));
        return glCanvas;
    }
    
    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowOpened(WindowEvent e) {
                if (!animator.isStarted()) {
                    animator.start();
                }
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
                if (animator.isStarted()) {
                    animator.stop();
                }
            }
            
        });
    }
    
}
