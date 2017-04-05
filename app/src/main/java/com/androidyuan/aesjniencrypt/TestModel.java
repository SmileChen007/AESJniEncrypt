package com.androidyuan.aesjniencrypt;

import android.util.Log;
/**
 * Copyright (c) 2016. BiliBili Inc.
 * Created by wei on 17-3-1 ,email:602807247@qq.com
 */

public class TestModel {

    //这个值希望设置为Runtime.getRuntime().maxMemory()-20M 的值,这样子内存中一旦有一个没有被释放则直接OOM
    final byte[] mBytes = new byte[170 * 1024 * 1024];//我的手里的 小米 5 是 256M 差不多 比最大值小几十M就可以了

    @Override
    protected void finalize() throws Throwable {
        Log.d("TestModel","finalize()");
        super.finalize();
    }
}
