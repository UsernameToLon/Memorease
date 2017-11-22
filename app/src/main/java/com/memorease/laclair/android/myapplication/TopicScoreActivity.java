package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;

public class TopicScoreActivity extends AppCompatActivity {

    private TextView topicTextView;
    private TextView totalText, proficientText, goodText, okText, needsWorkText;
    int proficient, good, ok, needsWork, answerValue, totalValue =0;
    Cursor cursor;
    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_score_activity);

        totalText = findViewById(R.id.totalCards);
        proficientText = findViewById(R.id.proficient);
        goodText = findViewById(R.id.good);
        okText = findViewById(R.id.ok);
        needsWorkText = findViewById(R.id.needsWork);

        topicTextView = findViewById(R.id.myScoresTitle);
        SQLiteDatabase cardReader = cardsDbHelper.getReadableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topic");

            topicTextView.setText(topicPassed);
        }

        String topic = (String) topicTextView.getText();
        String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + " WHERE topic LIKE \"%" + topic + "%\";";
        cursor = cardReader.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            answerValue = cursor.getInt(cursor.getColumnIndex("correctanswered"));
            incrementValue(answerValue);
            totalValue++;
        }
        if(cursor.moveToNext()){
            do {
                answerValue = cursor.getInt(cursor.getColumnIndex("correctanswered"));
                incrementValue(answerValue);
                totalValue++;
            }while(cursor.moveToNext());
        }


        //Set texts
        totalText.setText("Total Cards: "+totalValue);
        proficientText.setText("Proficient (3/3): "+getPercentage(totalValue,proficient));
        goodText.setText("Good (2/3): "+getPercentage(totalValue,good));
        okText.setText("Ok (1/3): "+getPercentage(totalValue,ok));
        needsWorkText.setText("Needs Work (0/3): "+getPercentage(totalValue,needsWork));

        cardReader.close();
    }

    public String getPercentage(int total, int amount){
        String str;
        String temp;
        int percentage;

        if(total!=0){
            percentage = ((amount/total)*100);
            temp = String.valueOf(percentage);
            str = temp+"%";
        }
        else
            str = "No Cards";

        return str;
    }

    public void incrementValue(int correct){
        switch (correct){
            case 4:
                proficient++;
            case 3:
                proficient++;
            case 2:
                good++;
            case 1:
                ok++;
            default:
                needsWork++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        cardsDbHelper.close();
    }
}