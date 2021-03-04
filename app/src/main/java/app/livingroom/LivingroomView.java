package app.livingroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import app.main.MainView;
import paulus.app.R;

public class LivingroomView extends MainView {


    Livingroom livingroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_livingroom_menu);

    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainView.class);
        startActivity(intent);
    }
}
