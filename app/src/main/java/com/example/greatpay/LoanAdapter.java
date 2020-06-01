package com.example.greatpay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder>{

    private Context mcontext;
    private List<LoanModel> mData;
    private FirebaseUser user;
    String name,email;


    public LoanAdapter(Context mcontext, List<LoanModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;


        user=FirebaseAuth.getInstance().getCurrentUser();


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
    public void onBindViewHolder(@NonNull LoanAdapter.MyViewHolder holder, final int position) {

        holder.tvUsername.setText(""+mData.get(position).getName());
        holder.tvMoney.setText(""+mData.get(position).getAmount());

        if(!(""+mData.get(position).getPurpose()).equals("")){
            holder.Tvpur.setText(""+mData.get(position).getPurpose());
        }

        holder.emailTv.setText(""+mData.get(position).getrUser());


        holder.paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String pUid=""+mData.get(position).getpUid();

                String key=""+mData.get(position).getCommonkey();
                Log.d("puid", "onClick: "+key);


                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            name=""+ds.child("name").getValue();
                            email=""+ds.child("email").getValue();
                            Log.d("puid", "onDataChange: "+name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

               DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("type","collection");
                hashMap.put("sUid",user.getUid());
                hashMap.put("sEmail",user.getEmail());
                hashMap.put("key",key);
                hashMap.put("name",name);
                hashMap.put("amount",""+mData.get(position).getAmount());
                reference.child("notification").child(key).setValue(hashMap);

            }
        });

        Glide.with(mcontext).load(mData.get(position).getImage()).placeholder(R.drawable.profile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvMoney,Tvpur,emailTv;
        CircleImageView imageView;
        Button paid,pay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername=itemView.findViewById(R.id.username_loan);
            tvMoney=itemView.findViewById(R.id.money);
            imageView=itemView.findViewById(R.id.img);
            Tvpur=itemView.findViewById(R.id.purpose);
            emailTv=itemView.findViewById(R.id.emailt);
            paid=itemView.findViewById(R.id.paid);
            pay=itemView.findViewById(R.id.payy);
        }

    }
}
