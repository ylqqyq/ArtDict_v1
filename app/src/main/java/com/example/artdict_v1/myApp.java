package com.example.artdict_v1;

import android.app.Application;

import java.util.ArrayList;

public class myApp extends Application {
    private NetworkingService networkingService = new NetworkingService();
    private JsonService jsonService = new JsonService();

    public NetworkingService getNetworkingService() { return networkingService; }

    public JsonService getJsonService() {
        return jsonService;
    }
}
