package com.koursh.inbar.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.koursh.inbar.notes.Database.Note;
import com.koursh.inbar.notes.Database.PublicDatabase;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        final PublicDatabase database = PublicDatabase.getPublicDatabaseInstance(this);
        long id = getIntent().getLongExtra("id", -1);
        final Note note = database.getNote(id);

        final EditText title = findViewById(R.id.note_title);
        final EditText body = findViewById(R.id.note_body);
        title.setText(note.title);
        body.setText(note.text);
//TODO: make more efficient
        TextWatcher save = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                note.title = title.getText().toString();
                note.text = body.getText().toString();
                database.update(note);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        title.addTextChangedListener(save);
        body.addTextChangedListener(save);
    }
}