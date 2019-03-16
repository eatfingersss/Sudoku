package top.eatfingersss.sudoku.view.activity.boot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.view.activity.main.MainActivity;


public class WaittingActivity extends AppCompatActivity {
    private final int STARTUP_DISPLAY_LENGHT = 3000; // 两秒后进入系统
    private int resView,resDraw;//R.layout.activity_start_up
    private Class<?> tarCls;

    WaittingActivity(int resView, int resDraw,Class<?> cls ){
        this.resView = resView;
        this.resDraw = resDraw;
        this.tarCls = cls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resView);

        ImageView imageView = (ImageView) findViewById(R.id.startUpImageView);
        Glide.with(this).load(resDraw).into(imageView);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(WaittingActivity.this,
                        tarCls);
                WaittingActivity.this.startActivity(mainIntent);
                WaittingActivity.this.finish();
            }

        }, STARTUP_DISPLAY_LENGHT);

    }
}
