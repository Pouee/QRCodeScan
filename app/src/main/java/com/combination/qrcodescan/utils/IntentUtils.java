package com.combination.qrcodescan.utils;


import android.content.Intent;
import android.net.Uri;


import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntentUtils {

    public interface ProtocolParsable{
        // 返回true表示解析成功 不往下执行
        Intent parse(@NonNull String data,@NonNull String prefix);
    }
    private static List<ProtocolParsable> protocolParsables = new ArrayList<>() ;
    static {
        // http协议 后续扩展可以
        protocolParsables.add((d,  p) -> {
            if("http".equals(p.toLowerCase())){
                Uri uri = Uri.parse(d);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                return intent ;
            }
           return null ;
        });
    }
    public static final  Pattern URI_PATTERN = Pattern.compile("(\\w+)://.+");


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
                    Intent intent = p.parse(data, matcher.group(1)) ;
                    if(intent != null) return intent ;
                }
            }
        }
        return null ;
    }
}
