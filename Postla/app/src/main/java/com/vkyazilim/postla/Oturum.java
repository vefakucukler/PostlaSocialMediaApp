package com.vkyazilim.postla;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Oturum extends AppCompatActivity {

    ListView listView;
    PostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCommentFromFB;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.ekle_post,menu);
        menuInflater.inflate(R.menu.log_out,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.ekle_post){
            Intent intent = new Intent(getApplicationContext(),Yukleme.class);
            startActivity(intent);

        }
        if(item.getItemId()==R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            Intent intent2=new Intent(getApplicationContext(),Giris.class);
            startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oturum);
        listView=findViewById(R.id.listView);
        userEmailFromFB=new ArrayList<String>();
        userImageFromFB=new ArrayList<String>();
        userCommentFromFB=new ArrayList<String>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        adapter= new PostClass(userEmailFromFB,userCommentFromFB,userImageFromFB,this);
        listView.setAdapter(adapter);
        getDataFromFirebase();

    }
    public void getDataFromFirebase(){

        DatabaseReference newReference= firebaseDatabase.getReference("Post");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap= (HashMap<String, String>) ds.getValue();
                    userEmailFromFB.add(hashMap.get("useremail"));
                    userCommentFromFB.add(hashMap.get("usercomment"));
                    userImageFromFB.add(hashMap.get("downloadurl"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
