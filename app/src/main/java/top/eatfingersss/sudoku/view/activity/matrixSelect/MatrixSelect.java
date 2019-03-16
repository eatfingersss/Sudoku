/**
 * GridView需要Adapter支持数据
 */
package top.eatfingersss.sudoku.view.activity.matrixSelect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import top.eatfingersss.sudoku.framework.view.snappingstepper.listener.SnappingStepperValueChangeListener;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.control.PublicInformation;
import top.eatfingersss.sudoku.framework.view.circularprogressbutton.CircularProgressButton;
import top.eatfingersss.sudoku.framework.view.lqrrefreshbutton.LQRRefreshButton;
import top.eatfingersss.sudoku.framework.view.numberprogressbar.OnProgressBarListener;
import top.eatfingersss.sudoku.framework.view.snappingstepper.SnappingStepper;
import top.eatfingersss.sudoku.model.GetFromHttp;
import top.eatfingersss.sudoku.model.SQLiteDAO;
import top.eatfingersss.sudoku.model.entity.Matrix;
import top.eatfingersss.sudoku.model.entity.ReturnInformation;
import top.eatfingersss.sudoku.view.EncapsilationView;
import top.eatfingersss.sudoku.view.MessageBox;
import top.eatfingersss.sudoku.view.activity.singleGame.SingleGame;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.ArrayList;


public class MatrixSelect extends AppCompatActivity {

    private AdapterAndData adapterAndData;
    private GridView gridView;
    private FloatingActionButton floatingActionButton;
    private TextView nullTextView;
    private TextView txvName,txvDifficulty,
            txvAuthor,txvUploadTime,txvScore,txvDescribe;
//    private NumberBar numberBar;

//    private Class clazz;
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedArrayListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_delete:
//                    mTextMessage.setText(R.string.title_delete);
//                    return true;
//                case R.id.navigation_add:
//                    mTextMessage.setText(R.string.title_add);
//                    return true;
////                case R.id.navigation_notifications:
////                    mTextMessage.setText(R.string.title_notifications);
////                    return true;
//            }
//            return false;
//        }
//    };

    private void nodeGet(){
        gridView=findViewById(R.id.matrix_select_gridView);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        nullTextView = findViewById(R.id.matrix_select_textView_null);

        txvName = findViewById(R.id.activity_matrix_select_textView_name_value);
        txvDifficulty = findViewById(R.id.activity_matrix_select_textView_difficulty_value);
        txvAuthor = findViewById(R.id.activity_matrix_select_textView_author_value);
        txvUploadTime = findViewById(R.id.activity_matrix_select_textView_upload_time_value);
        txvScore = findViewById(R.id.activity_matrix_select_textView_score_value);
        txvDescribe = findViewById(R.id.activity_matrix_select_textView_describe_value);
//        numberBar = new NumberBar();
//        numberBar.nodeGet(this);

    }

    private void listenerAdd(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DialogGetMatrix dialogGetMatrix=
                            new DialogGetMatrix(MatrixSelect.this);
                    dialogGetMatrix.show();
                    //adapterAndData.addItem();
                } catch (Exception e) {
                    MessageBox.showMessage(MatrixSelect.this,e.getMessage());
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //GridViewLoading
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_select);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedArrayListener(mOnNavigationItemSelectedArrayListener);

        nodeGet();listenerAdd();
        gridViewFlush();

        //actionBar上的返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        //开始反射：将toRun()作为参数丢进线程
//        try {
//            clazz = Class.forName("top.eatfingersss.sudoku.view.activity.matrixSelect.MatrixSelect");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        //加载

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void toWait(){
        gridView.setVisibility(GridView.GONE);
        nullTextView.setVisibility(View.VISIBLE);
//        numberBar.setBarVisible();
}

    protected void toRun(){
        gridView.setVisibility(GridView.VISIBLE);
        nullTextView.setVisibility(View.GONE);
//        numberBar.setBarGone();
    }

    private void gridViewFlush(){
        //进入等待模式，进度条可见，gridview隐藏
//        toWait();

//        //反射
//        try {
//            Method method = clazz.getDeclaredMethod("toRun");
//            GridViewLoading gridViewLoading = new GridViewLoading(method,numberBar);
//            gridViewLoading.execute(this);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }

        //不搞那些有的没的，本地数据库贼快
//        toRun();//隐藏
        List<Matrix> matrices = SQLiteDAO.getMatrixesMessageFromDatabase(this);
        if(matrices.isEmpty())//本地数据库空
            toWait();
        else
            toRun();
        adapterAndData = new AdapterAndData(matrices);

    }

