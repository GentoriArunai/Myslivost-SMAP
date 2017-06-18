package cz.folprechtova.hides.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

public class BaseBackButtonActivity extends AppCompatActivity {

    //předek aktivit se šipkou na zavření

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //zobrazení horní šipky
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); //zavření aktivity při kliku na šipku zpět
        }
        return super.onOptionsItemSelected(item);
    }
}
