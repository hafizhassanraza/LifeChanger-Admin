package com.bitsnest.lifechanger_admin.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger_admin.ActivityInvestment;
import com.bitsnest.lifechanger_admin.ActivityNotification;
import com.bitsnest.lifechanger_admin.ActivityTransaction;
import com.bitsnest.lifechanger_admin.ActivityWithdraw;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.bitsnest.lifechanger_admin.databinding.FragmentDashboardBinding;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DashboardFragment extends Fragment {

    private Utils utils;
    private FirebaseFirestore firestore;

    private Button btn_wReq,btn_iReq,btn_transactions;
    private int balance,icounter,wcounter;

    private TextView txt_balance;

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this.getContext());

        txt_balance=root.findViewById(R.id.txt_balance);
        btn_iReq=root.findViewById(R.id.btn_iReq);
        btn_wReq=root.findViewById(R.id.btn_wReq);
        btn_transactions=root.findViewById(R.id.btn_transactions);

        fetchBalance();
        fetchInvestment();
        fetchWithdraw();

        btn_iReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityInvestment.class);
                startActivity(intent);
            }
        });
        btn_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityTransaction.class);
                startActivity(intent);
            }
        });
        btn_wReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityWithdraw.class);
                startActivity(intent);

            }
        });

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchBalance();
                fetchInvestment();
                fetchWithdraw();
                pullToRefresh.setRefreshing(false);
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchBalance(){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collectionGroup("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            balance=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                balance=balance+Integer.parseInt(document.getString("balance"));
                            }


                            txt_balance.setText(String.valueOf(balance));
                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void fetchInvestment(){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collectionGroup("Invest").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            icounter=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("status").equals("pending"))
                                    icounter++;
                            }

                            btn_iReq.setText("Requests ("+icounter+")");
                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void fetchWithdraw(){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collectionGroup("Withdraw").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            wcounter=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("status").equals("pending"))
                                    wcounter++;
                            }

                            btn_wReq.setText("Requests ("+wcounter+")");
                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

}