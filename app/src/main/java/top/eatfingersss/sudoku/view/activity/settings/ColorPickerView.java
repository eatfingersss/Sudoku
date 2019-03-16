package top.eatfingersss.sudoku.view.activity.settings;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.control.PublicInformation;
import top.eatfingersss.sudoku.framework.view.holocolorpicker.*;
import top.eatfingersss.sudoku.view.MessageBox;

class XmlAndMainLayout{
    public String xml;
    public String mainLayout;
}
class BgcAndTc{
    public String backgroundColorKey,textColorKey;

    public BgcAndTc(String backgroundColor, String textColor) {
        this.backgroundColorKey = backgroundColor;
        this.textColorKey = textColor;
    }
}
class IntColor{//like #ffcdsa
    public static String intToString(int color)
    {
        return null;
    }
}
class StringColor{//like 2812471921
    public String color;
}

/**
 * Fragment that demonstrates how to use {@link ColorPicker} in combination with {@link SVBar} and
 * {@link OpacityBar}.
 */
public class ColorPickerView extends AppCompatActivity {

    private final int DEFAULT_COLOR=Color.argb(255, 255, 0, 0);

    private ColorPicker colorPicker;
    private SVBar svBar;
    private OpacityBar opacityBar;
    private View colorSquare;
    private Button button;
    private Switch switchSetting;
    protected TextView textViewColor,textViewSrc,textViewExa;


//    private LinearLayout colorPickerTop;

    protected ColorPickerExample selectColorPickerExample;
    private ColorPickerExample[] colorPickerExamples;
    private BgcAndTc[] bgcAndTcs;
//    private TextView textDefault0,textDefault1;//示例文本
//    private FrameLayout frameLayout0,frameLayout1;//用外部框架当作文本被选中时的边框

    public boolean isText=false;//背景/文字  开关
    public boolean colorPickerOnColorChange_able=true;//取色器监听启用
    public boolean enableSwitchSetting=true;//背景/文字设置转换启用


