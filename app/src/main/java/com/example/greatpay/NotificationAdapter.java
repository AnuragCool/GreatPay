package com.example.greatpay;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyNViewHolder> {
    private FirebaseUser user;
    @NonNull

    private Context mcontext;
    public List<NotificationModel> mData;


    public NotificationAdapter(Context mcontext, List<NotificationModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;

        user= FirebaseAuth.getInstance().getCurrentUser();




    }


    public static class MyNViewHolder extends RecyclerView.ViewHolder{

        TextView tvQues,tvEmail,tvMoney,Tvtime;
        Button yes,no;
        public MyNViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQues = itemView.findViewById(R.id.notify_ques);
            tvEmail = itemView.findViewById(R.id.email_notify);
            tvMoney = itemView.findViewById(R.id.money_notify);
            yes=itemView.findViewById(R.id.yes);
            no=itemView.findViewById(R.id.no);
            Tvtime=itemView.findViewById(R.id.timeNoti);
        }
    }


    @Override
    public NotificationAdapter.MyNViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        view=inflater.inflate(R.layout.custom_notifcation_item,parent,false);
        return new NotificationAdapter.MyNViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyNViewHolder holder, final int position) {

        String timestamp = String.valueOf(System.currentTimeMillis());
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        final String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.tvEmail.setText("from "+mData.get(position).getEmail()+" ?");
        holder.tvMoney.setText(""+mData.get(position).getMoney());
        holder.Tvtime.setText(""+mData.get(position).getTime());
        if((""+mData.get(position).getType()).equals("collection")){
            holder.tvQues.setText("Have you recieved ₹");
        }
        if((""+mData.get(position).getType()).equals("loan")){
            holder.tvQues.setText("Have you taken ₹");
        }
        if((""+mData.get(position).getType()).equals("declined")){
            holder.tvQues.setText(""+mData.get(position).getEmail()+" declined your collecion Request of ₹"+""+mData.get(position).getMoney());
            holder.yes.setVisibility(View.GONE);
            holder.tvEmail.setVisibility(View.GONE);
            holder.no.setVisibility(View.GONE);
            holder.tvMoney.setVisibility(View.GONE);
        }

        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((""+mData.get(position).getType()).equals("loan")){
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+""+user.getUid()+"/loan/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","verified");
                    reference.updateChildren(hashMap);

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification/"+mData.get(position).getKey());
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put("status","responded");
                    reference1.updateChildren(hashMap1);


                }
                if((""+mData.get(position).getType()).equals("collection")){

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user
                            .getUid()+"/collection/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","collected");
                    hashMap.put("time",pTime);
                    reference.updateChildren(hashMap);

                    DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("Users/"+mData.get(position).getsUid()+"/loan/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap2=new HashMap<>();
                    hashMap2.put("status","paid");
                    hashMap2.put("time",pTime);
                    reference2.updateChildren(hashMap2);

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification/"+mData.get(position).getKey());
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put("status","responded");
                    reference1.updateChildren(hashMap1);


                }
            }
        });

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((""+mData.get(position).getType()).equals("collection")){

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+""+mData.get(position).getsUid()+"/loan/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","verified");
                    reference.updateChildren(hashMap);

                    sendNotification(mData.get(position).getsUid(),position);

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification/"+mData.get(position).getKey());
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put("status","responded");
                    reference1.updateChildren(hashMap1);


                }
                if((""+mData.get(position).getType()).equals("loan")){

                    DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/loan/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap2=new HashMap<>();
                    hashMap2.put("status","");
                    reference2.updateChildren(hashMap2);

                    sendNotification(mData.get(position).getsUid(),position);
                    DatabaseReference reference3= FirebaseDatabase.getInstance().getReference("Users/"+mData.get(position).getsUid()
                            +"/notification/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap3=new HashMap<>();
                    hashMap3.put("type","declined");
                    hashMap3.put("name",""+mData.get(position).getMyname());
                    hashMap3.put("time",pTime);
                    hashMap3.put("sUid",user.getUid());
                    hashMap3.put("amount",""+mData.get(position).getMoney());
                    reference3.updateChildren(hashMap3);

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/notification/"+mData.get(position).getKey());
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put("status","responded");
                    reference1.updateChildren(hashMap1);

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+mData.get(position).getsUid()
                            +"/collection/"+""+mData.get(position).getKey());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","notVerified");
                    reference.updateChildren(hashMap);



                }

            }
        });
           }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    private void sendNotification(final String hisUid, final int position) {

        final RequestQueue requestQueue = Volley.newRequestQueue(mcontext);

        DatabaseReference allToken=FirebaseDatabase.getInstance().getReference("Tokens");
        allToken.orderByKey().equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Token token=ds.getValue(Token.class);

                    if((""+mData.get(position).getType()).equals("loan")){
                        Data data =new Data(user.getUid(),mData.get(position).getMyname()+" rejected your collection amount of ₹"+mData.get(position).getMoney(),"Declined the Loan",hisUid);
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
                    }else{
                        Data data =new Data(user.getUid(),mData.get(position).getMyname()+" didn't received the payed amount of "+mData.get(position).getMoney(),"Declined the Loan",hisUid);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

}
