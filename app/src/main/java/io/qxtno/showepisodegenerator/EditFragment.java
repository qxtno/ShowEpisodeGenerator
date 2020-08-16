package io.qxtno.showepisodegenerator;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

public class EditFragment extends Fragment {

    private ShowDBHelper dbHelper;
    private int id;
    private Show show;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        ((MainActivity)requireActivity()).toolbar.setTitle(R.string.edit);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            show = bundle.getParcelable("show");
        }
        final EditText editTitle = view.findViewById(R.id.activity_edit_title);
        final EditText editSeasons = view.findViewById(R.id.activity_edit_seasons);
        Button editSave = view.findViewById(R.id.activity_edit_save);

        assert show != null;
        editTitle.setText(show.getTitle());
        String seasonsText = Arrays.toString(show.getSeasons());
        seasonsText = seasonsText.substring(1, seasonsText.length() - 1);
        editSeasons.setText(seasonsText);
        id = show.getId();

        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTitle.getText().toString().equals("") && !editSeasons.getText().toString().equals("")) {
                    dbHelper = new ShowDBHelper(getActivity());
                    Show show1 = dbHelper.getShow(id);
                    show1.setTitle(editTitle.getText().toString());

                    String seasonString = editSeasons.getText().toString();
                    String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
                    int[] seasons = new int[items.length];

                    for (int i = 0; i < items.length; i++) {
                        try {
                            seasons[i] = Integer.parseInt(items[i]);
                        } catch (NumberFormatException nfe) {
                            Log.i("To Array Error DBHelper", "Error while parsing string into an array");
                        }
                    }
                    show1.setSeasons(seasons);

                    dbHelper.updateShow(show1);

                    editTitle.setText("");
                    editSeasons.setText("");
                    hideKeyboard();
                    Toast.makeText(requireContext(), R.string.edit_done, Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();

                } else if (!editTitle.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), R.string.season_empty, Toast.LENGTH_SHORT).show();
                } else if (!editSeasons.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), R.string.title_empty, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), R.string.all_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_edit_show_menu, menu);

        MenuItem help = menu.findItem(R.id.menu_help);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}