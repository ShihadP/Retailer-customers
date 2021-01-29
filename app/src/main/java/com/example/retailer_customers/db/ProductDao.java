package com.example.retailer_customers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.retailer_customers.Recording;
import com.example.retailer_customers.models.ProductModel;
import com.example.retailer_customers.utils.CommonUtils;
import com.example.retailer_customers.utils.ErrorMsg;

import java.util.ArrayList;
import java.util.List;

public class ProductDao extends DatabaseHandlerController {


    public static final String TABLE_NAME = "Products";
    public static final String id = "id";
    public static final String product_id = "product_id";
    public static final String product_name = "product_name";
    public static final String group_code = "group_code";
    public static final String mrp = "mrp";
    public static final String standard_price = "standard_price";
    public static final String description = "description";
    public static final String hsncode = "hsncode";
    public static final String tax = "tax";
    public static final String product_code = "product_code";
    public static final String image = "image";
    public static final String voice_note = "voice_note";
    public static final String status = "status";


    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    AudioDao audioDao;

    public ProductDao(Context context) {
        this.context = context;
        audioDao=new AudioDao(context);
    }


    public void insertProducts(ProductModel productModel, List<Recording> recordingList) {

        dbhelper = DatabaseHandler.getInstance(context);
        sqliteDB = dbhelper.getWritableDatabase();
        sqliteDB.beginTransaction();
        try {


            String[] fields_ar = {this.product_id, this.product_name, this.group_code,
                    this.mrp, this.standard_price, this.hsncode, this.tax,
                    this.product_code, this.image,this.voice_note,this.status,this.description};


            Object[] values_ar = new Object[]{productModel.getProduct_id(), productModel.getName(), productModel.getGroup_code(),
                    productModel.getMrp(), productModel.getSp(), productModel.getHsn(),
                    productModel.getTax(), productModel.getPtc(),
                    productModel.getImagePath(),productModel.getVoice_note(),productModel.getStatus(),productModel.getDescription()};


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
                Log.d("Insert Products", query);
                sqliteDB.execSQL(query);
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            ErrorMsg.showError(context, "Error while running DB query", e, "");
        } finally {
            sqliteDB.endTransaction();
            dbhelper.close();

        }
        if (recordingList!=null&&recordingList.size()>0) {


            audioDao.insertAudioPath(recordingList,getNewMemberId());
        }
    }

    public void delete(int id) {
        super.delete(context, TABLE_NAME, " id="+id);

    }

    public List<ProductModel> selectall(int status) {

        String query = "select * from " + TABLE_NAME+" where status="+status;

        List<ProductModel> customerModelList = prepareModelList(super.executeQuery(context, query));
        return customerModelList;
    }

    public ProductModel getProduct(int id) {

        String query = "select * from " + TABLE_NAME+ " where id="+id;

        List<ProductModel> customerModelList = prepareModelList(super.executeQuery(context, query));
        return customerModelList.get(0);
    }

   // public MemberModel getProduct(String productId) {

//        String query = "select * from " + TABLE_NAME+ " where userName="+CommonUtils.quoteString(membername);
//
//        List<ProductModel> customerModelList = prepareModelList(super.executeQuery(context, query));
//        if (customerModelList!=null&&customerModelList.size()>0) {
//            return customerModelList.get(0);
//        }else {
//            return null;
//        }
   // }

    public int getNewMemberId() {
        ArrayList<ArrayList<String>> result = super.executeQuery(context, "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1");
        String recordId = result.size() > 0 ? result.get(0).get(0) : "0";
        return CommonUtils.toInt(recordId);
    }





    public ArrayList<ProductModel> prepareModelList(ArrayList<ArrayList<String>> data) {
        ArrayList<ProductModel> productModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {

            ProductModel temp = new ProductModel();

            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setProduct_id(CommonUtils.toInt(tuple.get(1)));
            temp.setName(tuple.get(2));
            //temp.setBirth_date(new Date(tuple.get(3)));

            temp.setGroup_code(CommonUtils.toInt(tuple.get(3)));
            temp.setMrp(CommonUtils.toBigDecimal(tuple.get(4)));
            //temp.setLocationId(CommonUtils.toInt(tuple.get(19)));
            temp.setSp(CommonUtils.toBigDecimal(tuple.get(5)));
            temp.setDescription(tuple.get(6));
            temp.setHsn(tuple.get(7));
            temp.setTax(CommonUtils.toBigDecimal(tuple.get(8)));
            temp.setPtc(CommonUtils.toInt(tuple.get(9)));
            temp.setImagePath(tuple.get(10));
            temp.setVoice_note(tuple.get(11));
            temp.setStatus(CommonUtils.toInt(tuple.get(12)));

            productModels.add(temp);

        }

        return productModels;
    }

}

