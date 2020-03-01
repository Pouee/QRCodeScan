package com.combination.qrcodescan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.combination.qrcodescan.utils.ImageUtils;
import com.combination.qrcodescan.utils.IntentUtils;
import com.combination.qrcodescan.utils.PathUtils;
import com.combination.qrcodescan.utils.StringUtils;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.util.QRCodeProduceUtils;




public class MainActivity extends AppCompatActivity {
    private final int REQ_QR_CODE_SCAN_CAM = 5674 ;
    private final int REQ_QR_CODE_SCAN_GALLERY = 5647 ;
    private final int REQ_QR_CODE_BACKGROUND = 3456 ;
    /**
     * 二维码背景图片
     */
    private Bitmap backgroundImage = null;
    private Bitmap qrCodeBmp = null ;
    EditText inputText;
    Button saveButton ;
    ImageView qrCodeImage;
    EditText qrSize;
    EditText qrMargin;
    EditText qrDotScale;
    CheckBox qrAutoColor;
    EditText qrColorDark;
    EditText qrColorLight;
    Button selectBackground ;
    Button removeBackground ;
    CheckBox qrWhiteMargin;
    CheckBox qrBinarize;
    EditText qrBinarizeThreshold;
    Button createQRCode ;

    private void bindView(){

        inputText = findViewById(R.id.et_input);
        saveButton = findViewById(R.id.btn_save) ;
        qrCodeImage = findViewById(R.id.iv_qrcode);
        qrSize = findViewById(R.id.et_size) ;
        qrMargin = findViewById(R.id.et_margin) ;
        qrDotScale = findViewById(R.id.et_dotScale) ;
        qrAutoColor = findViewById(R.id.cb_autoColor) ;
        qrColorDark = findViewById(R.id.et_colorDark) ;
        qrColorLight = findViewById(R.id.et_colorLight) ;
        selectBackground = findViewById(R.id.btn_background_image) ;
        removeBackground = findViewById(R.id.btn_remove_background_image) ;
        qrWhiteMargin = findViewById(R.id.cb_whiteMargin) ;
        qrBinarize = findViewById(R.id.cb_binarize) ;
        qrBinarizeThreshold = findViewById(R.id.et_binarizeThreshold) ;
        createQRCode = findViewById(R.id.btn_create) ;
    }

    // 创建二维码 并显示
    private void createQRCode(){
        QRCodeProduceUtils.Builder builder = XQRCode.newQRCodeBuilder(inputText.getText().toString())
                .setAutoColor(qrAutoColor.isChecked())
                .setWhiteMargin(qrWhiteMargin.isChecked())
                .setBinarize(qrBinarize.isChecked())
                .setBackgroundImage(backgroundImage);
        if (qrSize.getText().length() != 0) {
            builder.setSize(StringUtils.toInt(qrSize.getText().toString(), 400));
        }
        if (qrMargin.getText().length() != 0) {
            builder.setMargin(StringUtils.toInt(qrMargin.getText().toString(), 20));
        }
        if (qrDotScale.getText().length() != 0) {
            builder.setDataDotScale(StringUtils.toFloat(qrDotScale.getText().toString(), 0.3F));
        }
        if (!qrAutoColor.isChecked()) {
            try {
                builder.setColorDark(Color.parseColor(qrColorDark.getText().toString()));
                builder.setColorLight(Color.parseColor(qrColorLight.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (qrBinarizeThreshold.getText().length() != 0) {
            builder.setBinarizeThreshold(StringUtils.toInt(qrBinarizeThreshold.getText().toString(), 128));
        }
        Bitmap qrCode = builder.build() ;
        qrCodeBmp = qrCode ;
        qrCodeImage.setImageBitmap(qrCode);
    }

    // 初始化事件监听器
    private void initListener(){
        qrAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                qrColorDark.setEnabled(!isChecked);
                qrColorLight.setEnabled(!isChecked);
            }
        });

        qrBinarize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                qrBinarizeThreshold.setEnabled(isChecked);
            }
        });

        createQRCode.setOnClickListener( e ->{
            inputText.setEnabled(false);
            if (!StringUtils.isSpace(inputText.getEditableText().toString())) createQRCode() ;
            else toast("请输入二维码内容");
            inputText.setEnabled(true);
        });

        selectBackground.setOnClickListener( e ->{
            startActivityForResult(IntentUtils.getImageFromGallery(), REQ_QR_CODE_BACKGROUND);
        });

        removeBackground.setOnClickListener(e ->{
            backgroundImage = null ;
            toast("成功去除背景图片");
        });

        saveButton.setOnClickListener(e ->{
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
            if(result == PackageManager.PERMISSION_GRANTED){
                Uri uri = ImageUtils.saveBitmap(this, qrCodeBmp) ;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                Intent chooser = Intent.createChooser(intent, "分享二维码");
                startActivity(chooser);
            }
            else requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qrcode_menu, menu);
        MenuItem scan = menu.findItem(R.id.scan_qrcode);
        MenuItem file = menu.findItem(R.id.file_qrcode);
        file.setOnMenuItemClickListener(e->{
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ;
            if(result == PackageManager.PERMISSION_GRANTED)
                startActivityForResult(IntentUtils.getImageFromGallery(), REQ_QR_CODE_SCAN_GALLERY);
            else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            return true ;
        });
        scan.setOnMenuItemClickListener(e->{
            XQRCode.startScan(this, REQ_QR_CODE_SCAN_CAM);
            return true;
        });

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK) return;
        //处理二维码扫描结果
        if (requestCode == REQ_QR_CODE_SCAN_CAM) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_SUCCESS) {
                        String result = bundle.getString(XQRCode.RESULT_DATA);
                        Intent intent = IntentUtils.getURIIntent(result) ;
                        setClipboardToast(result);
                        if(intent != null) startActivity(intent);
                    } else if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_FAILED) {
                        toast("二维码解析失败");
                    }
                }
            }

        }else if(requestCode == REQ_QR_CODE_SCAN_GALLERY){
            Uri uri = data.getData();
            String result = XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri));
            Intent intent = IntentUtils.getURIIntent(result) ;
            setClipboardToast(result);
            if(intent != null) startActivity(intent);
        }else if(requestCode == REQ_QR_CODE_BACKGROUND){
            Uri uri = data.getData();
            backgroundImage = BitmapFactory.decodeFile(PathUtils.getFilePathByUri(this, uri));
            toast("成功加载背景图片");
        }

    }

    private void setClipboardToast(String str){
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("QRCode", str);
        cm.setPrimaryClip(mClipData);
        toast("二维码解析成功,已复制到剪贴板:"+ str);
    }
    private void toast(CharSequence sequence){
        Toast.makeText(this,sequence,Toast.LENGTH_LONG).show();
    }
}
