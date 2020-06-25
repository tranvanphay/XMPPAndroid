package com.phaytran.xmppAndroidClient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private ArrayList<MessageData> messageDataList;

    public Adapter(ArrayList<MessageData> messageDataList) {
        this.messageDataList = messageDataList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MessageData messageData = messageDataList.get(position);
        holder.heading.setText(messageData.getHeading());
        holder.message.setText(messageData.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageDataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView heading, message;
        public Holder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            message = itemView.findViewById(R.id.messageBody);
        }
    }
}
