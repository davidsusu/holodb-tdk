package hu.webarticum.inno.holosky.render;

public class ViewState {
    
    public volatile double azimuthDeg = 0;
    public volatile double elevationDeg = 0;
    public volatile double zoom = 1.0;

    public void normalize() {
        azimuthDeg = (azimuthDeg % 360 + 360) % 360;
        elevationDeg = Math.max(0, Math.min(90, elevationDeg));
        zoom = Math.max(1.0, zoom);
    }

}