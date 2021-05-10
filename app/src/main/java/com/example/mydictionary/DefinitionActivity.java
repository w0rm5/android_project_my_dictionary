package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DefinitionActivity extends AppCompatActivity {

    static final String MEANINGS = "MEANINGS", ANTONYMS = "ANTONYMS", SYNONYMS = "SYNONYMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        try {
            Intent intent = getIntent();
            String wordKey = intent.getStringExtra(MainActivity.WORD_KEY);
            int layoutIndex = 0;

            ConstraintLayout layout = findViewById(R.id.definition_activity_layout);
            ConstraintSet constraintSet = new ConstraintSet();

            //Add word
            TextView word = new TextView(this);
            word.setId(View.generateViewId());
            word.setText(wordKey);
            word.setTextSize(48);
            word.setTextColor(Color.BLACK);
            word.setTypeface(null, Typeface.BOLD);

            layout.addView(word,layoutIndex++, new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));

            //Get meaning
            JSONObject wordValue = new JSONObject(intent.getStringExtra(MainActivity.WORD_VALUE));
            JSONObject wordMeaning = wordValue.getJSONObject(MEANINGS);
            Iterator<String> wordMeaningKeys = wordMeaning.keys();
            ArrayList<TextView[]> meaningTextViews = new ArrayList<>();

            while(wordMeaningKeys.hasNext()){
                TextView[] meaningsView = new TextView[7];

                //get number of different meaning
                String sense_num = wordMeaningKeys.next();
                meaningsView[0] = new TextView(this);
                meaningsView[0].setId(View.generateViewId());
                meaningsView[0].setText(Html.fromHtml("<sup>"+sense_num+"</sup>"), TextView.BufferType.SPANNABLE);
                meaningsView[0].setTextColor(Color.BLUE);
                meaningsView[0].setTextSize(12);
                meaningsView[0].setTypeface(null, Typeface.BOLD_ITALIC);

                layout.addView(meaningsView[0], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                //getting all definitions from that number
                JSONArray sense_num_array = wordMeaning.getJSONArray(sense_num);

                //getting part of speech
                String part_of_speech = sense_num_array.getString(0) + ": ";
                meaningsView[1] = new TextView(this);
                meaningsView[1].setId(View.generateViewId());
                meaningsView[1].setText(part_of_speech);
                meaningsView[1].setTextColor(Color.RED);
                meaningsView[1].setTextSize(24);
                meaningsView[1].setTypeface(null, Typeface.ITALIC);

                layout.addView(meaningsView[1], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                // getting the meaning
                String meaning = sense_num_array.getString(1);
                meaningsView[2] = new TextView(this);
                meaningsView[2].setId(View.generateViewId());
                meaningsView[2].setText(meaning);
                meaningsView[2].setTextColor(Color.BLACK);
                meaningsView[2].setTextSize(24);
                meaningsView[2].setTypeface(null, Typeface.NORMAL);
                layout.addView(meaningsView[2], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                //getting context
                meaningsView[3] = new TextView(this);
                meaningsView[3].setId(View.generateViewId());
                String dictionary_context = "Context: ";
                meaningsView[3].setText(dictionary_context);
                meaningsView[3].setTextColor(Color.BLUE);
                meaningsView[3].setTextSize(24);
                meaningsView[3].setTypeface(null, Typeface.NORMAL);

                layout.addView(meaningsView[3], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                //
                JSONArray contextArray = sense_num_array.getJSONArray(2);
//                String joinedContext = array.join(", ");
                StringBuilder joinedContext = new StringBuilder();
                for(int i = 0; i < contextArray.length(); i++){
                    joinedContext.append(contextArray.get(i).toString());
                    if(i < contextArray.length() - 1){
                        joinedContext.append(", ");
                    }
                }

                meaningsView[4] = new TextView(this);
                meaningsView[4].setId(View.generateViewId());

                meaningsView[4].setTextSize(24);

                String contextString = joinedContext.toString();
                if(contextString.isEmpty()){
                    meaningsView[4].setText("no context");
                    meaningsView[4].setTextColor(Color.GRAY);
                    meaningsView[4].setTypeface(null, Typeface.ITALIC);
                }else{
                    meaningsView[4].setText(contextString);
                    meaningsView[4].setTextColor(Color.BLACK);
                    meaningsView[4].setTypeface(null, Typeface.NORMAL);
                }

                layout.addView(meaningsView[4], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                //example
                meaningsView[5] = new TextView(this);
                meaningsView[5].setId(View.generateViewId());
                String dictionary_example = "Example: ";
                meaningsView[5].setText(dictionary_example);
                meaningsView[5].setTextColor(Color.MAGENTA);
                meaningsView[5].setTextSize(24);
                meaningsView[5].setTypeface(null, Typeface.NORMAL);

                layout.addView(meaningsView[5], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));
                //
                JSONArray exampleArray = sense_num_array.getJSONArray(3);
                StringBuilder joinedExamples = new StringBuilder();
                for(int i = 0; i < exampleArray.length(); i++){
                    joinedExamples.append("- ");
                    joinedExamples.append(exampleArray.get(i).toString());
                    if(i < exampleArray.length() - 1){
                        joinedExamples.append(System.getProperty("line.separator"));
                    }
                }

                meaningsView[6] = new TextView(this);
                meaningsView[6].setId(View.generateViewId());
                meaningsView[6].setTextSize(24);

                String exampleString = joinedExamples.toString();
                if(exampleString.isEmpty()){
                    meaningsView[6].setText("no examples");
                    meaningsView[6].setTextColor(Color.GRAY);
                }else{
                    meaningsView[6].setText(exampleString);
                    meaningsView[6].setTextColor(Color.BLACK);
                }
                meaningsView[6].setTypeface(null, Typeface.ITALIC);

                layout.addView(meaningsView[6], layoutIndex++, new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));

                meaningTextViews.add(meaningsView);
            }

            //Antonyms
            TextView antonymsTitle = new TextView(this);
            antonymsTitle.setId(View.generateViewId());
            antonymsTitle.setTextSize(24);
            antonymsTitle.setText(ANTONYMS + ": ");
            antonymsTitle.setTextColor(Color.RED);
            antonymsTitle.setTypeface(null, Typeface.BOLD);

            layout.addView(antonymsTitle,layoutIndex++, new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            //
            JSONArray antonymArray = wordValue.getJSONArray(ANTONYMS);
            StringBuilder joinedAntonyms = new StringBuilder();

            for(int i = 0; i < antonymArray.length(); i++){
                joinedAntonyms.append(antonymArray.get(i).toString());
                if(i < antonymArray.length() - 1){
                    joinedAntonyms.append(", ");
                }
            }

            TextView antonyms = new TextView(this);
            antonyms.setId(View.generateViewId());
            antonyms.setTextSize(24);

            String antonymsString = joinedAntonyms.toString();

            if(antonymsString.isEmpty()){
                antonyms.setText("no antonyms");
                antonyms.setTextColor(Color.GRAY);
            }else{
                antonyms.setText(antonymsString);
                antonyms.setTextColor(Color.BLACK);
            }
            antonyms.setTypeface(null, Typeface.ITALIC);


            layout.addView(antonyms,layoutIndex++, new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));

            //synonyms
            TextView synonymsTitle = new TextView(this);
            synonymsTitle.setId(View.generateViewId());
            synonymsTitle.setText(SYNONYMS + ": ");
            synonymsTitle.setTextSize(24);
            synonymsTitle.setTextColor(Color.GREEN);
            synonymsTitle.setTypeface(null, Typeface.BOLD);

            layout.addView(synonymsTitle, layoutIndex++, new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));
            //

            JSONArray synonymArray = wordValue.getJSONArray(SYNONYMS);
            StringBuilder joinedSynonym = new StringBuilder();
            for(int i = 0; i < synonymArray.length(); i++){
                joinedSynonym.append(synonymArray.get(i).toString());
                if(i < synonymArray.length() - 1){
                    joinedSynonym.append(", ");
                }
            }
            TextView synonyms = new TextView(this);
            synonyms.setId(View.generateViewId());
            synonyms.setTextSize(24);
            synonyms.setTypeface(null, Typeface.ITALIC);

            String synonymsString = joinedSynonym.toString();
            if(synonymsString.isEmpty()){
                synonyms.setText("no synonyms");
                synonyms.setTextColor(Color.GRAY);
            }else{
                synonyms.setText(synonymsString);
                synonyms.setTextColor(Color.BLACK);
            }


            layout.addView(synonyms, layoutIndex++, new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));

            constraintSet.clone(layout);
            constraintSet.connect(word.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            constraintSet.connect(word.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            for (int i = 0; i < meaningTextViews.size(); i++) {
                if(i == 0){
                    constraintSet.connect(meaningTextViews.get(i)[0].getId(), ConstraintSet.TOP, word.getId(), ConstraintSet.BOTTOM, 0);
                    constraintSet.connect(meaningTextViews.get(i)[1].getId(), ConstraintSet.TOP, word.getId(), ConstraintSet.BOTTOM, 0);
                }else{
                    constraintSet.connect(meaningTextViews.get(i)[0].getId(), ConstraintSet.TOP, meaningTextViews.get(i-1)[6].getId(), ConstraintSet.BOTTOM, 100);
                    constraintSet.connect(meaningTextViews.get(i)[1].getId(), ConstraintSet.TOP, meaningTextViews.get(i-1)[6].getId(), ConstraintSet.BOTTOM, 100);
                }
                constraintSet.connect(meaningTextViews.get(i)[0].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                constraintSet.connect(meaningTextViews.get(i)[1].getId(), ConstraintSet.START, meaningTextViews.get(i)[0].getId(), ConstraintSet.END, 0);
                //meaning
                constraintSet.connect(meaningTextViews.get(i)[2].getId(), ConstraintSet.TOP, meaningTextViews.get(i)[1].getId(), ConstraintSet.BOTTOM, 0);
                constraintSet.connect(meaningTextViews.get(i)[2].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 24);

                //context title
                constraintSet.connect(meaningTextViews.get(i)[3].getId(), ConstraintSet.TOP, meaningTextViews.get(i)[2].getId(), ConstraintSet.BOTTOM, 24);
                constraintSet.connect(meaningTextViews.get(i)[3].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                //context
                constraintSet.connect(meaningTextViews.get(i)[4].getId(), ConstraintSet.TOP, meaningTextViews.get(i)[3].getId(), ConstraintSet.BOTTOM, 24);
                constraintSet.connect(meaningTextViews.get(i)[4].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 24);

                //example title
                constraintSet.connect(meaningTextViews.get(i)[5].getId(), ConstraintSet.TOP, meaningTextViews.get(i)[4].getId(), ConstraintSet.BOTTOM, 24);
                constraintSet.connect(meaningTextViews.get(i)[5].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                //examples
                constraintSet.connect(meaningTextViews.get(i)[6].getId(), ConstraintSet.TOP, meaningTextViews.get(i)[5].getId(), ConstraintSet.BOTTOM, 24);
                constraintSet.connect(meaningTextViews.get(i)[6].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 24);
            }
            //antonyms
            if(meaningTextViews.size() > 0){
                constraintSet.connect(antonymsTitle.getId(), ConstraintSet.TOP, meaningTextViews.get(meaningTextViews.size() - 1)[6].getId(), ConstraintSet.BOTTOM, 24);
            }else{
                constraintSet.connect(antonymsTitle.getId(), ConstraintSet.TOP, word.getId(), ConstraintSet.BOTTOM, 24);
            }
            constraintSet.connect(antonymsTitle.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);

            constraintSet.connect(antonyms.getId(), ConstraintSet.TOP, antonymsTitle.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(antonyms.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 24);

            //synonyms
            constraintSet.connect(synonymsTitle.getId(), ConstraintSet.TOP, antonyms.getId(), ConstraintSet.BOTTOM, 24);
            constraintSet.connect(synonymsTitle.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);

            constraintSet.connect(synonyms.getId(), ConstraintSet.TOP, synonymsTitle.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(synonyms.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 24);

            constraintSet.applyTo(layout);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}