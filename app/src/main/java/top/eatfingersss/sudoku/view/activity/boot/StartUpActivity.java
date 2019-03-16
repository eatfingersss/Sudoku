package top.eatfingersss.sudoku.view.activity.boot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.view.activity.main.MainActivity;


public class StartUpActivity extends AppCompatActivity {
    private final int STARTUP_DISPLAY_LENGHT = 3000; // 两秒后进入系统

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        ImageView imageView = (ImageView) findViewById(R.id.startUpImageView);
        Glide.with(this).load(R.drawable.start_up).into(imageView);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(StartUpActivity.this,
                        MainActivity.class);
                StartUpActivity.this.startActivity(mainIntent);
                StartUpActivity.this.finish();
            }

        }, STARTUP_DISPLAY_LENGHT);

    }
}
