package com.bitsnest.lifechanger_admin.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger_admin.ActivityAnnouncement;
import com.bitsnest.lifechanger_admin.ActivityContacts;
import com.bitsnest.lifechanger_admin.ActivityDepositAccount;
import com.bitsnest.lifechanger_admin.ActivityNotification;
import com.bitsnest.lifechanger_admin.ActivitySignIn;
import com.bitsnest.lifechanger_admin.Calculator;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.bitsnest.lifechanger_admin.databinding.FragmentHomeBinding;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import static android.provider.Telephony.BaseMmsColumns.SUBJECT;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;

public class HomeFragment extends Fragment {


    private Utils utils;
    private FirebaseFirestore firestore;


    private String adm_tittle_acc1,adm_bank_acc1,adm_number_acc1,adm_tittle_acc2,adm_bank_acc2,adm_number_acc2;
    private String adm_name,adm_mail,adm_call,adm_whatsapp;

    private int nCounter=0,aCounter=0;
    //    SliderView sliderView;
//    private SliderAdapterExample adapter;

    private LinearLayout lout_logout,lout_contactUs,lout_account,lout_notifications,lout_announcement,lout_calculator;
    private TextView txt_notifiCounter,txt_anouncCounter;



    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this.getContext());





        fetchContacts();
        fetchNotifi();




        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchContacts();
                fetchNotifi();
//                newsSlider();
                pullToRefresh.setRefreshing(false);
            }
        });







        txt_notifiCounter = root.findViewById(R.id.txt_notifiCounter);

        lout_calculator = root.findViewById(R.id.lout_calculator);
        lout_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Calculator.class);
                startActivity(intent);

            }
        });
        lout_announcement = root.findViewById(R.id.lout_announcement);
        lout_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityAnnouncement.class);
                startActivity(intent);

            }
        });
        lout_notifications = root.findViewById(R.id.lout_notifications);
        lout_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityNotification.class);
                startActivity(intent);

            }
        });
        lout_logout = root.findViewById(R.id.lout_logout);
        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });
        lout_contactUs = root.findViewById(R.id.lout_contactUs);
        lout_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        lout_account = root.findViewById(R.id.lout_account);
        lout_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositAccount();
            }
        });


        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    private void depositAccount() {


        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View vie = getLayoutInflater().inflate(R.layout.bottom_sheet_account, null);


        TextView txt_adm_tittle_acc1 = vie.findViewById(R.id.adm_tittle_acc1);
        TextView txt_adm_bank_acc1 = vie.findViewById(R.id.adm_bank_acc1);
        TextView txt_adm_number_acc1 = vie.findViewById(R.id.adm_number_acc1);
        txt_adm_tittle_acc1.setText(adm_tittle_acc1);
        txt_adm_bank_acc1.setText(adm_bank_acc1);
        txt_adm_number_acc1.setText(adm_number_acc1);

        TextView txt_adm_tittle_acc2 = vie.findViewById(R.id.adm_tittle_acc2);
        TextView txt_adm_bank_acc2 = vie.findViewById(R.id.adm_bank_acc2);
        TextView txt_adm_number_acc2 = vie.findViewById(R.id.adm_number_acc2);
        txt_adm_tittle_acc2.setText(adm_tittle_acc2);
        txt_adm_bank_acc2.setText(adm_bank_acc2);
        txt_adm_number_acc2.setText(adm_number_acc2);



        LinearLayout lout_copy2 = vie.findViewById(R.id.lout_copy2);
        LinearLayout lout_copy1 = vie.findViewById(R.id.lout_copy1);


        dialog.setContentView(vie);
        dialog.setCancelable(true);
        dialog.show();

        ImageButton btn_accEdit= vie.findViewById(R.id.btn_accEdit);
        btn_accEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityDepositAccount.class);





                intent.putExtra("adm_tittle_acc1", adm_tittle_acc1);
                intent.putExtra("adm_bank_acc1", adm_bank_acc1);
                intent.putExtra("adm_number_acc1", adm_number_acc1);

                intent.putExtra("adm_tittle_acc2", adm_tittle_acc2);
                intent.putExtra("adm_bank_acc2", adm_bank_acc2);
                intent.putExtra("adm_number_acc2", adm_number_acc2);



                startActivity(intent);
                dialog.dismiss();
            }
        });
//        lout_copy1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setClipboard(getActivity().getApplicationContext(),adm_number_acc1);
//            }
//        });
//
//        lout_copy2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setClipboard(getActivity().getApplicationContext(),adm_number_acc2);
//            }
//        });


    }


    private void contactUs() {


        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View vie = getLayoutInflater().inflate(R.layout.bottam_sheet_contacts, null);


        TextView txt_admin_name = vie.findViewById(R.id.txt_admin_name);
        txt_admin_name.setText(adm_name);
        TextView cwNumber = vie.findViewById(R.id.cwNumber);
        cwNumber.setText(adm_whatsapp);
        TextView cpNumber = vie.findViewById(R.id.cpNumber);
        cpNumber.setText(adm_call);
        TextView cmAddress = vie.findViewById(R.id.cmAddress);
        cmAddress.setText(adm_mail);
        ImageButton btn_cEdit = vie.findViewById(R.id.btn_cEdit);


        dialog.setContentView(vie);
        dialog.setCancelable(true);
        dialog.show();


        btn_cEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ActivityContacts.class);


                intent.putExtra("adm_name", adm_name);
                intent.putExtra("adm_whatsapp", adm_whatsapp);
                intent.putExtra("adm_call", adm_call);
                intent.putExtra("adm_mail", adm_mail);

                startActivity(intent);

                dialog.dismiss();
            }
        });


    }

    private void fetchContacts() {

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();
        firestore.collection("Admin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){
                                adm_name=document.getString("fname")+" "+document.getString("lname");
                                adm_mail=document.getString("email");
                                adm_call=document.getString("cnumber");
                                adm_whatsapp=document.getString("wnumber");


                                adm_tittle_acc1=document.getString("tittle_acc1");
                                adm_bank_acc1=document.getString("bank_acc1");
                                adm_number_acc1=document.getString("number_acc1");

                                adm_tittle_acc2=document.getString("tittle_acc2");
                                adm_bank_acc2=document.getString("bank_acc2");
                                adm_number_acc2=document.getString("number_acc2");

                            }
                            lottie.dismiss();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void fetchNotifi(){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collectionGroup("NotificationsAdmin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            nCounter=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unread")){
                                    nCounter++;
                                }
                            }
                            if(nCounter>9) txt_notifiCounter.setText("9+");
                            else txt_notifiCounter.setText(String.valueOf(nCounter));

                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }



    public void logout(){
        MaterialDialog mDialog = new MaterialDialog.Builder(this.getActivity())
                .setTitle("Logout")
                .setMessage("Are you sure want to logout!")
                .setCancelable(false)
                .setPositiveButton("Logout", R.drawable.ic_baseline_exit_to_app_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
//                        utils.logout();
//                        startActivity(new Intent(getContext(), ActivitySignIn.class));
//                        getActivity().finish();
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






