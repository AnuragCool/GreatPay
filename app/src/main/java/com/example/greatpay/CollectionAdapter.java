package com.example.greatpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyCViewHolder>{

    private Context mcontext;
    private List<CollectionModel> mData;


    public CollectionAdapter(Context mcontext, List<CollectionModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;


    }


    @NonNull
    @Override
    public CollectionAdapter.MyCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        view=inflater.inflate(R.layout.custom_collection_item,parent,false);
        return new MyCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.MyCViewHolder holder, int position) {

        holder.tvUsername.setText(""+mData.get(position).getRname());
        holder.tvMoney.setText(""+mData.get(position).getAmount());
        holder.emailTv.setText(""+mData.get(position).getRemail());

        Glide.with(mcontext).load(mData.get(position).getImage()).placeholder(R.drawable.profile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyCViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvMoney,emailTv;
        CircleImageView imageView;

        public MyCViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername=itemView.findViewById(R.id.name_collection);
            tvMoney=itemView.findViewById(R.id.collection_money);
            imageView=itemView.findViewById(R.id.c_img);
            emailTv=itemView.findViewById(R.id.email_collection);
        }

    }
}