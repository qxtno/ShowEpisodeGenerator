package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShowAdapter.OnItemClickListener {
    public static final String TITLE = "title";
    public static final String SEASONS = "seasons";

    private ArrayList<Show> showArrayList;
    private ShowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createList();

        prepareRecyclerView();
    }

    private void createList() {
        Gson gson = new Gson();

        String jsonString = JsonHelper.getJsonFromAssets(getApplicationContext());

        Type type = new TypeToken<ArrayList<Show>>() {
        }.getType();
        showArrayList = gson.fromJson(jsonString, type);
    }

    private void prepareRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ShowAdapter(MainActivity.this, showArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ShowActivity.class);
        Show clickedItem = showArrayList.get(position);
        Bundle b = new Bundle();
        b.putIntArray(SEASONS, clickedItem.getSeasons());

        intent.putExtra(TITLE, clickedItem.getTitle());
        intent.putExtras(b);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });
        return true;

    }
}
