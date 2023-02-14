package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsnest.lifechanger_admin.Adapter.Adapter_Announ;
import com.bitsnest.lifechanger_admin.Adapter.Adapter_Investor;
import com.bitsnest.lifechanger_admin.Model.Model_Announ;
import com.bitsnest.lifechanger_admin.Model.Model_Investor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ActvityInvDetails extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;

    private int Balance;

    private TextView txt_invAmount,txt_investor,txt_date,txt_depositAcc;
    private ImageView img_investor,img_receipt;
    private EditText edit_invAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actvity_inv_details);
        getSupportActionBar().hide();
        firestore= FirebaseFirestore.getInstance();

        txt_invAmount=findViewById(R.id.txt_invbalance);
        txt_investor=findViewById(R.id.txt_investorName);
        txt_date=findViewById(R.id.txt_date_invest);
        txt_depositAcc=findViewById(R.id.txt_depositAccount_investment);

        img_investor=findViewById(R.id.img_invProfile);
        img_receipt=findViewById(R.id.img_invReceipt);

        edit_invAmount=findViewById(R.id.edit_invAmount);

        Button btn_invProceed=findViewById(R.id.btn_invProceed);
        btn_invProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_invAmount.getText().toString().isEmpty())edit_invAmount.setError("Please Enter Amount First");
                else updatestatus();
            }
        });
        Button btn_invDelete=findViewById(R.id.btn_invDelete);
        btn_invDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDialog mDialog = new MaterialDialog.Builder(ActvityInvDetails.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete!")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                deleteInv();
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
        getInvestment();


    }

    private void deleteInv() {

        final lottiedialog lottie=new lottiedialog(ActvityInvDetails.this);
        lottie.show();
        firestore.collection("Users").document(getIntent().getStringExtra("userId"))
                .collection("Transactions").document(getIntent().getStringExtra("userId"))
                .collection("Invest").document(getIntent().getStringExtra("id"))
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> map = new HashMap<>();
                            String msg="Dear "+txt_investor.getText().toString()
                                    +", your deposit request of "+txt_invAmount.getText().toString()
                                    +"Rs against " +txt_depositAcc.getText().toString()
                                    +" has been rejected by the admin for some reasons. For more details, please contact us. Thankyou!";
                            //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                            map.put("data",msg);
                            map.put("date",getdate());
                            map.put("heading","Investment Request");
                            map.put("status","unread");

                            firestore.collection("Users")
                                    .document(getIntent().getStringExtra("userId"))
                                    .collection("Notifications")
                                    .add(map)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            lottie.dismiss();
                                            Toast.makeText(ActvityInvDetails.this, "Request Deleted!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ActvityInvDetails.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lottie.dismiss();
                                    Toast.makeText(ActvityInvDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lottie.dismiss();
                                    Toast.makeText(ActvityInvDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

        Balance=Balance+Integer.parseInt(edit_invAmount.getText().toString());



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
                                .collection("Invest").document(getIntent().getStringExtra("id")).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Map<String, Object> map = new HashMap<>();
                                        String msg="Dear "+txt_investor.getText().toString()
                                                +", your deposit request of "+txt_invAmount.getText().toString()
                                                +"Rs against " +txt_depositAcc.getText().toString()
                                                +" has been completed successfully. Amount of "
                                                +edit_invAmount.getText().toString()+" Rs added to your account!";
                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                        map.put("data",msg);
                                        map.put("date",getdate());
                                        map.put("heading","Investment Request");
                                        map.put("status","unread");

                                        firestore.collection("Users")
                                                .document(getIntent().getStringExtra("userId"))
                                                .collection("Notifications")
                                                .add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        lottie.dismiss();
                                                        Toast.makeText(ActvityInvDetails.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(ActvityInvDetails.this, MainActivity.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActvityInvDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActvityInvDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        lottie.dismiss();
                                        Toast.makeText(ActvityInvDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActvityInvDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

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
                        txt_investor.setText(document.getString("fname")+" "+document.getString("lname"));
                        Glide.with(ActvityInvDetails.this)
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
                        Toast.makeText(ActvityInvDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getInvestment() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        getIntent().getStringExtra("userId");

        firestore.collection("Users").document(getIntent().getStringExtra("userId"))
                .collection("Transactions").document(getIntent().getStringExtra("userId"))
                .collection("Invest").document(getIntent().getStringExtra("id")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {

                        txt_invAmount.setText(document.getString("amount"));
                        txt_date.setText(document.getString("date"));
                        txt_depositAcc.setText(document.getString("deposit_account"));
                        Glide.with(ActvityInvDetails.this)
                                .load(document.getString("receipt"))
                                .fitCenter()
                                .into(img_receipt);
                        lottie.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActvityInvDetails.this, "Connection Error!", Toast.LENGTH_SHORT).show();

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