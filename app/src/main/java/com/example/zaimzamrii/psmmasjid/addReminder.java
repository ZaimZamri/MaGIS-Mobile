package com.example.zaimzamrii.psmmasjid;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zaimzamrii.psmmasjid.sqlite.NotesDBModel;
import com.example.zaimzamrii.psmmasjid.sqlite.noteDB;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class addReminder extends AppCompatActivity {

    Button btnSave;
    ActionBar toolbar;
    EditText tfTitle,tfDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        btnSave = findViewById(R.id.btnSave);
        tfTitle = findViewById(R.id.tfTitle);
        tfDesc = findViewById(R.id.tfDetails);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Add Notes");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fnSave();
                finish();
            }
        });
    }


    public void fnSave (){

        try{

            final NotesDBModel jb = new NotesDBModel(tfTitle.getText().toString(),tfDesc.getText().toString());
            noteDB db = new noteDB(getApplicationContext());
            db.fnInsertExpenses(jb);
            Toast.makeText(getApplicationContext(),"Note Save!",Toast.LENGTH_SHORT).show();



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
