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

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder>{

    private Context mcontext;
    private List<LoanModel> mData;


    public LoanAdapter(Context mcontext, List<LoanModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;


    }


    @NonNull
    @Override
    public LoanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        view=inflater.inflate(R.layout.custom_loan_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.MyViewHolder holder, int position) {

        holder.tvUsername.setText(""+mData.get(position).getrUser());
        holder.tvMoney.setText(""+mData.get(position).getAmount());

        Glide.with(mcontext).load(mData.get(position).getImage()).placeholder(R.drawable.profile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvMoney;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername=itemView.findViewById(R.id.username_loan);
            tvMoney=itemView.findViewById(R.id.money);
            imageView=itemView.findViewById(R.id.img);
        }

    }
}
