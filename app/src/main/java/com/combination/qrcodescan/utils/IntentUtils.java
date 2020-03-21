package com.combination.qrcodescan.utils;


import android.content.Intent;
import android.net.Uri;


import androidx.annotation.NonNull;

import com.combination.qrcodescan.intent.AliPayAwakeIntent;
import com.combination.qrcodescan.intent.HttpAwakeIntent;
import com.combination.qrcodescan.intent.ProtocolParsable;
import com.combination.qrcodescan.intent.WeChatPayAwakeIntent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntentUtils {


    private static List<ProtocolParsable> protocolParsables = new ArrayList<>() ;
    static {
        // http协议 后续扩展可以
        protocolParsables.add(new HttpAwakeIntent());
        protocolParsables.add(new AliPayAwakeIntent());
        protocolParsables.add(new WeChatPayAwakeIntent());
    }
    public static final  Pattern URI_PATTERN = Pattern.compile("(\\w+)://(.+)");


    public static Intent getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent ;
    }
    public static Intent getURIIntent(String data){
        if(data != null ){
            Matcher matcher =  URI_PATTERN.matcher(data) ;
            if(matcher.matches()){
                for (ProtocolParsable p :protocolParsables){
                    Intent intent = p.parse(matcher.group(2), matcher.group(1)) ;
                    if(intent != null) return intent ;
                }
            }
        }
        return null ;
    }
}
