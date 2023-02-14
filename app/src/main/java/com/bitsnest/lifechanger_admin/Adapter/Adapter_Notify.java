package com.bitsnest.lifechanger_admin.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger_admin.Model.Model_Notify;
import com.bitsnest.lifechanger_admin.R;

import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Adapter_Notify extends RecyclerView.Adapter<Adapter_Notify.ViewHolder> {
    private List<Model_Notify> model_Notify;


    // RecyclerView recyclerView;
    public Adapter_Notify(List<Model_Notify> model_Notify) {
        this.model_Notify = model_Notify;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_un_notify, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Notify current_Notify = model_Notify.get(position);
        holder.txt_date.setText(model_Notify.get(position).getDate());
        holder.txt_name.setText(model_Notify.get(position).getHeading());
        holder.txt_notifi.setText(model_Notify.get(position).getData());




    }

    @Override
    public int getItemCount() {
        return model_Notify.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_notifi;
        public Button btn_announce;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_noti);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_head_noti);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_data_noti);
            this.btn_announce =  itemView.findViewById(R.id.btn_delete_noti);
            btn_announce.setVisibility(View.GONE);
        }

    }
}


