package pl.epiklp.whiteboard.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import pl.epiklp.whiteboard.R;

public class ClientModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_mode);
        startDiscovery();
    }
    private EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            Toast.makeText(ClientModeActivity.this, discoveredEndpointInfo.getServiceId(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            Toast.makeText(ClientModeActivity.this, "A previously EndPoint was lost.", Toast.LENGTH_SHORT).show();
        }
    };

    private void startDiscovery(){
        Nearby.getConnectionsClient(this).startDiscovery(ServerModeActivity.SERVICE_ID, endpointDiscoveryCallback,
                new DiscoveryOptions(Strategy.P2P_STAR))
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClientModeActivity.this, "Start discoving!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClientModeActivity.this, "Unable to start dicovering!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
