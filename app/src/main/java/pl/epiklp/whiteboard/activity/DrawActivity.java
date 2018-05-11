package pl.epiklp.whiteboard.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import pl.epiklp.whiteboard.CanvasView;
import pl.epiklp.whiteboard.R;

public class DrawActivity extends AppCompatActivity {

    private CanvasView mCanvasView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity);

        mCanvasView = findViewById(R.id.canvasView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mCanvasView.init(500,600);
    }
}
