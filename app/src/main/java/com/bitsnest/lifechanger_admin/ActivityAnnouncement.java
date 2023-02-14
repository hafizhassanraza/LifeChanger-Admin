package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitsnest.lifechanger_admin.Adapter.Adapter_Announ;
import com.bitsnest.lifechanger_admin.Model.Model_Announ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAnnouncement extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private Utils utils;


    List<Model_Announ> list_Announ = new ArrayList<>();

    RecyclerView recy_Announ ;


    private EditText edt_head,edt_announ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        getSupportActionBar().hide();


        firestore= FirebaseFirestore.getInstance();
        recy_Announ = findViewById(R.id.list_Announ);
        recy_Announ.setHasFixedSize(true);
        recy_Announ.setLayoutManager(new LinearLayoutManager(this));
        edt_announ=findViewById(R.id.edit_annBody);
        edt_head=findViewById(R.id.edit_annHead);


        Button btn_announ=findViewById(R.id.btn_announcement);
        btn_announ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IsEmptyfield())
                    addAnnouncement();
            }
        });

        fetchAnnouncement();


        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAnnouncement();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void addAnnouncement() {



        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();


        Map<String, Object> map = new HashMap<>();
        map.put("data",edt_announ.getText().toString());
        map.put("status","unread");
        map.put("date",getDate());
        map.put("heading",edt_head.getText().toString());


        firestore.collection("Announcement").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        lottie.dismiss();
                        Toast.makeText(ActivityAnnouncement.this, "Announcement created Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityAnnouncement.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityAnnouncement.this, "Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void fetchAnnouncement(){

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        list_Announ.clear();

        firestore.collection("Announcement").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Model_Announ model_Announ =  new Model_Announ(
                                        document.getId(),
                                        document.getString("data"),
                                        document.getString("date"),
                                        document.getString("heading"));
                                list_Announ.add(model_Announ);

                            }

                            Adapter_Announ adapter_Announ = new Adapter_Announ(ActivityAnnouncement.this,list_Announ);
                            recy_Announ.setAdapter(adapter_Announ);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityAnnouncement.this, "something wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public boolean IsEmptyfield(){
        boolean empty=true;
        if(edt_head.getText().toString().isEmpty())edt_head.setError("Enter Tittle");
        else if(edt_announ.getText().toString().isEmpty())edt_announ.setError("Enter Announcement");
        else empty=false;
        return empty;
    }

    private String getDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}