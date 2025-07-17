package hu.webarticum.inno.holosky.render;

public class DisplayState {

    private volatile boolean galaxiesEnabled = false;
    private volatile boolean nebulasEnabled = true;
    private volatile boolean starsEnabled = true;
    private volatile boolean planetsEnabled = true;
    private volatile boolean moonEnabled = true;
    private volatile boolean groundEnabled = true;

    public boolean isGalaxiesEnabled() {
        return galaxiesEnabled;
    }

    public void setGalaxiesEnabled(boolean galaxiesEnabled) {
        this.galaxiesEnabled = galaxiesEnabled;
    }

    public boolean isNebulasEnabled() {
        return nebulasEnabled;
    }

    public void setNebulasEnabled(boolean nebulasEnabled) {
        this.nebulasEnabled = nebulasEnabled;
    }

    public boolean isStarsEnabled() {
        return starsEnabled;
    }

    public void setStarsEnabled(boolean starsEnabled) {
        this.starsEnabled = starsEnabled;
    }

    public boolean isPlanetsEnabled() {
        return planetsEnabled;
    }

    public void setPlanetsEnabled(boolean planetsEnabled) {
        this.planetsEnabled = planetsEnabled;
    }

    public boolean isMoonEnabled() {
        return moonEnabled;
    }

    public void setMoonEnabled(boolean moonEnabled) {
        this.moonEnabled = moonEnabled;
    }

    public boolean isGroundEnabled() {
        return groundEnabled;
    }

    public void setGroundEnabled(boolean groundEnabled) {
        this.groundEnabled = groundEnabled;
    }

}
