package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ActivityWithdrawDetails extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private Utils utils;

    private int Balance;

    private TextView txt_wdAmount,txt_investor,txt_date,txt_withdrawAcc,txt_balance;
    private ImageView img_investor;
    private EditText edit_sndrAccTittle,edit_sndrAccNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_details);
        getSupportActionBar().hide();

        firestore= FirebaseFirestore.getInstance();

        txt_wdAmount=findViewById(R.id.txt_wdbalance);
        txt_investor=findViewById(R.id.txt_investorName);
        txt_date=findViewById(R.id.txt_date_withdraw);
        txt_withdrawAcc=findViewById(R.id.txt_wdAccount);
        txt_balance=findViewById(R.id.txt_totalbalance);

        img_investor=findViewById(R.id.img_invProfile);

        edit_sndrAccTittle=findViewById(R.id.edit_sndrAccTittle);
        edit_sndrAccNumber=findViewById(R.id.edit_sndrAccNumber);

        Button btn_invProceed=findViewById(R.id.btn_wdProceed);
        btn_invProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_sndrAccTittle.getText().toString().isEmpty())edit_sndrAccTittle.setError("Please Enter Account Tittle");
                else if(edit_sndrAccNumber.getText().toString().isEmpty())edit_sndrAccNumber.setError("Please Enter Account Number");
                else updatestatus();
            }
        });
        Button btn_invDelete=findViewById(R.id.btn_wdDelete);
        btn_invDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDialog mDialog = new MaterialDialog.Builder(ActivityWithdrawDetails.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete!")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                deleteWdReq();
                            }
                        })
                        .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();

                // Show Dialog
                mDialog.show();

            }
        });

        getUser();
        getWithdraw();
    }

    private void deleteWdReq() {

        final lottiedialog lottie=new lottiedialog(ActivityWithdrawDetails.this);
        lottie.show();
        firestore.collection("Users").document(getIntent().getStringExtra("userId"))
                .collection("Transactions").document(getIntent().getStringExtra("userId"))
                .collection("Withdraw").document(getIntent().getStringExtra("id"))
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> map = new HashMap<>();
                            String msg="Dear "+txt_investor.getText().toString()
                                    +", your withdraw request of "+txt_wdAmount.getText().toString()
                                    +"Rs against " +txt_withdrawAcc.getText().toString()
                                    +" has been rejected by the admin for some reasons. For more details, please contact us. Thankyou!";
                            //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                            map.put("data",msg);
                            map.put("date",getdate());
                            map.put("heading","Withdraw Request");
                            map.put("status","unread");

                            firestore.collection("Users")
                                    .document(getIntent().getStringExtra("userId"))
                                    .collection("Notifications")
                                    .add(map)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            lottie.dismiss();
                                            Toast.makeText(ActivityWithdrawDetails.this, "Request Deleted!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ActivityWithdrawDetails.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lottie.dismiss();
                                    Toast.makeText(ActivityWithdrawDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lottie.dismiss();
                                    Toast.makeText(ActivityWithdrawDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            });



                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                    }
                });
    }

    private void updatestatus() {


        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Balance=Balance-Integer.parseInt(txt_wdAmount.getText().toString());



        Map<String, Object> m = new HashMap<>();
        m.put("balance", String.valueOf(Balance));

        firestore.collection("Users").document(getIntent().getStringExtra("userId")).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("status", "proceed");

                        firestore.collection("Users").document(getIntent().getStringExtra("userId"))
                                .collection("Transactions").document(getIntent().getStringExtra("userId"))
                                .collection("Withdraw").document(getIntent().getStringExtra("id")).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Map<String, Object> map = new HashMap<>();
                                        String msg="Dear "+txt_investor.getText().toString()
                                                +", your withdraw request of "+txt_wdAmount.getText().toString()
                                                +"Rs against " +txt_withdrawAcc.getText().toString()
                                                +" has been completed successfully. Amount of "
                                                +txt_wdAmount.getText().toString()+" Rs deducted to your account!";
                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                        map.put("data",msg);
                                        map.put("date",getdate());
                                        map.put("heading","Withdraw Request");
                                        map.put("status","unread");

                                        firestore.collection("Users")
                                                .document(getIntent().getStringExtra("userId"))
                                                .collection("Notifications")
                                                .add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        lottie.dismiss();
                                                        Toast.makeText(ActivityWithdrawDetails.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(ActivityWithdrawDetails.this, MainActivity.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityWithdrawDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityWithdrawDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        lottie.dismiss();
                                        Toast.makeText(ActivityWithdrawDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityWithdrawDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });




    }

    private void getUser() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();


        Balance=0;
        firestore.collection("Users").document(getIntent().getStringExtra("userId")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        Balance=Integer.parseInt(document.getString("balance"));
                        txt_balance.setText(document.getString("balance"));
                        txt_investor.setText(document.getString("fname")+" "+document.getString("lname"));
                        Glide.with(ActivityWithdrawDetails.this)
                                .load(document.getString("profile"))
                                .fitCenter()
                                .into(img_investor);
                        lottie.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActivityWithdrawDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getWithdraw() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        getIntent().getStringExtra("userId");

        firestore.collection("Users").document(getIntent().getStringExtra("userId"))
                .collection("Transactions").document(getIntent().getStringExtra("userId"))
                .collection("Withdraw").document(getIntent().getStringExtra("id")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {

                        //////////check
                        txt_wdAmount.setText(document.getString("amount"));
                        txt_date.setText(document.getString("date"));
                        txt_withdrawAcc.setText(document.getString("withdraw_account"));

                        lottie.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActivityWithdrawDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private String getdate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}