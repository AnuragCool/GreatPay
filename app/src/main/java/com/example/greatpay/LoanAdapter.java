package com.example.greatpay;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        String timestamp = String.valueOf(System.currentTimeMillis());
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        final String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.tvUsername.setText(""+mData.get(position).getName());
        holder.tvMoney.setText(""+mData.get(position).getAmount());

        if(!(""+mData.get(position).getPurpose()).equals("")){
            holder.Tvpur.setText(""+mData.get(position).getPurpose());
        }

        if((""+mData.get(position).getStatus()).equals("pending")){
            holder.paid.setText("pending");
            holder.paid.setEnabled(false);
        }

        holder.emailTv.setText(""+mData.get(position).getrUser());


        holder.paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String pUid=""+mData.get(position).getpUid();

                String key=""+mData.get(position).getCommonkey();
                Log.d("puid", "onClick: "+key);


                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/loan/"+key);
                HashMap<String,Object> h=new HashMap<>();
                h.put("status","pending");
                reference1.updateChildren(h);



               DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users/"+pUid);

                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("type","collection");
                hashMap.put("sUid",user.getUid());
                hashMap.put("sEmail",user.getEmail());
                hashMap.put("key",key);
                hashMap.put("status","");
                hashMap.put("time",pTime);
                hashMap.put("myname",""+mData.get(position).getName());
                hashMap.put("name",""+mData.get(position).getMyname());
                hashMap.put("amount",""+mData.get(position).getAmount());
                reference.child("notification").child(key).setValue(hashMap);

                sendNotification(pUid,position);

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
    private void sendNotification(final String hisUid, final int position) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mcontext);

        DatabaseReference allToken=FirebaseDatabase.getInstance().getReference("Tokens");
        allToken.orderByKey().equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Token token=ds.getValue(Token.class);
                    Data data =new Data(user.getUid(),"Have you recieved ₹ "+mData.get(position).getAmount()+" from "+mData.get(position).getMyname()+" ?","Loan Verification",hisUid);

                    Sender sender=new Sender(data,token.getToken());


                    try {
                        JSONObject SenderjsonObject=new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", SenderjsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("responce", "onResponse: "+response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> headers=new HashMap<>();
                                headers.put("Content-Type","application/json");
                                headers.put("Authorization","key=AAAAjL1k34w:APA91bEt-WyQg0VgQEXAeTjFe1OCJaQGZRIB_kXMu92eAaWwDage6jiHjRn29GzUWl6CZd04c1oo8c-XaMx4fPTnFZ479V6YojxXRxBriGakY4MBbLAgaf9mnI8OtxDCT_QszCvqCR1S");
                                return headers;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
