package hu.webarticum.inno.holosky.render;

import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class MainCanvas extends GLCanvas {

    private static final long serialVersionUID = 1L;

    private final FPSAnimator animator;
    private final ViewState viewState = new ViewState();
    private final DisplayState displayState = new DisplayState();
    
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean dragging = false;

    public MainCanvas() {
        super(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        setPreferredSize(new Dimension(400, 400));
        addGLEventListener(new MainRenderer(viewState, displayState));
        setFocusable(true);
        this.animator = new FPSAnimator(this, 60);
        initGuiListeners();
    }

    public ViewState getSharedViewState() {
        return viewState;
    }
    
    private void initGuiListeners() {
        addHierarchyListener(this::onHierarchyChanged);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { onKeyPressed(e); }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { onMousePressed(e); }
            @Override public void mouseReleased(MouseEvent e) { onMouseReleased(e); }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e) { onMouseDragged(e); }
        });
        addMouseWheelListener(this::onMouseWheelMoved);
    }
    
    private void onHierarchyChanged(HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            boolean showing = isShowing();
            if (showing && !animator.isAnimating()) {
                animator.start();
            } else if (!showing && animator.isAnimating()) {
                animator.stop();
            }
        }
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: viewState.changeAzimuthDeg(v -> v - 5); break;
            case KeyEvent.VK_RIGHT: viewState.changeAzimuthDeg(v -> v + 5); break;
            case KeyEvent.VK_UP: viewState.changeElevationDeg(v -> v + 5); break;
            case KeyEvent.VK_DOWN: viewState.changeElevationDeg(v -> v - 5); break;
            case KeyEvent.VK_MINUS: viewState.changeZoom(v -> v / 1.5); break;
            case KeyEvent.VK_PLUS: case KeyEvent.VK_PERIOD: viewState.changeZoom(v -> v * 1.5); break;
            case KeyEvent.VK_SPACE: viewState.setZoom(1.0); break;
            default: // nothing to do
        }
        display();
    }
    
    private void onMousePressed(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        dragging = true;
    }

    private void onMouseReleased(MouseEvent e) {
        dragging = false;
    }

    private void onMouseDragged(MouseEvent e) {
        if (!dragging) {
            return;
        }
        
        int currentX = e.getX();
        int currentY = e.getY();
        int dx = currentX - lastMouseX;
        int dy = currentY - lastMouseY;
        lastMouseX = currentX;
        lastMouseY = currentY;
        double zoom = viewState.getZoom();
        viewState.changeAzimuthDeg(v -> v - (dx * 0.08 * (1 + (viewState.getElevationDeg() / 90)) / zoom));
        viewState.changeElevationDeg(v -> v + (dy * 0.08 / zoom));
        display();
    }

    private void onMouseWheelMoved(MouseWheelEvent e) {
        double delta = e.getWheelRotation();
        viewState.changeZoom(v -> v / Math.pow(1.1, delta));
        display();
    }

    public boolean isGalaxiesEnabled() {
        return displayState.isGalaxiesEnabled();
    }

    public void setGalaxiesEnabled(boolean galaxiesEnabled) {
        displayState.setGalaxiesEnabled(galaxiesEnabled);
        display();
    }

    public boolean isNebulasEnabled() {
        return displayState.isNebulasEnabled();
    }

    public void setNebulasEnabled(boolean nebulasEnabled) {
        displayState.setNebulasEnabled(nebulasEnabled);
        display();
    }

    public boolean isStarsEnabled() {
        return displayState.isStarsEnabled();
    }

    public void setStarsEnabled(boolean starsEnabled) {
        displayState.setStarsEnabled(starsEnabled);
        display();
    }

    public boolean isPlanetsEnabled() {
        return displayState.isPlanetsEnabled();
    }

    public void setPlanetsEnabled(boolean planetsEnabled) {
        displayState.setPlanetsEnabled(planetsEnabled);
        display();
    }

    public boolean isMoonEnabled() {
        return displayState.isMoonEnabled();
    }

    public void setMoonEnabled(boolean moonEnabled) {
        displayState.setMoonEnabled(moonEnabled);
        display();
    }

    public boolean isGroundEnabled() {
        return displayState.isGroundEnabled();
    }

    public void setGroundEnabled(boolean groundEnabled) {
        displayState.setGroundEnabled(groundEnabled);
        display();
    }
    
}
