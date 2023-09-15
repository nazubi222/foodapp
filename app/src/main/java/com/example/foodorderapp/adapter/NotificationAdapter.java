package com.example.foodorderapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> listNotification;
    private Context context;

    public NotificationAdapter(List<Notification> listNotification, Context context) {
        this.listNotification = listNotification;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        NotificationAdapter.NotificationViewHolder viewHolder = new NotificationAdapter.NotificationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = listNotification.get(position);
        if(notification == null){
            return;
        }

        holder.tvName.setText(notification.getName());
        holder.tvInfo.setText(String.valueOf(notification.getInfomation()));
        holder.tvDate.setText(dateFormat(notification.getDate()));
        if(notification.isClick() == true){
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.setClick(true);
                holder.linearLayout.setBackgroundColor(Color.WHITE);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(listNotification != null){
            return listNotification.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private TextView tvDate, tvName, tvInfo;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ln_itemnotification);
            tvName = itemView.findViewById(R.id.tv_namenotification);
            tvDate = itemView.findViewById(R.id.tv_datenotification);
            tvInfo = itemView.findViewById(R.id.tv_infonotification);
        }
    }
    private String dateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String ngay = dateFormat.format(date);
        String gio = timeFormat.format(date);

        return ngay + " " + gio;
    }
}
