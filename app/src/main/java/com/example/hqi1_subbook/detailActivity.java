/*
 * detailActivity
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

import java.util.EmptyStackException;
/**
 *  The activity to handle new subscription from user.
 *  Send new subscription to MainActivity
 *
 *  @author hqi1
 *
 *  @see subInfo
 *  @see MainActivity
 */
public class detailActivity extends AppCompatActivity {
    private EditText theName;
    private EditText theDateY;
    private EditText theDateM;
    private EditText theDateD;
    private EditText theCharge;
    private EditText theComment;

    /**
     * called when activity create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Button saveButton = (Button) findViewById(R.id.btnSaveSub);
        theName = (EditText) findViewById(R.id.editName);
        theDateY = (EditText) findViewById(R.id.editDateY);
        theDateM = (EditText) findViewById(R.id.editDateM);
        theDateD = (EditText) findViewById(R.id.editDateD);
        theCharge = (EditText) findViewById(R.id.editCharge);
        theComment = (EditText) findViewById(R.id.editComment);

        //create the button listener to active save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);

                //get text input by user
                String textN = theName.getText().toString();
                String textDY = theDateY.getText().toString();
                String textDM = theDateM.getText().toString();
                String textDD = theDateD.getText().toString();
                String textCh = theCharge.getText().toString();
                String textCo = theComment.getText().toString();
                String textD = textDY + "-" + textDM + "-" + textDD;

                //check if input legal
                if ((textN.length() == 0) || (textDY.length() == 0) || (textDM.length() == 0) || (textDD.length() == 0) || (textCh.length() == 0)){
                    Toast.makeText(getApplicationContext(), "Pls at least fill in \'name\', \'date\' and \'charge\'",
                            Toast.LENGTH_LONG).show();
                }
                else if(textDY.length() < 4){
                    Toast.makeText(getApplicationContext(), "Pls fill in 4 digits for year",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    //send new subscription to MainActivity
                    String text = textN + "\n" + textD + "\n" + textCh + "\n" + textCo;
                    Intent mainScreen = new Intent();
                    mainScreen.putExtra("newInfo",text);
                    setResult(RESULT_OK,mainScreen);
                    finish();
                }
            }
        });
    }
}
