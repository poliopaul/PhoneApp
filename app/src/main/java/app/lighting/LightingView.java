package app.lighting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import app.main.MainView;
import paulus.app.R;

public class LightingView extends MainView implements AdapterView.OnItemSelectedListener {
    public static SeekBar sbIntensity, sbColor;
    public static TextView tIntensity, tColor;
    public static ImageView iLightbulbOff, iLightbulbFull, iBackButton;
    public static Spinner spLocation, spModes, spDevice;
    public static TableRow trLightbulb, trLightbulbText;
    public static TableRow trLedstripeIntensity, trLedStripeIntensityText, trLedstripeColor, trLedstripeColorText;

    Lighting lighting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lighting_menu);

        iLightbulbOff = (ImageView) findViewById(R.id.imagelightbulbOff);
        iLightbulbFull = (ImageView) findViewById(R.id.imagelightbulbFull);
        iBackButton = (ImageView) findViewById(R.id.imageBack);
        sbIntensity = (SeekBar)findViewById(R.id.seekbarIntensity);
        sbColor = (SeekBar)findViewById(R.id.seekbarColor);
        tIntensity = (TextView) findViewById(R.id.textintensity);
        tColor = (TextView) findViewById(R.id.textcolor);
        spLocation = (Spinner) findViewById(R.id.spinnerLocation);
        spModes = (Spinner) findViewById(R.id.spinnerModes);
        spDevice = (Spinner) findViewById(R.id.spinnerDevice);
        trLightbulb = (TableRow) findViewById(R.id.lightbulbTableRow1);
        trLightbulbText = (TableRow) findViewById(R.id.lightbulbTableRow2);
        trLedstripeIntensity = (TableRow) findViewById(R.id.ledstripeTableRow1);
        trLedStripeIntensityText = (TableRow) findViewById(R.id.ledstripeTableRow2);
        trLedstripeColor = (TableRow) findViewById(R.id.ledstripeTableRow3);
        trLedstripeColorText = (TableRow) findViewById(R.id.ledstripeTableRow4);

        lighting = new Lighting(this);

        spLocation.setOnItemSelectedListener(this);
        spModes.setOnItemSelectedListener(this);
        spDevice.setOnItemSelectedListener(this);

        iLightbulbOff.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iLightbulbOff.setScaleY((float) 0.9);
                    iLightbulbOff.setScaleX((float) 0.9);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLightbulbOff.setScaleY((float) 1.0);
                    iLightbulbOff.setScaleX((float) 1.0);
                    lighting.setLightBulb("OFF");
                }
                return false;
            }
        });

        iLightbulbFull.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iLightbulbFull.setScaleY((float) 0.9);
                    iLightbulbFull.setScaleX((float) 0.9);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLightbulbFull.setScaleY((float) 1.0);
                    iLightbulbFull.setScaleX((float) 1.0);
                    lighting.setLightBulb("FULL");
                }
                return false;
            }
        });

        iBackButton.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iBackButton.setScaleY((float) 0.9);
                    iBackButton.setScaleX((float) 0.9);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    iBackButton.setScaleY((float) 1.0);
                    iBackButton.setScaleX((float) 1.0);
                    goToMainMenu();
                }
                return false;
            }
        });

        sbIntensity.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tIntensity.setText("Intensity: " + progress + "%");
                    }
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        lighting.setLedStripe(sbIntensity.getProgress(), sbColor.getProgress());
                    }
                }
        );

        sbColor.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tColor.setText(String.format("Color: %dK", progress));
                    }
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        lighting.setLedStripe(sbIntensity.getProgress(), sbColor.getProgress());
                    }
                }
        );
    }

    private void goToMainMenu() {
        lighting.quit();
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getSelectedItem().toString();

        if(adapterView.getId() == R.id.spinnerLocation) {
            lighting.setNewLocation(selected);
        }
        else if(adapterView.getId() == R.id.spinnerModes) {
            lighting.setNewMode(selected);
        }
        else if(adapterView.getId() == R.id.spinnerDevice) {
            lighting.setNewDevice(selected);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
