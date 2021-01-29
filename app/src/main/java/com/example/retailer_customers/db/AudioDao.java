package com.example.retailer_customers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.retailer_customers.Recording;
import com.example.retailer_customers.utils.CommonUtils;
import com.example.retailer_customers.utils.ErrorMsg;

import java.util.ArrayList;
import java.util.List;

public class AudioDao extends DatabaseHandlerController {


    public static final String TABLE_NAME = "AudioDao";
    public static final String id = "id";
    public static final String headerId = "headerId";
    public static final String tableName = "tableName";
    public static final String filename = "filename";
    public static final String uri = "uri";



    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public AudioDao(Context context) {
        this.context = context;
    }


    public void insertAudioPath(List<Recording> recordings, int headerId) {

        dbhelper = DatabaseHandler.getInstance(context);
        sqliteDB = dbhelper.getWritableDatabase();
        sqliteDB.beginTransaction();
        try {
            for (Recording tuple:recordings) {

                String[] fields_ar = {this.headerId,this.tableName, this.filename,this.uri};


                Object[] values_ar = new Object[]{headerId,tuple.getTableName(), tuple.getFileName(),tuple.getUri()};


                String values = "", fields = "";
                for (int i = 0; i < values_ar.length; i++) {
                    if (values_ar[i] != null) {
                        values += CommonUtils.quoteIfString(values_ar[i]) + ",";
                        fields += fields_ar[i] + ",";
                    }
                }
                if (!values.isEmpty()) {
                    values = values.substring(0, values.length() - 1);
                    fields = fields.substring(0, fields.length() - 1);
                    String query = "INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");";
                    Log.d("Insert Audio Path", query);
                    sqliteDB.execSQL(query);
                }
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            ErrorMsg.showError(context, "Error while running DB query", e, "");
        } finally {
            sqliteDB.endTransaction();
            dbhelper.close();

        }
    }


    public ArrayList<Recording> selectall(int headerId) {

        String query = "select * from " + TABLE_NAME+ " where headerId = "+headerId;

        ArrayList<Recording> recordingList = prepareModelList(super.executeQuery(context, query));
        return recordingList;
    }




    public ArrayList<Recording> prepareModelList(ArrayList<ArrayList<String>> data) {
        ArrayList<Recording> recordingArrayList = new ArrayList<>();
        for (ArrayList<String> tuple : data) {

            Recording temp = new Recording();

            //temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setHeaderId(CommonUtils.toInt(tuple.get(1)));
            temp.setFileName(tuple.get(2));
            temp.setUri(tuple.get(3));

            recordingArrayList.add(temp);

        }

        return recordingArrayList;
    }

}
