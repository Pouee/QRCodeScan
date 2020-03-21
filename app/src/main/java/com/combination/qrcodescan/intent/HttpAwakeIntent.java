package com.combination.qrcodescan.intent;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class HttpAwakeIntent implements ProtocolParsable{
    @Override
    public Intent parse(@NonNull String data, @NonNull String prefix) {
        if("http".equals(prefix.toLowerCase()) || "https".equals(prefix.toLowerCase())){
            return convertToIntent(data, prefix) ;
        }
        return null ;
    }

}
