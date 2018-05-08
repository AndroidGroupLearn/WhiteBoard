package pl.epiklp.whiteboard.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import pl.epiklp.whiteboard.R;

public class LogoActivity extends AppCompatActivity {

    private ImageView logoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        logoMain = findViewById(R.id.logoMain);
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.anim);
        logoMain.startAnimation(logoAnimation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //startActivity(mIntent);
                    //finish();
                }
            }
        }).start();

    }
}
