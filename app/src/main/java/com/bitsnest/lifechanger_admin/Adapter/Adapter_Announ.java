package com.bitsnest.lifechanger_admin.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger_admin.ActivitySignIn;
import com.bitsnest.lifechanger_admin.MainActivity;
import com.bitsnest.lifechanger_admin.Model.Model_Announ;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Adapter_Announ extends RecyclerView.Adapter<Adapter_Announ.ViewHolder> {
    private List<Model_Announ> model_Announ;

    Context context;
    private FirebaseFirestore firestore;
    private Utils utils;

    // RecyclerView recyclerView;
    public Adapter_Announ(Context context, List<Model_Announ> model_Announ) {
        this.context=context;
        this.model_Announ = model_Announ;
        firestore= FirebaseFirestore.getInstance();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_announcement, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Announ current_Announ = model_Announ.get(position);
        holder.txt_date.setText(model_Announ.get(position).getDate());
        holder.txt_name.setText(model_Announ.get(position).getHeading());
        holder.txt_notifi.setText(model_Announ.get(position).getData());


        holder.btn_announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                MaterialDialog mDialog = new MaterialDialog.Builder((Activity) view.getContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete!")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                DeleteAnnoun(model_Announ.get(position).getId());
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


                //Toast.makeText(view.getContext(),"click on item: "+current_Announ.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
            }
        });
    }




    private void DeleteAnnoun(String id) {



        final lottiedialog lottie=new lottiedialog(context);
        lottie.show();

        firestore.collection("Announcement").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            lottie.dismiss();
                            Toast.makeText(context, "Announcement Deleted!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);

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

    @Override
    public int getItemCount() {
        return model_Announ.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_notifi;
        public LinearLayout layout_Announ;
        public Button btn_announce;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_ann);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_head_ann);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_data_ann);
            this.btn_announce =  itemView.findViewById(R.id.btn_delete_ann);
        }

    }
}


