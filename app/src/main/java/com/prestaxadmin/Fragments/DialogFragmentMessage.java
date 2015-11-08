package com.prestaxadmin.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prestaxadmin.Activities.MainActivity;
import com.prestaxadmin.Networking.KEY;
import com.prestaxadmin.Networking.RESULTCODE;
import com.prestaxadmin.Networking.RequestType;
import com.prestaxadmin.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragmentMessage extends DialogFragment implements OnClickListener{

    public static final String TAG = "DialogFragmentMessage";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";

    private LinearLayout linear_container_payment_reference;
    private TextView txv_payment_reference;
    private EditText edt_payment_reference;
    private Button btn_send_payment_reference;
    private Bundle arguments;

    public static DialogFragmentMessage newInstance(String message, boolean type){
        DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putBoolean(TYPE, type);
        dialogFragmentMessage.setArguments(bundle);
        return dialogFragmentMessage;
    }

    public static DialogFragmentMessage newInstanceWithCredentials(String message, boolean type, String password){
        DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putBoolean(TYPE, type);
        bundle.putString(KEY.PASSWORD, password);
        dialogFragmentMessage.setArguments(bundle);
        return dialogFragmentMessage;
    }

    public static DialogFragmentMessage newInstanceWithCredentialsAndReference(String message, boolean type,String folio, String password, String reference){
        DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        bundle.putBoolean(TYPE, type);
        bundle.putString(KEY.FOLIO, folio);
        bundle.putString(KEY.PASSWORD, password);
        bundle.putString(KEY.PAYMENT_REFERENCE, reference);
        dialogFragmentMessage.setArguments(bundle);
        return dialogFragmentMessage;
    }

    public DialogFragmentMessage() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arguments = getArguments();
        String message = arguments.getString(MESSAGE);
        boolean type = arguments.getBoolean(TYPE);

        View view = inflater.inflate(R.layout.fragment_dialog_fragment_message, container, false);
        Button btn_close = (Button) view.findViewById(R.id.btn_close_dialog_message);
        btn_close.setOnClickListener(this);

        TextView txv_message = (TextView) view.findViewById(R.id.txv_message_service);
        ImageView image_response = (ImageView) view.findViewById(R.id.imgv_image_response);
        LinearLayout layout_password = (LinearLayout) view.findViewById(R.id.layout_password);
        TextView txv_password = (TextView) view.findViewById(R.id.txv_show_password);

        linear_container_payment_reference = (LinearLayout) view.findViewById(R.id.linear_container_payment_reference);
        txv_payment_reference = (TextView) view.findViewById(R.id.txv_payment_reference);
        edt_payment_reference = (EditText) view.findViewById(R.id.edt_payment_reference);
        btn_send_payment_reference = (Button) view.findViewById(R.id.btn_send_payment_reference);
        btn_send_payment_reference.setOnClickListener(this);

        if(arguments.containsKey(KEY.PASSWORD)){
            layout_password.setVisibility(View.VISIBLE);
            txv_password.setText(arguments.getString(KEY.PASSWORD));

            if (arguments.containsKey(KEY.PAYMENT_REFERENCE)) {

                linear_container_payment_reference.setVisibility(View.VISIBLE);

                if (arguments.getString(KEY.PAYMENT_REFERENCE) == "0") {
                    txv_payment_reference.setText(getString(R.string.message_input_payment_reference));
                    btn_send_payment_reference.setVisibility(View.VISIBLE);
                } else {
                    txv_payment_reference.setText(getString(R.string.message_show_payment_reference));
                    edt_payment_reference.setText(arguments.getString(KEY.PAYMENT_REFERENCE));
                    edt_payment_reference.setEnabled(false);
                    edt_payment_reference.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }

        }else{
            layout_password.setVisibility(View.GONE);
        }

        txv_message.setText(message);

        if (type)
            image_response.setImageResource(R.drawable.ic_ok);
        else
            image_response.setImageResource(R.drawable.ic_warning);

        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close_dialog_message:
                dismiss();
                break;
            case R.id.btn_send_payment_reference:
                if (!edt_payment_reference.getText().toString().isEmpty()){
                    Map<String, String> data = new HashMap<>();
                    data.put(KEY.FOLIO, arguments.getString(KEY.FOLIO));
                    data.put(KEY.PAYMENT_REFERENCE, edt_payment_reference.getText().toString());
                    ((MainActivity)getActivity()).sendRequest(RequestType.REFERENCE, data);

                }else{
                    Toast.makeText(getActivity(), getString(R.string.message_invalid_payment_reference), Toast.LENGTH_SHORT).show();
                }
        break;
        }

    }
}
