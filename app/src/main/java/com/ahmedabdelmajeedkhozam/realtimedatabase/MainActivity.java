package com.ahmedabdelmajeedkhozam.realtimedatabase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ahmedabdelmajeedkhozam.realtimedatabase.adapter.ArtistList;
import com.ahmedabdelmajeedkhozam.realtimedatabase.model.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextName;
    Button btnAdd;
    Spinner spinnerGenries;


    DatabaseReference databaseArtists;

    ListView listViewArtists;

    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = findViewById(R.id.editTextName);
        spinnerGenries = findViewById(R.id.spinnerGeneries);
        btnAdd = findViewById(R.id.buttonAddArtist);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });

        listViewArtists =findViewById(R.id.listViewArtists);
        artistList=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();
                for (DataSnapshot artistSnapeshot :
                        dataSnapshot.getChildren()) {
                    Artist artist = artistSnapeshot.getValue(Artist.class);

                    artistList.add(artist);
                }
                ArtistList adapter=new ArtistList(MainActivity.this,artistList);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addArtist() {
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenries.getSelectedItem().toString();


        if (!TextUtils.isEmpty(name)) {
            String id = databaseArtists.push().getKey();

            Artist artist=new Artist(id,name,genre);

           databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist Added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enter the name ", Toast.LENGTH_SHORT).show();
        }
    }

}
