package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Gson gson = new Gson();
        ArrayList<Show> showArrayList;
        String jsonFileString = JsonHelper.getJsonFromAssets(getApplicationContext());

        showArrayList.add(new Show() )
    }
}
