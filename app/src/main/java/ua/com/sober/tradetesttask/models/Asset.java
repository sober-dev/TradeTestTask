package ua.com.sober.tradetesttask.models;


/**
 * Created by Dmitry on 11/29/2016.
 */

public class Asset {
    private String name;
    private boolean isEnabled;
    // direction == true if UP or false if DOWN
    private boolean direction;
    private String currentRateString;
    private float currentRate;
    private String change;

    public Asset() {
        this.currentRateString = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public String getCurrentRateString() {
        return currentRateString;
    }

    public void setCurrentRateString(String currentRateString) {
        this.currentRateString = currentRateString;
    }

    public float getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(float currentRate) {
        this.currentRate = currentRate;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }
}
