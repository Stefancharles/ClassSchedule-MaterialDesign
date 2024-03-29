package com.stefan.classscheduleforusc.mvp.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.text.TextUtils;
import android.view.MenuItem;

import com.stefan.classscheduleforusc.BaseActivity;
import com.stefan.classscheduleforusc.app.Cache;
import com.stefan.classscheduleforusc.utils.ActivityUtil;

public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressLint("RestrictedApi")
        ContentFrameLayout contentFrameLayout = new ContentFrameLayout(this);
        setContentView(contentFrameLayout);

        ActivityUtil.replaceFragmentToActivity(getSupportFragmentManager(),
                HomeFragment.newInstance(), android.R.id.content);
    }

    @Override
    protected boolean canInitTheme() {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch (item.getItemId()) {
        //    case android.R.id.home:
        //        finish();
        //        break;
        //}
        return super.onOptionsItemSelected(item);
    }
}
