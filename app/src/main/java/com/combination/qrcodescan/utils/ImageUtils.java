package com.combination.qrcodescan.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    // 图片的默认存放路径
    private static final String SAVE_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/CPQR/" ;

    public static Uri saveBitmap(Context context, Bitmap bm) {
        try {
            String dir= SAVE_DIR_PATH + System.currentTimeMillis()+".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            // 新版本API要求使用FileProvider
            Uri uri = FileProvider.getUriForFile(context,"qrcode_share", f);
            return uri;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
