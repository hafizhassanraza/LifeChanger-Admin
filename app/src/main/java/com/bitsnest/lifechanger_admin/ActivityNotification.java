package com.bitsnest.lifechanger_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsnest.lifechanger_admin.Adapter.Adapter_Notify;
import com.bitsnest.lifechanger_admin.Adapter.Adapter_unNotify;
import com.bitsnest.lifechanger_admin.Model.Model_Notify;
import com.bitsnest.lifechanger_admin.Model.Model_unNotify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityNotification extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;

    List<Model_unNotify> list_unNotify = new ArrayList<>();
    List<Model_Notify> list_Notify = new ArrayList<>();
    RecyclerView recy_unNotify ;
    RecyclerView recy_Notify ;

    private TextView txt_balanceSC,txt_balanceEFU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().hide();

        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        txt_balanceSC=findViewById(R.id.txt_balanceSC);
        txt_balanceEFU=findViewById(R.id.txt_balanceEFU);

        recy_unNotify = findViewById(R.id.list_UnNotify);
        recy_unNotify.setHasFixedSize(true);
        recy_unNotify.setLayoutManager(new LinearLayoutManager(this));

        recy_Notify = findViewById(R.id.list_Notify);
        recy_Notify.setHasFixedSize(true);
        recy_Notify.setLayoutManager(new LinearLayoutManager(this));

        fetchNotifi(utils.getToken());
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifi(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });




    }

    private void fetchNotifi(String token){

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        list_Notify.clear();
        list_unNotify.clear();



        firestore.collection("Admin").document(utils.getToken())
                .collection("NotificationsAdmin").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unread")){
                                    Model_unNotify model_unNotify =  new Model_unNotify(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_unNotify.add(model_unNotify);

                                }
                                if(document.getString("status").equals("read")){


                                    Model_Notify model_Notify =  new Model_Notify(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_Notify.add(model_Notify);
                                }
                            }


                            Adapter_unNotify adapter_unNotify = new Adapter_unNotify(ActivityNotification.this,list_unNotify);
                            recy_unNotify.setAdapter(adapter_unNotify);

                            Adapter_Notify adapter_Notify = new Adapter_Notify(list_Notify);
                            recy_Notify.setAdapter(adapter_Notify);
                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityNotification.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//
//        firestore.collection("Admin").document()// whereEqualTo("studentID",token)
//                .collection("NotificationsAdmin").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                Toast.makeText(ActivityNotification.this, ""+document.getString("status"), Toast.LENGTH_SHORT).show();
//                                Model_unNotify model_unNotify =  new Model_unNotify(
//                                        document.getId(),
//                                        document.getString("data"),
//                                        document.getString("date"),
//                                        document.getString("heading"));
//                                list_unNotify.add(model_unNotify);
//
////                                if(document.getString("status").equals("unread")){
////                                    Model_unNotify model_unNotify =  new Model_unNotify(
////                                            document.getId(),
////                                            document.getString("data"),
////                                            document.getString("date"),
////                                            document.getString("heading"));
////                                    list_unNotify.add(model_unNotify);
////                                }
////                                if(document.getString("status").equals("read")){
////                                    Model_Notify model_Notify =  new Model_Notify(
////                                            document.getId(),
////                                            document.getString("data"),
////                                            document.getString("date"),
////                                            document.getString("heading"));
////                                    list_Notify.add(model_Notify);
////                                }
//
//                            }
//                            Adapter_unNotify adapter_unNotify = new Adapter_unNotify(ActivityNotification.this,list_unNotify);
//                            recy_unNotify.setAdapter(adapter_unNotify);
//
////                            Adapter_Notify adapter_Notify = new Adapter_Notify(list_Notify);
////                            recy_Notify.setAdapter(adapter_Notify);
//
//                            lottie.dismiss();
//                        }
//                        else {
//                            lottie.dismiss();
//                            Toast.makeText(ActivityNotification.this, "failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });


    }

}