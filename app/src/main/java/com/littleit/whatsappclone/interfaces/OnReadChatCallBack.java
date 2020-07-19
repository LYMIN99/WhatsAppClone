package com.littleit.whatsappclone.interfaces;

import com.littleit.whatsappclone.model.chat.Chats;

import java.util.List;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
