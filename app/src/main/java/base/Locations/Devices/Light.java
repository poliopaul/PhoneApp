package base.Locations.Devices;

public class Light {

    private static double PCA9685_MAX_PWM   = 4095.0;
    private static double LED_MIN_INTENSITY =    0.0;
    private static double LED_MAX_INTENSITY =  100.0;
    private static double LED_MIN_COLOR     = 2700.0;
    private static double LED_MAX_COLOR     = 6500.0;

    private String light_type;
    public String controlString;
    private int intensity;
    private int color;
    private int warm_pwm;
    private int cold_pwm;

    public Light(String light_type) {
        this.light_type = light_type;
    }

    public String getLightType() { return this.light_type; }

    public int getIntensity() { return this.intensity; }

    public int getColor() { return this.color; }

    public void updatePwm(String pwm) {
        int index;

        index = pwm.indexOf(";");

        if(index < 0) setPwmLightBulb(pwm);
        else setPwmLed(pwm);
    }

    public void setLightControlString(String controlString) { this.controlString = controlString; }

    public String getLightControlString(int intensity, int color) {
        if(intensity < LED_MIN_INTENSITY) this.intensity = (int)LED_MIN_INTENSITY;
        else if(intensity > (int)LED_MAX_INTENSITY) this.intensity = (int)LED_MAX_INTENSITY;
        else this.intensity = intensity;

        if(color < LED_MIN_COLOR) this.color = (int)LED_MIN_COLOR;
        else if(color > LED_MAX_COLOR) this.color = (int)LED_MAX_COLOR;
        else this.color = color;

        calculatePwmValue();

        return String.format(controlString, warm_pwm, -1, cold_pwm, -1);
    }

    //Example: ch3:ch15,4095,-1
    private void setPwmLightBulb(String pwm) {
        String tmp[] = pwm.split(",");
        int pwm_val = Integer.parseInt(tmp[1]);

        if(pwm_val > 0) intensity = 100;
        else intensity = 0;
    }

    //Example: ch0:ch1,1228,0;ch2:ch3,0,0
    private void setPwmLed(String pwm) {
        String tmp[] = pwm.split(";");
        String warm[] = tmp[0].split(",");
        String cold[] = tmp[1].split(",");

        warm_pwm = Integer.parseInt(warm[1]);
        cold_pwm = Integer.parseInt(cold[1]);
        double tot_pwm = warm_pwm + cold_pwm + 0.5;

        intensity = (int)(tot_pwm * LED_MAX_INTENSITY / PCA9685_MAX_PWM + 0.5);
        color = (int)((cold_pwm*LED_MAX_COLOR - cold_pwm*LED_MIN_COLOR) / tot_pwm + LED_MIN_COLOR);
    }

    private void calculatePwmValue() {
        double tot_pwm = (double) intensity / LED_MAX_INTENSITY * PCA9685_MAX_PWM;
        cold_pwm = (int) (((double) color - LED_MIN_COLOR) / (LED_MAX_COLOR - LED_MIN_COLOR) * tot_pwm);
        warm_pwm = (int) tot_pwm - cold_pwm;

        if (warm_pwm > PCA9685_MAX_PWM) warm_pwm = (int) PCA9685_MAX_PWM;
        else if (warm_pwm < 0) warm_pwm = 0;

        if (cold_pwm > PCA9685_MAX_PWM) cold_pwm = (int) PCA9685_MAX_PWM;
        else if (cold_pwm < 0) cold_pwm = 0;
    }

}
