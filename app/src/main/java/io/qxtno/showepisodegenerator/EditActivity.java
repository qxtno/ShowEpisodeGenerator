package io.qxtno.showepisodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    ShowDBHelper dbHelper = new ShowDBHelper(this);
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Show show = intent.getParcelableExtra("Show Item Edit");

        final EditText editTitle = findViewById(R.id.activity_edit_title);
        final EditText editSeasons = findViewById(R.id.activity_edit_seasons);
        Button editSave = findViewById(R.id.activity_edit_save);

        assert show != null;
        editTitle.setText(show.getTitle());
        editSeasons.setText(Arrays.toString(show.getSeasons()));
        id = show.getId();

        editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_show_menu, menu);

        MenuItem help = menu.findItem(R.id.menu_help);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle(R.string.help).setMessage(R.string.add_edit_message).setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(this.getSystemService(Context.INPUT_METHOD_SERVICE));
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
    }
}