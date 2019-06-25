package com.example.zaimzamrii.psmmasjid.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zaimzamrii.psmmasjid.R;

public class editReminder extends AppCompatActivity {
    int id;
    EditText tftitle,tfDesc;
    Button btnUpdate,btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        tftitle = findViewById(R.id.tfTitle);
        tfDesc = findViewById(R.id.tfDetails);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        String tempid = intent.getStringExtra("id");
        id =0;
        if (tempid == null){

        }else{
            id = Integer.parseInt(tempid);
        }

        if (id> 0){
            noteDB obj = new noteDB(getApplicationContext());
            NotesDBModel ob = new NotesDBModel();
            ob =  obj.fnGetExpenses(id);

            tftitle.setText(ob.getTitle());
            tfDesc.setText(ob.getDesc());

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fnUpdate(view);


                    Toast.makeText(getApplicationContext(),"Update Successfully",Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fnDelete(view);
                    Toast.makeText(getApplicationContext(),"Delete Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    public void fnUpdate(final View view){
        final NotesDBModel bean = new NotesDBModel(tftitle.getText().toString(),tfDesc.getText().toString());
        noteDB db = new noteDB(getApplicationContext());
        bean.setId(String.valueOf(id));
        db.fnUpdateExpenses(bean);
    }

    public void fnDelete(final View view){
        noteDB db = new noteDB(getApplicationContext());
        db.fnDeleteExpenses(id);
    }
}
