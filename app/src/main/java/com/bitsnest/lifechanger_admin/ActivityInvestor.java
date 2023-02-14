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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ActivityInvestor extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private Utils utils;

    private int Balance;

    private TextView txt_balanceEFU,txt_balanceSC;

    private TextView txt_nameInv,txt_balanceInv,txt_cnicInv,txt_numberInv,txt_addressInv,nName,nCnic,nPhone,nAddress;
    private EditText edit_profit,edit_notiHead,edit_notiBody,edit_updateSC,edit_updateEFU;
    private Button btn_profitHistory,btn_profitAdd,btn_sendNoti,btn_profitdeduct;
    private ImageView img_invProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor);
        getSupportActionBar().hide();
        firestore= FirebaseFirestore.getInstance();

        img_invProfile=findViewById(R.id.img_invProfile);

        txt_balanceEFU=findViewById(R.id.txt_balanceEFU);
        txt_balanceSC=findViewById(R.id.txt_balanceSC);

        txt_nameInv=findViewById(R.id.txt_nameInv);
        txt_balanceInv=findViewById(R.id.txt_balanceInv);
        txt_cnicInv=findViewById(R.id.txt_cnicInv);
        txt_numberInv=findViewById(R.id.txt_numberInv);
        txt_addressInv=findViewById(R.id.txt_addressInv);
        nName=findViewById(R.id.nName);
        nCnic=findViewById(R.id.nCnic);
        nPhone=findViewById(R.id.nPhone);
        nAddress=findViewById(R.id.nAddress);


        edit_updateSC=findViewById(R.id.edit_updateSC);
        edit_updateEFU=findViewById(R.id.edit_updateEFU);

        edit_profit=findViewById(R.id.edit_profit);
        edit_notiHead=findViewById(R.id.edit_notiHead);
        edit_notiBody=findViewById(R.id.edit_notiBody);

        btn_profitHistory=findViewById(R.id.btn_profitHistory);
        btn_profitHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ActivityProfits.class);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                startActivity(intent);


            }
        });


        btn_profitAdd=findViewById(R.id.btn_profitAdd);
        btn_profitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_profit.getText().toString().isEmpty())edit_profit.setError("Please Add Amount");
                else {

                    MaterialDialog mDialog = new MaterialDialog.Builder(ActivityInvestor.this)
                            .setTitle("Add Profit")
                            .setMessage("Are you sure want to Add!")
                            .setCancelable(false)
                            .setPositiveButton("Add", R.drawable.ic_baseline_add_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    AddProfit();
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
            }
        });
        btn_profitdeduct=findViewById(R.id.btn_profitdeduct);
        btn_profitdeduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_profit.getText().toString().isEmpty())edit_profit.setError("Please Add Amount");
                else {

                    MaterialDialog mDialog = new MaterialDialog.Builder(ActivityInvestor.this)
                            .setTitle("Deduct Tax")
                            .setMessage("Are you sure want to Deduct!")
                            .setCancelable(false)
                            .setPositiveButton("Deduct", R.drawable.ic_baseline_add_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    DeductTax();
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
            }
        });
        Button btn_updateSC=findViewById(R.id.btn_updateSC);
        btn_updateSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_updateSC.getText().toString().isEmpty())edit_updateSC.setError("Please Add Amount");
                else {


                    MaterialDialog mDialog = new MaterialDialog.Builder(ActivityInvestor.this)
                            .setTitle("Add Somecovered")
                            .setMessage("Are you sure want to Add!")
                            .setCancelable(false)
                            .setPositiveButton("Add", R.drawable.ic_baseline_add_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    AddSomeCovered();
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
            }
        });
        Button btn_updateEFU=findViewById(R.id.btn_updateEFU);
        btn_updateEFU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_updateEFU.getText().toString().isEmpty())edit_updateEFU.setError("Please Add Amount");
                else {

                    MaterialDialog mDialog = new MaterialDialog.Builder(ActivityInvestor.this)
                            .setTitle("Add EFU Takaful")
                            .setMessage("Are you sure want to Add!")
                            .setCancelable(false)
                            .setPositiveButton("Add", R.drawable.ic_baseline_add_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    AddEFU();
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
            }
        });

        btn_sendNoti=findViewById(R.id.btn_sendNoti);
        btn_sendNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_notiHead.getText().toString().isEmpty())edit_notiHead.setError("Please Enter Tittle");
                else if(edit_notiBody.getText().toString().isEmpty())edit_notiBody.setError("Please Enter Notification");
                else sendNotification();
            }
        });


        getUser();
