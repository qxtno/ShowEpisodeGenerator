package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class ShowActivity extends AppCompatActivity {
    Button randomize;
    ShowDBHelper dbHelper = new ShowDBHelper(this);
    Show show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        show = intent.getParcelableExtra("Show Item");

        assert show != null;
        final String title = show.getTitle();
        final int[] seasons = show.getSeasons();

        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);

        randomize = findViewById(R.id.random);

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random generator = new Random();
                int randomEpisode;
                assert seasons != null;
                int randomSeason = generator.nextInt(seasons.length) + 1;
                randomEpisode = generator.nextInt(seasons[randomSeason - 1]) + 1;
                String space = " ";

                String episodeInfo = getResources().getString(R.string.se) +
                        randomSeason +
                        space +
                        getResources().getString(R.string.ep) +
                        randomEpisode;
                TextView resultTextView = findViewById(R.id.result);
                resultTextView.setText(episodeInfo);
            }
        });

        Button add = findViewById(R.id.add_to_fav);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show.isFav()) {
                    show.setFav(true);
                } else {
                    show.setFav(false);
                }
                dbHelper.updateShow(show);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_activity_menu, menu);

        MenuItem edit = menu.findItem(R.id.action_edit);
        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (show.isCustom()) {
                    Intent intent = new Intent(ShowActivity.this, EditActivity.class);

                    intent.putExtra("Show Item Edit", show);

                    startActivity(intent);

                    return true;
                } else {
                    Toast.makeText(ShowActivity.this, R.string.cannot_edit, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        MenuItem delete = menu.findItem(R.id.action_delete);
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (show.isCustom()) {
                    dbHelper.deleteShow(show);
                    Toast.makeText(ShowActivity.this, R.string.delete_done, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(ShowActivity.this, R.string.cannot_delete, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        return true;
    }
}
