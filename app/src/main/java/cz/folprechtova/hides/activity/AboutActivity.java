package cz.folprechtova.hides.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cz.folprechtova.hides.R;

public class AboutActivity extends BaseBackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button btnLink = (Button) findViewById(R.id.btnLink);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBrowser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.zakonyprolidi.cz/cs/2001-449"));
                startActivity(intentBrowser);
            }
        });

    }
}
