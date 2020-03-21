package com.combination.qrcodescan.intent;

import android.content.Intent;

import androidx.annotation.NonNull;

public class WeChatPayAwakeIntent implements ProtocolParsable{
    @Override
    public Intent parse(@NonNull String data, @NonNull String prefix) {
        // 这里似乎需要去申请API
//        if("wxp".equals(prefix.toLowerCase())){
//            return convertToIntent(data, prefix) ;
//        }
        return null;
    }
}
