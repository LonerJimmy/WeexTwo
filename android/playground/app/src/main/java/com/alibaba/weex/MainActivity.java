package com.alibaba.weex;

import android.os.Bundle;

public class MainActivity extends WeexBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getHost() {
        return "10.12.65.114";
    }

    @Override
    protected String setHotRefreshUrl() {
        return "main";
    }

}

