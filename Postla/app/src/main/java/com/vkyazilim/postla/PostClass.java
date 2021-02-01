package com.vkyazilim.postla;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class PostClass extends ArrayAdapter<String> {
    EditText yorumplain;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private final ArrayList<String> userEmail;
    private final ArrayList<String> userComment;
    private final ArrayList<String> userImage;
    private final Activity context;

    public PostClass(ArrayList<String> userEmail, ArrayList<String> userComment, ArrayList<String> userImage, Activity context) {
        super(context,R.layout.custom_view,userEmail);
        this.userEmail = userEmail;
        this.userComment = userComment;
        this.userImage = userImage;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        mStorageRef=FirebaseStorage.getInstance().getReference();


        LayoutInflater layoutInflater=context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);
        TextView userEmailText=customView.findViewById(R.id.userEmailTxtCustomView);
        TextView CommentText=customView.findViewById(R.id.commentTextViewCustomView);
        ImageView imageView=customView.findViewById(R.id.userImageViewCustomView);

        userEmailText.setText(userEmail.get(position));
        CommentText.setText(userComment.get(position));
        Picasso.get().load(userImage.get(position)).into(imageView);
        return customView;
    }




    /*public void btnyukle(View view){
        //final UUID uuıd=UUID.randomUUID();
        //final String commentnames="usercomments/"+uuıd;
        FirebaseUser user1=mAuth.getCurrentUser();
        String userEmails=user1.getEmail();
        String uuserComment= yorumplain.getText().toString();
        UUID uuıd1=UUID.randomUUID();
        String comment="post--"+uuıd1.toString();
        myRef.child("UserPost").child(comment).child("uusercoment").setValue(uuserComment);
        //myRef.child("UserPost").child(userEmails).child("UserEmail").setValue(userEmails);

    }*/


}
