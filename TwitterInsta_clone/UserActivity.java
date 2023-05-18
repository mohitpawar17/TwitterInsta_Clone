package com.example.twitterinstafbclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class UserActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerViewUsers;
    ArrayList<ProfileObject> mUSersArrayList;
    public static String TAG = "USerActivity";
    ArrayList<String> followings, currentFollowers;
    String email;
    String currentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerViewUsers = findViewById(R.id.userRecyclerView);

        Intent intent=getIntent();
        email= intent.getStringExtra("email");

        fillData();
    }
    private void fillData(){
        FirebaseDatabase.getInstance("https://twitterinstafb-45871-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mUSersArrayList = new ArrayList<>();
                for(DataSnapshot childSnapshot: snapshot.getChildren()){

                    HashMap<String ,String> values = (HashMap<String, String>) childSnapshot.getValue();
                    //String key= childSnapshot.getKey();

                    String name= values.get("name");
                    Log.i(TAG,"name " +name);

                    Object isFollowing = values.get("isFollowing");
                    followings = new ArrayList<>();
                    followings = (ArrayList)isFollowing;
                    Log.i(TAG, "Followers "+followings);

                    String nameLowerCase = name.toLowerCase();
                    if(email.contains(nameLowerCase)){
                        currentFollowers = followings;
                        currentName = name;

                    }

                    mUSersArrayList.add(new ProfileObject(name,followings,email));


                }

                recyclerViewUsers.setLayoutManager(new LinearLayoutManager(UserActivity.this));

                adapter = new MyRecyclerViewAdapter(UserActivity.this, mUSersArrayList,UserActivity.this, currentFollowers);
                recyclerViewUsers.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClicked(boolean isChecked, int position) {

        if(isChecked){
            currentFollowers.add(adapter.getName(position));
            FirebaseDatabase.getInstance("https://twitterinstafb-45871-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user").child(currentName).child("isFollowing").setValue(currentFollowers);
        }else{
            currentFollowers.remove(adapter.getName(position));
            FirebaseDatabase.getInstance("https://twitterinstafb-45871-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user").child(currentName).child("isFollowing").setValue(currentFollowers);

        }

    }
}a