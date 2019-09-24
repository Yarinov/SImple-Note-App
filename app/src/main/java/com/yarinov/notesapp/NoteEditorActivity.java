package com.yarinov.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    EditText noteEdit;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        noteEdit = findViewById(R.id.noteEditText);

        //Get the note
        Intent currentIntent = getIntent();
        noteId = currentIntent.getIntExtra("noteId", -1);

        //if we found the noteId - set the current text to the stored text of the note
        if (noteId != -1){
            noteEdit.setText(MainActivity.notesArrayList.get(noteId));
        } else {
            //If there isn't any note (When creating a new note) add new String to the array
            MainActivity.notesArrayList.add("");
            noteId = MainActivity.notesArrayList.size()-1;
        }

        noteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesArrayList.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet<>(MainActivity.notesArrayList);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
