package app.hobbyroom;

import android.widget.ArrayAdapter;

import base.Home;
import base.HomeEvents;
import paulus.app.R;

public class Hobbyroom extends Home implements HomeEvents {

    private HobbyroomView context;
    private String selectedMode;

    public Hobbyroom(HobbyroomView hobbyroom) {
        super(hobbyroom);
        this.context = hobbyroom;
        this.selectedMode = "";
        initialize(this, "hobbyroom");
    }

    @Override
    public void configInit(String cfg) {
        initConfig(cfg);
        setModeSpinnerView();
    }

    @Override
    public void modeTrigger(String location, String mode) {
        selectedMode = mode;
        context.spModes.setSelection(getIndexByString(context.spModes, mode), true);
    }

    @Override
    public void pwmTrigger(String location, String deviceID, String pwm) {
        updatePwm(location, deviceID, pwm);
        if(deviceID.equals("wallswitch")) setLightBulbView(getIntensity(deviceID) == 100 ? "FULL" : "OFF");
        else if(deviceID.equals("pi")) setLedStripeView(getIntensity(deviceID), getColor(deviceID));
    }

    @Override
    public void temperatureUpdate(String deviceID, String temperature) {
        setTemperature(deviceID, temperature);
        context.tAvgTemp.setText(String.format("%.2f°C", getTemperature()));
    }

    @Override
    public void brightnessUpdate(String deviceID, String brightness) {
        setBrightness(deviceID, brightness);
        context.tAvgBrightness.setText(String.format("%d%%", getBrightness()));
    }

    public void setLightBulb(String value) {
        setLight("wallswitch", value.equals("FULL") ? 100 : 0, 0);
    }

    public void setLedStripe(int intensity, int color) {
        setLight("pi", intensity, color);
    }

    public void setNewMode(String mode) {
        if(!selectedMode.equals(mode)) {
            selectedMode = mode;
            setMode(mode);
        }
    }

    public void setLightBulbView(String value) {
        if(value.equals("FULL")) {
            context.iLightbulbFull.setBackgroundResource(R.drawable.border_style);
            context.iLightbulbOff.setBackgroundResource(android.R.color.transparent);
        }
        else {
            context.iLightbulbOff.setBackgroundResource(R.drawable.border_style);
            context.iLightbulbFull.setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setLedStripeView(int intensity, int color) {
        context.tIntensity.setText(String.format("Intensity: %d%%", intensity));
        context.sbIntensity.setProgress(intensity, true);

        context.tColor.setText(String.format("Color: %dK", color));
        context.sbColor.setProgress(color, true);
    }

    public void setModeSpinnerView() {
        String[] items = getAvailableModes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        context.spModes.setAdapter(adapter);
    }

}
