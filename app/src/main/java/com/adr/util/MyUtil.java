package com.adr.util;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adr.androidapplication.MainActivity;
import com.adr.androidapplication.R;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Administrator on 2018/1/8.
 */

public class MyUtil extends AppCompatActivity {

    public List<View> nullViewList,allViewList;//所有空格子集合，所有格子集合
    public ViewGroup viewGroup;//获取整个界面对象
    public int mark=0;//分数

    /**
     * 获取到所有没有数值的格子对象
     * @param viewGroup 视图集合
     * @return 返回包含所有TextView的集合
     */
    public List<View> getNullTextView(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return null;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) { // 若是TextView记录下
                TextView textView = (TextView) view;
                if(textView.getText()=="")
                    nullViewList.add(textView);
            } else if (view instanceof ViewGroup) {
                // 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                this.getNullTextView((ViewGroup) view);
            }
        }
        return nullViewList;
    }

    /**
     * 获取所有的显示元素
     * @param viewGroup 视图集合
     * @return 包含所有格子对象
     */
    private List<View> getMyTextView(ViewGroup viewGroup){
        if (viewGroup == null) {
            return null;
        }
        int count = viewGroup.getChildCount();
        Log.i("count",count+"");
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if ((view instanceof TextView)&&!(view instanceof Button)) { // 若是TextView记录并且不是Button下
                Log.i("i",i+"");
                allViewList.add(view);
            } else if (view instanceof ViewGroup) {
                // 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                this.getMyTextView((ViewGroup) view);
            }
        }
        return allViewList;
    }

    /**
     * 获取16个格子的textview
     * @param viewGroup
     * @return
     */
    public List<View> getAllTextView(ViewGroup viewGroup){
        allViewList.clear();//清空原先保存的数据
        List<View> list = new ArrayList<View>();//重新获取所有格子集合
        list = getMyTextView(viewGroup);
        //因为头部有3个同样的TextView，需要删除那是那个TextView
        list.remove(list.get(0));
        list.remove(list.get(0));
        list.remove(list.get(0));
        return list;
    }

    /**
     * 改变方块颜色
     * @param list 传入包含所有格子对象的集合
     */
    public void changeColor(final List list){
        //遍历该集合
        for (int i=0;i<list.size();i++) {
            TextView textView = (TextView) list.get(i);//获取到每一个对象
            textView.setTextColor(getResources().getColor(R.color.colorBlack));//设置文字颜色为黑色
            //判断格子上显示的数值为多少，并通过其数值更换相应的颜色
            switch (textView.getText().toString()) {
                case "":
                    textView.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    break;
                case "2":
                    textView.setBackgroundColor(getResources().getColor(R.color.color2));
                    break;
                case "4":
                    textView.setBackgroundColor(getResources().getColor(R.color.color4));
                    break;
                case "8":
                    textView.setBackgroundColor(getResources().getColor(R.color.color8));
                    break;
                case "16":
                    textView.setBackgroundColor(getResources().getColor(R.color.color16));
                    break;
                case "32":
                    textView.setBackgroundColor(getResources().getColor(R.color.color32));
                    break;
                case "64":
                    textView.setBackgroundColor(getResources().getColor(R.color.color64));
                    break;
                case "128":
                    textView.setBackgroundColor(getResources().getColor(R.color.color128));
                    break;
                case "256":
                    textView.setBackgroundColor(getResources().getColor(R.color.color256));
                    break;
                case "512":
                    textView.setBackgroundColor(getResources().getColor(R.color.color512));
                    break;
                case "1024":
                    textView.setBackgroundColor(getResources().getColor(R.color.color1024));
                    break;
                case "2048":
                    textView.setBackgroundColor(getResources().getColor(R.color.color2048));
                    break;
                case "4096":
                    textView.setBackgroundColor(getResources().getColor(R.color.color4096));
                    break;
                case "8192":
                    textView.setBackgroundColor(getResources().getColor(R.color.color8192));
                    break;
                case "16384":
                    textView.setBackgroundColor(getResources().getColor(R.color.color16384));
                    break;
                default:
                    textView.setBackgroundColor(getResources().getColor(R.color.color64));
                    break;
            }
        }
    }

    /**
     * 添加一个数字
     */
    public void addNumber(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nullViewList.clear();//先将原先获取到空格子集合清空
                        nullViewList = getNullTextView(viewGroup);//重新获取值
                        if (nullViewList.size()>0){//如果还有空格子
                            int result = new Random().nextInt(nullViewList.size());//产生一个随机数
                            if (result==nullViewList.size())//如果随机数产生了最大值，则设定为最小值，（能力不足，因为最大值不知如何小一位，所以只能用这种渣方法）
                                result=0;
                            TextView textView = (TextView)nullViewList.get(result);//通过随机出来的下标绑定一个空格子对象
                            textView.setText("2");//给其赋值为2
                            changeColor(allViewList);//执行一遍换色
                            if (nullViewList.size()==1){//如果只有一个空格子，并且被填满了，则
                                //判断是否还能被移动
                                if (!canNoCanMove()) {//如果不能移动
                                    new MaterialDialog.Builder(MyUtil.this)
                                            .title("失败")
                                            .content("本次得分为"+mark)
                                            .positiveText("新的游戏")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                    cleanData(findViewById(R.id.newGame));
                                                }
                                            })
                                            .negativeText("退出")
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                    String maxMark = ((Button) findViewById(R.id.showMaxMark)).getText().toString();
                                    saveMaxMark(maxMark);
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 滑动时实现数字改变
     */
    public boolean changeToDirection(String dir){
        boolean rtn=false;
        TextView textView1, textView2,textView3,textView4;//定义四个TextView变量，用来保存获取到的一行或者一列所有格子
        int min = -1,min2=-1,min3=-1,max=-1;//定义判断获取到的一行或者一列中所有格子的下标
        for (int colum=1;colum<=4;colum++) {//因为有4行或者4列，所以需要循环四次
            if(dir=="top"||dir=="bottom"){//如果是上下滑动，给下标赋值
                min = colum - 1;//循环的最小值 如：0
                min2 = colum + 3;//循环的最小值 如：4
                min3 = colum + 7;//循环的最小值 如：8
                max = colum + 11;//循环的最大值 如：12
            }else if(dir=="left"||dir=="right"){//如果是左右滑动，给下标赋值
                min = colum*4-4;//循环的最小值 如：0
                min2 = colum*4-3;//循环的第二小 如：1
                min3 = colum*4-2;//循环的第三小 如：2
                max = colum * 4 - 1;//循环的最大值 如：3
            }
            boolean change = true;//判断是否可以被合并
            int sub = -1;//改变的格子的下标
            // 获取到一行或者一列中所有TextView对象
            textView1 = (TextView) allViewList.get(min);
            textView2 = (TextView) allViewList.get(min2);
            textView3 = (TextView) allViewList.get(min3);
            textView4 = (TextView) allViewList.get(max);
            //获取到每个TextView对象被移动前的值
            String num_1 = textView1.getText().toString();
            String num_2 = textView2.getText().toString();
            String num_3 = textView3.getText().toString();
            String num_4 = textView4.getText().toString();
            for (int y = 0; y < 3; y++) {//经测试，循环三次即可全部排列
                //通过三目运算，判断for循环中各个位置的值
                for (int i = (dir == "top"||dir=="left") ? min : max; (dir == "top"||dir=="left") ? i < max : i > min; i = (dir == "top"||dir=="left") ?(dir=="left"?(i+1): (i + 4)) :(dir=="right"?(i-1): (i - 4)) ){//开始循环//从右向左滑//从左开始循环
                    textView1 = (TextView) allViewList.get(i);//将当前格子对象保存给textView1
                    textView2 = (TextView) allViewList.get((dir == "top"||dir=="left") ?(dir=="left"?(i+1): (i + 4)) :(dir=="right"?(i-1): (i - 4)));//将后一个格子对象保存给textView2
                    String num1 = textView1.getText().toString();//将当前格子的值保存给num1
                    String num2 = textView2.getText().toString();//将后一个格子对象的值保存给num2
                    if (num1 == "" && num2 != "") {//如果当前值为空
                        textView1.setText(num2);//将后一个的值移到当前的格子中
                        textView2.setText("");//将后一个格子的值清空
                    } else if (num1 != "" && num1.equals(num2)) {//如果当前格子的值和后一个格子的值相等
                        if (change || (i != sub && ((dir == "top"||dir=="left") ?(dir=="left"?(i+1): (i + 4)) :(dir=="right"?(i-1): (i - 4))) != sub && !change)) {//如果可以合并
                            int addNum = Integer.parseInt(num1) * 2;//定义变量保存合并后的值
                            textView1.setText(addNum + "");//将合并后的值赋值给当前格子对象
                            textView2.setText("");//后面格子的值清空
                            sub = i;//合并的格子其下标保存下来
                            change = false;//设置为不可合并
                            mark+=addNum;//合并后总分数加上此次合并的分数
                            int maxMark = Integer.parseInt(((TextView)findViewById(R.id.showMaxMark)).getText().toString());
                            ((TextView)findViewById(R.id.showMark)).setText(mark+"");//设置总分数
                            ((TextView)findViewById(R.id.showMaxMark)).setText((mark>maxMark?mark:maxMark)+"");//设置最大分数
                        }
                    }
                }
            }
            //判断是否有格子改变，有的话则为true
            if (!num_1.equals(((TextView) allViewList.get(min)).getText().toString())){
                rtn=true;
            }
            if (!num_2.equals(((TextView) allViewList.get(min2)).getText().toString())){
                rtn=true;
            }
            if (!num_3.equals(((TextView) allViewList.get(min3)).getText().toString())){
                rtn=true;
            }
            if (!num_4.equals(((TextView) allViewList.get(max)).getText().toString())){
                rtn=true;
            }
        }
        changeColor(allViewList);//执行一遍换色
        return rtn;
    }

    /**
     * 实现判断格子还能不能移动
     * 放在当最后一个空格子被填满的时候进行判断
     */
    public boolean canNoCanMove(){
        boolean isReturn = false;//记录是否还可以被移动，默认为不能移动
        TextView textView1,textView2,textView3,textView4;//记录当前行或者当前列的四个格子
        int sub1=-1,sub2=-1,sub3=-1,sub4=-1;//设定四个格子的下标，给予初始值
        for (int m=0;m<2;m++) {//判断两次，横着一次，竖着一次
            for (int i = 1; i <= 4; i++) {//不管是横着或者竖着都要执行四次循环
                if (m==0){//如果是横着（第一次用横着判断），开始赋值
                    sub1 = i*4-4;//循环的最小值 如：0
                    sub2 = i*4-3;//循环的第二小 如：1
                    sub3 = i*4-2;//循环的第三小 如：2
                    sub4 = i * 4 - 1;//循环的最大值 如：3
                } else{//如果是竖着
                    sub1 = i-1;//循环的最小值 如：0
                    sub2 = i+3;//循环的第二小 如：4
                    sub3 = i+7;//循环的第三小 如：8
                    sub4 = i+11;//循环的最大值 如：12
                }
                textView1 = (TextView) allViewList.get(sub1);
                textView2 = (TextView) allViewList.get(sub2);
                textView3 = (TextView) allViewList.get(sub3);
                textView4 = (TextView) allViewList.get(sub4);
                if (textView1.getText().toString().equals(textView2.getText().toString()))
                    isReturn = true;
                if (textView2.getText().toString().equals(textView3.getText().toString()))
                    isReturn = true;
                if (textView3.getText().toString().equals(textView4.getText().toString()))
                    isReturn = true;
            }
        }
        return isReturn;
    }

    /**
     * 保存最大分数方法
     * @param maxMark
     */
    public void saveMaxMark(String maxMark){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("maxMark",Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(maxMark);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer!=null){
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取最大分数方法
     * @return
     */
    public String getMaxMark(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("maxMark");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine())!=null){
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public void cleanData(View view){
        //清空所有格子数据
        for (int i=0;i<allViewList.size();i++){
            ((TextView)allViewList.get(i)).setText("");
        }
        //清空得分
        ((Button)findViewById(R.id.showMark)).setText("0");
        mark=0;
        //更换颜色
        changeColor(allViewList);
        String maxMark = ((Button)findViewById(R.id.showMaxMark)).getText().toString();
        saveMaxMark(maxMark);
        view.setVisibility(View.GONE);
        findViewById(R.id.startGame).setVisibility(View.VISIBLE);
    }
}
