package pl.epiklp.whiteboard.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import pl.epiklp.whiteboard.R;

public class ServerModeActivity extends AppCompatActivity {

    private static final String TAG = ServerModeActivity.class.getSimpleName();
    private static String userNickname = "test";
    public final static String SERVICE_ID = "pl.epiklp.whiteboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_mode);
        startAdvertising();
    }

    private void startAdvertising() {
        Nearby.getConnectionsClient(this).startAdvertising(
                userNickname,
                SERVICE_ID,
                mConnectionLifecycleCallback,
                new AdvertisingOptions(Strategy.P2P_STAR))
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                Toast.makeText(ServerModeActivity.this, "Start advertising!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Advertising");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Unable to start advertising!");
                            }
                        });
    }

    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            Toast.makeText(ServerModeActivity.this, "OK", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            Toast.makeText(ServerModeActivity.this, "Wysyłam", Toast.LENGTH_SHORT).show();
        }
    };

    private ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            //Show alertBox with confirm connection with Client
            final String id = s;
            new AlertDialog.Builder(ServerModeActivity.this)
                    .setTitle("Accept connection with " + connectionInfo.getEndpointName())
                    .setMessage("Confirm the code " + connectionInfo.getAuthenticationToken())
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User confirm connection
                            Nearby.getConnectionsClient(ServerModeActivity.this).acceptConnection(id, payloadCallback);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Nearby.getConnectionsClient(ServerModeActivity.this).rejectConnection(id);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            switch (connectionResolution.getStatus().getStatusCode()){
                case ConnectionsStatusCodes.STATUS_OK:
                    Toast.makeText(ServerModeActivity.this, "Connection is OK!", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Toast.makeText(ServerModeActivity.this, "Connection is rejected!", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Toast.makeText(ServerModeActivity.this, "Connection is broken!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            //After disconnected, we can't sending or receiving.
            Toast.makeText(ServerModeActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

}