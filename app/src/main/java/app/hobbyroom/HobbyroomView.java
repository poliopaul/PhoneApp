package app.hobbyroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import app.main.MainView;
import paulus.app.R;


public class HobbyroomView extends MainView implements AdapterView.OnItemSelectedListener {
    public SeekBar sbIntensity, sbColor;
    public Spinner spModes;
    public TextView tIntensity, tColor, tAvgTemp, tAvgBrightness;
    public ImageView iLightbulbOff, iLightbulbFull, iBackButton;

    Hobbyroom hobbyroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hobbyroom_menu);

        sbIntensity = (SeekBar)findViewById(R.id.seekbarIntensity);
        sbColor = (SeekBar)findViewById(R.id.seekbarColor);
        tIntensity = (TextView)findViewById(R.id.textintensity);
        tColor = (TextView)findViewById(R.id.textcolor);
        spModes = (Spinner) findViewById(R.id.spinnerModes);
        iBackButton = (ImageView) findViewById(R.id.imageBack);
        iLightbulbOff = (ImageView) findViewById(R.id.imagelightbulbOff);
        iLightbulbFull = (ImageView) findViewById(R.id.imagelightbulbFull);
        tAvgTemp = (TextView) findViewById(R.id.temperatureText);
        tAvgBrightness = (TextView) findViewById(R.id.brightnessText);

        hobbyroom = new Hobbyroom(this);

        spModes.setOnItemSelectedListener(this);

        iLightbulbOff.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    iLightbulbOff.setScaleY((float) 0.9);
                    iLightbulbOff.setScaleX((float) 0.9);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLightbulbOff.setScaleY((float) 1.0);
                    iLightbulbOff.setScaleX((float) 1.0);
                    hobbyroom.setLightBulb("OFF");
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
                    hobbyroom.setLightBulb("FULL");
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
                        hobbyroom.setLedStripe(sbIntensity.getProgress(), sbColor.getProgress());
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
                        hobbyroom.setLedStripe(sbIntensity.getProgress(), sbColor.getProgress());
                    }
                }
        );
    }

    private void goToMainMenu() {
        hobbyroom.quit();
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected = adapterView.getSelectedItem().toString();

        if(adapterView.getId() == R.id.spinnerModes) {
            hobbyroom.setNewMode(selected);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


