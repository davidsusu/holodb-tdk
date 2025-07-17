package hu.webarticum.inno.holosky.render;

import java.util.EventListener;
import java.util.function.DoubleUnaryOperator;

import javax.swing.event.EventListenerList;

public class ViewState {
    
    private final EventListenerList listeners = new EventListenerList();
    
    private volatile double azimuthDeg = 0;
    private volatile double elevationDeg = 0;
    private volatile double zoom = 1.0;

    public void addListener(ViewStateListener listener) {
        listeners.add(ViewStateListener.class, listener);
    }

    public void removeListener(ViewStateListener listener) {
        listeners.remove(ViewStateListener.class, listener);
    }

    private void triggerListeners() {
        for (ViewStateListener listener : listeners.getListeners(ViewStateListener.class)) {
            listener.onViewStateChanged(this);
        }
    }

    public double getAzimuthDeg() {
        return azimuthDeg;
    }

    public synchronized void setAzimuthDeg(double azimuthDeg) {
        this.azimuthDeg = (azimuthDeg % 360 + 360) % 360;
        triggerListeners();
    }

    public synchronized void changeAzimuthDeg(DoubleUnaryOperator mapper) {
        this.setAzimuthDeg(mapper.applyAsDouble(this.azimuthDeg));
    }

    public double getElevationDeg() {
        return elevationDeg;
    }

    public synchronized void setElevationDeg(double elevationDeg) {
        this.elevationDeg = Math.max(0, Math.min(90, elevationDeg));
        triggerListeners();
    }

    public synchronized void changeElevationDeg(DoubleUnaryOperator mapper) {
        this.setElevationDeg(mapper.applyAsDouble(this.elevationDeg));
    }

    public double getZoom() {
        return zoom;
    }

    public synchronized void setZoom(double zoom) {
        this.zoom = Math.max(1.0, zoom);
        triggerListeners();
    }

    public synchronized void changeZoom(DoubleUnaryOperator mapper) {
        this.setZoom(mapper.applyAsDouble(this.zoom));
    }
    
    @FunctionalInterface
    public interface ViewStateListener extends EventListener {
        
        public void onViewStateChanged(ViewState viewState);
        
    }
    
}
