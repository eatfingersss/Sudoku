package top.eatfingersss.sudoku.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.control.PublicInformation;
import top.eatfingersss.sudoku.model.entity.Matrix;
import top.eatfingersss.sudoku.model.entity.ReturnInformation;
import top.eatfingersss.sudoku.view.MessageBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


/**
 * 通过页面获取信息的线程类
 * 返回值: Object[0]=boolean, 表示请求数据成功与否
 *        Object[1]=(Matrix/String)Object,请求成功就是对应Matrix对象，否则为失败原因
 */
public class GetFromHttp  {
    private String url  ;
    //连接管理器
    //private ConnectivityManager connectivityManager;
//检测网络状态还没写

    private String getMessgeByURL(String address){
        StringBuffer buffer = new StringBuffer();
        try{
            //水源
            URL url=new URL(address);
            //网站没有拿到证书，水闸
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //水管
            InputStream inputStream = httpURLConnection.getInputStream();
            //蓄水池
            InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
            //用水桶盛水
            BufferedReader bufferedReader = new BufferedReader(reader);


            String temp = null;
            while((temp = bufferedReader.readLine())!=null){
                buffer.append(temp);
            }

            bufferedReader.close();
            reader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            buffer.append(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            buffer.append(e.getMessage());
            e.printStackTrace();
        }

        return buffer.toString();
    }


    public String getRandomName(){
        return getMessgeByURL(PublicInformation.GET_RANDOM_NAME_URL);
    }

    public String getRandomMatrix(int difficulty){
        return getMessgeByURL(PublicInformation.GET_RANDOM_MATRIX_URL+"?level="+difficulty);
    }

    public String getEstablishedMatrix(){
        return getMessgeByURL(PublicInformation.GET_ESTABLISHED_MATRIX_URL);
    }

}