package com.vkyazilim.postla;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Giris extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtTxtUser;
    EditText edtTxtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        mAuth = FirebaseAuth.getInstance();
        edtTxtUser = findViewById(R.id.edtTxtUser);
        edtTxtPsw = findViewById(R.id.edtTxtPsw);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), Oturum.class);
            startActivity(intent);
        }


    }

    public void signIn(View view) {
        if (edtTxtUser.length() != 0 && edtTxtPsw.length() != 0) {
            mAuth.signInWithEmailAndPassword(edtTxtUser.getText().toString(), edtTxtPsw.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), Oturum.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adı ve şifreyi giriniz.", Toast.LENGTH_LONG).show();
        }

    }
    public void signUp(View view){
        if(edtTxtUser.length()!= 0&&edtTxtPsw.length()!=0) {
            mAuth.createUserWithEmailAndPassword(edtTxtUser.getText().toString(), edtTxtPsw.getText().toString())
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Kullanıcı oluşturuldu.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Oturum.class);
                        startActivity(intent);

                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Kullanıcı Adı ve Şifre Oluşturmanız Gerekiyor.",Toast.LENGTH_LONG).show();
        }
    }

}
