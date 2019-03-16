package top.eatfingersss.sudoku.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import top.eatfingersss.sudoku.model.entity.Matrix;
import top.eatfingersss.sudoku.model.entity.ReturnInformation;
import top.eatfingersss.sudoku.view.MessageBox;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class TableAndColumnName{
    String tableName;
    String[] columnName;

    public TableAndColumnName(String tableName, String[] columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }
}
public class SQLiteDAO extends SQLiteOpenHelper {
    // 数据库名
    private static final String DB_NAME = "sudoku.db";

    // 数据表名
    public static final TableAndColumnName MATRIX =
            new TableAndColumnName(
                    "tbl_matrix",
                    new String[] {"mat_id","mat_name","mat_author","mat_describe",
                                    "mat_score","mat_difficulty","mat_context",
                                    "mat_answer","mat_createTime",
                                    "mat_completeTime","mat_lastUseTime"}
            );


    private static final int VERSION = 1;
    //用来办事的对象
//    private SQLiteDatabase sqLiteDatabase;

    //俩个构造
    public SQLiteDAO(Context context,
                     String name,
                     SQLiteDatabase.CursorFactory factory,
                     int version,
                     DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public SQLiteDAO(Context context,String dbName) {
        super(context, dbName, null, VERSION);
    }


    public static void storeMatrixInDatabase(View view, Matrix matrix){
        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(view.getContext(),tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mat_context",matrix.contextToString());
        values.put("mat_answer",matrix.answerToString());//从外部得到数据的时候是没有答案的，至于残局以后再说
        values.put("mat_completeTime",
                matrix.completeTime == null?null:matrix.completeTime.getTime());
        values.put("mat_lastUseTime",matrix.lastUseTime.getTime());

        //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
        sqLiteDatabase.update(tableName,values,"mat_id=?",new String[]{matrix.getId()});

    }


    public static void putMatrixInDatabase(View view, Matrix matrix){
        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(view.getContext(),tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mat_id",matrix.getId());
        values.put("mat_name",matrix.getName());
        values.put("mat_author",matrix.getAuthor());
        values.put("mat_describe", matrix.getDescribe());
        values.put("mat_score",matrix.getScore());
        values.put("mat_difficulty",matrix.getDifficulty());
        values.put("mat_context",matrix.contextToString());
        values.put("mat_answer",matrix.answerToString());//从外部得到数据的时候是没有答案的，至于残局以后再说
        values.put("mat_createTime",matrix.createTime.getTime());
        values.put("mat_completeTime",
                matrix.completeTime == null?null:matrix.completeTime.getTime());
        values.put("mat_lastUseTime",matrix.lastUseTime.getTime());

        //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
        sqLiteDatabase.insert(tableName,null,values);

    }


    public static int getMatrixesCount(Context context){

        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(context,tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getReadableDatabase();

        //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，
        // 进一步约束指定查询结果的排序方式

        Cursor cursor=
                sqLiteDatabase.rawQuery(
                        "Select count(*) from "+tableName+";",null
                );
        int result = -1;
        if (cursor.moveToFirst()) {
            do {
                result=cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;


    }


    public static List<Integer> getMatrixesIds(Context context){
        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=new SQLiteDAO(context,tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                tableName, new String[]{"mat_id"}, null,
                null, null,
                null, null);
        List<Integer> ids= new LinkedList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("mat_id"));
                ids.add(id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    public static Matrix getMatrixesById(Context context,String id){

        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(context,tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getReadableDatabase();

        //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，
        // 进一步约束指定查询结果的排序方式
        Cursor cursor = sqLiteDatabase.query(
                tableName, null, "mat_id=?" +
                        "",
                new String[]{id+""}, null,
                null, "mat_createTime");
        Matrix result = null;
        if (cursor.moveToFirst()) {
            do {
                result = new Matrix(
                        cursor.getString(cursor.getColumnIndex("mat_id")),
                        cursor.getString(cursor.getColumnIndex("mat_name")),
                        cursor.getString(cursor.getColumnIndex("mat_author")),
                        cursor.getString(cursor.getColumnIndex("mat_describe")),
                        cursor.getString(cursor.getColumnIndex("mat_score")),
                        cursor.getInt(cursor.getColumnIndex("mat_difficulty")),
                        Matrix.stringToContext(
                                cursor.getString(
                                        cursor.getColumnIndex("mat_context"))
                        ),
                        //突然想起好像没有区分""与null
                        Matrix.stringToContext(
                                cursor.getString(
                                        cursor.getColumnIndex("mat_answer"))
                        ),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_createTime"))),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_completeTime"))),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_lastUseTime")))
                );
//                sb.append("id：" + pid + "：" + name + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;

    }

        /***
         *仅仅获取信息
         * @param //,i表示取得个数，暂定为 6,后来想了想还是去掉了
         * @return 矩阵列表
         */
    public static List<Matrix> getMatrixesMessageFromDatabase(Context context){

        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(context,tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getReadableDatabase();

//        String sql="";
//        sqLiteDatabase.execSQL(sql);
//        Cursor cursor =  sqLiteDatabase.rawQuery(
//                "select * from tbl_matrix ;",null);
        //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，
        // 进一步约束指定查询结果的排序方式
        Cursor cursor = sqLiteDatabase.query(
                tableName, null, null,
                null, null,
                null, "mat_createTime");
        List<Matrix> result = new LinkedList<Matrix>();
        if (cursor.moveToFirst()) {
            do {
                Matrix temp= new Matrix(
                        cursor.getString(cursor.getColumnIndex("mat_id")),
                        cursor.getString(cursor.getColumnIndex("mat_name")),
                        cursor.getString(cursor.getColumnIndex("mat_author")),
                        cursor.getString(cursor.getColumnIndex("mat_describe")),
                        cursor.getString(cursor.getColumnIndex("mat_score")),
                        cursor.getInt(cursor.getColumnIndex("mat_difficulty")),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_createTime"))),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_completeTime"))),
                        new Date(
                                cursor.getLong(
                                        cursor.getColumnIndex(
                                                "mat_lastUseTime")))
                );
                result.add(temp);
//                sb.append("id：" + pid + "：" + name + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;


    }

    public static void deleteMatrixById(View view,String id){
        String tableName=MATRIX.tableName;
        SQLiteDAO sqLiteDAO=
                new SQLiteDAO(view.getContext(),tableName);
        SQLiteDatabase sqLiteDatabase = sqLiteDAO.getWritableDatabase();
        sqLiteDatabase.delete(tableName, "mat_id = ?", new String[]{id});
    }


    /**
     * 第一次打开数据库时执行
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("[Log]","没有数据库，开始创建(￣▽￣)\"");
        db.execSQL(
                "CREATE TABLE [tbl_matrix](\n" +
                        "  [mat_id] VARCHAR PRIMARY KEY, \n" +
                        "  [mat_name] VARCHAR, \n" +
                        "  [mat_author] VARCHAR, \n" +
                        "  [mat_describe] TEXT, \n" +
                        "  [mat_score] VARCHAR, \n" +
                        "  [mat_difficulty] INT, \n" +
                        "  [mat_context] VARCHAR, \n" +
                        "  [mat_answer] VARCHAR, \n" +
                        "  [mat_createTime] INTEGER, \n" +
                        "  [mat_completeTime] INTEGER, \n" +
                        "  [mat_lastUseTime] INTEGER);\n"
                );

    }

    /**
     * 数据库版本升高时执行
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("[Log]","雾草数据库居然升级了w(ﾟДﾟ)w");
    }
}
