package com.example.databaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //buttonsLayout
    private Button buttonsLayoutBtnAddNewRecord;
    private Button buttonsLayoutBtnUpdateRecord;
    private Button buttonsLayoutBtnDeleteRecord;

    //saveRecordLayout
    private Button saveRecordLayoutBtnSaveRecord;
    private EditText saveRecordLayoutEdtName;
    private EditText saveRecordLayoutEdtAddress;


    //updateRecordLayout
    private Button updateRecordLayoutBtnUpdateRecord;
    private EditText updateRecordLayoutEdtName;
    private EditText updateRecordLayoutEdtAddress;

    private ConstraintLayout welcomeLayout;
    private ConstraintLayout saveRecordLayout;
    private ConstraintLayout updateRecordLayout;
    private ConstraintLayout deleteRecordLayout;

    public DatabaseManager mDatabaseManager;
    public String personName;
    public  String personAddress;

    public ListView allrecordsListView;
    public TextView listviewTxtCloseConnection;

    //deleteRecordLayout
    public TextView deleteRecordLayoutTxt;
    public Button deleteRecordsLayoutBtnDelete;


        // public ArrayList<String> records;
  //  public ArrayAdapter adapter;

    public ArrayList<HashMap<String, String>> records;

    public SimpleAdapter sAdapter;

    //public HashMap<String, String> recordData;

    int[] to = new int[]{android.R.id.text1, android.R.id.text2};

    public String getId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buttonsLayout components init
        buttonsLayoutBtnAddNewRecord = findViewById(R.id.buttonsLayout_btnAddNewRecord);
        buttonsLayoutBtnUpdateRecord = findViewById(R.id.buttonsLayout_btnUpdateRecord);
        buttonsLayoutBtnDeleteRecord = findViewById(R.id.buttonsLayout_btnDeleteRecord);

        //layouts init
        welcomeLayout = findViewById(R.id.welcomeLayout);
        saveRecordLayout = findViewById(R.id.saveRecordLayout);
        updateRecordLayout = findViewById(R.id.updateRecordLayout);
        deleteRecordLayout = findViewById(R.id.deleteRecordLayout);

        //saveRecordLayout
        saveRecordLayoutEdtName = findViewById(R.id.saveRecordLayout_edtName);
        saveRecordLayoutEdtAddress = findViewById(R.id.saveRecordLayout_edtAddress);
        saveRecordLayoutBtnSaveRecord = findViewById(R.id.saveRecordLayout_btnSaveRecord);

        //updateRecordLayout
        updateRecordLayoutBtnUpdateRecord = findViewById(R.id.updateRecordLayout_btnUpdateRecord);
        updateRecordLayoutEdtAddress = findViewById(R.id.updateRecordLayout_edtAddress);
        updateRecordLayoutEdtName = findViewById(R.id.updateRecordLayout_edtName);

        //deleteRecordLayout
        deleteRecordLayoutTxt = findViewById(R.id.deleteRecordLayout_txt);
        deleteRecordsLayoutBtnDelete = findViewById(R.id.deleteRecordLayout_btnDelete);



        //listeners
        buttonsLayoutBtnAddNewRecord.setOnClickListener(this);
        buttonsLayoutBtnUpdateRecord.setOnClickListener(this);
        buttonsLayoutBtnDeleteRecord.setOnClickListener(this);

        allrecordsListView = findViewById(R.id.allRecordsListView);
        listviewTxtCloseConnection = findViewById(R.id.listviewTxtCloseConnection);

        mDatabaseManager = new DatabaseManager(MainActivity.this);

        records = new ArrayList<>();

        //recordData = new HashMap<>();

        sAdapter = new SimpleAdapter(MainActivity.this,records,android.R.layout.simple_list_item_2,new String[]{"recordName", "recordAddress"},to);


        allrecordsListView.setAdapter(sAdapter);

        viewData();

        saveRecordLayoutBtnSaveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personName = saveRecordLayoutEdtName.getText().toString();
                personAddress = saveRecordLayoutEdtAddress.getText().toString();

                if(mDatabaseManager.addNewRecord(personName,personAddress)){
                    FancyToast.makeText(MainActivity.this,"Record saved successfully !", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                    records.clear();
                    viewData();

                   // mDatabaseManager.close();
                   // listviewTxtCloseConnection.setVisibility(View.VISIBLE);
                   // allrecordsListView.setVisibility(View.GONE);

                    closeConncection();

                } else {
                    FancyToast.makeText(MainActivity.this,"Error: Record not saved !!!", Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }


            }
        });

        allrecordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String recordId = records.get(position).get("recordId").toString();
                String recordName = records.get(position).get("recordName").toString();
                String recordAddress = records.get(position).get("recordAddress").toString();

                getId = recordId;


                updateRecordLayoutEdtName.setText(recordName);
                updateRecordLayoutEdtAddress.setText(recordAddress);

                deleteRecordLayoutTxt.setText(recordName);

            }
        });




        updateRecordLayoutBtnUpdateRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = updateRecordLayoutEdtName.getText().toString();
                String address = updateRecordLayoutEdtAddress.getText().toString();

                int id = Integer.parseInt(getId);

                if(mDatabaseManager.updateRecord(id,name,address)){
                    FancyToast.makeText(MainActivity.this,"Record updated successfully !", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                    records.clear();
                    viewData();

                    closeConncection();

                } else {
                    FancyToast.makeText(MainActivity.this,"Error: Record not updated !!!", Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }




            }
        });

        deleteRecordsLayoutBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt(getId);

                if(mDatabaseManager.deleteRecord(id)){
                    FancyToast.makeText(MainActivity.this,"Record deleted successfully !", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    records.clear();
                    viewData();
                    deleteRecordLayoutTxt.setText("");
                    closeConncection();
                } else {
                    FancyToast.makeText(MainActivity.this,"Error: Record not deleted !!!", Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }

            }
        });


    }

    private void viewData() {

        Cursor cursor = mDatabaseManager.getAllRecords();



        if(cursor.getCount() == 0){
            FancyToast.makeText(MainActivity.this,"Sorry no data to show",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
        } else {

            while (cursor.moveToNext()){


                HashMap<String, String> recordData = new HashMap<>();
                recordData.put("recordId",cursor.getString(0));
                recordData.put("recordName",cursor.getString(2));
                recordData.put("recordAddress",cursor.getString(1));
               // Log.d("TAG****************","Name: "+ recordData.get("recordName"));
               // Log.d("TAG****************","Address: "+ recordData.get("recordAddress"));
               // Log.d("THISISTAG","@@@@@@@@@@@@@@@@@@@" + recordData);
                records.add(recordData);
                //Log.d("THISISTAG","++++++++++++++++++" + records.add(recordData));
                //sAdapter.notifyDataSetChanged();
            }

            sAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonsLayout_btnAddNewRecord:
                welcomeLayout.setVisibility(View.GONE);
                updateRecordLayout.setVisibility(View.GONE);
                deleteRecordLayout.setVisibility(View.GONE);
                saveRecordLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonsLayout_btnUpdateRecord:
                welcomeLayout.setVisibility(View.GONE);
                deleteRecordLayout.setVisibility(View.GONE);
                saveRecordLayout.setVisibility(View.GONE);
                updateRecordLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonsLayout_btnDeleteRecord:
                welcomeLayout.setVisibility(View.GONE);
                saveRecordLayout.setVisibility(View.GONE);
                updateRecordLayout.setVisibility(View.GONE);
                deleteRecordLayout.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void closeConncection(){
         mDatabaseManager.close();
       //  listviewTxtCloseConnection.setVisibility(View.VISIBLE);
        // allrecordsListView.setVisibility(View.GONE);
    }




}
