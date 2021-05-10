package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public static final String WORD_KEY = "word_key";
    public static final String WORD_VALUE = "word_definition";

    JSONObject jsonObject;
    ArrayList<String> foundWords;
    boolean resetSearch = true;
    Iterator<String> keys;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView wordList = findViewById(R.id.word_list);
        foundWords = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.word_list_item, foundWords);
        wordList.setAdapter(adapter);

        wordList.setOnItemClickListener((parent, view, position, id) -> {
            try {
                Intent intent = new Intent(MainActivity.this,
                        DefinitionActivity.class);
                String wordKey = foundWords.get(position);
                JSONObject clickedWord = jsonObject.getJSONObject(wordKey);
                intent.putExtra(WORD_KEY, wordKey);
                intent.putExtra(WORD_VALUE, clickedWord.toString());
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        TextInputEditText searchBox = findViewById(R.id.search_box);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){

                    if(resetSearch){
                        try {
                            jsonObject = new JSONObject(readJSON(s.charAt(0)));
                            resetSearch = false;

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    foundWords.clear();
                    keys = jsonObject.keys();
                    String nextWord;

                    while(keys.hasNext()){
                        nextWord = keys.next();
                        if(nextWord.matches(s.toString().toUpperCase() + ".*")){
                            foundWords.add(nextWord);
                        }
                    }

                    adapter.notifyDataSetChanged();

                }else{
                    resetSearch = true;
                    adapter.clear();
                }
            }
        });


        setSupportActionBar(toolbar);
    }

    public String readJSON(Character c) {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = getAssets().open("D"+ Character.toUpperCase(c) +".json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}