//        getIntent().getStringExtra("userId");

    }

    private void DeductTax() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Balance=Balance-Integer.parseInt(edit_profit.getText().toString());



        Map<String, Object> m = new HashMap<>();
        m.put("balance", String.valueOf(Balance));

        firestore.collection("Users").document(getIntent().getStringExtra("userId")).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map = new HashMap<>();
                        String msg="Dear "+txt_nameInv.getText().toString()
                                +", Tax amount -"
                                +edit_profit.getText().toString()+" Rs Deduct to your account. For more information,contact us!";
                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                        map.put("data",msg);
                        map.put("date",getdate());
                        map.put("heading","Tax Deduction");
                        map.put("status","unread");

                        firestore.collection("Users")
                                .document(getIntent().getStringExtra("userId"))
                                .collection("Notifications")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Map<String, Object> map = new HashMap<>();
                                        String msg="Dear "+txt_nameInv.getText().toString()
                                                +", Tax amount -"
                                                +edit_profit.getText().toString()+" Rs Deduct to your account. For more information,contact us!";
                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                        map.put("data",msg);
                                        map.put("date",getdate());
                                        map.put("heading","Tax Deduction");
                                        map.put("status","claimed");

                                        firestore.collection("Users")
                                                .document(getIntent().getStringExtra("userId"))
                                                .collection("Profit")
                                                .add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        lottie.dismiss();
                                                        Toast.makeText(ActivityInvestor.this, "Tax deducted Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(ActivityInvestor.this, MainActivity.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });




                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lottie.dismiss();
                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lottie.dismiss();
                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void AddEFU() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();


        Map<String, Object> m = new HashMap<>();
        m.put("takaful", edit_updateEFU.getText().toString());

        firestore.collection("Users").document(getIntent().getStringExtra("userId")).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        Map<String, Object> map = new HashMap<>();
                        String msg="Dear "+txt_nameInv.getText().toString()
                                +", Amount of EFU Takaful of "
                                +edit_updateEFU.getText().toString()+" Rs added to your EFU Takaful account!";
                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                        map.put("data",msg);
                        map.put("date",getdate());
                        map.put("heading","EFU Takaful");
                        map.put("status","unread");

                        firestore.collection("Users")
                                .document(getIntent().getStringExtra("userId"))
                                .collection("Notifications")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        lottie.dismiss();
                                        Toast.makeText(ActivityInvestor.this, "Somecovered Amount Updated!", Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(ActivityInvestor.this, MainActivity.class);
                                        startActivity(myIntent);
                                        finish();

                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        lottie.dismiss();
                                        Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void AddSomeCovered() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();


        Map<String, Object> m = new HashMap<>();
        m.put("sumcovered", edit_updateSC.getText().toString());

        firestore.collection("Users").document(getIntent().getStringExtra("userId")).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        Map<String, Object> map = new HashMap<>();
                        String msg="Dear "+txt_nameInv.getText().toString()
                                +", Amount of Somecovered of "
                                +edit_updateSC.getText().toString()+" Rs added to your Somecovered account!";
                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                        map.put("data",msg);
                        map.put("date",getdate());
                        map.put("heading","Somecovered Amount");
                        map.put("status","unread");

                        firestore.collection("Users")
                                .document(getIntent().getStringExtra("userId"))
                                .collection("Notifications")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        lottie.dismiss();
                                        Toast.makeText(ActivityInvestor.this, "Somecovered Amount Updated!", Toast.LENGTH_SHORT).show();
                                        Intent myIntent = new Intent(ActivityInvestor.this, MainActivity.class);
                                        startActivity(myIntent);
                                        finish();

                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        lottie.dismiss();
                                        Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();

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

                        txt_balanceSC.setText(document.getString("sumcovered")+"Rs");
                        txt_balanceEFU.setText(document.getString("takaful")+"Rs");


                        Balance=Integer.parseInt(document.getString("balance"));
                        txt_balanceInv.setText(document.getString("balance"));
                        txt_nameInv.setText(document.getString("fname")+" "+document.getString("lname"));
                        txt_cnicInv.setText(document.getString("cnic"));
                        txt_numberInv.setText(document.getString("phone"));
                        txt_addressInv.setText(document.getString("address"));
                        Glide.with(ActivityInvestor.this)
                                .load(document.getString("profile"))
                                .fitCenter()
                                .into(img_invProfile);

                        nAddress.setText(document.getString("n_address"));
                        nCnic.setText(document.getString("n_cnic"));
                        nName.setText(document.getString("n_name"));
                        nPhone.setText(document.getString("n_phone"));

                        lottie.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void AddProfit() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Balance=Balance+Integer.parseInt(edit_profit.getText().toString());



        Map<String, Object> m = new HashMap<>();
        m.put("balance", String.valueOf(Balance));

        firestore.collection("Users").document(getIntent().getStringExtra("userId")).update(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> map = new HashMap<>();
                        String msg="Dear "+txt_nameInv.getText().toString()
                                +", Profit of "
                                +edit_profit.getText().toString()+" Rs added to your account!";
                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                        map.put("data",msg);
                        map.put("date",getdate());
                        map.put("heading","Profit Added");
                        map.put("status","unread");

                        firestore.collection("Users")
                                .document(getIntent().getStringExtra("userId"))
                                .collection("Notifications")
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Map<String, Object> map = new HashMap<>();
                                        String msg="Dear "+txt_nameInv.getText().toString()
                                                +", Profit of "
                                                +edit_profit.getText().toString()+" Rs added to your account!";
                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                        map.put("data",msg);
                                        map.put("date",getdate());
                                        map.put("heading","Profit Added");
                                        map.put("status","claimed");

                                        firestore.collection("Users")
                                                .document(getIntent().getStringExtra("userId"))
                                                .collection("Profit")
                                                .add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        lottie.dismiss();
                                                        Toast.makeText(ActivityInvestor.this, "Profit Added Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(ActivityInvestor.this, MainActivity.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lottie.dismiss();
                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lottie.dismiss();
                                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Connection Error!", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void sendNotification() {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();
          map.put("data",edit_notiHead.getText().toString());
        map.put("date",getdate());
        map.put("heading",edit_notiBody.getText().toString());
        map.put("status","unread");

        firestore.collection("Users")
                .document(getIntent().getStringExtra("userId"))
                .collection("Notifications")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        lottie.dismiss();
                        Toast.makeText(ActivityInvestor.this, "Notification Sent Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityInvestor.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lottie.dismiss();
                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lottie.dismiss();
                Toast.makeText(ActivityInvestor.this, "Connection Error", Toast.LENGTH_SHORT).show();
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