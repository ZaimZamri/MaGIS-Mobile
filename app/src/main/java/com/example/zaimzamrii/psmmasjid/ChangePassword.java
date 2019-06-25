package com.example.zaimzamrii.psmmasjid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private ActionBar toolbar;
    EditText tfEmail;
    EditText tfPass,tfNewPass;
    Button btnUpdate;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        toolbar = getSupportActionBar();
        toolbar.setTitle("Change Password");

        tfEmail = (EditText) findViewById(R.id.editText3);
        tfPass = (EditText) findViewById(R.id.editText);
        tfNewPass = (EditText) findViewById(R.id.editText2);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.updatePassword(tfNewPass.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePassword.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChangePassword.this, "Failed to update password!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });



    }
}
