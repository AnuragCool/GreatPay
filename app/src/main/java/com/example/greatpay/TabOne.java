package com.example.greatpay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TabOne extends Fragment {
    private RecyclerView recyclerView;
    private SummaryModel summaryModel;
    private List<SummaryModel> summaryModelList;
    private SummaryAdapter summaryAdapter;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_one, container, false);


        recyclerView = view.findViewById(R.id.recycler_summary);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        summaryModelList=new ArrayList<>();

        user=FirebaseAuth.getInstance().getCurrentUser();
        loadPaidSummary();

        return view;

    }

        private void loadPaidSummary() {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid()+"/loan");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String name=""+ds.child("name").getValue();
                        String amount=""+ds.child("amount").getValue();
                        String status=""+ds.child("status").getValue();
                        if(status.equals("paid")){
                            summaryModel=new SummaryModel("paid to "+name,"₹"+amount);
                            summaryModelList.add(summaryModel);
                        }
                        summaryAdapter=new SummaryAdapter(getActivity(),summaryModelList);
                        recyclerView.setAdapter(summaryAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }
