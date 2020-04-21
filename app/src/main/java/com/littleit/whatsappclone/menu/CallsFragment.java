package com.littleit.whatsappclone.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littleit.whatsappclone.R;
import com.littleit.whatsappclone.adapter.CallListAdapter;
import com.littleit.whatsappclone.model.CallList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calls, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CallList> lists = new ArrayList<>();

        lists.add(new CallList(
                "001",
                "Justin BB",
                "15/04/2020 , 9:24 pm",
                "https://www.biography.com/.image/t_share/MTM2OTI2NTY2Mjg5NTE2MTI5/justin_bieber_2015_photo_courtesy_dfree_shutterstock_348418241_croppedjpg.jpg",
                "income"));

        lists.add(new CallList(
                "001",
                "Justin BB",
                "15/04/2020 , 9:25 pm",
                "https://www.biography.com/.image/t_share/MTM2OTI2NTY2Mjg5NTE2MTI5/justin_bieber_2015_photo_courtesy_dfree_shutterstock_348418241_croppedjpg.jpg",
                "missed"));

        lists.add(new CallList(
                "001",
                "Justin BB",
                "15/04/2020 , 9:27 pm",
                "https://www.biography.com/.image/t_share/MTM2OTI2NTY2Mjg5NTE2MTI5/justin_bieber_2015_photo_courtesy_dfree_shutterstock_348418241_croppedjpg.jpg",
                "income"));

        lists.add(new CallList(
                "001",
                "Justin BB",
                "15/04/2020 , 9:20 pm",
                "https://www.biography.com/.image/t_share/MTM2OTI2NTY2Mjg5NTE2MTI5/justin_bieber_2015_photo_courtesy_dfree_shutterstock_348418241_croppedjpg.jpg",
                "out"));


        recyclerView.setAdapter(new CallListAdapter(lists,getContext()));
        return view;
    }
}
