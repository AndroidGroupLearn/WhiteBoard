package pl.epiklp.whiteboard.activity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_server);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        final ImageView clientImage = findViewById(R.id.clientImage);
        clientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mIntent = new Intent(ClientServerActivity.this, ClientSearchActivity.class);
                startActivity(mIntent);
            }
        });

        final ImageView serverImage = findViewById(R.id.serverImage);
        serverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
