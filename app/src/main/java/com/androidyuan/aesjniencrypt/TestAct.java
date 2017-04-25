package com.androidyuan.aesjniencrypt;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidyuan.aesjni.AESEncrypt;
import com.androidyuan.aesjni.SignatureTool;

/**
 * Copyright (c) 2016. BiliBili Inc.
 * Created by wei on 17-3-1 ,email:602807247@qq.com
 */

public class TestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toast(SignatureTool.getSignature(this)+"");

        test();

        testBaseType();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void d(String s) {

        Log.d("TestModel", s);
    }



    private void test() {


        d("MAX_MEM:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");



        //1.会OOM 局部字段确实 释放是需要等到方法执行完毕才能释放
//            TestModel model = new TestModel();
//            TestModel model2 = new TestModel();
//            TestModel model3 = new TestModel();
//            TestModel model4 = new TestModel();


        //2.不会OOM，由此可见 无指引临时字段 不需要等到方法执行完毕 就可以释放
//            new TestModel();
//            new TestModel();
//            new TestModel();
//            new TestModel();


        // 3.这里无效的因为本身 不可释放就不可释放，写GC 也不可被释放
//        TestModel model = new TestModel();
//        System.gc();
//        TestModel model2 = new TestModel();
//        System.gc();
//        TestModel model3 = new TestModel();
//        System.gc();
//        TestModel model4 = new TestModel();
//        System.gc();


        // 4.这里并没有效果 代码执行未离开这个作用域,这个作用域的所有字段都不会被释放

//        TestModel model = new TestModel();
//        System.gc();
//        TestModel model2 = new TestModel();
//        System.gc();
//        TestModel model3 = new TestModel();
//        System.gc();
//        TestModel model4 = new TestModel();
//        System.gc();


        //5.不会OOM   (因为作用域太小了,即使我没有主动调用GC,但是内存达到JVM参数配置的 警戒线会自动触发系统的GC)
            {
                TestModel model = new TestModel();
            }
            {
                TestModel model2 = new TestModel();
            }
            {
                TestModel model3 = new TestModel();
            }
            {
                TestModel model4 = new TestModel();
            }


        // 6.理论上来说=null这行代码，会被JIT编译器删除掉，但是确实有效果了，这里很快看到了 finalize,但是我把 model 里面的内存消耗降到1M就看不到释放了
//        TestModel model = new TestModel();
//        model=null;
//        TestModel model2 = new TestModel();
//        model2=null;
//        TestModel model3 = new TestModel();
//        model3=null;
//        TestModel model4 = new TestModel();
//        model4=null;


        // 7.依旧会触发 finalize  因为那块地址 已经被清空了  同上原理一致
//        TestModel model = new TestModel();
//        model=  new TestModel();
//        model = new TestModel();
//        model =  new TestModel();


        //8.会OOM finalize 并没有什么卵用 java不推荐你用，但是你用了也没有效果根本释放不了的

//        TestModel model = new TestModel();
//
//        try {
//            model.finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        TestModel model2 = new TestModel();
//
//        try {
//            model2.finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        TestModel model3 = new TestModel();
//        try {
//            model3.finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        TestModel model4 = new TestModel();
//        try {
//            model4.finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }


    }




    private void testBaseType() {

        //1.会OOM  但是非基础类型就不会  遵循 上面那一套逻辑 这里作用域虽然小了 但是 这里不讲堆逻辑
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//        }
//        {
//            byte[] mBytes1 = new byte[TestModel.getXmxHalf()];
//        }

        //3.会OOM 因为内存消耗过高 自动触发FullGC  这里写不写GC都没差
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//        }
//        System.gc();
//        {
//            byte[] mBytes1 = new byte[TestModel.getXmxHalf()];
//        }
//        System.gc();


        //3.会OOm
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//        }
//        System.gc(); //写不写这句 没差 因为已经触发了 Full GC
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//        }
//        System.gc();


        //4. 会OOm  请对比后面的一个 测试
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//
//            int a=0;
//        }
//        System.gc();
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//
//            int a=0;
//        }
//        System.gc();


        //3. 不会OOm 比上面的代码多了  这就是 说明局部字段在栈中 不在堆中 释放规则 比较特殊
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//            int a=0;
//        }
////        System.gc();//这句写不写都不会 OOM
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//            int a=0;
//        }
////        System.gc();


        //3. 请对比上面的 代码  不会OOm  这里会自动触发Full GC,而System.gc()只是 major GC
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//            int a=0;
//        }
//        {
//            byte[] mBytes = new byte[TestModel.getXmxHalf()];
//            mBytes=null;
//            int a=0;
//        }


    }


    @Override
    protected void finalize() throws Throwable {
        Log.d("TestModel", "act finalize.");
        super.finalize();
    }

    void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
