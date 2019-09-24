package com.yarinov.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ListView notesListView;

    static ArrayList<String> notesArrayList = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    HashSet<String> set;

    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_note_op_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.custom_note_op_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.addNote){
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
            startActivity(intent);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize sharedPreferences and retrieve the stored data
        sharedPreferences = getApplicationContext()
                .getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        //If the set is empty (First use in app) - add an example note
        //else get all the data stored and display it
        if (set == null){
            notesArrayList.add("Example Note");
        }else {
            notesArrayList = new ArrayList<>(set);
        }

        notesListView = findViewById(R.id.noteList);

        //Set the note array and initialize the array adapter
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notesArrayList);
        notesListView.setAdapter(arrayAdapter);

        //Short press on a note will move the user to edit the note
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent moveToEdit = new Intent(MainActivity.this, NoteEditorActivity.class);
                moveToEdit.putExtra("noteId", i);
                startActivity(moveToEdit);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemTODelete = i;

                //Set the alert dialog for deleting a note
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note?").setMessage("Are you sure you want to delete this note?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Remove the item from the list and notify
                                notesArrayList.remove(itemTODelete);
                                arrayAdapter.notifyDataSetChanged();

                                //Update the sharedPreferences
                                HashSet<String> set = new HashSet<>(notesArrayList);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null).show();


                return true;
            }
        });

    }
}