    private void setTxv(Matrix matrix){
        txvName.setText(matrix.getName());
        txvDifficulty.setText(matrix.getDifficulty()+"");
        txvAuthor.setText(matrix.getAuthor() == null?"":matrix.getAuthor());
        txvUploadTime.setText(
                PublicInformation.SDF_OUT_TIME.format(matrix.getCreateTime()));
        txvScore.setText(matrix.getScore() == null?"":matrix.getScore());
        txvDescribe.setText(matrix.getDescribe() == null?"":matrix.getDescribe());
    }

    public void gameStart(Matrix matrix){
        Intent intent = new Intent();
        intent.setClass(this,SingleGame.class);
        // 新建Bundle对象
        Bundle mBundle = new Bundle();
        // 放入account对象
        mBundle.putSerializable(
                "matrix",
                SQLiteDAO.getMatrixesById(this,matrix.getId())
        );////
        intent.putExtras(mBundle);
        startActivity(intent);
    }


    /**
     * 应用于GridView的Adapter
     */
    class AdapterAndData{
        private SimpleAdapter adapter;
        public ArrayList<Map<String, Object>> data;
        private List<Matrix> matrices;//存数据
        private View selecting=null;//被选中的item

        AdapterAndData(List<Matrix> matrices){
            data=new ArrayList<Map<String, Object>>();
            this.matrices = new ArrayList<Matrix>();
            for(Matrix matrix : matrices){
                Map<String, Object> item = new HashMap<>();
                //应该是类似于winform里的tag一样的操作
                //为了能适应各种类型所以用Object
                item.put("id", matrix.getId());
                item.put("title", matrix.getName());
                data.add(item);
                this.matrices.add(matrix);
            }
            adapterInit(MatrixSelect.this);
            setAdapter(MatrixSelect.this.gridView);
        }

        public void adapterInit(Context context){
            //适配器初始化
            /**
             * data对应单位xml内 键与值（填充值)
             */
            adapter= new SimpleAdapter
                    (context,data,
                    R.layout.item_matrix_select_selecting,
                    new String[]{"id","title"},
                            //填写的控件必须是可填充位置的,估计就是toString那样取值
                    new int[]{R.id.item_matrix_select_selecting_textView_id,R.id.item_matrix_select_selecting_textView})
            {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final View view=super.getView(position, convertView, parent);

                    ImageButton imgbtnDelete=
                            (ImageButton)view.findViewById(
                                R.id.item_matrix_select_selecting_imageButton_delete);
                    final LinearLayout linearLayout =
                            view.findViewById(R.id.item_matrix_select_selecting);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(selecting != null)
                                        selecting.setBackgroundResource(R.drawable.item_matrix_select_default);
                                    linearLayout.setBackgroundResource(R.drawable.item_matrix_select_selecting);
                                    selecting = linearLayout;

                                    setTxv(matrices.get(position));
                                }
                            });

                    //点击删除事件
                    imgbtnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            android.support.v7.app.AlertDialog.Builder builder =
                                    new android.support.v7.app.AlertDialog.Builder(
                                            MatrixSelect.this);

                            builder.setTitle("真的要删除吗");
                            builder.setMessage("这意味着你再也看不到它了");
                            builder.setPositiveButton("是的(￢︿̫̿￢☆)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(position);
                                }
                            });
                            builder.setNegativeButton("我再想想 (:◎)≡", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.show();
                        }
                    });

                    ImageButton imgbtnAction=(ImageButton)view.findViewById(
                            R.id.item_matrix_select_selecting_imageView_start);
                    imgbtnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //开始游戏
                            //跳转怎么写
                            //生成对话框dialog
                            MatrixSelect.this.gameStart(matrices.get(position));

                        }
                    });

                    return view;
                }
            };
        }

        public void setAdapter(GridView target){
            target.setAdapter(adapter);
        }


