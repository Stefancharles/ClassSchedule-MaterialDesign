package com.stefan.classscheduleforusc.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.stefan.classscheduleforusc.BaseActivity;
import com.stefan.classscheduleforusc.R;
import com.stefan.classscheduleforusc.utils.RequestPermission;

public class SettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().add(R.id.content, new SettingFragment()).commit();
        initBackToolbar(getString(R.string.setting));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * fragment申请权限用到
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
