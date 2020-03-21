package com.combination.qrcodescan.intent;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * 协议解释器接口
 * 该接口定义各种协议如何被执行
 */

public interface ProtocolParsable {
    Intent parse(@NonNull String data, @NonNull String prefix);
    default Intent convertToIntent(@NonNull String data, @NonNull String prefix){
        if (prefix == null) prefix = "http" ;
        Uri uri = Uri.parse(prefix+"://"+data);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent ;
    }
}
