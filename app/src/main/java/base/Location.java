package base;

import base.Locations.Device;

import static base.Config.MAX_DEVICES_PER_LOCATION;

public class Location {

    public Device devices[];

    private String name;
    private String mode;
    private String modeString;
    private int timeoutDuration;

    public int num_devices;

    public Location(String name) {
        this.name = name;
        this.devices = new Device[MAX_DEVICES_PER_LOCATION];
        this.num_devices = -1;
    }

    public String getLocation() { return this.name; }

    public void setModeString(String modeString) { this.modeString = modeString; }

    public String[] getModeString() { return this.modeString.split(","); }

    public void setMode(String mode) { this.mode = mode; }

    public String getMode() { return this.mode; }

    public void setTimeoutDuration(String duration) { timeoutDuration = Integer.parseInt(duration); }

    public int getTimeoutDuration() { return timeoutDuration; }

    public double getTemperature() {
        int tmp_cnt;
        double d, temperature;

        temperature = 0.5;
        tmp_cnt = 0;
        for(int i = 0; i <= num_devices; i++) {
             d = devices[i].sensor().getTemperature();
             if(d >= 0) temperature += d; tmp_cnt++;
        }
        return temperature / (double) tmp_cnt;
    }

    public int getBrightness() {
        int tmp_cnt;
        double d, brightness;

        brightness = 0.5;
        tmp_cnt = 0;
        for(int i = 0; i <= num_devices; i++) {
            d = devices[i].sensor().getBrightness();
            if(d >= 0)  brightness += d; tmp_cnt++;
        }
        return (int) (brightness / (double) tmp_cnt);
    }

    public String[] getLightsDeviceString() {
        String tmp[] = new String[num_devices + 1];
        for(int i = 0; i <= num_devices; i++) {
            if(devices[i].light() != null) {
                tmp[i] = devices[i].getDeviceName();
            }
        }
        return tmp;
    }

    public String getLightDeviceIdFromDeviceName(String deviceName) {
        for(int i = 0; i <= num_devices; i++) {
            if(devices[i].getDeviceName().equals(deviceName)) {
                return devices[i].getDeviceID();
            }
        }
        return "";
    }

    public Device device(String deviceID) {
        Device d = null;
        String dev_name;

        for(int i = 0; i <= num_devices; i++) {
            dev_name = devices[i].getDeviceID();
            if(dev_name.equals(deviceID)) {
                d = devices[i];
                break;
            }
        }

        if(d == null) {
            System.out.printf("Unknown deviceName in device() selection: [%s]", deviceID);
        }

        return d;
    }

}
