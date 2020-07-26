package io.qxtno.showepisodegenerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CardView lastShowcard = view.findViewById(R.id.last_show);
        lastShowcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireActivity(),"Test",Toast.LENGTH_SHORT).show();
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
}
