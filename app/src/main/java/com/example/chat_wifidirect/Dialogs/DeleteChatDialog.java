package com.example.chat_wifidirect.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chat_wifidirect.R;

public class DeleteChatDialog extends DialogFragment {

    public static DeleteChatDialog newInstance() {
        DeleteChatDialog f = new DeleteChatDialog();
        return f;
    }

    View yes;
    View no;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delete_chat_pop_up, container, false);
        Activity act = getActivity();
        yes = (Button)v.findViewById(R.id.yes);
        no = (Button)v.findViewById(R.id.no);

//            return super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListeners();

    }

    public void addListeners() {
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String newName = et.getText().toString();
//                rp.saveFile(newName, message, path);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
