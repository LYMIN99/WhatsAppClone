package com.littleit.whatsappclone.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.databinding.ActivitySetUserInfoBinding;
import com.littleit.whatsappclone.model.user.Users;
import com.littleit.whatsappclone.view.MainActivity;

public class SetUserInfoActivity extends AppCompatActivity {

    private ActivitySetUserInfoBinding binding;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_set_user_info);

        progressDialog = new ProgressDialog(this);
        initButtonClick();
    }

    private void initButtonClick() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.edName.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please input username",Toast.LENGTH_SHORT).show();
                } else {
                    doUpdate();
                }

            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // pickImage();
                // I will do next video
                Toast.makeText(getApplicationContext(),"This function is not ready to use",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doUpdate() {
        ///
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            String userID = firebaseUser.getUid();
            Users users = new Users(userID,
                    binding.edName.getText().toString(),
                    firebaseUser.getPhoneNumber(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "");
           firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(users)
                  // .update("userName",binding.edName.getText().toString())
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           progressDialog.dismiss();
                           Toast.makeText(getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       }
                   }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   progressDialog.dismiss();
                   Log.d("Update", "onFailure: "+e.getMessage());
                   Toast.makeText(getApplicationContext(),"Update Failed :"+e.getMessage(),Toast.LENGTH_SHORT).show();

               }
           });
        } else {
            Toast.makeText(getApplicationContext(),"you need to login first",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
