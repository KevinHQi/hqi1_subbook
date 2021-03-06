/*
 * editActivity
 *
 *
 * feb 5, 2018
 *
 * Copyright (c) 2018 Haotian Qi. CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and condition of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact me.
 */
package com.example.hqi1_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 *  The activity to handle edit subscription from user.
 *  Receive text from MainActivity and sent result back
 *
 *  @author hqi1
 *
 *  @see subInfo
 *  @see MainActivity
 */
public class editActivity extends AppCompatActivity {

    /**
     * called when activity create
     * and split text
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //receive text from MainActivity
        Bundle comingText = getIntent().getExtras();
        String toBeEdit = comingText.getString("toBeEdit");

        //identify each string
        String[] lines = toBeEdit.split("\n");
        String line1 = lines[0];
        String[] line2 = lines[1].split("-");
        String line3 = lines[2];
        String line4 = lines[3];
        String lineY = line2[0];
        String lineM = line2[1];
        String lineD = line2[2];

        //find corresponding EditText
        final EditText theName = (EditText) findViewById(R.id.editName);
        final EditText theDateY = (EditText) findViewById(R.id.editDateY);
        final EditText theDateM = (EditText) findViewById(R.id.editDateM);
        final EditText theDateD = (EditText) findViewById(R.id.editDateD);
        final EditText theCharge = (EditText) findViewById(R.id.editCharge);
        final EditText theComment = (EditText) findViewById(R.id.editComment);

        //show the text in EditText
        theName.setText(line1);
        theDateY.setText(lineY);
        theDateM.setText(lineM);
        theDateD.setText(lineD);
        theCharge.setText(line3);
        theComment.setText(line4);

        //get text after edit
        Button saveButton = (Button) findViewById(R.id.btnSaveSub);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                String textN = theName.getText().toString();
                String textDY = theDateY.getText().toString();
                String textDM = theDateM.getText().toString();
                String textDD = theDateD.getText().toString();
                String textCh = theCharge.getText().toString();
                String textCo = theComment.getText().toString();
                String textD = textDY + "-" + textDM + "-" + textDD;

                //check if value legal
                if ((textN.length() == 0) || (textDY.length() == 0) || (textDM.length() == 0) || (textDD.length() == 0) || (textCh.length() == 0)){
                    Toast.makeText(getApplicationContext(), "Pls at least fill in \'name\', \'date\' and \'charge\'",
                            Toast.LENGTH_LONG).show();
                }
                else if(textDY.length() < 4){
                    Toast.makeText(getApplicationContext(), "Pls fill in 4 digits for year",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //send back to MainActivity
                    String text = textN + "\n" + textD + "\n" + textCh + "\n" + textCo + " ";
                    Intent mainScreen = new Intent();
                    Bundle editText = new Bundle();
                    editText.putString("newInfo",text);
                    mainScreen.putExtras(editText);
                    setResult(RESULT_OK,mainScreen);

                    finish();
                }
            }
        });
    }
}
