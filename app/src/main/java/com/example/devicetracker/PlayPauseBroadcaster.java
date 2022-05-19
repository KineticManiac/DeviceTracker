package com.example.devicetracker;

import android.content.Context;
import android.content.Intent;

public final class PlayPauseBroadcaster {
    private static final String ACTION = "com.example.devicetracker.PLAY_PAUSE_BROADCAST_ACTION";

    private final Context context;

    public PlayPauseBroadcaster(Context context){
        this.context = context;
    }

    public void sendBroadcast(boolean playing){
        Intent intent = new Intent(ACTION);
        intent.putExtra("play", playing);
        context.sendBroadcast(intent);
    }

    public void play(){
        sendBroadcast(true);
    }

    public void pause(){
        sendBroadcast(false);
    }
}
