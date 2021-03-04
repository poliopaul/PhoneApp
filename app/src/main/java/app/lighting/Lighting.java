package app.lighting;

import android.view.View;
import android.widget.ArrayAdapter;

import base.Home;
import base.HomeEvents;
import paulus.app.R;

public class Lighting extends Home implements HomeEvents {

    private LightingView context;

    private String selectedLocation;
    private String selectedMode;
    private String selectedDevice;

    public Lighting(LightingView lighting) {
        super(lighting);
        this.context = lighting;
        this.selectedMode = "";
        this.selectedDevice = "";
        initialize(this, "");
    }

    @Override
    public void configInit(String cfg) {
        initConfig(cfg);
        setLocationSpinnerView();
        setModeSpinnerView();
        setDeviceSpinnerView();

        context.trLedstripeIntensity.setVisibility(View.VISIBLE);
        context.trLightbulb.setVisibility(View.GONE);
    }

    @Override
    public void modeTrigger(String location, String mode) {
        updateMode(location, mode);
        if (selectedLocation.equals(location)) {
            selectedMode = mode;
            context.spModes.setSelection(getIndexByString(context.spModes, mode), true);
        }
    }

    @Override
    public void pwmTrigger(String location, String deviceID, String pwm) {
        updatePwm(location, deviceID, pwm);

        if(deviceID.equals(selectedDevice)) {
            if (getLightType(location, deviceID).equals("Lightbulb")) {
                    if(selectedDevice.equals("hallway")) {
                        setLightBulbView((getIntensity(deviceID) > 0) ? "OFF" : "FULL");
                    }
                    else {
                        setLightBulbView((getIntensity(deviceID) > 0) ? "FULL" : "OFF");
                    }
            } else {
                setLedStripeView(getIntensity(deviceID), getColor(deviceID));
            }
        }
    }

    @Override
    public void temperatureUpdate(String deviceID, String temperature) {
        setTemperature(deviceID, temperature);
    }

    @Override
    public void brightnessUpdate(String deviceID, String brightness) {
        setBrightness(deviceID, brightness);
    }

    public void setLightBulb(String value) {
        int i = (value.equals("FULL")) ? 100 : 0;
        setLight(selectedDevice, i, 0);
    }

    public void setLedStripe(int intensity, int color) {
        setLight(selectedDevice, intensity, color);
    }

    public void setNewLocation(String location) {
        String loc = location.toLowerCase();
        if(!selectedLocation.equals(loc)) {
            selectedLocation = loc;
            setHomeLocation(selectedLocation);
            setModeSpinnerView();
            selectedMode = getMode(selectedLocation);
            context.spModes.setSelection(getIndexByString(context.spModes, selectedMode));
            setDeviceSpinnerView();
        }
    }

    public void setNewMode(String mode) {
        if(!selectedMode.equals(mode)) {
            selectedMode = mode;
            setMode(mode);
        }
    }

    public void setNewDevice(String device) {
        String dev = getLightDeviceIdFromDeviceName(device);
        if(!selectedDevice.equals(dev)) {
            selectedDevice = dev;

            if(location(getHomeLocation()).device(selectedDevice).light().getLightType().equals("Lightbulb")) {
                setLightBulbView((getIntensity(selectedDevice) > 0) ? "FULL" : "OFF");
            }
            else {
                setLedStripeView(getIntensity(selectedDevice), getColor(selectedDevice));
            }
        }
    }

    public void setLocationSpinnerView() {
        String[] items = getLocations();
        selectedLocation = "livingroom"; // Default, show livingroom
        setHomeLocation(selectedLocation);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        context.spLocation.setAdapter(adapter);
        context.spLocation.setSelection(getIndexByString(context.spLocation, "Livingroom"));
    }

    public void setModeSpinnerView() {
        String[] items = getAvailableModes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        context.spModes.setAdapter(adapter);
    }

    public void setDeviceSpinnerView() {
        String[] items = getAvaiableLights();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        context.spDevice.setAdapter(adapter);
        setNewDevice(items[0]);
    }

    public void setLightBulbView(String value) {
        if(context.trLedstripeIntensity.getVisibility() == View.VISIBLE) {
            context.trLedstripeIntensity.setVisibility(View.GONE);
            context.trLedstripeColor.setVisibility(View.GONE);
            context.trLedStripeIntensityText.setVisibility(View.GONE);
            context.trLedstripeColorText.setVisibility(View.GONE);
            context.trLightbulb.setVisibility(View.VISIBLE);
            context.trLightbulbText.setVisibility(View.VISIBLE);
        }

        if (value.equals("FULL")) {
            context.iLightbulbFull.setBackgroundResource(R.drawable.border_style);
            context.iLightbulbOff.setBackgroundResource(android.R.color.transparent);
        }
        else  {
            context.iLightbulbOff.setBackgroundResource(R.drawable.border_style);
            context.iLightbulbFull.setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setLedStripeView(int intensity, int color) {
        if(context.trLightbulb.getVisibility() == View.VISIBLE) {
            context.trLightbulb.setVisibility(View.GONE);
            context.trLightbulbText.setVisibility(View.GONE);
            context.trLedstripeIntensity.setVisibility(View.VISIBLE);
            context.trLedstripeColor.setVisibility(View.VISIBLE);
            context.trLedStripeIntensityText.setVisibility(View.VISIBLE);
            context.trLedstripeColorText.setVisibility(View.VISIBLE);
        }
        setSeekbarIntensityView(intensity);
        setSeekbarColorView(color);
    }

    public void setSeekbarIntensityView(int intensity) {
        context.tIntensity.setText(String.format("Intensity: %d%%", intensity));
        context.sbIntensity.setProgress(intensity, true);
    }

    public void setSeekbarColorView(int color) {
        context.tColor.setText(String.format("Color: %dK", color));
        context.sbColor.setProgress(color, true);
    }

}