//        public ReturnInformation addData(Matrix selecting)
//        {
//
//            return null;
//        }
//        protected void addItem()
//        {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("only",null);
//            data.add(map);
//            adapter.notifyDataSetChanged();
//        }
//        private void reflush(){
//            adapter.notifyDataSetChanged();
//        }

        //删除时不需要重新从数据库读，为了空时改外观还是读一下
        private void deleteItem(int position){
            int size = data.size();
            if( size > 0 )
            {
                Map<String, Object>  discarded= data.remove(position);
                if(discarded.get("id")==null)
                    MessageBox.showMessage(MatrixSelect.this,"这个数据不该存在，联系开发者");
                try{
                    //删库....
                    SQLiteDAO.deleteMatrixById(
                            new View(MatrixSelect.this),//这样真的好么
                            (String)discarded.get("id")
                    );
                    int count = SQLiteDAO.getMatrixesCount(MatrixSelect.this);
                    if (count==0)//发现空库
                        toWait();
                }
                catch (Exception e){
                    data.add(position,discarded);
                    MessageBox.showMessage(MatrixSelect.this,"删除失败:"+e.getMessage());
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 对话框类
     */
    class DialogGetMatrix {
        /**
         * 下拉菜单栏和适配器
         */
        class SpinnerAndAdapter implements EncapsilationView{
            //下拉菜单的适配器
            private ArrayAdapter<String> apporachToGetMatrix;
            //下拉菜单
            private Spinner spinner;

            SpinnerAndAdapter(){
                apporachToGetMatrix = new ArrayAdapter<String>(
                        view.getContext(),
                        //这是一个Android自带的仅含一个TextView的布局，
                        //                          作为spinner的每一个item项
                        R.layout.support_simple_spinner_dropdown_item,
                        view.getResources().getStringArray(R.array.approach_to_get_matrix)
                );
//        apporachToGetMatrix.setDropDownViewResource();
                spinner = view.findViewById(R.id.dialog_get_matrix_spinner);
                spinner.setAdapter(apporachToGetMatrix);
//            spinner.setSelected();

                //监听
                spinner.setOnItemSelectedListener(null);
            }

            @Override
            public void nodeGet() {

            }

            @Override
            public void addListenner() {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0)//随机生成
                            approachGetMatrix.randomModeInit();
                        else if(position ==1)//找现有的
                            approachGetMatrix.establishedModeInit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        return;
                    }
                });
            }
        }



        /**
         * 控制难度的条
         */
        class Stepper implements EncapsilationView, SnappingStepperValueChangeListener{

            private SnappingStepper stepper;
            Stepper(int max,int min){
                nodeGet();
                //设定上下限
                stepper.setMaxValue(max);
                stepper.setMinValue(min);
                stepper.setValue(min);

            }

            public void setValue (int value){
                stepper.setValue(value);
            }

            @Override
            public void nodeGet() {
                stepper = (SnappingStepper) view.findViewById(R.id.stepper);
            }

            @Override
            public void addListenner() {
                stepper.setOnValueChangeListener(this);
            }

            @Override
            public void onValueChange(View view, int value) {

            }

            public void setEnable(Boolean arg){
                stepper.setEnabled_(arg);
            }

            public int getValue(){
                return stepper.getValue();
            }
        }


        /**
         * 应用于GridView的现成获取的Adapter
         */
        class AdapterAndData{
            private SimpleAdapter adapter;
            public ArrayList<Map<String, Object>> data;
            private List<Matrix> matrices;//存数据
            private View selecting=null;//被选中的item
            //显示现有矩阵的gridview及其刷新按钮

            AdapterAndData(List<Matrix> matrices){
                data=new ArrayList<Map<String, Object>>();
                this.matrices = new ArrayList<Matrix>();
                for(Matrix matrix : matrices){
                    Map<String, Object> item = new HashMap<>();
                    //应该是类似于winform里的tag一样的操作
                    //为了能适应各种类型所以用Object
                    item.put("name", matrix.getName());
//                    item.put("img", null);
                    data.add(item);
                    this.matrices.add(matrix);
                }
                adapterInit(view.getContext());
                setAdapter(gridView_established);

            }

            public void adapterInit(Context context){

                //适配器初始化
                /**
                 * data对应单位xml内 键与值（填充值)
                 */
                adapter= new SimpleAdapter
                        (context,data,
                                R.layout.item_dialog_matrix_select_gridview,
                                new String[]{"name"},//,"img"
                                //填写的控件必须是可填充位置的,估计就是toString那样取值
                                new int[]{
                                        R.id.item_dialog_matrix_select_gridview_textView_title
//                                        R.id.item_dialog_matrix_select_gridview_imageView
                                }
                        )
                {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        final View view=super.getView(position, convertView, parent);

                        final LinearLayout linearLayout =
                                view.findViewById(R.id.item_dialog_matrix_select_gridview);

                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(selecting != null)
                                    selecting.setBackgroundResource(R.drawable.item_matrix_select_default);
                                linearLayout.setBackgroundResource(R.drawable.item_matrix_select_selecting);
                                selecting = linearLayout;

                                DialogGetMatrix.this.matrix = matrices.get(position);
                                setTxv(matrices.get(position));
                                createButton.setEnabled(true);
                            }
                        });


                        return view;
                    }
                };
            }

            public void setAdapter(GridView target){
                target.setAdapter(adapter);
            }

        }


        /**
         * 控制获取模式及相应界面
         */
        class ApproachGetMatrix{
            private String mode;//标注模式 random|established

            private void publicInit(){
                matrix = null;
                if(getMatrixTesk != null)getMatrixTesk.cancel(true);
                if(getRandomNameTesk != null)getRandomNameTesk.cancel(true);
                if(getEstablishedMatrixTesk != null)getEstablishedMatrixTesk.cancel(true);
                setCircularButtonStatusReady();
                createButton.setEnabled(false);
            }
            private void randomModeInit(){
                publicInit();
                stepper.setEnable(true);
                llGridView.setVisibility(View.GONE);
                llButton.setVisibility(View.VISIBLE);
                llDifficulty.setVisibility(View.VISIBLE);
                llName.setVisibility(View.VISIBLE);
                llAuthor.setVisibility(View.GONE);
                llUploadTime.setVisibility(View.GONE);
                llScore.setVisibility(View.GONE);
                llDescribe.setVisibility(View.GONE);
            }

            private void establishedModeInit(){
                publicInit();
                stepper.setEnable(false);
                llGridView.setVisibility(View.VISIBLE);
                llButton.setVisibility(View.GONE);
                llName.setVisibility(View.VISIBLE);
                llDifficulty.setVisibility(View.VISIBLE);
                llAuthor.setVisibility(View.VISIBLE);
                llUploadTime.setVisibility(View.VISIBLE);
                llScore.setVisibility(View.VISIBLE);
                llDescribe.setVisibility(View.VISIBLE);
                //由于是刚点开所以先刷新一下
                getEstablishedMatrixTesk = new GetEstablishedMatrixTesk();

                getEstablishedMatrixTesk.execute();

            }
        }


        private static final int MAX_RUN_TIME=PublicInformation.MAX_RUN_TIME;//秒
        private Dialog dialog;
        private View view;
        //控制模式及其显示
        private ApproachGetMatrix approachGetMatrix;
        //三连
        private GetEstablishedMatrixTesk getEstablishedMatrixTesk;
        private GridView gridView_established;
        private LQRRefreshButton progressBarGetEstablishedMatrix;
        //获取现成的矩阵数据并显示的封装类
        private AdapterAndData adapterAndData;
        //等待动画
        private CircularProgressButton circularButton;
        //等待动画的状态（开发模式用）
        private TextView textViewCircularProcressButtonStatus;
        //调整难度的插件
        private Stepper stepper;
        //取名的等待动画
        private LQRRefreshButton progressBarGetRandomName;
        //7+1个布局
        private LinearLayout llGridView,llButton,llName,llAuthor,llDifficulty,llUploadTime,llScore,llDescribe;
        //6-1个文本框
        private TextView txvName,txvAuthor,txvUploadTime,txvScore;
        private EditText mttxDescribe;
        //获取矩阵的方式
        private SpinnerAndAdapter spinnerAndAdapter;
        //两个按钮
        protected Button cancelButton,createButton;
        //最后用来入库的数度题目，不用实例化
        protected Matrix matrix;
        //两个自带超时判定的异步线程
        private GetMatrixTesk getMatrixTesk;
        private GetRandomNameTesk getRandomNameTesk;

