/*
 * MainActivity
 *
 *
 * feb 5, 2018
 *
 * Copyright (c) 2018 Haotian Qi. CMPUT301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and condition of the Code of Student Behaviour at University of Alberta.
 * You can find a copy of the license in this project. Otherwise please contact me.
 */

package com.example.hqi1_subbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *  The main activity start from the beginning of the app, other activity
 *  will intent from here
 *
 *  @author hqi1
 *
 *  @see subInfo
 *  @see detailActivity
 *  @see editActivity
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "sub.sav";
    private ListView mainSubList;

    private ArrayList<subInfo> subList;
    private ArrayAdapter<subInfo> adapter;

    ////////////////////////////////onCreate/////////////////////////////////////////////////

    /**
     * called when app create
     * and setup some clickable action
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newButton = (Button) findViewById(R.id.btnNewSub);
        mainSubList = (ListView) findViewById(R.id.subListView);

        //create a button listener for add new subscription
        newButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(new Intent(getApplicationContext(), detailActivity.class),0);
            }
        });

        //create a listView listener for the editing an exist subscription
        mainSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the text to be edit
                subInfo info = subList.get(i);
                String text = info.getInfo();
                subList.remove(i);
                adapter.notifyDataSetChanged();
                saveInFile();

                //send the text to the editActivity and request text after edit
                Intent editMode = new Intent(MainActivity.this, editActivity.class);
                Bundle editText = new Bundle();
                editText.putString("toBeEdit",text);
                editMode.putExtras(editText);
                startActivityForResult(editMode,1);
            }
        });

        //create longClick listener for delete subscription
        mainSubList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //create a alertDialog to confirm the delete action
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                }
                builder.setTitle("Delete subscription")
                        .setMessage("Are you sure you want to delete this subscription?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //when user confirmed action
                                subList.remove(i);
                                adapter.notifyDataSetChanged();
                                saveInFile();
                                Toast.makeText(getApplicationContext(), "Subscription deleted",
                                        Toast.LENGTH_LONG).show();
                                totalCharge();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // when user canceled action
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }

    ////////////////////////////////onStart/////////////////////////////////////////////////

    /**
     * called when app start working
     * and prepare the main layout
     */
    protected void onStart() {
        super.onStart();

        loadFromFile();
        
        adapter = new ArrayAdapter<subInfo>(this,
                    R.layout.list_item, subList);
        mainSubList.setAdapter(adapter);
        totalCharge(); /* calculate monthly charge */

    }

    ///////////////////////////////onDestroy////////////////////////////////////////////////////

    /**
     * called when app end
     */
    protected void onDestroy() {
        super.onDestroy();

    }

    ///////////////////////////////other function////////////////////////////////////////////

    /**
     * try to read texts from the file
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<objectSubInfo>>(){}.getType();
            subList = gson.fromJson(in, listType);
        //throw exception when not able to read file
        } catch (FileNotFoundException e) {
            subList = new ArrayList<subInfo>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * try to save the changes back to file
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();
        //throw exception when not able to save
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /**
     * receive text from detailActivity and editActivity as request
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            //get and save text which received
            String rInfo = data.getStringExtra("newInfo");
            subInfo info = new objectSubInfo(rInfo);
            subList.add(info);
            adapter.notifyDataSetChanged();
            saveInFile();

            //give different notifications
            if(requestCode == 0){
                Toast.makeText(getApplicationContext(), "New subscription added",
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Subscription edited",
                        Toast.LENGTH_LONG).show();
            }
            //calculate monthly charge after file edited
            totalCharge();
        }
    }

    /**
     * calculate the monthly charge and display on main screen
     */
    public void totalCharge(){
        double total = 0;
        int n = subList.size();
        //check every charges in subList
        for(int i=0 ; i < n;i++ ){
            subInfo info = subList.get(i);
            String text = info.getInfo();
            String[] line = text.split("\n");
            double charge = Double.parseDouble(line[2]);
            total += charge;
        }
        String theTotal = String.valueOf(total);
        TextView totalText = (TextView)findViewById(R.id.tCharge);
        totalText.setText(" Total Monthly Charge: " + theTotal + " CAD");
    }
}

