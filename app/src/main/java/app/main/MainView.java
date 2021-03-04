package app.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import app.hobbyroom.HobbyroomView;
import app.lighting.LightingView;
import app.livingroom.LivingroomView;
import app.themes.ThemesView;
import paulus.app.R;

public class MainView extends AppCompatActivity {

    ImageView iThemes, iLighting, iHobbyroom, iLivingroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        iThemes = (ImageView) findViewById(R.id.imagethemes);
        iThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToThemesMenu();
            }
        });

        iLighting = (ImageView) findViewById(R.id.imagelighting);
        iLighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLightingMenu();
            }
        });

        iHobbyroom = (ImageView) findViewById(R.id.imagehobbyroom);
        iHobbyroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHobbyroomMenu();
            }
        });

        iLivingroom = (ImageView) findViewById(R.id.imagelivingroom);
        iLivingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLivingroomMenu();
            }
        });

    }

    private void goToThemesMenu() {
        Intent intent = new Intent(this, ThemesView.class);
        startActivity(intent);
    }

    private void goToLightingMenu() {
        Intent intent = new Intent(this, LightingView.class);
        startActivity(intent);
    }

    private void goToHobbyroomMenu() {
        Intent intent = new Intent(this, HobbyroomView.class);
        startActivity(intent);
    }

    private void goToLivingroomMenu() {
        Intent intent = new Intent(this, LivingroomView.class);
        startActivity(intent);
    }
}
