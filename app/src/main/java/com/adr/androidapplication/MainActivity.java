package com.adr.androidapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adr.util.MyUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends MyUtil {

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//关闭标题栏
        setContentView(R.layout.activity_main);
        init();//初始化数据
    }

    /**
     * 初始化数据
     */
    public void init(){
        viewGroup = findViewById(R.id.line_all);//记录布局集合
        nullViewList = new ArrayList<View>();//实例化(剩下的空格子控件集合)
        allViewList = new ArrayList<View>();//实例化(所有格子控件集合)
        allViewList = getAllTextView(viewGroup);//获取到所有格子数据集合
        String maxMark = getMaxMark();//定义一个总分数变量，保存从数据文件中读到的最大值
        if (maxMark=="")//如果最大值显示为空，
            maxMark="0";//默认为0
        ((Button)findViewById(R.id.showMaxMark)).setText(maxMark);//设置最大分数
    }

    /**
     * 监听手指滑动效果
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50&&Math.abs(x1-x2)<(y1-y2)) {
                if(changeToDirection("top"))//执行向上滑动，如果有数字移动或改变才添加新数字，下同
                    addNumber(MainActivity.this);
            } else if(y2 - y1 > 50&&Math.abs(x2-x1)<(y2-y1)) {
                if(changeToDirection("bottom"))
                    addNumber(MainActivity.this);
            } else if(x1 - x2 > 50&&Math.abs(y1-y2)<(x1-x2)) {
                if(changeToDirection("left"))
                    addNumber(MainActivity.this);
            } else if(x2 - x1 > 50&&Math.abs(y2-y1)<(x2-x1)) {
                if(changeToDirection("right"))
                    addNumber(MainActivity.this);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始游戏
     * @param view
     */
    public void startGame(View view){
        addNumber(MainActivity.this);//新增数字
        view.setVisibility(View.GONE);//隐藏（开始游戏）按钮
        findViewById(R.id.newGame).setVisibility(View.VISIBLE);//显示（新的游戏）按钮
    }

    /**
     * 重新开始
     * @param view
     */
    public void newGame(final View view){

        new MaterialDialog.Builder(this)
                .title("通知")
                .content("即将上传数据并重新开始，是否继续？")
                .positiveText("继续")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        cleanData(view);//执行清空当前数据方法
                    }
                })
                .negativeText("我点错了")
                .show();

    }


}
