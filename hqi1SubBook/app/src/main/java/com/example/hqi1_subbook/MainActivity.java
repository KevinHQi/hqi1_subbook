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


public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "sub.sav";
    private ListView mainSubList;

    private ArrayList<subInfo> subList;
    private ArrayAdapter<subInfo> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button newButton = (Button) findViewById(R.id.btnNewSub);
        mainSubList = (ListView) findViewById(R.id.subListView);

        newButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(new Intent(getApplicationContext(), detailActivity.class),0);
            }
        });

        mainSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subInfo info = subList.get(i);
                String text = info.getInfo();
                subList.remove(i);
                adapter.notifyDataSetChanged();
                saveInFile();

                Intent editMode = new Intent(MainActivity.this, editActivity.class);
                Bundle editText = new Bundle();
                editText.putString("toBeEdit",text);
                editMode.putExtras(editText);
                startActivityForResult(editMode,1);
            }
        });

        mainSubList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
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
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });


    }

    ////////////////////////////////start/////////////////////////////////////////////////

    protected void onStart() {
        super.onStart();

        loadFromFile();
        
        adapter = new ArrayAdapter<subInfo>(this,
                    R.layout.list_item, subList);
        mainSubList.setAdapter(adapter);
        totalCharge();

    }

    ///////////////////////////////destroy////////////////////////////////////////////////////
    protected void onDestroy() {
        super.onDestroy();

    }

    ///////////////////////////////other function////////////////////////////////////////////

    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<objectSubInfo>>(){}.getType();
            subList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            subList = new ArrayList<subInfo>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }


    private void saveInFile() {
        try {

            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void writeToFile(String toBeSave){
        setResult(RESULT_OK);
        subInfo info = new objectSubInfo(toBeSave);
        subList.add(info);

        adapter.notifyDataSetChanged();

        saveInFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //new info

        if(resultCode == RESULT_OK){
            String rInfo = data.getStringExtra("newInfo");
            subInfo info = new objectSubInfo(rInfo);
            subList.add(info);

            adapter.notifyDataSetChanged();

            saveInFile();
            if(requestCode == 0){
                Toast.makeText(getApplicationContext(), "New subscription added",
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Subscription edited",
                        Toast.LENGTH_LONG).show();
            }
            totalCharge();
        }
    }

    public void totalCharge(){
        double total = 0;
        int n = subList.size();
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

