package com.prestaxadmin.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prestaxadmin.Networking.KEY;
import com.prestaxadmin.Networking.Messages;
import com.prestaxadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragmentMessage extends DialogFragment implements OnClickListener{

    public static final String TAG = "DialogFragmentMessage";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";

    public static DialogFragmentMessage newInstance(String message, int type){
        DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putInt(TYPE, type);
        dialogFragmentMessage.setArguments(bundle);
        return dialogFragmentMessage;
    }

    public static DialogFragmentMessage newInstanceWithCredentials(String message, int type, String password){
        DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putInt(TYPE, type);
        bundle.putString(KEY.PASSWORD, password);
        dialogFragmentMessage.setArguments(bundle);
        return dialogFragmentMessage;
    }

    public DialogFragmentMessage() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String message = arguments.getString(MESSAGE);
        int type = arguments.getInt(TYPE);

        View view = inflater.inflate(R.layout.fragment_dialog_fragment_message, container, false);
        Button btn_close = (Button) view.findViewById(R.id.btn_close_dialog_message);
        btn_close.setOnClickListener(this);

        TextView txv_message = (TextView) view.findViewById(R.id.txv_message_service);
        ImageView image_response = (ImageView) view.findViewById(R.id.imgv_image_response);
        LinearLayout layout_password = (LinearLayout) view.findViewById(R.id.layout_password);
        TextView txv_password = (TextView) view.findViewById(R.id.txv_show_password);

        if(arguments.containsKey(KEY.PASSWORD)){
            layout_password.setVisibility(View.VISIBLE);
            txv_password.setText(arguments.getString(KEY.PASSWORD));
        }else{
            layout_password.setVisibility(View.GONE);
        }

        txv_message.setText(message);

        if (type == Messages.OK) {
            image_response.setImageResource(R.drawable.ic_ok);

        }else if(type == Messages.ERROR){
            image_response.setImageResource(R.drawable.ic_warning);
        }

        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);

        return view;

    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
