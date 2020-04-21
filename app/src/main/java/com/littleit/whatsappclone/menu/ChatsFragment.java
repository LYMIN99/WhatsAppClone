package com.littleit.whatsappclone.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.adapter.ChatListAdapter;
import com.littleit.whatsappclone.model.Chatlist;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        List<Chatlist> list = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list.add(new Chatlist("11","Justin BB","hello frie","15/04/2020","https://www.biography.com/.image/t_share/MTM2OTI2NTY2Mjg5NTE2MTI5/justin_bieber_2015_photo_courtesy_dfree_shutterstock_348418241_croppedjpg.jpg"));
        list.add(new Chatlist("22","Selena GM","hello frie","15/04/2020","https://static.billboard.com/files/media/07-selena-gomez-hair-evolution-billboard-1548-1024x677.jpg"));
        list.add(new Chatlist("33","six 9","hello frie","15/04/2020","https://ichef.bbci.co.uk/news/410/cpsprodpb/12CFB/production/_104415077_tekashi69_976.jpg"));

        recyclerView.setAdapter(new ChatListAdapter(list,getContext()));

        return view;
    }
}
