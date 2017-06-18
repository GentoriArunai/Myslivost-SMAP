package cz.folprechtova.hides.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cz.folprechtova.hides.R;
import cz.folprechtova.hides.utils.Preferences;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

    }
}
