package top.eatfingersss.sudoku.view.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import io.flutter.facade.Flutter;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.view.MessageBox;
import top.eatfingersss.sudoku.view.activity.matrixSelect.MatrixSelect;
import top.eatfingersss.sudoku.view.activity.settings.SettingsActivity;




public class MainActivity extends AppCompatActivity {
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //历史遗留问题：java.lang.ClassNotFoundException: Didn't find class "android.view.View$OnUnhandledKeyEventListener"
                    //Rejecting re-init on previously-failed class java.lang.Class<android.support.v4.view.ViewCompat$OnUnhandledKeyEventListenerWrapper>
                    //at void android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(android.view.View, android.support.v4

        setContentView(R.layout.activity_main);

//        View flutterView = Flutter.createView(
//                ChartInit.this,
//                getLifecycle(),
//                "route1"
//        );
//        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(600, 800);
//        layout.leftMargin = 100;
//        layout.topMargin = 200;
//        addContentView(flutterView, layout);

        Button button_settings = findViewById(R.id.button_settings);  // get the button, it is in activity_main.xml
        button_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent =
                        new Intent(MainActivity.this,
                                SettingsActivity.class);
            startActivity(intent);
        } });

        Button button_singleGame = findViewById(R.id.button_singleGame);  // get the button, it is in activity_main.xml
        button_singleGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent =
                        new Intent(MainActivity.this,
                                MatrixSelect.class);
                startActivity(intent);
            } });

        if(isFirstRun()) {
            MessageBox.showMessage(this,"第一次打开，正在加载配置文件...");
            storeData();
            MessageBox.showMessage(this, "加载完毕！");
        }
        else
            MessageBox.showMessage(this, "不是第一次！");


    }


    /**
     * 再按一次退出程序の功能
     * */
    private Handler mHandler = new Handler();
    private Runnable mFinish = new Runnable(){
        @Override public void run() {
            finish();
        }
    };
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((System.currentTimeMillis()-exitTime) > 2000){
            exitTime = System.currentTimeMillis();
            Toast.makeText
            (this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            mHandler.postDelayed(mFinish, 0);
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("first_run",0);
        Boolean first_run = sharedPreferences.getBoolean("first",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("first",false).apply();
            return true;
        }
        else {
            return false;
        }
    }
    private void storeData(){
        SharedPreferences sps = getSharedPreferences("block_color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sps.edit();

        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_default_block0_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_default_block0_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_default_block0_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_default_block0_text_color_default_value)

        );

        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_default_block1_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_default_block1_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_default_block1_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_default_block1_text_color_default_value)
        );


        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_given_block_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_given_block_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_given_block_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_given_block_text_color_default_value)
        );


        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_repeated_block_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_repeated_block_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_repeated_block_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_repeated_block_text_color_default_value)
        );


        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_able_block_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_able_block_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_able_block_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_able_block_text_color_default_value)
        );

        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_select_block_background_color_key),
                getApplicationContext().getString
                        (R.string.data_store_select_block_background_color_default_value)
        );
        editor.putString(
                getApplicationContext().getString
                        (R.string.data_store_select_block_text_color_key),
                getApplicationContext().getString
                        (R.string.data_store_select_block_text_color_default_value)
        );

        editor.apply();

    }
}
