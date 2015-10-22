package com.prestaxadmin.Networking;

import android.content.Context;

import com.prestaxadmin.R;

/**
 * Created by jose.sanchez on 17/09/2015.
 */
public class Messages {

    public static final int OK = 1;
    public static final int ERROR = 2;

    public static String getResponseFromResultCode(Context context, int resultCode){
        switch (resultCode){
            case 1:
                return context.getString(R.string.succes_login);
            case 2:
                return context.getString(R.string.error_credentials);
            case 10:
                return context.getString(R.string.provider_register_succesfull);
            case 11:
                return context.getString(R.string.provider_insert_error);
            case 12:
                return context.getString(R.string.provider_already_register);
            case 40:
                return context.getString(R.string.folio_exist);
            case 41:
                return context.getString(R.string.folio_not_found);
            default:
                return context.getString(R.string.error_unknow);
        }
    }
}