    /**
     * 菜单栏返回的时候保存
     * */
    private Handler mHandler = new Handler();
    private Runnable mFinish = new Runnable(){
        @Override public void run() {
            finish();
        }
    };
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        dataStore();
        mHandler.postDelayed(mFinish, 0);
        //return super.onKeyDown(keyCode, event);
        return false;
    }


    /**
     * 在标题栏上的返回 并保存数据
     */
    public void addBackButton(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                dataStore();//保存数据
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *常规操作
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        addBackButton();
        nodeGet();
        colorPicker.setColor(DEFAULT_COLOR);
        colorPicker.setOldCenterColor(DEFAULT_COLOR);

        String arg=getIntent().getStringExtra("json");
        try {

            JSONObject json=new JSONObject(arg);
            dataGet(json);
        } catch (JSONException e) {
            MessageBox.showMessage(this,e.getMessage());
        }

        //listenerAdd
        colorPicker.setOnColorChangedListener(new ColorPickerOnChange());

        if(enableSwitchSetting==false)
            switchSetting.setEnabled(false);
        switchSetting.setOnCheckedChangeListener(new SwitchOnChange());
//        button.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                dataStore();
//            }
//
//        });
//        button.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                colorPicker.setOldCenterColor(colorPicker.getColor());
//            }
//        });

        if(PublicInformation.developerMode)
            textViewSrc.setText("old:"+colorPicker.getOldCenterColor()+
                ",\n"+colorPicker.getColor());

    }

    private TextView getTextViewByType(String type,int num){
        if(type.equals("default")&&num==0)
            return (TextView) findViewById(
                    R.id.default_block_color_textView_default0);
        else if (type.equals("default")&&num==1)
            return (TextView) findViewById(
                    R.id.default_block_color_textView_default1);
        else
            return (TextView) findViewById(
                    R.id.other_block_textView_default0);
    }
    private FrameLayout getFrameLayoutByType(String type,int num){
        if(type.equals("default")&&num==0)
            return (FrameLayout) findViewById(
                    R.id.default_block_color_textView_default0_frame);
        else if (type.equals("default")&&num==1)
            return (FrameLayout) findViewById(
                    R.id.default_block_color_textView_default1_frame);
        else
            return (FrameLayout) findViewById(
                    R.id.other_block_textView_default0_frame);
    }

    @SuppressLint("ResourceType")
    void dataGet(JSONObject json) throws JSONException {
        String type=json.getString("example_type");
        setTitle(type);
        //手动引入xml
        {
            final LayoutInflater inflater = LayoutInflater.from(this);
            // 获取需要被添加控件的布局
            @SuppressLint("ResourceType") final LinearLayout lin =
                    (LinearLayout) findViewById(R.id.color_picker_top);
            // 获取需要添加的布局
            LinearLayout layout ;

            if(type.equals("default"))//默认块选取
                layout = (LinearLayout) inflater.inflate(
                        R.layout.content_default_block_color, null).findViewById(R.id.default_block_color_main);
            else//其他的
                layout = (LinearLayout) inflater.inflate(
                        R.layout.content_other_block_color, null).findViewById(R.id.other_block_color_main);

            if(type.equals("repeated"))//重复块的背景被禁用||type.equals("default")
                enableSwitchSetting = false;//先禁用开关监听时间

            //必要的外观调整
//        layout.setGravity(Gravity.CENTER);//由于不能设置layoutGravity，遂从父元素直接改
            //只要求自身的大小,适用于gravity
            layout.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
            // 将布局加入到当前布局中
            lin.addView(layout);
        }
//        String type=json.getString("example_type");
//        if(type.equals("default"))//默认快选取
//            addContentView(findViewById(R.layout.content_default_block_color),);
//        else;

        int count=json.getInt("example_count");
        colorPickerExamples=
                new ColorPickerExample[count];//count示例块个数
        //记录下key以便存储
        bgcAndTcs=new BgcAndTc[count];
        //打开共享参数
        SharedPreferences sps = getSharedPreferences("block_color", Context.MODE_PRIVATE);
        JSONArray source=json.getJSONArray("key_name");
        String res="";
        for(int i=0;i<count;i++)
        {
//            int bgc=
//            temp.textView.setBackgroundColor(bgc);
//            temp.backgroundColor
            JSONArray one=source.getJSONArray(i);
            int color1=Color.WHITE,color2=Color.BLACK;//随手写的
            try {

                bgcAndTcs[i]=new BgcAndTc(one.getString(0),one.getString(1));

                //BackgoundColorKey  TextColorKey
                String bgcKey=sps.getString(one.getString(0),DEFAULT_COLOR+"");
                String tcKey=sps.getString(one.getString(1),DEFAULT_COLOR+"");

                color1=Color.parseColor(bgcKey);
                color2=Color.parseColor(tcKey);

//                color1=Color.parseColor(
//                            sps.getString(one.getString(0),DEFAULT_COLOR+""));
//                color2=Color.parseColor(
//                            sps.getString(one.getString(1),DEFAULT_COLOR+""));

//            int color1=sps.getInt(one.getString(0),DEFAULT_COLOR),
//                    color2=sps.getInt(one.getString(1),DEFAULT_COLOR);

            }catch (IllegalArgumentException e)
            {
                MessageBox.showMessage(this,"颜色数据获取失败,don't touch");
                return;
            }
            res+="\nbgc:"+color1+","+Integer.toHexString(color1)+"\ntxc:"+color2+","+Integer.toHexString(color1);

            ColorPickerExampleBlock temp1 = new ColorPickerExampleBlock(
                    getTextViewByType(type,i),
                    getFrameLayoutByType(type,i),
                    color1,color2
            );

            colorPickerExamples[i]=
                    new ColorPickerExample(temp1,
                            new ColorPickerView.TextOnClick());
        }
        if(colorPickerExamples.length>1)//如果有两个即默认快则需要链接another
        {
            colorPickerExamples[0].another = colorPickerExamples[1];
            colorPickerExamples[1].another = colorPickerExamples[0];
        }

        //默认选中第一个
        colorPickerExamples[0].select(colorPicker);
        //之后selectColorPickerExample才有意义

        //不喜欢的写法，但是到现在为止想不出更好的
        if(enableSwitchSetting == false) {
            switchSetting.setChecked(true);//转向右边，即定死再字体颜色
            //因为此时事件还没注册，所以手动调用
            new SwitchOnChange().onCheckedChanged(switchSetting,true);
        }
//        if(type.equals("default")) {
//            switchSetting.setChecked(false);//转向右边，即定死再字体颜色
//            //因为此时事件还没注册，所以手动调用
//            new SwitchOnChange().onCheckedChanged(switchSetting,true);
//        }else if(type.equals("repeated")){
//            switchSetting.setChecked(true);//转向右边，即定死再字体颜色
//            //因为此时事件还没注册，所以手动调用
//            new SwitchOnChange().onCheckedChanged(switchSetting,true);
//        }

//        if(true)
//        {//两个示例块
//
//            temp1.textView =
//            temp1.frameLayout = ;
//            temp1.backgroundColor = Color.RED;
//            temp1.textView.setBackgroundColor(temp1.backgroundColor);

//            ColorPickerExampleBlock temp1 = new ColorPickerExampleBlock(
//                    (TextView) findViewById(R.id.textView_default0),
//                    (FrameLayout) findViewById(R.id.textView_default0_frame),
//
//                    );
//        ColorPickerExample.DEFAULT_COLOR= Color.WHITE;
//
//            colorPickerExamples[0] =
//                    new ColorPickerExample(temp1,
//                            new ColorPickerView.TextOnClick());
//
//            temp1 = new ColorPickerExampleBlock();
//            temp1.textView = (TextView) findViewById(R.id.textView_default1);
//            temp1.frameLayout = (FrameLayout) findViewById(R.id.textView_default1_frame);
//            temp1.backgroundColor = Color.BLUE;
//            temp1.textView.setBackgroundColor(temp1.backgroundColor);
//
//            colorPickerExamples[1] =
//                    new ColorPickerExample(temp1,
//                            new ColorPickerView.TextOnClick());
//
//            colorPickerExamples[0].another = colorPickerExamples[1];
//            colorPickerExamples[1].another = colorPickerExamples[0];
//        }
//        else{//一个示例块
//            ColorPickerExampleBlock temp1 = new ColorPickerExampleBlock();
//
//            temp1.textView = (TextView) findViewById(R.id.textView_default0);
//            temp1.frameLayout = (FrameLayout) findViewById(R.id.textView_default0_frame);
//            temp1.backgroundColor = Color.RED;
//            temp1.textView.setBackgroundColor(temp1.backgroundColor);
//
//
//            colorPickerExamples[0] =
//                    new ColorPickerExample(temp1,
//                            new ColorPickerView.TextOnClick());
//        }
        if(PublicInformation.developerMode) {
            textViewSrc.setText(res);
        }
    }

    void nodeGet(){
//        colorPickerTop = (LinearLayout) findViewById(R.id.color_picker_top);
        colorPicker = (ColorPicker) findViewById(R.id.color_picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        switchSetting=(Switch)findViewById(R.id.switch_setting);
        textViewColor=(TextView)findViewById(R.id.textView_color);
        textViewSrc=(TextView)findViewById(R.id.textView_src);
        //colorSquare = findViewById(R.id.color_square);
        //button = (Button) findViewById(R.id.button2);

        colorPicker.addSVBar(svBar);
        colorPicker.addOpacityBar(opacityBar);

        ColorPickerExample.colorPickerView=this;
    }

    public void dataStore(){
        SharedPreferences.Editor editor=
                getSharedPreferences("block_color",Context.MODE_PRIVATE)
                        .edit();
        int count=0;//不好的写法
        String str="";
        for(ColorPickerExample one :colorPickerExamples)//只-有一个或两个
        {
            String bgC="#"+Integer.toHexString(
                    one.colorPickerExampleBlock.getBackgroundColor()),
                   tC="#"+Integer.toHexString(
                            one.colorPickerExampleBlock.getTextColor());

            str+="\nbc:"+bgC;
            str+=",t:"+tC;

            editor.putString(bgcAndTcs[count].backgroundColorKey,bgC);
            editor.putString(bgcAndTcs[count].textColorKey,tC);

            count++;//每次都忘了 ，淦!
        }
        /**commit() 提交后立刻执行并返回结果
         * apply()  提交后等待空闲时再执行，不返回结果
         *                          ————from 23:42 2019/1/27
         * */

        boolean result=editor.commit();
        if(!result)
            MessageBox.showMessage(this,"保存失败:"+str);
        ///等待画面
        else MessageBox.showMessage(this,"保存成功");
    }


    public void svBarRevise(){//时期一直为0.5
         svBar.setValue((float)0.5);
    }

    class SwitchOnChange implements Switch.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isText=isChecked;
                selectColorPickerExample.select(colorPicker);
        }
    }

    //取色器颜色变化事件
    class ColorPickerOnChange implements  ColorPicker.OnColorChangedListener {
        @Override
        public void onColorChanged(int color) {
            if(colorPickerOnColorChange_able)//只有启用时才执行
            {
                if (isText)//如果改的时字体颜色
                    selectColorPickerExample.setTextColor(color);
                else//如果改的是背景颜色
                    selectColorPickerExample.setBackgroundColor(color);
                if(PublicInformation.developerMode)
                    textViewColor.setText(color + "," + Integer.toHexString(color));
            }
        }
    }

    //示例文本被选中事件
    class TextOnClick implements  View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView target=(TextView)v;
            for(ColorPickerExample one : colorPickerExamples)
            {
                boolean result=one.selectIt(target,colorPicker);
                if(result){
                    selectColorPickerExample=one;
                    return;
                }
            }
            MessageBox.showMessage(ColorPickerExample.colorPickerView,"没有文本被选中");
        }
    }



}

