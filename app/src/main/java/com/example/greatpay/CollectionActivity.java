package com.example.greatpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CollectionAdapter collectionAdapter;
    private CollectionModel collectionModel;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FloatingActionButton floatingActionButton;
    String hisUid,amount;
    String mail,ruser,url;
    private List<CollectionModel> collectionModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        floatingActionButton=findViewById(R.id.c_add);






        getSupportActionBar().setTitle("My Collections");


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();


        recyclerView=findViewById(R.id.recycler_collection);
        collectionModelList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CollectionActivity.this,AddCollectionActivity.class));
            }
        });


        load();
    }

    private void load() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid());

        ref.child("collection").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectionModelList.clear();


                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    String status=""+ds.child("status").getValue();
                    if(!status.equals("collected")){
                        collectionModel=ds.getValue(CollectionModel.class);
                        collectionModelList.add(collectionModel);

                    }










                }
                collectionAdapter=new CollectionAdapter(CollectionActivity.this,collectionModelList);
                recyclerView.setAdapter(collectionAdapter);
                collectionAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
