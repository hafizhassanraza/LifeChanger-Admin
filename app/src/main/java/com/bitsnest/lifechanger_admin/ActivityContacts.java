package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityContacts extends AppCompatActivity {


    private EditText edit_wNumber,edit_cNumber,edit_mail;
    private Button btn_cUpdate,btn_cClear;


    private Utils utils;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().hide();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);


        edit_wNumber=findViewById(R.id.edit_wNumber);
        edit_cNumber=findViewById(R.id.edit_cNumber);
        edit_mail=findViewById(R.id.edit_mail);

//        Toast.makeText(this, ""+getIntent().getStringExtra("adm_whatsapp"), Toast.LENGTH_SHORT).show();
        edit_wNumber.setText(getIntent().getStringExtra("adm_whatsapp").substring(3));
        edit_cNumber.setText(getIntent().getStringExtra("adm_call").substring(3));
        edit_mail.setText(getIntent().getStringExtra("adm_mail"));



        btn_cUpdate=findViewById(R.id.btn_cUpdate);
        btn_cUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IsEmptyfield())
                    updateContacts();
            }
        });

        btn_cClear=findViewById(R.id.btn_cClear);
        btn_cClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edit_wNumber.setText("");
                edit_cNumber.setText("");
                edit_mail.setText("");
            }
        });

    }

    private void updateContacts() {
        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();

        map.put("wnumber","+92"+edit_wNumber.getText().toString());
        map.put("cnumber","+92"+edit_cNumber.getText().toString());
        map.put("email",edit_mail.getText().toString());



        firestore.collection("Admin").document("ZTCzJoCKYReCLEPoUEYI").update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityContacts.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityContacts.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityContacts.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });


    }
//
//
    public boolean IsEmptyfield(){



        boolean empty=true;
        if(edit_mail.getText().toString().isEmpty())edit_mail.setError("Enter Email");
        else if(edit_wNumber.getText().toString().isEmpty())edit_wNumber.setError("Enter Whatsapp #");
        else if(edit_wNumber.getText().toString().charAt(0)!='3')edit_wNumber.setError("Incorrect Format");
        else if(edit_wNumber.getText().toString().length()!=10)edit_wNumber.setError("Incorrect Number");

        else if(edit_cNumber.getText().toString().isEmpty())edit_cNumber.setError("Enter Phone #");
        else if(edit_cNumber.getText().toString().charAt(0)!='3')edit_cNumber.setError("Incorrect Format");
        else if(edit_cNumber.getText().toString().length()!=10)edit_cNumber.setError("Incorrect Number");
        else empty=false;
        return empty;
    }
}