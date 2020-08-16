package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    private int id;
    private Show show;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        CardView lastShowCard = view.findViewById(R.id.last_show);

        id = prefs.getInt("id", -1);

        TextView homeResults = view.findViewById(R.id.home_title_results);
        homeResults.setText(R.string.show_appear);

        if (id != -1) {
            ShowDBHelper dbHelper = new ShowDBHelper(requireActivity().getApplicationContext());
            show = dbHelper.getShow(id);
            String seasonString = Arrays.toString(show.getSeasons());
            String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
            int[] seasons = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    seasons[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                    Log.i("To Array Error DBHelper", "Error while parsing string into an array");
                }
            }
            show.setSeasons(seasons);

            String result = prefs.getString("resultString", String.valueOf(R.string.result_here));

            homeResults.setText(show.getTitle() + "\n\n" + result);
        }

        lastShowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id == -1) {
                    Toast.makeText(requireActivity(), R.string.no_show, Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("show", show);

                    Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.showFragment, bundle);
                }
            }
        });

        ImageButton list = view.findViewById(R.id.home_list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.showListFragment);
            }
        });

        ImageButton fav = view.findViewById(R.id.home_fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.favouritesFragment);
            }
        });

        ImageButton newShow = view.findViewById(R.id.home_add);
        newShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.newShowFragment);
            }
        });

        return view;
    }

    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        ((MainActivity) requireActivity()).drawerLayout.setDrawerLockMode(lockMode);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDrawerEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setDrawerEnabled(false);
    }
}