class ColorPickerExampleBlock{
    private TextView textView;
    private FrameLayout frameLayout;
    private int backgroundColor;//因为textView没法获取背景色  很难受

    public TextView getTextView() {
        return textView;
    }
    public void setBorderColor(int color){//被选中时显示的颜色
        frameLayout.setBackgroundColor(color);
    }
    public void setTextColor(int color){
        textView.setTextColor(color);
    }
    public void setBackgroundColor(int color){
        textView.setBackgroundColor(color);
        backgroundColor=color;
    }
    public int getTextColor(){
        return textView.getCurrentTextColor();
    }
    public int getBackgroundColor(){
        return backgroundColor;
    }
    public ColorPickerExampleBlock(TextView textView,FrameLayout frameLayout,int color1,int color2)
    {
        this.textView=textView;
        this.frameLayout=frameLayout;
        setBackgroundColor(color1);
        setTextColor(color2);
    }
    public void register(ColorPickerView.TextOnClick target){
        this.textView.setOnClickListener(target);
    }

}

class ColorPickerExample{
    //优化以后再写吧
    public static int SELECT_COLOR=0x61ff0000;
    public static int DEFAULT_COLOR=0x00000000;//透明
    public static ColorPickerView colorPickerView;//主体的备份
    public ColorPickerExampleBlock colorPickerExampleBlock;
    private boolean isSelect;
    public ColorPickerExample another=null;//用于找另一个



