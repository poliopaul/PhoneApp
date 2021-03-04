package app.themes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import app.main.MainView;
import paulus.app.R;

public class ThemesView extends MainView {

    private static ImageView backButton;

    Themes themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_themes_menu);

        backButton = (ImageView) findViewById(R.id.imageBack);


        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    backButton.setScaleY((float) 0.9);
                    backButton.setScaleX((float) 0.9);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    backButton.setScaleY((float) 1.0);
                    backButton.setScaleX((float) 1.0);
                    goToMainMenu();
                }
                return false;
            }
        });
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }
}