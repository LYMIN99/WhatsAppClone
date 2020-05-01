package com.littleit.whatsappclone.view.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.databinding.ActivityNewChatsBinding;
import com.littleit.whatsappclone.model.user.Users;

import java.util.ArrayList;
import java.util.List;

public class NewChatsActivity extends AppCompatActivity {

    private ActivityNewChatsBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private List<Users> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore= FirebaseFirestore.getInstance();
        if (firebaseUser!=null){
            getContact();
        }
    }

    private void getContact() {
        firestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                    Log.d("Contact", "onSuccess: data "+snapshots.toString());
                }
            }
        });
    }
}
