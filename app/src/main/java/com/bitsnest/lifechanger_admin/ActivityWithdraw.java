package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.bitsnest.lifechanger_admin.Adapter.Adapter_Withdraw;
import com.bitsnest.lifechanger_admin.Model.Model_Withdraw;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithdraw extends AppCompatActivity {



    private FirebaseFirestore firestore;
    private Utils utils;


    private List<Model_Withdraw> list_Withdraw ;
    RecyclerView recy_Withdraw ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        getSupportActionBar().hide();
        
        
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);



        list_Withdraw= new ArrayList<Model_Withdraw>();
        recy_Withdraw = findViewById(R.id.list_tWithdraw);
        recy_Withdraw.setHasFixedSize(true);
        recy_Withdraw.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    private void fetchData(){


        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        list_Withdraw.clear();

        firestore.collectionGroup("Withdraw").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("pending")){
                                    Model_Withdraw model_Withdraw =  new Model_Withdraw(
                                            document.getString("userID"),
                                            document.getId(),
                                            document.getString("amount"),
                                            document.getString("date"),
                                            document.getString("withdraw_account"));
                                    list_Withdraw.add(model_Withdraw);
                                }

                            }


                            Adapter_Withdraw adapter_Withdraw = new Adapter_Withdraw(ActivityWithdraw.this,list_Withdraw);
                            recy_Withdraw.setAdapter(adapter_Withdraw);

                            lottie.dismiss();


                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ActivityWithdraw.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                });




    }

    


}