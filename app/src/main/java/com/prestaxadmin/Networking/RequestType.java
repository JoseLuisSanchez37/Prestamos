package com.prestaxadmin.Networking;

import java.lang.ref.Reference;

/**
 * Created by jose.sanchez on 06/09/2015.
 */
public enum RequestType {
    SIGN_IN     ("login"),
    REGISTER    ("register"),
    SEARCH      ("searchwithoutpassword"),
    REFERENCE   ("payment_reference");

    private final String method;

    RequestType(final String method){
        this.method = method;
    }
    public String getMethod(){
        return method;
    }

}
