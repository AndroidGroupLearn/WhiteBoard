package pl.epiklp.whiteboard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import pl.epiklp.whiteboard.R;

/**
 * Created by epiklp on 08.05.18.
 */

public class ClientServerActivity extends AppCompatActivity {

    private ImageView clientImage, serverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_server_activity);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        clientImage = findViewById(R.id.clientImage);
        clientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "client", Toast.LENGTH_SHORT).show();
            }
        });

        serverImage = findViewById(R.id.serverImage);
        serverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "server", Toast.LENGTH_SHORT).show();
            }
        });

    }
}