    public ColorPickerExample(ColorPickerExampleBlock colorPickerExampleBlock,ColorPickerView.TextOnClick target) {
        this.colorPickerExampleBlock=colorPickerExampleBlock;
        this.isSelect=false;
        colorPickerExampleBlock.register(target);
    }
    public void setBackgroundColor(int color){
        colorPickerExampleBlock.setBackgroundColor(color);
    }
    public void setTextColor(int color){
        colorPickerExampleBlock.setTextColor(color);
    }
    public void select(ColorPicker colorPicker){//通过isText判断显示的主体（背景/文字）
        colorPickerView.colorPickerOnColorChange_able=false;//先禁用取色器监听
        //设置默认颜色
        if(colorPickerView.isText)//选中的是文字
        {
            colorPicker.setColor(colorPickerExampleBlock.getTextColor());
            colorPicker.setOldCenterColor(colorPicker.getColor());
        }
        else {
            colorPicker.setColor(colorPickerExampleBlock.getBackgroundColor());
            colorPicker.setOldCenterColor(colorPicker.getColor());
        }
        //colorPickerView.svBarRevise();//饱和度修正

        colorPickerView.colorPickerOnColorChange_able=true;//取色器监听 启用

        isSelect=true;
        colorPickerExampleBlock.setBorderColor(SELECT_COLOR);

        colorPickerView.selectColorPickerExample=this;
        if(PublicInformation.developerMode)
            colorPickerView.textViewSrc.setText("old:"+colorPicker.getOldCenterColor()+
                                                ",\n"+colorPicker.getColor());
    }
    public void unSelect(){
        isSelect=false;
        colorPickerExampleBlock.setBorderColor(DEFAULT_COLOR);
    }
    public boolean selectIt(TextView target,
                            ColorPicker colorPicker){
        if(target==colorPickerExampleBlock.getTextView()) {
            select(colorPicker);
            if(this.another!=null)this.another.unSelect();
            return true;
        }
        return false;
    }
}