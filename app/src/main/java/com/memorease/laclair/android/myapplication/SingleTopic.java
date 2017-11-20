package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;

public class SingleTopic extends AppCompatActivity {

    private TextView topicTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_topic);

        topicTextView = findViewById(R.id.topicTop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topic");

            topicTextView.setText(topicPassed);
        }
    }

    public void takeToStudyCards(View view){
        Intent intent = new Intent(this, StudyCards.class);
        intent.putExtra("topic", topicTextView.getText());
        startActivity(intent);
    }

    public void takeToCreateCard(View view){
        Intent intent = new Intent(this, CreateCardActivity.class);
        intent.putExtra("topicName", topicTextView.getText());
        startActivity(intent);
    }

    public void deleteAllCards(View view){

        String topic = (String) topicTextView.getText();

        CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
        SQLiteDatabase cards = cardsDbHelper.getWritableDatabase();

        cards.execSQL("DELETE FROM "+CardContract.CardEntry.TABLE_NAME+" WHERE topic LIKE \"%"+topic+"%\";");

        cardsDbHelper.close();

        Toast.makeText(SingleTopic.this, "All Cards Deleted", Toast.LENGTH_LONG).show();
    }
}
