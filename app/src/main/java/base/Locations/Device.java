package base.Locations;

import base.Locations.Devices.Light;
import base.Locations.Devices.Sensor;

public class Device {

    private Light light;
    private Sensor sensor;
    private String deviceID;
    private String deviceName;

    public int num_lights;
    public int num_sensors;

    public Device() {
        this.num_lights = -1;
        this.num_sensors = -1;
    }

    public void initLight(String light_type) { this.light = new Light(light_type); }

    public void initSensor() { this.sensor = new Sensor(); }

    public Light light() { return this.light; }

    public Sensor sensor() { return this.sensor; }

    public void setDeviceID(String deviceID) { this.deviceID =  deviceID;}

    public String getDeviceID() { return this.deviceID; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceName() { return this.deviceName; }

}
