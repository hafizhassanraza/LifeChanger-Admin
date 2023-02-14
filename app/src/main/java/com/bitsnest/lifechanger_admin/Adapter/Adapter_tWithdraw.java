package com.bitsnest.lifechanger_admin.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bitsnest.lifechanger_admin.ActivityWithdrawDetails;
import com.bitsnest.lifechanger_admin.Model.Model_Withdraw;
import com.bitsnest.lifechanger_admin.R;
import com.bitsnest.lifechanger_admin.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter_tWithdraw extends RecyclerView.Adapter<Adapter_tWithdraw.ViewHolder> {
    private List<Model_Withdraw> model_tWithdraw;

    Context context;

    private FirebaseFirestore firestore;
    private Utils utils;

    // RecyclerView recyclerView;
    public Adapter_tWithdraw(Context context, List<Model_Withdraw> model_tWithdraw) {
        this.context = context;

        this.model_tWithdraw = model_tWithdraw;
        firestore= FirebaseFirestore.getInstance();
    }
    @NonNull
    @Override
    public Adapter_tWithdraw.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_withdraw, parent, false);
        Adapter_tWithdraw.ViewHolder viewHolder = new Adapter_tWithdraw.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_tWithdraw.ViewHolder holder, int position) {
        final Model_Withdraw current_tWithdraw = model_tWithdraw.get(position);



        holder.txt_date.setText(model_tWithdraw.get(position).getDate());
        holder.txt_amount.setText(model_tWithdraw.get(position).getAmount()+" Rs");



        holder.btn_iProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), ActivityWithdrawDetails.class);
                intent.putExtra("id",model_tWithdraw.get(position).getId());
                intent.putExtra("userId", model_tWithdraw.get(position).getUserID());
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return model_tWithdraw.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_amount,txt_AcDeposit,txt_vis;
        public ImageView img_receipt;
        public Button btn_iProceed,btn_iDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_invest);
            this.txt_amount = (TextView) itemView.findViewById(R.id.txt_amount_invest);
            this.txt_AcDeposit = (TextView) itemView.findViewById(R.id.txt_depositAccount_investment);
            this.txt_vis = (TextView) itemView.findViewById(R.id.vis);
            txt_vis.setVisibility(View.GONE);
            this.btn_iProceed = (Button) itemView.findViewById(R.id.btn_iProceed);
            btn_iProceed.setVisibility(View.GONE);
            this.btn_iDelete = (Button) itemView.findViewById(R.id.btn_iDelete);

        }

    }
}
