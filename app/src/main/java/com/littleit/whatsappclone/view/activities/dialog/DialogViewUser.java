package com.littleit.whatsappclone.view.activities.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.common.Common;
import com.littleit.whatsappclone.model.Chatlist;
import com.littleit.whatsappclone.view.activities.chats.ChatsActivity;
import com.littleit.whatsappclone.view.activities.display.ViewImageActivity;
import com.littleit.whatsappclone.view.activities.profile.ProfileActivity;
import com.littleit.whatsappclone.view.activities.profile.UserProfileActivity;

import java.util.Objects;

public class DialogViewUser {
    private Context context;

    public DialogViewUser(Context context, Chatlist chatlist) {
        this.context = context;
        initialize(chatlist);
    }
    public void initialize(final Chatlist chatlist){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); // before
        dialog.setContentView(R.layout.dialog_view_user);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton btnChat, btnCall, btnVideoCall, btnInfo;
        final ImageView profile;
        TextView userName;

        btnChat = dialog.findViewById(R.id.btn_chat);
        btnCall = dialog.findViewById(R.id.btn_call);
        btnVideoCall = dialog.findViewById(R.id.btn_video);
        btnInfo = dialog.findViewById(R.id.btn_info);

        profile = dialog.findViewById(R.id.image_profile);
        userName = dialog.findViewById(R.id.tv_username);

        userName.setText(chatlist.getUserName());
        Glide.with(context).load(chatlist.getUrlProfile()).into(profile);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userID",chatlist.getUserID())
                        .putExtra("userName",chatlist.getUserName())
                        .putExtra("userProfile",chatlist.getUrlProfile()));
                dialog.dismiss();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Call Clicked",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Video Call Clicked",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserProfileActivity.class)
                        .putExtra("userID",chatlist.getUserID())
                        .putExtra("userProfile",chatlist.getUrlProfile())
                        .putExtra("userName",chatlist.getUserName()));
                dialog.dismiss();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.invalidate();
                Drawable dr = profile.getDrawable();
                Common.IMAGE_BITMAP = ((GlideBitmapDrawable)dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profile, "image");
                Intent intent = new Intent(context, ViewImageActivity.class);
                context.startActivity(intent, activityOptionsCompat.toBundle());

            }
        });


        dialog.show();
    }
}
