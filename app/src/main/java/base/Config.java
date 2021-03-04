package base;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import base.Locations.Device;

public class Config {

    public static int MAX_LOCATIONS             =   10;
    public static int MAX_DEVICES_PER_LOCATION  =    5;
    public static int MAX_LIGHTS_PER_LOCATION   =    5;
    public static int MAX_SENSORS_PER_LOCATION  =    5;

    public Location locations[];
    private JSONObject config;
    public int num_locations;

    public Config() {
        this.locations = new Location[MAX_LOCATIONS];
        this.num_locations = -1;
    }

    public void _initConfig(String cfg) {
        try {
            config = new JSONObject(cfg);

            for(Iterator loc = config.keys(); loc.hasNext();) {
                String location = (String) loc.next();

                if(num_locations++ >= MAX_LOCATIONS) {
                    System.out.printf("To many locations defined in class Config, max location = [%d]", MAX_LOCATIONS);
                    break;
                }

                locations[num_locations] = new Location(location);
                initLocation(num_locations, config.getString(location));
            }

        } catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    private void initLocation(int location_index, String cfg) {
        try {
            JSONObject conf = new JSONObject(cfg);

            for (Iterator param = conf.keys(); param.hasNext(); ) {
                String parameter = (String) param.next();

                if (parameter.equals("ModeString"))
                    locations[location_index].setModeString(conf.getString(parameter));
                else if (parameter.equals("TimeoutDuration"))
                    locations[location_index].setTimeoutDuration(conf.getString(parameter));
                else if (parameter.equals("Lights")) {
                    for (Iterator light = conf.getJSONObject(parameter).keys(); light.hasNext(); ) {
                        String light_type = (String) light.next();

                        if (locations[location_index].num_devices++ >= MAX_DEVICES_PER_LOCATION) {
                            System.out.printf("To many devices defined in class Location, max devices = [%d]", MAX_DEVICES_PER_LOCATION);
                            break;
                        }

                        int dev_index = locations[location_index].num_devices;
                        locations[location_index].devices[dev_index] = new Device();
                        initDeviceWithLight(location_index, dev_index, light_type, conf.getJSONObject(parameter).getString(light_type));
                    }
                }
            }
        } catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public void initDeviceWithLight(int location_index, int device_index, String light_type, String cfg) {

        locations[location_index].devices[device_index].initLight(light_type);

        try {

            JSONObject conf = new JSONObject(cfg);

            for(Iterator param = conf.keys(); param.hasNext();) {
                String parameter = (String) param.next();

                if(parameter.equals("DeviceID")) locations[location_index].devices[device_index].setDeviceID(conf.getString(parameter));
                else if(parameter.equals("DeviceName")) locations[location_index].devices[device_index].setDeviceName(conf.getString(parameter));
                else if(parameter.equals("ControlString")) locations[location_index].devices[device_index].light().setLightControlString(conf.getString(parameter));
            }

        } catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

    public void initDeviceWithSensor(String location, String deviceID) {
        int location_index = -1;
        int dev_index;

        for(int i = 0; i <= num_locations; i++) {
            if(locations[i].getLocation().equals(location)) {
                location_index = i;
                break;
            }
        }

        if(location_index < 0) {
            System.out.printf("Did not find location in initDeviceWithSensor()");
            return;
        }

        for(dev_index = 0; dev_index <= locations[location_index].num_devices + 1; dev_index++) {
            if(locations[location_index].devices[dev_index].getDeviceID().equals(deviceID)) break;
        }

        if(dev_index > locations[location_index].num_devices) {
            if (dev_index >= MAX_DEVICES_PER_LOCATION) {
                System.out.printf("To many devices defined in class Location, max devices = [%d]", MAX_DEVICES_PER_LOCATION);
                return;
            }
            locations[location_index].num_devices++;
            locations[location_index].devices[dev_index] = new Device();
        }
        locations[location_index].device(deviceID).initSensor();
    }

    public Location location(String location) {
        Location l = null;
        String name;

        for(int i = 0; i <= num_locations; i++) {
            name = locations[i].getLocation();
            if(name.equals(location)) {
                l = locations[i];
                break;
            }
        }

        if(l == null) {
            System.out.printf("Unknown location in location() selection: [%s]", location);
        }

        return l;
    }

    public int getNumLocations() {
        return num_locations;
    }

}
