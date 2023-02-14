package com.bitsnest.lifechanger_admin.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger_admin.ActivityInvestor;
import com.bitsnest.lifechanger_admin.ActvityInvDetails;
import com.bitsnest.lifechanger_admin.Model.Model_Investor;
import com.bitsnest.lifechanger_admin.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter_Investor extends RecyclerView.Adapter<Adapter_Investor.ViewHolder> {
    private List<Model_Investor> model_Investor;

    Context context;

    // RecyclerView recyclerView;
    public Adapter_Investor(Context context,List<Model_Investor> model_Investor) {
        this.context = context;

        this.model_Investor = model_Investor;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_investor, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Investor current_Investor = model_Investor.get(position);



        holder.txt_name.setText(model_Investor.get(position).getName());
        holder.txt_cnic.setText(model_Investor.get(position).getCnic());
        holder.txt_number.setText(model_Investor.get(position).getPhone());
        Glide.with(holder.itemView)
                .load(model_Investor.get(position).getProfile())
                .fitCenter()
                .into(holder.profile);


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_photo);
                ImageView temp_photo = dialog.findViewById(R.id.img_photo);
                Glide.with(context)
                        .load(model_Investor.get(position).getProfile())
                        .into(temp_photo);

                dialog.show();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityInvestor.class);
                intent.putExtra("userId", model_Investor.get(position).getId());
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return model_Investor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_name,txt_cnic,txt_number;
        public ImageView profile;



        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            this.txt_cnic = (TextView) itemView.findViewById(R.id.txt_cnic);
            this.txt_number = (TextView) itemView.findViewById(R.id.txt_number);
            this.profile = (ImageView) itemView.findViewById(R.id.img_invProfile);

        }

    }
}