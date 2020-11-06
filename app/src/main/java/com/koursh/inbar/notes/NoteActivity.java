package com.koursh.inbar.notes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.koursh.inbar.notes.Database.Note;
import com.koursh.inbar.notes.Database.PublicDatabase;

public class NoteActivity extends AppCompatActivity {
    private final static int AUDIO_INTENT = 1;
    private boolean save_pending = false;
    private Note note;
    private EditText title, body;
    private PublicDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        database = PublicDatabase.getPublicDatabaseInstance(this);
        long id = getIntent().getLongExtra("id", -1);
        note = database.getNote(id);

        title = findViewById(R.id.note_title);
        body = findViewById(R.id.note_body);
        title.setText(note.title);
        body.setText(note.text);
        final TextWatcher save = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                save_pending = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        title.addTextChangedListener(save);
        body.addTextChangedListener(save);

        final Handler handler = new Handler();
        Runnable saveWatcher = new Runnable() {
            @Override
            public void run() {
                save(false);
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(saveWatcher);

    }

    public void audio(View view) {
        String model = PreferenceManager.getDefaultSharedPreferences(this).getString("model", "google");
        switch (model) {
            case "google":
                Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent2, AUDIO_INTENT);
                break;
            case "mozilla":
                Toast.makeText(this, "Mozilla text to speech is still pending implementation", Toast.LENGTH_SHORT).show();
//                SpeechResultCallback mVoiceSearchListener = new SpeechResultCallback() {
//
//                    @Override
//                    public void onStartListen() {
//                        // Handle when the api successfully opened the microphone and started listening
//                    }
//
//                    @Override
//                    public void onMicActivity(double fftsum) {
//                        // Captures the activity from the microphone
//                    }
//
//                    @Override
//                    public void onDecoding() {
//                        // Handle when the speech object changes to decoding state
//                    }
//
//
//                    @Override
//                    public void onSTTResult(@Nullable STTResult result) {
//                        if (result != null)
//                            body.setText(String.format("%s\n%s", body.getText(), result.mTranscription));
//                        // When the api finished processing and returned a hypothesis
//
//                    }
//
//                    @Override
//                    public void onNoVoice() {
//                        // Handle when the api didn't detect any voice
//                    }
//
//                    @Override
//                    public void onError(@SpeechResultCallback.ErrorType int errorType, @Nullable String error) {
//                        // Handle when any error occurred
//
//                    }
//                };
//
//                SpeechService speechService = new SpeechService(this);
//
//                SpeechServiceSettings.Builder builder = new SpeechServiceSettings.Builder()
//                        .withLanguage("en-US")
//                        .withStoreSamples(true)
//                        .withStoreTranscriptions(true)
//                        .withProductTag("product-tag");
//
//                speechService.start(builder.build(), mVoiceSearchListener);
        }

    }


    public void save(boolean force) {
        if (save_pending || force) {
            save_pending = false;
            note.title = title.getText().toString();
            note.text = body.getText().toString();
            database.update(note);
        }
    }

    @Override
    protected void onStop() {
        save(true);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        save(true);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AUDIO_INTENT:
                if (resultCode == RESULT_OK && null != data) {
                    body.setText(String.format("%s\n%s", body.getText(), data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0)));
                }
                break;
        }
    }
}