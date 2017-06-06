package com.jeepc.sharegif;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener{

    private View mVSelect;
    private ImageView mIvRecentPic;
    private TranslateAnimation shakeAnim;

    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mVSelect = findViewById(R.id.v_select);
        mVSelect.setOnClickListener(this);
        mIvRecentPic = (ImageView) findViewById(R.id.iv_recent_pic);
        initAnimation();

    }

    private void initAnimation() {
        int range = DensityUtil.px2dip(this,10);
        shakeAnim = new TranslateAnimation(0, 0, range, -range);
        shakeAnim.setInterpolator(new OvershootInterpolator());
        shakeAnim.setDuration(1000);
        shakeAnim.setRepeatCount(10);
        shakeAnim.setRepeatMode(Animation.REVERSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                mIvRecentPic.setImageBitmap(bitmap);
                mIvRecentPic.startAnimation(shakeAnim);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.v_select){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/gif");
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }
}
