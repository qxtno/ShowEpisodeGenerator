package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class NewShowFragment extends Fragment {
    EditText titleEditText;
    EditText seasonsEditText;
    Button addShow;
    CheckBox favCheckBox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_show, container, false);

        final Show show = new Show();

        titleEditText = view.findViewById(R.id.title_edit_text);
        seasonsEditText = view.findViewById(R.id.seasons_edit_text);
        addShow = view.findViewById(R.id.add_new_show);
        favCheckBox = view.findViewById(R.id.fav_check_box);

        addShow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                show.setTitle(titleEditText.getText().toString());

                String seasonString = seasonsEditText.getText().toString();
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

                boolean checked = favCheckBox.isChecked();

                if (checked) {
                    show.setFav(true);
                } else {
                    show.setFav(false);
                }
                show.setCustom(true);

                ShowDBHelper dbHelper = new ShowDBHelper(getActivity());
                dbHelper.addShow(show);

                titleEditText.setText("");
                seasonsEditText.setText("");
                hideKeyboard(view);
                favCheckBox.setChecked(false);

                Toast.makeText(getActivity(), R.string.added, Toast.LENGTH_SHORT).show();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_show_menu, menu);

        MenuItem help = menu.findItem(R.id.menu_help);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.help).setMessage(R.string.add_edit_message).setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}