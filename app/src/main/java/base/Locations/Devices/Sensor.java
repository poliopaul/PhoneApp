package base.Locations.Devices;

public class Sensor {

    private double temperature;
    private int brightness;

    public Sensor() {
        this.temperature = -1;
        this.brightness = -1;
    }

    public void setTemperature(String temperature) { this.temperature = Double.parseDouble(temperature); }

    public double getTemperature() { return temperature; }

    public void setBrightness(String brightness) { this.brightness = Integer.parseInt(brightness); }

    public int getBrightness() { return this.brightness; }

}
