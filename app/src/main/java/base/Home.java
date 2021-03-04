package base;

import android.content.Context;
import android.widget.Spinner;

public class Home extends Config {

    private Context context;
    private Mqtt mqtt;
    private String location;

    public Home(Context context) {
        super();
        this.context = context;
    }

    public void initialize(HomeEvents event, String location) {
        mqtt = new Mqtt(context, event);
        mqtt.initialize();
        this.location = location;
    }

    public void initConfig(String cfg) {
        _initConfig(cfg);

        if(location == "") {
            for (int i = 0; i <= num_locations; i++) {
                mqtt.subscribe("home/" + locations[i].getLocation() + "/mode/output");
                mqtt.subscribe("home/" + locations[i].getLocation() + "/+/pwm/input");
            }
        }
        else {
            mqtt.subscribe("home/" + this.location + "/mode/output");
            mqtt.subscribe("home/" + this.location + "/+/pwm/input");
            mqtt.subscribe("home/" + this.location + "/+/temperature/output");
            mqtt.subscribe("home/" + this.location + "/+/brightness/output");
        }
    }

    public void updatePwm(String location, String deviceID, String pwm) { location(location).device(deviceID).light().updatePwm(pwm); }

    public void updateMode(String location, String mode) { location(location).setMode(mode); }

    public String getMode(String location) { return location(location).getMode(); }

    public int getIntensity(String deviceID) { return location(this.location).device(deviceID).light().getIntensity(); }

    public int getColor(String deviceID) { return location(this.location).device(deviceID).light().getColor(); }

    public void setTemperature(String deviceID, String temperature) {
        if(location(this.location).device(deviceID).sensor() == null) initDeviceWithSensor(this.location, deviceID);
        location(this.location).device(deviceID).sensor().setTemperature(temperature);
    }

    public double getTemperature() { return location(this.location).getTemperature(); }

    public void setBrightness(String deviceID, String brightness) {
        if(location(this.location).device(deviceID).sensor() == null) initDeviceWithSensor(this.location, deviceID);
        location(this.location).device(deviceID).sensor().setBrightness(brightness);
    }

    public int getBrightness() { return location(this.location).getBrightness(); }

    public String[] getAvaiableLights() { return location(this.location).getLightsDeviceString(); }

    public String getLightType(String location, String deviceID) { return location(location).device(deviceID).light().getLightType(); }

    public String getLightDeviceIdFromDeviceName(String deviceName) { return location(this.location).getLightDeviceIdFromDeviceName(deviceName); }

    public String[] getLocations() {
        String tmp[] = new String[num_locations + 1];
        for(int i = 0; i <= num_locations; i++) {
            String s = locations[i].getLocation();
            tmp[i] = s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return tmp;
    }

    public String[] getAvailableModes() { return location(this.location).getModeString(); }

    public void setLight(String deviceID, int intensity, int color) {
        String tmp = location(this.location).device(deviceID).light().getLightControlString(intensity, color);
        mqtt.publish("home/" + this.location + "/" + deviceID + "/pwm/input", tmp, true);
    }

    public void setMode(String mode) {
        mqtt.publish("home/" + this.location + "/mode/input", mode, true);
    }

    public int getIndexByString(Spinner spinner, String string) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void setHomeLocation(String location) {
        this.location = location;
    }

    public String getHomeLocation() {
        return this.location;
    }

    public void quit() {
        mqtt.stopAllSubscriptions();
        mqtt.disconnect();
    }

}
