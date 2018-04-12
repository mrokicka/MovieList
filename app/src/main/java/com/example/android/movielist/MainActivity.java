package com.example.android.movielist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String[] moviesArray = {"Jaws", "Airplane!", "Raider of the Lost Ark", "Ghostbusters", "Groundhog Day", "Dumb and Dumber"};
    private static final ArrayList<String> movies = new ArrayList<String>();
    private static final ArrayList<String> movieID = new ArrayList<String>();
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        String[] b = {"tt0073195", "tt0080339", "tt0082971", "tt0087332", "tt0107048", "tt0109686"};

        Map<String, ?> map = getPreferences(this.MODE_PRIVATE).getAll();

        if(map.isEmpty()) {
            for(int i = 0; i < moviesArray.length; i++) {
                movies.add(moviesArray[i]);
                movieID.add(b[i]);
            }
        } else {
            for(int i = 0; i < (Integer) map.get("SIZE"); i++) {
                movies.add((String) map.get("MOVIE" + i));
                movieID.add((String) map.get("MOVIE_ID" + i));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_view, movies);
        //second argument is the layout that will be used to display each item in the LISTVIEW.
        //third argument is the array containing the data we want to display in the LISTVIEW

        list.setAdapter(adapter);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String url = "https://www.imdb.com/title/" + movieID.get(position);
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(in);
                    }
                }
        );

        list.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setMessage("Do you want to remove this?\n\n" + movies.get(position) + " will be removed!").setTitle("DELETE");

                        builder.setTitle("DELETE");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                movies.remove(position);
                                movieID.remove(position);



                                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item_view, movies);

                                list.setAdapter(adapter1);
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Doesn't do anything.
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        return true;
                    }
                }
        );
    }

    public void addClicked(View v) {
        Intent i = new Intent(this, AddActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        movies.add(data.getStringExtra("MOVIE"));
        movieID.add(data.getStringExtra("CODE"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_view, movies);

        list.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences.Editor e = getPreferences(this.MODE_PRIVATE).edit();
        Map<String, ?> map = getPreferences(this.MODE_PRIVATE).getAll();

        if(map.isEmpty()) {
            e.putString("INITIAL_LAUNCH", "Dummy String"); // The first time we close the application, an inaccessible string
            // will be placed in the map to avoid the program adding the default movies whenever the map is empty.
        }

        for(int i = 0; i < movies.size(); i++) {
            e.putString("MOVIE" + i, movies.get(i));
            e.putString("MOVIE_ID" + i, movieID.get(i));
        }

        e.putInt("SIZE", movies.size());

        e.apply();
    }
}
