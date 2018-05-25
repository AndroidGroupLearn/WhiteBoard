package pl.epiklp.whiteboard.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import pl.epiklp.whiteboard.CanvasView;
import pl.epiklp.whiteboard.R;

public class DrawActivity extends AppCompatActivity {

    private CanvasView mCanvasView;
    private ImageButton mImageButtonMenu;
    private ConstraintLayout mLayoutMenu;
    private boolean isOpen;
    private LinearLayout toolsMenu;
    private DisplayMetrics metrics;

    private ImageButton zoomButton;
    private ImageButton drawButton;
    private ImageButton moveButton;
    private ImageButton clearButton;
    private ImageButton backButton;
    private ImageButton uploadButton;
    private Button textButton;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity);
        isOpen = false;
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mLayoutMenu = findViewById(R.id.LayoutMenu);
        mImageButtonMenu = findViewById(R.id.buttonMenu);
        mImageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen = !isOpen) {
                    mImageButtonMenu.setY(mImageButtonMenu.getY() - 96 * metrics.scaledDensity);
                    mLayoutMenu.setY(mLayoutMenu.getY() - 96 * metrics.scaledDensity);
                } else {
                    mImageButtonMenu.setY(mImageButtonMenu.getY() + 96 * metrics.scaledDensity);
                    mLayoutMenu.setY(mLayoutMenu.getY() + 96 * metrics.scaledDensity);
                }
            }
        });

        toolsMenu = findViewById(R.id.tools);


        mCanvasView = findViewById(R.id.canvasView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mCanvasView.init(displayMetrics);
        initButton();
    }

    private void initButton(){
        zoomButton = findViewById(R.id.zoomButton);
        drawButton = findViewById(R.id.drawButton);
        moveButton = findViewById(R.id.moveButton);
        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.clear();
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.back();
            }
        });
        uploadButton = findViewById(R.id.uploadButton);
        textButton = findViewById(R.id.textButton);

    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
            if (Build.VERSION.SDK_INT >= 19)
                if (hasFocus) {
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
    }

}
