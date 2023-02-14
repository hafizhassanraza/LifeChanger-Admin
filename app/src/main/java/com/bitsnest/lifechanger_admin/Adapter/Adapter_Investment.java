package com.bitsnest.lifechanger_admin.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger_admin.ActvityInvDetails;
import com.bitsnest.lifechanger_admin.Model.Model_Investment;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.MainActivity;
import com.bitsnest.lifechanger_admin.Model.Model_Investment;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Adapter_Investment extends RecyclerView.Adapter<Adapter_Investment.ViewHolder> {
    private List<Model_Investment> model_Investment;

    Context context;

    private FirebaseFirestore firestore;
    private Utils utils;

    // RecyclerView recyclerView;
    public Adapter_Investment(Context context, List<Model_Investment> model_Investment) {
        this.context = context;

        this.model_Investment = model_Investment;
        firestore= FirebaseFirestore.getInstance();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_investment, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Investment current_Investment = model_Investment.get(position);



        holder.txt_date.setText(model_Investment.get(position).getDate());
        holder.txt_amount.setText(model_Investment.get(position).getAmount()+" Rs");
        holder.txt_AcDeposit.setText(model_Investment.get(position).getAc_deposit());



        holder.btn_iDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog mDialog = new MaterialDialog.Builder((Activity) v.getContext())
                                .setTitle("Delete")
                        .setMessage("Are you sure want to Delete!")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.ic_baseline_delete_24, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                DeleteReq(model_Investment.get(position).getUserID(),
                                        model_Investment.get(position).getId());
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
        holder.btn_iProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), ActvityInvDetails.class);
                intent.putExtra("id",model_Investment.get(position).getId());
                intent.putExtra("userId", model_Investment.get(position).getUserID());
                v.getContext().startActivity(intent);
            }
        });

    }

    private void DeleteReq(String userId,String invID) {


        final lottiedialog lottie=new lottiedialog(context);
        lottie.show();
        firestore.collection("Users").document(userId)
                .collection("Transactions").document(userId)
                .collection("Invest").document(invID)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(context, "Request Deleted!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.getApplicationContext().startActivity(intent);
                            lottie.dismiss();
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
        return model_Investment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_amount,txt_AcDeposit;
        public ImageView img_receipt;
        public Button btn_iProceed,btn_iDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_invest);
            this.txt_amount = (TextView) itemView.findViewById(R.id.txt_amount_invest);
            this.txt_AcDeposit = (TextView) itemView.findViewById(R.id.txt_depositAccount_investment);
            this.btn_iProceed = (Button) itemView.findViewById(R.id.btn_iProceed);
            this.btn_iDelete = (Button) itemView.findViewById(R.id.btn_iDelete);

        }

    }
}


