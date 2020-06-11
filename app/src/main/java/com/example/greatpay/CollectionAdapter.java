package com.example.greatpay;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyCViewHolder>{

    private Context mcontext;
    private List<CollectionModel> mData;
    private FirebaseUser user;
    String uid;

    public CollectionAdapter(Context mcontext, List<CollectionModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
        user= FirebaseAuth.getInstance().getCurrentUser();

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
    public void onBindViewHolder(@NonNull CollectionAdapter.MyCViewHolder holder, final int position) {

        holder.tvUsername.setText(""+mData.get(position).getRname());
        holder.tvMoney.setText(""+mData.get(position).getAmount());
        holder.emailTv.setText(""+mData.get(position).getRemail());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key=""+mData.get(position).getKey();
                final String hisemail=""+mData.get(position).getRemail();
                final String hisUid=""+mData.get(position).getbUid();


                DatabaseReference refe=FirebaseDatabase.getInstance().getReference("Users");
                refe.orderByChild("email").equalTo(hisemail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                           uid=""+ds.child("uId").getValue();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(mcontext, "I got Clicked"+position, Toast.LENGTH_SHORT).show();
                Log.d("on", "onClick: "+uid+" "+key);
                DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("Users/"+hisUid+"/loan/"+key);
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("status","paid");
                reference2.updateChildren(hashMap);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/collection/"+key);
                HashMap<String,Object> hashMap1=new HashMap<>();
                hashMap1.put("status","collected");
                reference.updateChildren(hashMap1);



            }
        });

        holder.remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hisUid=""+mData.get(position).getbUid();
                sendNotification(hisUid,position);
            }
        });

        Glide.with(mcontext).load(mData.get(position).getImage()).placeholder(R.drawable.profile).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyCViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvMoney,emailTv;
        CircleImageView imageView;
        Button delete,remind;

        public MyCViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername=itemView.findViewById(R.id.name_collection);
            tvMoney=itemView.findViewById(R.id.collection_money);
            imageView=itemView.findViewById(R.id.c_img);
            emailTv=itemView.findViewById(R.id.email_collection);
            delete=itemView.findViewById(R.id.deletec);
            remind=itemView.findViewById(R.id.remind);
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
                    Data data =new Data(user.getUid(),"Hey %=name% is asking for his Money","Loan Remainder",hisUid);

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