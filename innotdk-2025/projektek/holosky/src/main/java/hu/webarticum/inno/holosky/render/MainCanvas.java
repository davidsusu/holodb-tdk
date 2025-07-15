package hu.webarticum.inno.holosky.render;

import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class MainCanvas extends GLCanvas {

    private static final long serialVersionUID = 1L;

    private final ViewState view = new ViewState();
    private final DisplayState displayState = new DisplayState();
    
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean dragging = false;

    public MainCanvas() {
        super(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        FPSAnimator animator = new FPSAnimator(this, 60);
        setPreferredSize(new Dimension(400, 400));
        addGLEventListener(new MainRenderer(view, displayState));
        setFocusable(true);
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                boolean showing = isShowing();
                if (showing && !animator.isAnimating()) {
                    animator.start();
                } else if (!showing && animator.isAnimating()) {
                    animator.stop();
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: view.azimuthDeg -= 5; break;
                    case KeyEvent.VK_RIGHT: view.azimuthDeg += 5; break;
                    case KeyEvent.VK_UP: view.elevationDeg += 5; break;
                    case KeyEvent.VK_DOWN: view.elevationDeg -= 5; break;
                    case KeyEvent.VK_MINUS: view.zoom /= 1.5; break;
                    case KeyEvent.VK_PLUS: case KeyEvent.VK_PERIOD: view.zoom *= 1.5; break;
                    case KeyEvent.VK_SPACE: view.zoom = 1.0; break;
                    default: // nothing to do
                }
                view.normalize();
                repaint();
            }
            
        });
        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
            
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                int currentX = e.getX();
                int currentY = e.getY();
                int dx = currentX - lastMouseX;
                int dy = currentY - lastMouseY;
                lastMouseX = currentX;
                lastMouseY = currentY;
                view.azimuthDeg -= dx * 0.08 / view.zoom;
                view.elevationDeg += dy * 0.08 / view.zoom;
                view.normalize();
                display();
            }
            
        });
        addMouseWheelListener(e -> {
            double delta = e.getWheelRotation();
            view.zoom /= Math.pow(1.1, delta);
            view.normalize();
            display();
        });
    }

    public boolean isGalaxiesEnabled() {
        return displayState.galaxiesEnabled;
    }

    public void setGalaxiesEnabled(boolean galaxiesEnabled) {
        displayState.galaxiesEnabled = galaxiesEnabled;
        display();
    }

    public boolean isNebulasEnabled() {
        return displayState.nebulasEnabled;
    }

    public void setNebulasEnabled(boolean nebulasEnabled) {
        displayState.nebulasEnabled = nebulasEnabled;
        display();
    }

    public boolean isStarsEnabled() {
        return displayState.starsEnabled;
    }

    public void setStarsEnabled(boolean starsEnabled) {
        displayState.starsEnabled = starsEnabled;
        display();
    }

    public boolean isPlanetsEnabled() {
        return displayState.planetsEnabled;
    }

    public void setPlanetsEnabled(boolean planetsEnabled) {
        displayState.planetsEnabled = planetsEnabled;
        display();
    }

    public boolean isMoonEnabled() {
        return displayState.moonEnabled;
    }

    public void setMoonEnabled(boolean moonEnabled) {
        displayState.moonEnabled = moonEnabled;
        display();
    }

    public boolean isGroundEnabled() {
        return displayState.groundEnabled;
    }

    public void setGroundEnabled(boolean groundEnabled) {
        displayState.groundEnabled = groundEnabled;
        display();
    }
    
}
