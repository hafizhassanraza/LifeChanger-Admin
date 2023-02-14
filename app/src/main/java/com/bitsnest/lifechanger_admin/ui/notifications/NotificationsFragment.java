package com.bitsnest.lifechanger_admin.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.utils.Utils;
import com.bitsnest.lifechanger_admin.Adapter.Adapter_Investor;
import com.bitsnest.lifechanger_admin.Model.Model_Investor;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.databinding.FragmentNotificationsBinding;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {



    private FirebaseFirestore firestore;
    private Utils utils;

    private List<Model_Investor> list_Investor ;
    RecyclerView recy_Investor ;


    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore= FirebaseFirestore.getInstance();



        list_Investor= new ArrayList<Model_Investor>();
        recy_Investor = root.findViewById(R.id.rec_investor);
        recy_Investor.setHasFixedSize(true);
        recy_Investor.setLayoutManager(new LinearLayoutManager(getContext()));


        //final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchInvestor();
                pullToRefresh.setRefreshing(false);
            }
        });

        fetchInvestor();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchInvestor(){


        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();
        list_Investor.clear();

        firestore.collection("Users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Model_Investor model_Investor =  new Model_Investor(
                                        document.getId(),
                                        document.getString("profile"),
                                        document.getString("fname")+" "+document.getString("lname"),
                                        document.getString("cnic"),
                                        document.getString("phone"));
                                list_Investor.add(model_Investor);

                            }


                            Adapter_Investor adapter_Investor = new Adapter_Investor(getContext(),list_Investor);
                                recy_Investor.setAdapter(adapter_Investor);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}