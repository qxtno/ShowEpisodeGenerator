package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import static io.qxtno.showepisodegenerator.HomeFragment.SEASONS;
import static io.qxtno.showepisodegenerator.HomeFragment.TITLE;

public class ShowActivity extends AppCompatActivity {
    Button randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        Bundle b = this.getIntent().getExtras();

        assert b != null;
        final int[] seasons = b.getIntArray(SEASONS);

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
    }
}
