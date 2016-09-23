package com.wanbenyu.nerdlauncher;

import android.support.v4.app.Fragment;

/**
 * Created by 本钰 on 2016/9/23.
 */
public class TaskSwitchActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new TaskSwitchFragment();
    }
}
