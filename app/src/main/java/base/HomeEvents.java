package base;

public interface HomeEvents {

    void configInit(String cfg);

    void modeTrigger(String location, String mode);

    void pwmTrigger(String location, String deviceID, String pwm);

//    void themeTrigger();

    void brightnessUpdate(String deviceID, String brightness);

    void temperatureUpdate(String deviceID, String temperature);

}
