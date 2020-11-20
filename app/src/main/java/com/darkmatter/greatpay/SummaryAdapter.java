package com.darkmatter.greatpay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MySViewHolder>{

    private Context mcontext;
    private List<SummaryModel> mData;


    public SummaryAdapter(Context mcontext, List<SummaryModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;


    }


    @NonNull
    @Override
    public SummaryAdapter.MySViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        view=inflater.inflate(R.layout.custom_summary_item,parent,false);
        return new MySViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.MySViewHolder holder, int position) {

        holder.tvUsername.setText(""+mData.get(position).getName());
        holder.tvMoney.setText(""+mData.get(position).getAmount());
        holder.timeTv.setText(""+mData.get(position).getTime());

        Glide.with(mcontext).load(mData.get(position).getImage()).placeholder(R.drawable.profile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MySViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvMoney,timeTv;
        CircleImageView imageView;

        public MySViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView=itemView.findViewById(R.id.image_summary);

            tvUsername=itemView.findViewById(R.id.name_summary);
            tvMoney=itemView.findViewById(R.id.money_summary);
            timeTv= itemView.findViewById(R.id.time);

        }

    }
}