package com.combination.qrcodescan.intent;

import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * 支付宝是通过https
 */
public class AliPayAwakeIntent extends HttpAwakeIntent implements ProtocolParsable{
    @Override
    public Intent parse(@NonNull String data, @NonNull String prefix) {
        return super.parse(data, prefix);
    }
}