//        //判断创造按钮是否启用
//        private int createButtonJudgeBox;
//        private final int CREATE_BUTTON_JUDGE_BOX_MAX = 2;


        public DialogGetMatrix(Context context) {

            nodeGet(context);
            addListener();

            //某种模式启用
            circularButton.setIndeterminateProgressMode(true);
        }


        private void gridViewFlush(List<Matrix> matrices){

            adapterAndData = new AdapterAndData(matrices);
        }
        //将参数写到textView上
        private void setTxv(Matrix args){
            try {

                stepper.setValue(args.getDifficulty());
                txvAuthor.setText(args.getAuthor());
                txvUploadTime.setText(
                        args.getCreateTime()==null?
                                "null":
                                PublicInformation
                                        .SDF_OUT_TIME.format(args.getCreateTime())
                );
                txvScore.setText(args.getScore());
                mttxDescribe.setText(args.getDescribe());
            }catch (Exception e)
            {MessageBox.showMessage(view.getContext(),e.getMessage());}
        }

        //所有实例化
        private void nodeGet(Context context){
            //view对象
            view=LayoutInflater.from(context)
                    .inflate(R.layout.dialog_get_matrix,null);
            //支持对话框功能的dialog
            this.dialog = new Dialog(context);

            this.gridView_established =
                    view.findViewById(R.id.dialog_get_matrix_gridView);
            this.progressBarGetEstablishedMatrix =
                    view.findViewById(R.id.dialog_get_matrix_gridView_refresh_button);
            //控制模式及其显示
            approachGetMatrix = new ApproachGetMatrix();
            //7+1个布局
            llGridView = view.findViewById(R.id.dialog_get_matrix_gridView_linearlayout);
            llButton = view.findViewById(R.id.dialog_get_matrix_linearlayout_getmatrix_linear_layout);
            llName = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_name);
            llDifficulty = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_difficulty);
            llAuthor = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_author);
            llUploadTime = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_upload_time);
            llScore = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_score);
            llDescribe = view.findViewById(R.id.dialog_get_matrix_linearlayout_textView_describe);

            //等待动画兼按钮
            this.circularButton =
                    view.findViewById(R.id.dialog_get_matrix_linearlayout_getmatrix_linear_circularButton);
            textViewCircularProcressButtonStatus=
                    view.findViewById(R.id.dialog_get_matrix_linearlayout_getmatrix_linear_circularButton_status);
            //取名字的等待动画
            this.progressBarGetRandomName =
                    view.findViewById(R.id.activity_matrix_select_btn_refresh_name);
            //6个填充项
            this.txvName =
                    view.findViewById(R.id.dialog_get_matrix_textView_name_value);
