package hu.webarticum.inno.holosky.render;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class MainRenderer implements GLEventListener {

    private final ViewState viewState;
    private final DisplayState displayState;
    private final Random rand = new Random(42);
    private final List<double[]> stars = new ArrayList<>();
    
    private Texture horizonTexture;
    
    public MainRenderer(ViewState viewState, DisplayState displayState) {
        this.viewState = viewState;
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
        for (int i = 0; i < 500000; i++) {
            double theta = rand.nextDouble() * 2 * Math.PI;
            double phi = Math.asin(rand.nextDouble() * Math.PI / 2);
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            double size = Math.pow(rand.nextDouble(), 15) * 3;
            stars.add(new double[] { x, y, z, size });
        }
        
        for (int i = 0; i < 60; i++) {
            double theta = (i * 0.005) * 2 * Math.PI;
            double phi = 0.3 * Math.PI / 2;
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            stars.add(new double[] { x, y, z, 5.0 });
        }
        for (int i = 0; i < 30; i++) {
            double theta = 0;
            double phi = (i * 0.01) * Math.PI / 2;
            double x = Math.cos(theta) * Math.cos(phi);
            double y = Math.sin(theta) * Math.cos(phi);
            double z = Math.sin(phi);
            stars.add(new double[] { x, y, z, 5.0 });
        }

        horizonTexture = loadTextureFromResource("/hu/webarticum/inno/holosky/resources/horizon.png");
        horizonTexture.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        horizonTexture.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        horizonTexture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        horizonTexture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
    }
    
    private Texture loadTextureFromResource(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Texture resource not found: " + path);
            }
            String fileExtension = path.substring(path.lastIndexOf('.') + 1);
            return TextureIO.newTexture(stream, true, fileExtension);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

        double[] dir = toDirectionVector(viewState);
        GLU glu = GLU.createGLU(gl);
        glu.gluLookAt(
                0, 0, 1.7,
                dir[0], dir[1], dir[2] + 1.7,
                0, 0, 1);
        
        if (displayState.isStarsEnabled()) {
            drawStars(gl);
        }
        if (displayState.isMoonEnabled()) {
            drawMoon(gl);
        }
        if (displayState.isGroundEnabled()) {
            drawGround(gl);
        }

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();

        double fovY = 70 / viewState.getZoom();
        double aspect = 1.6; // FIXME: (float) width / height;
        double zNear = 0.1f;
        double zFar = 1000f;

        glu.gluPerspective(fovY, aspect, zNear, zFar);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }

    private double[] toDirectionVector(ViewState viewState) {
        double az = Math.toRadians(viewState.getAzimuthDeg() + 90);
        double el = Math.toRadians(viewState.getElevationDeg());
        double x = Math.cos(el) * Math.sin(az);
        double y = Math.cos(el) * Math.cos(az);
        double z = Math.sin(el);
        return new double[] {x, y, z};
    }
    
    private void drawGround(GL2 gl) {
        drawSoil(gl);
        drawHorizon(gl);
    }

    private void drawSoil(GL2 gl) {
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

    private void drawHorizon(GL2 gl) {
        float radius = 50.0f;
        float height = 25.0f;
        int segments = 64;
        horizonTexture.enable(gl);
        horizonTexture.bind(gl);
        gl.glColor3f(1f, 1f, 1f);
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            float u = (float) i / segments;
            float x = cos * radius;
            float y = sin * radius;
            gl.glTexCoord2f(u, 0.0f);
            gl.glVertex3f(x, y, 0.0f);
            gl.glTexCoord2f(u, 1.0f);
            gl.glVertex3f(x, y, height);
        }
        gl.glEnd();
        horizonTexture.disable(gl);
    }

    private void drawStars(GL2 gl) {
        drawStarsBetweenSizes(gl, 7.0, 15.0);
        drawStarsBetweenSizes(gl, 5.5, 7.0);
        drawStarsBetweenSizes(gl, 4.0, 5.5);
        drawStarsBetweenSizes(gl, 3.0, 4.0);
        drawStarsBetweenSizes(gl, 2.0, 3.0);
        drawStarsBetweenSizes(gl, 1.0, 2.0);
        drawStarsBetweenSizes(gl, 0.5, 1.0);
    }
    
    private void drawStarsBetweenSizes(GL2 gl, double low, double high) {
        float scale = 100f;
        gl.glColor3f(1f, 1f, 1f);
        gl.glPointSize((float) Math.min(4.0, Math.min(low + 0.5, (low + high) / 2f)));
        gl.glBegin(GL.GL_POINTS);
        for (double[] star : stars) {
            double apparentSize = star[3] * viewState.getZoom();
            if (apparentSize > low && apparentSize <= high) {
                gl.glVertex3f(
                        (float) (star[0] * scale),
                        (float) (star[1] * scale),
                        (float) (star[2] * scale));
            }
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
