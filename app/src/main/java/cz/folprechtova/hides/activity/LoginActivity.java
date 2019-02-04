package cz.folprechtova.hides.activity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.folprechtova.hides.R;
import cz.folprechtova.hides.utils.Preferences;

public class LoginActivity extends AppCompatActivity {

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                Toast.makeText(context, device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    //permissions pro polohu uživatele, nelze se povolení vyhnout
    BluetoothAdapter btAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        final EditText loginTxt = ((EditText) findViewById(R.id.loginTxt));
        loginTxt.setText(Preferences.getUserName()); //natáhne poslední jméno uložené (existuje-li)

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginTxt.getText().toString().isEmpty()){
                    Preferences.setUserName(loginTxt.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(intent);
                }else{
                    loginTxt.setError("Musíte vyplnit jméno!");
                }
            }
        });



        if (getIntent().getExtras() != null && !getIntent().getExtras().getString("MAC", "").isEmpty()) {
            //vešli jsme do regionu, známe MAC nějak ji zpracujeme
            Toast.makeText(this, getIntent().getExtras().getString("MAC", ""), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReciever, filter);
        btAdapter.startDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bReciever);
        btAdapter.cancelDiscovery();
    }
}
