package com.bitsnest.lifechanger_admin.Adapter;

import android.app.Activity;
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

import com.bitsnest.lifechanger_admin.ActivityWithdrawDetails;
import com.bitsnest.lifechanger_admin.ActvityInvDetails;
import com.bitsnest.lifechanger_admin.MainActivity;
import com.bitsnest.lifechanger_admin.Model.Model_Withdraw;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.bitsnest.lifechanger_admin.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Adapter_Withdraw extends RecyclerView.Adapter<Adapter_Withdraw.ViewHolder> {
    private List<Model_Withdraw> model_Withdraw;

    Context context;

    private FirebaseFirestore firestore;
    private Utils utils;

    // RecyclerView recyclerView;
    public Adapter_Withdraw(Context context, List<Model_Withdraw> model_Withdraw) {
        this.context = context;

        this.model_Withdraw = model_Withdraw;
        firestore= FirebaseFirestore.getInstance();
    }
    @NonNull
    @Override
    public Adapter_Withdraw.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_withdraw, parent, false);
        Adapter_Withdraw.ViewHolder viewHolder = new Adapter_Withdraw.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Withdraw.ViewHolder holder, int position) {
        final Model_Withdraw current_Withdraw = model_Withdraw.get(position);



        holder.txt_date.setText(model_Withdraw.get(position).getDate());
        holder.txt_amount.setText(model_Withdraw.get(position).getAmount()+" Rs");



        holder.btn_iProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), ActivityWithdrawDetails.class);
                intent.putExtra("id",model_Withdraw.get(position).getId());
                intent.putExtra("userId", model_Withdraw.get(position).getUserID());
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return model_Withdraw.size();
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
