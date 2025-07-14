package hu.webarticum.inno.holosky.render;

public class ViewState {
    
    public double azimuthDeg = 0;
    public double elevationDeg = 0;
    public double zoom = 1.0;

    public void normalize() {
        azimuthDeg = (azimuthDeg % 360 + 360) % 360;
        elevationDeg = Math.max(0, Math.min(90, elevationDeg));
        zoom = Math.max(1.0, zoom);
    }

    public double[] directionVector() {
        double az = Math.toRadians(azimuthDeg);
        double el = Math.toRadians(elevationDeg);
        double x = Math.cos(el) * Math.sin(az);
        double y = Math.cos(el) * Math.cos(az);
        double z = Math.sin(el);
        return new double[] {x, y, z};
    }
    
}