package com.androidyuan.aesjniencrypt;

import android.util.Log;
/**
 * Copyright (c) 2016. BiliBili Inc.
 * Created by wei on 17-3-1 ,email:602807247@qq.com
 */

public class TestModel {

    //这个值希望设置为Runtime.getRuntime().maxMemory()-20M 的值,这样子内存中一旦有一个没有被释放则直接OOM
    final byte[] mBytes = new byte[getXmxHalf()];//

    @Override
    protected void finalize() throws Throwable {
        Log.d("TestModel","finalize()");
        super.finalize();
    }

    //内存的最大值取决于 JVM的xmx参数配置 所以这里取一半，只要内存中有两个  即可发生OOM
    public static int getXmxHalf()
    {
        return (int)(Runtime.getRuntime().maxMemory()/2);
    }
}
