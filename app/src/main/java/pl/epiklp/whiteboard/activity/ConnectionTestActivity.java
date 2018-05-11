package pl.epiklp.whiteboard.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import pl.epiklp.whiteboard.R;

import static java.nio.charset.StandardCharsets.UTF_8;


public class ConnectionTestActivity extends AppCompatActivity {

    public final static String SERVICE_ID = "pl.epiklp.whiteboard";
    private static final String TAG = ConnectionTestActivity.class.getSimpleName();
    private boolean isServer;

    private String msg;
    private static String userNickname = "test";
    private ToggleButton advertisingBtn;
    private ToggleButton discoveryBtn;
    private TextView msgTv;

    private List<String> endpointIds;
    private ConnectionsClient connectionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_test);

        isServer = getIntent().getExtras().getBoolean("isServer");

        endpointIds = new ArrayList<>();

        advertisingBtn = findViewById(R.id.advertisingToggleBtn);
        discoveryBtn = findViewById(R.id.discoveryToggleBtn);
        msgTv = findViewById(R.id.msg_tv);

        advertisingBtn.setChecked(false);
        advertisingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!advertisingBtn.isChecked()) {
                    connectionsClient.stopAdvertising();
                    Log.d(TAG, "Stop advertising");
                } else {
                    startAdvertising();
                    Log.d(TAG, "Start advertising");
                }
            }
        });

        discoveryBtn.setChecked(false);
        discoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!discoveryBtn.isChecked()) {
                    connectionsClient.stopDiscovery();
                    Log.d(TAG, "Stop discovering");
                } else {
                    startDiscovery();
                    Log.d(TAG, "Start discovering");
                }
            }
        });

       // if(isServer)    discoveryBtn.setVisibility(View.INVISIBLE);
       // else            advertisingBtn.setVisibility(View.INVISIBLE);
    }

    private void startAdvertising() {
        connectionsClient = Nearby.getConnectionsClient(this);
        connectionsClient.startAdvertising(userNickname, SERVICE_ID, mConnectionLifecycleCallback, new AdvertisingOptions(Strategy.P2P_CLUSTER))
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                Toast.makeText(ConnectionTestActivity.this, "Start advertising!", Toast.LENGTH_SHORT).show();
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

    public void sendTestMessage(View view) {
        EditText editText = findViewById(R.id.message_et);
        String msg = editText.getText().toString();
        msgTv.append(msg + "\n");
        editText.setText("");
        connectionsClient.sendPayload(endpointIds, Payload.fromBytes(msg.getBytes(UTF_8)));
    }

    private void startDiscovery() {
        connectionsClient = Nearby.getConnectionsClient(this);
        connectionsClient.startDiscovery(SERVICE_ID, endpointDiscoveryCallback, new DiscoveryOptions(Strategy.P2P_CLUSTER))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ConnectionTestActivity.this, "Start discovering!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConnectionTestActivity.this, "Unable to start discovering!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String endpointID, @NonNull Payload payload) {
            try {
                msg = new String(endpointID.getBytes("UTF-8" ));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            msgTv.append(msg);
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String endpointID, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
        }
    };
    private ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String endpointID, @NonNull ConnectionInfo connectionInfo) {
            //Show alertBox with confirm connection with Client
            //TODO bottom alertDialog isn't showing
            final String id = endpointID;
            new AlertDialog.Builder(ConnectionTestActivity.this)
                    .setTitle("Accept connection with " + connectionInfo.getEndpointName())
                    .setMessage("Confirm the code " + connectionInfo.getAuthenticationToken())
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User confirm connection
                            Nearby.getConnectionsClient(ConnectionTestActivity.this).acceptConnection(id, payloadCallback);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Nearby.getConnectionsClient(ConnectionTestActivity.this).rejectConnection(id);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void onConnectionResult(@NonNull String endpointID, @NonNull ConnectionResolution connectionResolution) {
            switch (connectionResolution.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK: {
                    Toast.makeText(ConnectionTestActivity.this, "Connection is OK!", Toast.LENGTH_SHORT).show();
                    endpointIds.add(endpointID);
                    break;
                }
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Toast.makeText(ConnectionTestActivity.this, "Connection is rejected!", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Toast.makeText(ConnectionTestActivity.this, "Connection is broken!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
        @Override
        public void onDisconnected(@NonNull String s) {
            //After disconnected, we can't sending or receiving.
            Toast.makeText(ConnectionTestActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            //TODO make label for this
            discoveryBtn.setChecked(false);
        }
    };
    private EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointID, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            Toast.makeText(ConnectionTestActivity.this, discoveredEndpointInfo.getEndpointName(), Toast.LENGTH_SHORT).show();

            connectionsClient.requestConnection(userNickname, endpointID, mConnectionLifecycleCallback);

            //after connection we stop searching
            connectionsClient.stopAdvertising();
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            Toast.makeText(ConnectionTestActivity.this, "A previously EndPoint was lost.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "A previously EndPoint was lost.");
        }
    };
}