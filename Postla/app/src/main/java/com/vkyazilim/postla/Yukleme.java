package com.vkyazilim.postla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Yukleme extends AppCompatActivity
{
    Uri secilenResim;
    ImageView postImageView;
    EditText postYorumTxt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yukleme);
        postYorumTxt=findViewById(R.id.postYorumTxt);
        postImageView=findViewById(R.id.postImageView);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth=FirebaseAuth.getInstance();
        mStorageRef=FirebaseStorage.getInstance().getReference();
    }

    public void yukle(View view){
        if(postYorumTxt.length()<100) {
            final UUID uuıd = UUID.randomUUID();
            final String imageNames = "images/" + uuıd + ".jpg";
            StorageReference storageReference = mStorageRef.child(imageNames);
            storageReference.putFile(secilenResim).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageNames);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail = user.getEmail();
                            String userComment = postYorumTxt.getText().toString();
                            UUID uuıd1 = UUID.randomUUID();
                            String postname = "post-" + uuıd1.toString();
                            myRef.child("Post").child(postname).child("useremail").setValue(userEmail);
                            myRef.child("Post").child(postname).child("usercomment").setValue(userComment);
                            myRef.child("Post").child(postname).child("downloadurl").setValue(downloadUrl);
                            Toast.makeText(getApplicationContext(), "Post Oluşturuldu.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Oturum.class);
                            startActivity(intent);
                        }
                    });
                    //Toast.makeText(getApplicationContext(), "Post Oluşturuldu..", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Lütfen 100 karakterden az bir yorum girin.Ve Bir Fotoğraf Seçin. ",Toast.LENGTH_LONG).show();
        }



    }
    public void resimsec(View view){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }


    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            secilenResim=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),secilenResim);
                postImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}
