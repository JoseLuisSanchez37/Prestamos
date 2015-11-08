package com.prestaxadmin.Networking;

import android.content.Context;

import com.prestaxadmin.R;

/**
 * Created by jose.sanchez on 17/09/2015.
 */
public class RESULTCODE {

    public static final int LOGIN_SUCCESS                   = 1;
    public static final int LOGIN_FAILED                    = 2;
    public static final int REGISTER_FOLIO_SUCCESS          = 10;
    public static final int REGISTER_FOLIO_FAILED           = 11;
    public static final int REGISTER_FOLIO_ALREADY          = 12;
    public static final int SEARCH_FOLIO_SUCCESS            = 20;
    public static final int SEARCH_FOLIO_NOT_FOUND          = 21;
    public static final int UPLOAD_BAUCHER_SUCCESS          = 30;
    public static final int UPLOAD_BAUCHER_FAILED           = 31;
    public static final int SEARCH_FOLIO_WITHOUT_SUCCESS    = 40;
    public static final int SEARCH_FOLIO_WITHOUT_NOT_FOUND  = 41;
    public static final int REGISTER_PAY_REFERENCE_SUCCESS  = 50;
    public static final int REGISTER_PAY_REFERENCE_FAILED   = 51;

    public static final boolean SUCCESS = true;
    public static final boolean FAILED = false;

    public static String getMessage(Context context, int resultCode){
        switch (resultCode){
            case LOGIN_SUCCESS:
                return context.getString(R.string.succes_login);
            case LOGIN_FAILED:
                return context.getString(R.string.error_credentials);
            case REGISTER_FOLIO_SUCCESS:
                return context.getString(R.string.provider_register_succesfull);
            case REGISTER_FOLIO_FAILED:
                return context.getString(R.string.provider_insert_error);
            case REGISTER_FOLIO_ALREADY:
                return context.getString(R.string.provider_already_register);
            case SEARCH_FOLIO_WITHOUT_SUCCESS:
                return context.getString(R.string.folio_exist);
            case SEARCH_FOLIO_WITHOUT_NOT_FOUND:
                return context.getString(R.string.folio_not_found);
            case REGISTER_PAY_REFERENCE_SUCCESS:
                return context.getString(R.string.payment_reference_register_success);
            case REGISTER_PAY_REFERENCE_FAILED:
                return context.getString(R.string.payment_reference_register_failed);
            default:
                return context.getString(R.string.error_unknow);
        }
    }
}
