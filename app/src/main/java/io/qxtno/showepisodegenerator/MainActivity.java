package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShowAdapter.OnItemClickListener {
    public static final String TITLE = "title";
    public static final String SEASONS = "seasons";

    private ArrayList<Show> showArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();

        String jsonString = JsonHelper.getJsonFromAssets(getApplicationContext());

        Type type = new TypeToken<ArrayList<Show>>() {
        }.getType();
        showArrayList = gson.fromJson(jsonString, type);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShowAdapter mAdapter = new ShowAdapter(MainActivity.this, showArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ShowActivity.class);
        Show clickedItem = showArrayList.get(position);
        Bundle b = new Bundle();
        b.putIntArray(SEASONS,clickedItem.getSeasons());

        intent.putExtra(TITLE,clickedItem.getTitle());
        intent.putExtras(b);

        startActivity(intent);
    }
}
