package byeatfingersss.sudoku;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewManager;
import android.widget.*;

import android.widget.Button;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePart extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Map< String ,String> idToName=new HashMap();
    TextView[][] textBox=new TextView[9][9];
    List <TextView> wrongItem=new ArrayList<TextView>();
    TextView darkText=null;

    int light =Color.parseColor("#f5c59d") ;
    int dark =Color.parseColor("#deb887") ;
    int textColor=Color.parseColor("#23212b") ;
    int hardColor=Color.parseColor("#7d6030") ;
    private void dataIn(String[] data){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if(data[i].charAt(j)=='*') textBox[i][j].setText(" ");
                else {
                    textBox[i][j].setText(data[i].charAt(j)+"");
                    textBox[i][j].setBackgroundColor(hardColor);
                }
            }
    }
    private void wrongJudge(){
        String flag="";
        for(TextView value : wrongItem)
        {
            String[] temp=idToName.get(value.getId()+"").split(",");
            int i=Integer.parseInt(temp[0]),j=Integer.parseInt(temp[1]);
            boolean flag_=false;
            for(int x=0;x<9;x++) {
                if(textBox[x][j].getText().equals(textBox[i][j].getText())&&x!=i){
                    flag_=true;
                }
            }
            for(int x=0;x<9;x++){
                if(textBox[i][x].getText().equals(textBox[i][j].getText())&&x!=j){
                    flag_=true;
                }
            }
            for(int x=i/3*3;x<i/3*3+3;x++)
                for(int y=j/3*3;y<j/3*3+3;y++)
                {
                    if(textBox[x][y].getText().equals(textBox[i][j].getText())&&(x!=i||y!=j)){
                        flag_=true;
                    }
                }
            if(!flag_){
                textBox[i][j].setTextColor(textColor);//flag==false
                System.out.println(i+" "+j+" 恢复");
                flag+=i;
                flag+=j;
            }
        }
        for(int i=0;i<flag.length()-1;i+=2){
            wrongItem.remove(textBox[Integer.parseInt(flag.charAt(i)+"")][Integer.parseInt(flag.charAt(i+1)+"")]);
        }
    }
    private void judge(int i,int j){
        for(int x=0;x<9;x++) {
            if(textBox[x][j].getText().equals(textBox[i][j].getText())&&x!=i){
                System.out.println("x="+x+"j="+j);
                textBox[x][j].setTextColor(Color.RED);
                textBox[i][j].setTextColor(Color.RED);
                if(!wrongItem.contains(textBox[i][j]))wrongItem.add(textBox[i][j]);
                if(!wrongItem.contains(textBox[x][j]))wrongItem.add(textBox[x][j]);
            }
            if(textBox[i][x].getText().equals(textBox[i][j].getText())&&x!=j){
                System.out.println("i="+i+"x="+x);
                textBox[i][x].setTextColor(Color.RED);
                textBox[i][j].setTextColor(Color.RED);
                if(!wrongItem.contains(textBox[i][j]))wrongItem.add(textBox[i][j]);
                if(!wrongItem.contains(textBox[i][x]))wrongItem.add(textBox[i][x]);
            }
        }
        for(int x=i/3*3;x<i/3*3+3;x++)
            for(int y=j/3*3;y<j/3*3+3;y++)
            {
                if(textBox[x][y].getText().equals(textBox[i][j].getText())&&(x!=i||y!=j)){
                    System.out.println("x="+x+"y="+y);
                    textBox[x][y].setTextColor(Color.RED);
                    textBox[i][j].setTextColor(Color.RED);
                    if(!wrongItem.contains(textBox[i][j]))wrongItem.add(textBox[i][j]);
                    if(!wrongItem.contains(textBox[x][y]))wrongItem.add(textBox[x][y]);
                }
            }
    }
    class ButtonOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Button target=(Button)v;
            darkText.setText(target.getText());
            String[] temp=idToName.get(darkText.getId()+"").split(",");
            int i=Integer.parseInt(temp[0]),j=Integer.parseInt(temp[1]);
            wrongJudge();
            judge(i,j);
            TextView text= findViewById(R.id.textView_wrong);
            text.setText("");
            for(TextView var:wrongItem)
            {
                text.setText(text.getText()+""+var.getText()+"  ");
            }
        }
    }
    class TextOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            TextView target=(TextView)v;
            ColorDrawable color=(ColorDrawable) target.getBackground();
            if(color.getColor()==hardColor)return;
            if(darkText!=null){
                String[] temp=idToName.get(darkText.getId()+"").split(",");
                int i=Integer.parseInt(temp[0]),j=Integer.parseInt(temp[1]);
//                System.out.println(i+" "+j);
                if(i<3||i>5)
                {
                    if(j<3||j>5) darkText.setBackgroundColor(light);
                    else darkText.setBackgroundColor(dark);
                }
                else{
                    if(j<3||j>5) darkText.setBackgroundColor(dark);
                    else darkText.setBackgroundColor(light);
                }
                darkText=null;
            }

            target.setBackgroundColor(Color.WHITE);
            darkText=target;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_part);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] data={
                "*3*8*****",
                "4******61",
                "******2**",
                "*58****7*",
                "****2****",
                "*7*******",
                "***7*5***",
                "1********",
                "2***6*4**"
        };
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
            {
                Resources res = this.getResources();
//        int titleid=res.getIdentifier(title,//需要转换的资源名称
//                "string",        //资源类型
//                "com.spring.sky.cycle.touch");//R类所在的包名
                int txv_id = res.getIdentifier("textView" + i+j+"", "id", getPackageName());
                textBox[i][j]= findViewById(txv_id);
                textBox[i][j].setOnClickListener(new TextOnClick());
                idToName.put(textBox[i][j].getId()+"",""+ i+","+j);
            }
        for(int i=0;i<=9;i++){
            Resources res = this.getResources();
            int txv_id = res.getIdentifier("button_" + i, "id", getPackageName());
            findViewById(txv_id).setOnClickListener(new ButtonOnClick());
        }
        dataIn(data);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.game_part, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
