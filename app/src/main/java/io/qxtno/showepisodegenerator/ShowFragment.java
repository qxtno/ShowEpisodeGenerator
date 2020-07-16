package io.qxtno.showepisodegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class ShowFragment extends Fragment {

    Button randomize;
    View view;
    Show show;
    int id;
    int randomEpisode;
    int randomSeason;
    int[] seasons;
    String title;
    TextView resultTextView;
    TextView titleTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_show, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            show = bundle.getParcelable("Show item");
        }

        assert show != null;
        title = show.getTitle();
        seasons = show.getSeasons();
        id = show.getId();

        titleTextView = view.findViewById(R.id.frag_title);
        titleTextView.setText(title);

        randomize = view.findViewById(R.id.frag_random);

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random generator = new Random();
                assert seasons != null;
                randomSeason = generator.nextInt(seasons.length) + 1;
                randomEpisode = generator.nextInt(seasons[randomSeason - 1]) + 1;
                String space = " ";

                String episodeInfo = getResources().getString(R.string.se) +
                        randomSeason +
                        space +
                        getResources().getString(R.string.ep) +
                        randomEpisode;
                resultTextView = view.findViewById(R.id.frag_result);
                resultTextView.setText(episodeInfo);
            }
        });

        Button add = view.findViewById(R.id.frag_add_to_fav);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDBHelper dbHelper = new ShowDBHelper(Objects.requireNonNull(getActivity()).getApplicationContext());
                if (!show.isFav()) {
                    show.setFav(true);
                } else {
                    show.setFav(false);
                }
                dbHelper.updateFav(show);
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (show.isCustom()) {
            inflater.inflate(R.menu.show_activity_menu, menu);

            final MenuItem edit = menu.findItem(R.id.action_edit);
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (show.isCustom()) {
                        Intent intent = new Intent(getActivity(), EditActivity.class);

                        intent.putExtra("Show Item Edit", show);

                        startActivity(intent);

                        return true;
                    } else {
                        Toast.makeText(getActivity(), R.string.cannot_edit, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });

            final MenuItem delete = menu.findItem(R.id.action_delete);
            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (show.isCustom()) {
                        ShowDBHelper dbHelper = new ShowDBHelper(Objects.requireNonNull(getActivity()).getApplicationContext());
                        dbHelper.deleteShow(show);
                        getActivity().onBackPressed();
                        Toast.makeText(getActivity(), R.string.delete_done, Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        Toast.makeText(getActivity(), R.string.cannot_delete, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });
        } else {
            inflater.inflate(R.menu.non_stock_menu, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpGen();
    }

    private void setUpGen() {
        ShowDBHelper dbHelper = new ShowDBHelper(Objects.requireNonNull(getActivity()).getApplicationContext());
        show = dbHelper.getShow(id);
        assert show != null;
        title = show.getTitle();
        seasons = show.getSeasons();
        id = show.getId();

        titleTextView = view.findViewById(R.id.frag_title);
        titleTextView.setText(title);

        String seasonString = Arrays.toString(show.getSeasons());
        String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
        seasons = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                seasons[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                Log.i("To Array Error DBHelper", "Error while parsing string into an array");
            }
        }
        show.setSeasons(seasons);

        randomize = view.findViewById(R.id.frag_random);

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random generator = new Random();
                randomSeason = generator.nextInt(seasons.length) + 1;
                randomEpisode = generator.nextInt(seasons[randomSeason - 1]) + 1;
                String space = " ";

                String episodeInfo = getResources().getString(R.string.se) +
                        randomSeason +
                        space +
                        getResources().getString(R.string.ep) +
                        randomEpisode;
                resultTextView = view.findViewById(R.id.frag_result);
                resultTextView.setText(episodeInfo);
            }
        });
    }
}
