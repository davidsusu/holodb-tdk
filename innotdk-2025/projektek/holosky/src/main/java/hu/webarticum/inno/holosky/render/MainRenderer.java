package hu.webarticum.inno.holosky.render;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

public class MainRenderer implements GLEventListener {

    private final ViewState view;
    private final DisplayState displayState;
    private final Random rand = new Random(42);
    private final List<double[]> stars = new ArrayList<>();
    
    private TextRenderer textRenderer;
    
    public MainRenderer(ViewState view, DisplayState displayState) {
        this.view = view;
        this.displayState = displayState;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.02f, 0.02f, 0.08f, 1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2ES1.GL_POINT_SMOOTH);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glPointSize(3.0f);
        for (int i = 0; i < 1000; i++) {
            double theta = rand.nextDouble() * 2 * Math.PI;
            double phi = Math.pow(rand.nextDouble(), 2) * Math.PI / 2;
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            stars.add(new double[] { x, y, z });
        }
        
        // XXX
        for (int i = 0; i < 60; i++) {
            double theta = (i * 0.005) * 2 * Math.PI;
            double phi = 0.3 * Math.PI / 2;
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            stars.add(new double[] { x, y, z });
        }
        for (int i = 0; i < 30; i++) {
            double theta = 0;
            double phi = (i * 0.01) * Math.PI / 2;
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            stars.add(new double[] { x, y, z });
        }
        
        textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 16));
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // currently no data
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        double[] dir = toDirectionVector(view);
        GLU glu = GLU.createGLU(gl);
        glu.gluLookAt(
                0, 0, 1.7,
                dir[0], dir[1], dir[2] + 1.7,
                0, 0, 1);
        
        if (displayState.groundEnabled) {
            drawGround(gl);
        }
        if (displayState.starsEnabled) {
            drawStars(gl);
        }
        if (displayState.moonEnabled) {
            drawMoon(gl);
        }

        textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        textRenderer.setColor(1f, 1f, 1f, 1f);
        textRenderer.draw(String.format("Viewing azimuth: %.2f°", view.azimuthDeg), 10, 50);
        textRenderer.draw(String.format("Elevation: %.2f°", view.elevationDeg), 10, 30);
        textRenderer.draw(String.format("Zoom: %.2f", view.zoom), 10, 10);
        textRenderer.endRendering();

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();

        double fovY = 70 / view.zoom;
        double aspect = 1.6; // FIXME: (float) width / height;
        double zNear = 0.1f;
        double zFar = 1000f;

        glu.gluPerspective(fovY, aspect, zNear, zFar);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }

    public double[] toDirectionVector(ViewState view) {
        double az = Math.toRadians(view.azimuthDeg);
        double el = Math.toRadians(view.elevationDeg);
        double x = Math.cos(el) * Math.sin(az);
        double y = Math.cos(el) * Math.cos(az);
        double z = Math.sin(el);
        return new double[] {x, y, z};
    }
    
    private void drawGround(GL2 gl) {
        gl.glColor3f(0.0f, 0.4f, 0.0f);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0, 0, 0);
        int segments = 64;
        float radius = 100f;
        for (int i = 0; i <= segments; i++) {
            double theta = 2 * Math.PI * i / segments;
            gl.glVertex3f(
                    (float) (radius * Math.cos(theta)),
                    (float) (radius * Math.sin(theta)),
                    0f);
        }
        gl.glEnd();
    }

    private void drawStars(GL2 gl) {
        gl.glColor3f(1f, 1f, 1f);
        gl.glBegin(GL.GL_POINTS);
        for (double[] star : stars) {
            float scale = 100f;
            gl.glVertex3f(
                    (float) (star[0] * scale),
                    (float) (star[1] * scale),
                    (float) (star[2] * scale));
        }
        gl.glEnd();
    }

    private void drawMoon(GL2 gl) {
        double[] dir = sphericalToVector(Math.toRadians(45), Math.toRadians(90));
        float dist = 100f;
        float x = (float) (dir[0] * dist);
        float y = (float) (dir[1] * dist);
        float z = (float) (dir[2] * dist);

        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glColor3f(0.9f, 0.9f, 0.8f);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0, 0, 0);
        int segments = 32;
        float r = 1.5f;
        for (int i = 0; i <= segments; i++) {
            double t = 2 * Math.PI * i / segments;
            gl.glVertex3f((float) (r * Math.cos(t)), (float) (r * Math.sin(t)), 0f);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    private double[] sphericalToVector(double alt, double az) {
        double x = Math.cos(alt) * Math.sin(az);
        double y = Math.cos(alt) * Math.cos(az);
        double z = Math.sin(alt);
        return new double[] { x, y, z };
    }
    
}
