package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.bitsnest.lifechanger_admin.Adapter.Adapter_Claimed;
import com.bitsnest.lifechanger_admin.Model.Model_Claimed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityProfits extends AppCompatActivity {

    private FirebaseFirestore firestore;

    List<Model_Claimed> list_Claimed = new ArrayList<>();
    RecyclerView recy_Claimed ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profits);
        getSupportActionBar().hide();
        firestore= FirebaseFirestore.getInstance();

        recy_Claimed = findViewById(R.id.list_Claimed);
        recy_Claimed.setHasFixedSize(true);
        recy_Claimed.setLayoutManager(new LinearLayoutManager(this));


        fetchNotifi(getIntent().getStringExtra("userId"));
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifi(getIntent().getStringExtra("userId"));
                pullToRefresh.setRefreshing(false);
            }
        });
    }
    private void fetchNotifi(String token){

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        list_Claimed.clear();

        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Profit").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {



                                if(document.getString("status").equals("claimed")){
                                    Model_Claimed model_Claimed =  new Model_Claimed(
                                            document.getId(),
                                            document.getString("heading"),
                                            document.getString("data"),
                                            document.getString("date"));
                                    list_Claimed.add(model_Claimed);
                                }

                            }


                            Adapter_Claimed adapter_Claimed = new Adapter_Claimed(list_Claimed);
                            recy_Claimed.setAdapter(adapter_Claimed);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityProfits.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}