package com.prestaxadmin.Listeners;

import com.prestaxadmin.Networking.RequestType;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jose.sanchez on 06/09/2015.
 */
public interface ListenerVolleyResponse {
    public void onResponse(JSONObject response);
    public void sendRequest(RequestType requestType, Map<String, String> params);
}
