package com.example.project.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Interface.IRecyclerItemClickListener;
import com.example.project.R;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_user_email;
    IRecyclerItemClickListener iRecyclerItemClickListener;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_user_email = (TextView)itemView.findViewById(R.id.txt_user_email);
        itemView.setOnClickListener(this);
    }
    public void setiRecyclerItemClickListener(IRecyclerItemClickListener iRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener;
    }
    @Override
    public void onClick(View view) {
        iRecyclerItemClickListener.onItemClickListener(view, getAdapterPosition());
    }
}
