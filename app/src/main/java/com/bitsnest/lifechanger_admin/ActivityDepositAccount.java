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

public class ActivityDepositAccount extends AppCompatActivity {

    private EditText edit_accBank,edit_accTittle,edit_accNumber,edit_accBank_,edit_accTittle_,edit_accNumber_;
    private Button btn_updateAcc,btn_clearAcc,btn_updateAcc_,btn_clearAcc_;


    private Utils utils;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_account);
        getSupportActionBar().hide();

        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        edit_accBank=findViewById(R.id.edit_accBank);
        edit_accTittle=findViewById(R.id.edit_accTittle);
        edit_accNumber=findViewById(R.id.edit_accNumber);

        edit_accBank_=findViewById(R.id.edit_accBank_);
        edit_accTittle_=findViewById(R.id.edit_accTittle_);
        edit_accNumber_=findViewById(R.id.edit_accNumber_);


        edit_accBank.setText(getIntent().getStringExtra("adm_bank_acc1"));
        edit_accTittle.setText(getIntent().getStringExtra("adm_tittle_acc1"));
        edit_accNumber.setText(getIntent().getStringExtra("adm_number_acc1"));

        edit_accBank_.setText(getIntent().getStringExtra("adm_bank_acc2"));
        edit_accTittle_.setText(getIntent().getStringExtra("adm_tittle_acc2"));
        edit_accNumber_.setText(getIntent().getStringExtra("adm_number_acc2"));



        btn_updateAcc=findViewById(R.id.btn_updateAcc);
        btn_updateAcc_=findViewById(R.id.btn_updateAcc_);

        btn_clearAcc=findViewById(R.id.btn_clearAcc);
        btn_clearAcc_=findViewById(R.id.btn_clearAcc_);

        btn_clearAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_accBank.setText(getIntent().getStringExtra(""));
                edit_accTittle.setText(getIntent().getStringExtra(""));
                edit_accNumber.setText(getIntent().getStringExtra(""));
            }
        });
        btn_clearAcc_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_accBank.setText(getIntent().getStringExtra(""));
                edit_accTittle.setText(getIntent().getStringExtra(""));
                edit_accNumber.setText(getIntent().getStringExtra(""));
            }
        });
        btn_updateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfield()){
                    updateAcc();
                }
            }
        });
        btn_updateAcc_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfield_()){
                    updateAcc_();
                }
            }
        });


    }

    private void updateAcc() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();

        map.put("number_acc1",edit_accNumber.getText().toString());
        map.put("tittle_acc1",edit_accTittle.getText().toString());
        map.put("bank_acc1",edit_accBank.getText().toString());



        firestore.collection("Admin").document(utils.getToken()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityDepositAccount.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityDepositAccount.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityDepositAccount.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateAcc_() {
        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();

        map.put("number_acc2",edit_accNumber_.getText().toString());
        map.put("tittle_acc2",edit_accTittle_.getText().toString());
        map.put("bank_acc2",edit_accBank_.getText().toString());



        firestore.collection("Admin").document("ZTCzJoCKYReCLEPoUEYI").update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityDepositAccount.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityDepositAccount.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityDepositAccount.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public boolean IsEmptyfield(){

        boolean empty=true;
        if(edit_accBank.getText().toString().isEmpty())edit_accBank.setError("Enter Bank Name");
        else if(edit_accTittle.getText().toString().isEmpty())edit_accTittle.setError("Enter Account Tittle");
        else if(edit_accNumber.getText().toString().isEmpty())edit_accNumber.setError("Enter Account Number");
        else empty=false;
        return empty;
    }
    public boolean IsEmptyfield_(){

        boolean empty=true;
        if(edit_accBank_.getText().toString().isEmpty())edit_accBank_.setError("Enter Bank Name");
        else if(edit_accTittle_.getText().toString().isEmpty())edit_accTittle_.setError("Enter Account Tittle");
        else if(edit_accNumber_.getText().toString().isEmpty())edit_accNumber_.setError("Enter Account Number");
        else empty=false;
        return empty;
    }

}