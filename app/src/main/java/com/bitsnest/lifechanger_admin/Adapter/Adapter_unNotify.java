package com.bitsnest.lifechanger_admin.Adapter;

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

import com.bitsnest.lifechanger_admin.ActivityAnnouncement;
import com.bitsnest.lifechanger_admin.MainActivity;
import com.bitsnest.lifechanger_admin.Model.Model_unNotify;
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

public class Adapter_unNotify extends RecyclerView.Adapter<Adapter_unNotify.ViewHolder> {
    private List<Model_unNotify> model_unNotify;
    Context context;
    private FirebaseFirestore firestore;
    private Utils utils;


    LayoutInflater inflater;
    private OnItemClickListener mListener;

    /////////////////////click listner for outside adopter
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    // RecyclerView recyclerView;
    public Adapter_unNotify(Context context,List<Model_unNotify> model_unNotify) {
        this.model_unNotify = model_unNotify;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(context);
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
        final Model_unNotify current_unNotify = model_unNotify.get(position);
        holder.txt_date.setText(model_unNotify.get(position).getDate());
        holder.txt_name.setText(model_unNotify.get(position).getHeading());
        holder.txt_notifi.setText(model_unNotify.get(position).getData());

        holder.btn_announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final lottiedialog lottie=new lottiedialog(context);
                lottie.show();
                Map<String, Object> m = new HashMap<>();
                m.put("status", "read");
                firestore.collection("Admin").document(utils.getToken())// whereEqualTo("studentID",token)
                        .collection("NotificationsAdmin")
                        .document(current_unNotify.getId()).update(m)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(context, "Status Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    lottie.dismiss();

                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                Toast.makeText(context, "debug!"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                lottie.dismiss();
                            }
                        });
               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
                intent.putExtra("id", current_unNotify.getNotificatioID().toString().trim());
                view.getContext().startActivity(intent);*/
                //Toast.makeText(view.getContext(),"click on item: "+current_unNotify.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_unNotify.size();
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
        }

    }
}

