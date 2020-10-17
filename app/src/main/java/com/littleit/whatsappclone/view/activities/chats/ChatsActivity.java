package com.littleit.whatsappclone.view.activities.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.adapter.ChatsAdapder;
import com.littleit.whatsappclone.databinding.ActivityChatsBinding;
import com.littleit.whatsappclone.interfaces.OnReadChatCallBack;
import com.littleit.whatsappclone.managers.ChatService;
import com.littleit.whatsappclone.model.chat.Chats;
import com.littleit.whatsappclone.service.FirebaseService;
import com.littleit.whatsappclone.view.activities.dialog.DialogReviewSendImage;
import com.littleit.whatsappclone.view.activities.profile.UserProfileActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatsActivity extends AppCompatActivity {

    private static final String TAG = "ChatsActivity";
    private static final int REQUEST_CORD_PERMISSION = 332;
    private ActivityChatsBinding binding;
    private String receiverID;
    private ChatsAdapder adapder;
    private List<Chats>list = new ArrayList<>();
    private String userProfile,userName;
    private boolean isActionShown = false;
    private ChatService chatService;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;

    //Audio
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        initialize();
        initBtnClick();
        readChats();

    }

    private void initialize(){

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        userProfile = intent.getStringExtra("userProfile");

        chatService = new ChatService(this,receiverID);

        if (receiverID!=null){
            Log.d(TAG, "onCreate: receiverID "+receiverID);
            binding.tvUsername.setText(userName);
            if (userProfile != null) {
                if (userProfile.equals("")){
                    binding.imageProfile.setImageResource(R.drawable.icon_male_ph);  // set  default image when profile user is null
                } else {
                    Glide.with(this).load(userProfile).into( binding.imageProfile);
                }
            }
        }

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    binding.btnSend.setVisibility(View.INVISIBLE);
                    binding.recordButton.setVisibility(View.VISIBLE);
                } else {
                    binding.btnSend.setVisibility(View.VISIBLE);
                    binding.recordButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        //adapder = new ChatsAdapder(list,this);
        //binding.recyclerView.setAdapter(new ChatsAdapder(list,this));

        //initialize record button
        binding.recordButton.setRecordView(binding.recordView);
        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {

                //Start Recording..
                if (!checkPermissionFromDevice()) {
                    binding.btnEmoji.setVisibility(View.INVISIBLE);
                    binding.btnFile.setVisibility(View.INVISIBLE);
                    binding.btnCamera.setVisibility(View.INVISIBLE);
                    binding.edMessage.setVisibility(View.INVISIBLE);

                    startRecord();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(100);
                    }

                } else {
                    requestPermission();
                }

            }

            @Override
            public void onCancel() {
                try {
                    mediaRecorder.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(long recordTime) {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);

                //Stop Recording..
                try {
                    sTime = getHumanTimeText(recordTime);
                    stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLessThanSecond() {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });
        binding.recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void readChats() {
        chatService.readChatData(new OnReadChatCallBack() {
            @Override
            public void onReadSuccess(List<Chats> list) {
                //adapder.setList(list);
                Log.d(TAG, "onReadSuccess: List "+list.size());
                binding.recyclerView.setAdapter(new ChatsAdapder(list,ChatsActivity.this));
            }

            @Override
            public void onReadFailed() {
                Log.d(TAG, "onReadFailed: ");
            }
        });
    }

    private void initBtnClick(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    chatService.sendTextMsg(binding.edMessage.getText().toString());
                    binding.edMessage.setText("");
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsActivity.this, UserProfileActivity.class)
                .putExtra("userID",receiverID)
                .putExtra("userProfile",userProfile)
                .putExtra("userName",userName));
            }
        });

        binding.btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActionShown){
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;
                } else {
                    binding.layoutActions.setVisibility(View.VISIBLE);
                    isActionShown = true;
                }

            }
        });

        binding.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_GALLERY_REQUEST);

    }

    private boolean checkPermissionFromDevice() {
        int write_external_strorage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_strorage_result == PackageManager.PERMISSION_DENIED || record_audio_result == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CORD_PERMISSION);
    }

    private void startRecord(){
        setUpMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //  Toast.makeText(InChatActivity.this, "Recording...", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ChatsActivity.this, "Recording Error , Please restart your app ", Toast.LENGTH_LONG).show();
        }

    }

    private void stopRecord(){
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                //sendVoice();
                chatService.sendVoice(audio_path);

            } else {
                Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Stop Recording Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMediaRecorder() {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "audio_record.m4a";
        audio_path = path_save;

        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        } catch (Exception e) {
            Log.d(TAG, "setUpMediaRecord: " + e.getMessage());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            imageUri = data.getData();

            //uploadToFirebase();
             try {
                 Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                 reviewImage(bitmap);
             }catch (Exception e){
                 e.printStackTrace();
             }

        }
    }
    private void reviewImage(Bitmap bitmap){
        new DialogReviewSendImage(ChatsActivity.this,bitmap).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {
              // to Upload Image to firebase storage to get url image...
                if (imageUri!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(ChatsActivity.this);
                    progressDialog.setMessage("Sending image...");
                    progressDialog.show();

                    //hide action buttonss
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;

                    new FirebaseService(ChatsActivity.this).uploadImageToFireBaseStorage(imageUri, new FirebaseService.OnCallBack() {
                        @Override
                        public void onUploadSuccess(String imageUrl) {
                            // to send chat image//
                            chatService.sendImage(imageUrl);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailed(Exception e) {
                           e.printStackTrace();
                        }
                    });
                }

            }
        });
    }

}