//            this.txvDifficulty =
//                    view.findViewById(R.id.dialog_get_matrix_textView_difficulty_value);
            this.txvAuthor =
                    view.findViewById(R.id.dialog_get_matrix_textView_author_value);
            this.txvUploadTime =
                    view.findViewById(R.id.dialog_get_matrix_textView_upload_time_value);
            this.txvScore =
                    view.findViewById(R.id.dialog_get_matrix_textView_score_value);
            this.mttxDescribe =
                    view.findViewById(R.id.dialog_get_matrix_textView_describe_value);
            //下拉菜单及其适配器
            spinnerAndAdapter = new SpinnerAndAdapter();
            //难度选择的插件
            stepper = new Stepper(64,15);
            //两个按钮的实例化
            cancelButton=view.findViewById(R.id.dialog_get_matrix_button_cancel);
            createButton=view.findViewById(R.id.dialog_get_matrix_button_create);
            //确认（创造）按钮先禁用
            createButton.setEnabled(false);

        }

        //所有的监听
        private void addListener() {
            spinnerAndAdapter.addListenner();
            progressBarGetEstablishedMatrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击刷新时执行刷新操作
                    getEstablishedMatrixTesk = new GetEstablishedMatrixTesk();
                    getEstablishedMatrixTesk.execute();
                }
            });

            circularButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (circularButton.getProgress()){
                        case 0://ready
                        case -1://error
                            //获得挖空数量
                            int difficulty = stepper.getValue();
                            stepper.setEnable(false);
                            getMatrixTesk = new GetMatrixTesk(difficulty);
                            getMatrixTesk.executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR,MAX_RUN_TIME);

                        default://running/complete
                            break;//似乎有没有都可以
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(getMatrixTesk != null && !getMatrixTesk.isCancelled()){
                        getMatrixTesk.cancel(true);
                    }

                    if(getRandomNameTesk != null && !getRandomNameTesk.isCancelled()){
                        getRandomNameTesk.cancel(true);
                    }

                    //按cancel退出
                    DialogGetMatrix.this.dismiss();
                }
            });

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //按create将数据写入数据库
//                matrix;
                    //为matrix取名称
                    matrix.setName(txvName.getText().toString());
                    MessageBox.showMessage(view.getContext(),"写入数据库...");
                    try{
                        SQLiteDAO.putMatrixInDatabase(view,matrix);
                    }catch (Exception e){
                        MessageBox.showMessage(view.getContext(),e.getMessage());
                        return;
                    }
                    //刷新GridView
                    MatrixSelect.this.gridViewFlush();
                    DialogGetMatrix.this.dismiss();
                }
            });

            progressBarGetRandomName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //向http要名字
                    GetRandomNameTesk getRandomNameTesk = new GetRandomNameTesk();
                    getRandomNameTesk.execute();

                }
            });

            stepper.addListenner();
        }

        //给下面的按钮状态
        private void setCircularButtonStatus(int status){

            circularButton.setProgress(status);
            if(PublicInformation.developerMode)
                textViewCircularProcressButtonStatus.setText(status+"");
        }
        //按钮状态设置
        private void setCircularButtonStatusComplete(){
            setCircularButtonStatus(100);
        }
        private void setCircularButtonStatusRuning(){
            setCircularButtonStatusReady();
            setCircularButtonStatus(50);
        }
        private void setCircularButtonStatusReady(){
            setCircularButtonStatus(0);
        }
        private void setCircularButtonStatusFail(){
            setCircularButtonStatus(-1);
        }

        //调用
        public void show(){
            dialog.getWindow().setContentView(view);
            dialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }

        public void dismiss(){
            if(dialog != null&& dialog.isShowing()){
                dialog.dismiss();
            }
        }

        public boolean isShowing(){
            if(dialog !=null)
                return dialog.isShowing();
            else return false;
        }



        /**
         * 一个简单的计时器类，结束后会取消数据请求（判定超时）
         */
        class TimeCheaker extends AsyncTask<Integer,Integer,String>{
            protected AsyncTask targetTask;

            TimeCheaker(AsyncTask targetTask){
                this.targetTask = targetTask;
            }

            @Override
            protected void onPreExecute(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"计时器:"+targetTask.toString().split("x")[3]+"就绪");
            }

            @Override
            protected String doInBackground(Integer... integers) {
                String result="";
                try{
                    publishProgress();
                    Thread.sleep(5*1000);
                    result=null;
                }catch (InterruptedException e)
                {
                    result=e.getMessage();
                }
                return result;
            }

            //执行后
            @Override
            protected void onPostExecute(String args){
                int count=1;
                if(args ==null)//计时器线程正常运行,开始取消数据请求
                    while(targetTask.isCancelled()==false){
                        targetTask.cancel(true);
                        if(PublicInformation.developerMode)
                            MessageBox.showMessage(view.getContext(),"计时器取消"+targetTask.toString().split("x")[3]);

                        count++;
                    }
                else//计时器出了问题，报错
                    MessageBox.showMessage(view.getContext(),"计时器"+targetTask.toString().split("x")[3].toString()+"报错:"+args);
            }

            @Override
            protected void onCancelled(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"计时器 from:"+targetTask.toString().split("x")[3].toString()+"被取消");

            }


            @Override
            protected void onProgressUpdate(Integer... values){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"计时器 from:"+targetTask.toString().split("x")[3].toString()+" 正在运行");
            }


        }



        /**
         * 获得已存在的矩阵并刷新gridview
         */
        class GetEstablishedMatrixTesk extends AsyncTask<Integer,Integer, ReturnInformation>{
            //建立计时器线程
            TimeCheaker timeCheaker;
            //计时器实例
            GetEstablishedMatrixTesk(){
                timeCheaker = new TimeCheaker(this);
            }
            //执行前
            @Override
            protected void onPreExecute(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetEstablishedMatrixTesk 就绪");

                progressBarGetEstablishedMatrix.start();
                //这里到底该不该跑这个线程Di
                //向http要名字
//                getRandomNameTesk = new GetRandomNameTesk();
//                getRandomNameTesk.executeOnExecutor(
//                        AsyncTask.THREAD_POOL_EXECUTOR,0);//异步线程

//                //启动计时器
//                timeCheaker.executeOnExecutor(
//                        AsyncTask.THREAD_POOL_EXECUTOR,0);//异步线程
            }

            //执行时  大概就是要做的事情 run()
            @Override
            protected ReturnInformation doInBackground(Integer... integers) {
                publishProgress();

                while(true){
                    try {
                        //开始请求数据
                        GetFromHttp getMatrixByHttp = new GetFromHttp();
                        return new ReturnInformation(
                                true,getMatrixByHttp.getEstablishedMatrix()
                        );
//                        Thread.sleep(1000);
//                        System.out.println("getMatrix");

                    } catch (Exception e) {
                        return new ReturnInformation(false,e.getMessage());
                    }

                }
            }

            //执行后
            @Override
            protected void onPostExecute(ReturnInformation returnInformation){
                _finally();
                if(!returnInformation.isSuccess) return;

                returnInformation =
                        Matrix.jsonToMatrix(returnInformation.information.toString());

                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetMatrixTesk 完成");
                //不论结果，首先取消计时器判定

                if(returnInformation.isSuccess){//成功,则第二项应该是Matrix本体
//                    List<Matrix> result =;

                    gridViewFlush(
                            (List<Matrix>)returnInformation.information
                    );
                }
                else //失败,第二项是错误信息
                {
                    //显示错误信息
                    MessageBox.showMessage(
                            view.getContext(),
                            (String) returnInformation.information);
                }
            }

            @Override
            protected void onCancelled(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetEstablishedMatrixTesk 结束");
                _finally();

            }

            private void _finally(){
                int count=1;
                while(timeCheaker.isCancelled() ==false){
                    timeCheaker.cancel(false);

                    if(PublicInformation.developerMode)
                        MessageBox.showMessage(view.getContext(),"现有矩阵请求取消计时器"+count+"次");

                    count++;
                }
                progressBarGetEstablishedMatrix.stop();
            }

            //似乎用不着,用于更新进度条
            @Override
            protected void onProgressUpdate(Integer... values){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"现有矩阵请求 开始运行");
            }
        }


        //从http获取生成的矩阵 <超时时间长度，进度，返回信息>
        //该线程运行完毕后会自动跑起获取随机名的线程GetRandomNameTesk
        class GetMatrixTesk extends AsyncTask<Integer,Integer, ReturnInformation>{
            private int difficulty;
            //建立计时器线程
            TimeCheaker timeCheaker;
            //计时器实例
            GetMatrixTesk(int dif){
                difficulty = dif;
                timeCheaker = new TimeCheaker(this);
            }
            //执行前
            @Override
            protected void onPreExecute(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetMatrixTesk就绪");
                setCircularButtonStatusRuning();
                //这里到底该不该跑这个线程Di
                //向http要名字
                getRandomNameTesk = new GetRandomNameTesk();
                getRandomNameTesk.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,0);//异步线程

                //启动计时器
                timeCheaker.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,0);//异步线程
            }

            //执行时  大概就是要做的事情 run()
            @Override
            protected ReturnInformation doInBackground(Integer... integers) {
                publishProgress();

                while(true){
                    try {
                        //开始请求数据
                        GetFromHttp getMatrixByHttp = new GetFromHttp();
                        return new ReturnInformation(
                                true,getMatrixByHttp.getRandomMatrix(difficulty)
                        );
//                        Thread.sleep(1000);
//                        System.out.println("getMatrix");

                    } catch (Exception e) {
                        return new ReturnInformation(false,e.getMessage());
                    }

                }
            }

            //执行后
            @Override
            protected void onPostExecute(ReturnInformation returnInformation){

                _finally();
                if(!returnInformation.isSuccess) return;
                returnInformation =
                        Matrix.stringToMatrix(returnInformation.information.toString());

                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetMatrixTesk 完成");
                //不论结果，首先取消计时器判定

                if(returnInformation.isSuccess)//成功,则第二项应该是Matrix本体
                {
                    //上次使用时间同创造时间
                    Matrix matrix = (Matrix) returnInformation.information;
                    matrix.lastUseTimeIsCreateTime();
                    matrix.setDifficulty(difficulty);
                    //填充信息
                    //setTxv((Matrix) returnInformation.information);
                    //等待动画显示完成
                    setCircularButtonStatusComplete();
                    //矩阵填入对话框存储
                    DialogGetMatrix.this.matrix=matrix;
                }
                else //失败,第二项是错误信息
                {
                    //显示错误信息
                    MessageBox.showMessage(
                            view.getContext(),
                            (String) returnInformation.information);
                    //等待动画显示失败
                    setCircularButtonStatusFail();
                }
                //创建按钮 启用
                createButton.setEnabled(true);
            }

            @Override
            protected void onCancelled(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetMatrixTesk 结束");
                _finally();
                //等待动画显示失败
                setCircularButtonStatusFail();

            }

            private void _finally(){
                int count=1;
                while(timeCheaker.isCancelled() ==false){
                    timeCheaker.cancel(false);

                    if(PublicInformation.developerMode)
                        MessageBox.showMessage(view.getContext(),"矩阵请求取消计时器"+count+"次");

                    count++;
                }
            }

            //似乎用不着,用于更新进度条
            @Override
            protected void onProgressUpdate(Integer... values){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"矩阵请求 开始运行");
            }
        }


        //从http获取随机名称<超时时间长度，进度，返回信息>
        class GetRandomNameTesk extends AsyncTask<Integer,Integer,ReturnInformation>{
            //建立计时器线程
            TimeCheaker timeCheaker;

            GetRandomNameTesk(){
                timeCheaker = new TimeCheaker(this);
            }
            //执行前
            @Override
            protected void onPreExecute(){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetRandomNameTesk就绪");

                //等待动画出现
                progressBarGetRandomName.start();
//                setButtonStatusRuning();
                //开始计时
                timeCheaker.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,0);//异步线程
            }

            @Override
            protected ReturnInformation doInBackground(Integer... integers) {
                publishProgress();
                String result = "";

                try {
                    if(PublicInformation.developerMode)
                        System.out.println("GetRandomNameTesk 运行");

                    result = new GetFromHttp().getRandomName();
                    return new ReturnInformation(true,result);

                }catch (Exception e){
                    return new ReturnInformation(false,e.getMessage());
                }
            }

            //执行后
            @Override
            protected void onPostExecute(ReturnInformation returnInformation){
                _finally();
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"随机取名 完成");
                if(returnInformation.isSuccess)//成功,则第二项应该是Matrix本体
                {
                    //上次使用时间同创造时间
                    String value = (String)returnInformation.information;
                    //填充信息
                    txvName.setText(value);

                    //等待动画显示完成
//                    setButtonStatusComplete();

                    //矩阵填入对话框存储
//                    DialogGetMatrix.this.matrix=matrix;
                }
                else //失败,第二项是错误信息
                {
                    //显示错误信息
                    MessageBox.showMessage(
                            view.getContext(),
                            (String) returnInformation.information);
                }

            }

            @Override
            protected void onCancelled(){

                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetRandomNameTesk 结束");
                //不论结果，首先取消计时器判定
                _finally();
            }

            private void _finally(){
                int count=1;
                while(timeCheaker.isCancelled() ==false){
                    timeCheaker.cancel(false);

                    if(PublicInformation.developerMode)
                        MessageBox.showMessage(view.getContext(),"随机名取消计时器"+count+"次");

                    count++;
                }
                //等待动画消失
                progressBarGetRandomName.stop();
            }

            @Override
            protected void onProgressUpdate(Integer... values){
                if(PublicInformation.developerMode)
                    MessageBox.showMessage(view.getContext(),"GetRandomNameTesk 开始运行");
            }

        }


    }

}


/**
 * 供选择以开始游戏的按钮
 */
class MatrixSelectItem{
    public MatrixSelectItem(ImageButton imgbtAction, ImageButton imgbtDelete, TextView txvTitle) {
        this.imgbtAction = imgbtAction;
        this.txvTitle = txvTitle;
        this.imgbtDelete = imgbtDelete;
    }
    public MatrixSelectItem(String title){}

    public MatrixSelectItem(){}

    private ImageButton imgbtAction,imgbtDelete;
    private TextView txvTitle;
//    private void staticAdapterInit(BaseAdapter baseAdapter){
//        if(adapter==null)   adapter=baseAdapter;
//    }

}





