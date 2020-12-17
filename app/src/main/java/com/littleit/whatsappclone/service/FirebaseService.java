package com.littleit.whatsappclone.service;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.littleit.whatsappclone.model.StatusModel;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class FirebaseService {

    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
    }

    public void uploadImageToFireBaseStorage(Uri uri, final OnCallBack onCallBack){
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("ImagesChats/" + System.currentTimeMillis()+"."+getFileExtention(uri));
        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                final String sdownload_url = String.valueOf(downloadUrl);

                onCallBack.onUploadSuccess(sdownload_url);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCallBack.onUploadFailed(e);
            }
        });
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface OnCallBack{
        void onUploadSuccess(String imageUrl);
        void onUploadFailed(Exception e);
    }

    public void addNewStatus(StatusModel statusModel, final OnAddNewStatusCallBack onAddNewStatusCallBack){
        //
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Status Daily").document(statusModel.getId()).set(statusModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onAddNewStatusCallBack.onSuccess();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onAddNewStatusCallBack.onFailed();
            }
        });

    }

    public interface OnAddNewStatusCallBack{
        void onSuccess();
        void onFailed();
    }
}
