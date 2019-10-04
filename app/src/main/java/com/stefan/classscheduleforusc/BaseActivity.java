package com.stefan.classscheduleforusc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stefan.classscheduleforusc.app.Constant;
import com.stefan.classscheduleforusc.utils.ActivityUtil;
import com.stefan.classscheduleforusc.utils.LogUtil;
import com.stefan.classscheduleforusc.utils.Preferences;
import com.stefan.classscheduleforusc.utils.ToastUtils;


/**
 * Created by stefan on 17-10-2.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");

        if (canInitTheme()) {
            initTheme();
        }

        ActivityUtil.addActivity(this);
    }



    protected boolean canInitTheme() {
        return true;
    }

    /**
     * 初始化toolbar功能为返回
     *
     * @param title
     */
    protected void initBackToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void gotoActivity(Class clzz) {
        Intent intent = new Intent(this, clzz);
        startActivity(intent);
    }

    protected void initTheme() {
        int anInt = Preferences.getInt(getString(R.string.app_preference_theme), 0);
        setTheme(Constant.themeArray[anInt]);
    }

    public void toast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        ActivityUtil.removeActivity(this);
    }
}
