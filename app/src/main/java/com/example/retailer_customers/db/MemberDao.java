package com.example.retailer_customers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.retailer_customers.Recording;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.utils.CommonUtils;
import com.example.retailer_customers.utils.ErrorMsg;

import java.util.ArrayList;
import java.util.List;

public class MemberDao extends DatabaseHandlerController {


    public static final String TABLE_NAME = "Members";
    public static final String member_id = "member_id";
    public static final String name = "name";
    public static final String district = "district";
    public static final String pin_code = "pin_code";
    public static final String latitude = "latitude";
    public static final String loungitude = "loungitude";
    public static final String email = "email";
    public static final String phone_no = "phone_no";
    public static final String varified_memberId = "varified_memberId";
    public static final String varified_memberName = "varified_memberName";
    public static final String userName = "userName";
    public static final String login_pin = "login_pin";
    public static final String status = "status";
    public static final String isAgent = "isAgent";
    public static final String imagePath = "imagePath";
    public static final String locationAddress = "locationAddress";
    public static final String address = "address";
    public static final String address1 = "address1";
    public static final String deviceId = "deviceId";



    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    AudioDao audioDao;

    public MemberDao(Context context) {
        this.context = context;
        audioDao=new AudioDao(context);
    }


    public void insertmember(MemberModel memberModel, List<Recording> recordingList) {

        dbhelper = DatabaseHandler.getInstance(context);
        sqliteDB = dbhelper.getWritableDatabase();
        sqliteDB.beginTransaction();
        try {

            String[] fields_ar = {this.member_id, this.name, this.district,
                    this.pin_code, this.latitude, this.loungitude, this.email,
                    this.phone_no, this.varified_memberId,this.varified_memberName,
                    this.userName , this.login_pin,this.status,this.isAgent,this.imagePath,this.locationAddress,this.address,
            this.address1,this.deviceId};


            Object[] values_ar = new Object[]{memberModel.getMember_id(), memberModel.getName(), memberModel.getDistrict(),
                    memberModel.getPin_code(), memberModel.getLatitude(), memberModel.getLoungitude(),
                    memberModel.getEmail(), memberModel.getPhone_no(),
                    memberModel.getvarified_memberId(),memberModel.getVarified_memberName(),memberModel.getUserName(), memberModel.getLogin_pin(),
                    memberModel.getStatus(),memberModel.getName().equalsIgnoreCase("Agent")?"1":"0",
                    memberModel.getImagePath(),memberModel.getLocationAddress(),memberModel.getAddress1(),memberModel.getAddress2(),memberModel.getDeviceId()};


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
                Log.d("Insert Customer", query);
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

    public List<MemberModel> selectall() {

        String query = "select * from " + TABLE_NAME+" where name != 'agent'";

        List<MemberModel> customerModelList = prepareModelList(super.executeQuery(context, query));
        return customerModelList;
    }

    public MemberModel getMember(int id) {

        String query = "select * from " + TABLE_NAME+ " where id="+id;

        List<MemberModel> customerModelList = prepareModelList(super.executeQuery(context, query));
        return customerModelList.get(0);
    }

    public void updateStatus(int i_d, String status) {

        String query = "UPDATE " + TABLE_NAME + " set status =" + CommonUtils.quoteString( status ) +
                " where id =" + i_d;
        super.execute( context, query );

    }

    public MemberModel getMember(String membername) {

        String query = "select * from " + TABLE_NAME+ " where userName="+ CommonUtils.quoteString(membername);

        List<MemberModel> customerModelList = prepareModelList(super.executeQuery(context, query));
        if (customerModelList!=null&&customerModelList.size()>0) {
            return customerModelList.get(0);
        }else {
            return null;
        }
    }

    public int getNewMemberId() {
        ArrayList<ArrayList<String>> result = super.executeQuery(context, "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1");
        String recordId = result.size() > 0 ? result.get(0).get(0) : "0";
        return CommonUtils.toInt(recordId);
    }




    public ArrayList<MemberModel> prepareModelList(ArrayList<ArrayList<String>> data) {
        ArrayList<MemberModel> customerModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {

            MemberModel temp = new MemberModel();

            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setMember_id(CommonUtils.toInt(tuple.get(1)));
            temp.setName(tuple.get(2));
            //temp.setBirth_date(new Date(tuple.get(3)));

            temp.setDistrict(tuple.get(4));
            temp.setPin_code(CommonUtils.toInt(tuple.get(5)));
            temp.setLatitude((tuple.get(6)));
            temp.setLoungitude(tuple.get(7));
            temp.setEmail(tuple.get(8));
            temp.setPhone_no(tuple.get(9));
            temp.setvarified_memberId(CommonUtils.toInt(tuple.get(10)));
            temp.setVarified_memberName(tuple.get(11));
            temp.setLogin_pin((tuple.get(12)));
            temp.setStatus(tuple.get(13));
            int isag=CommonUtils.toInt(tuple.get(14));
            temp.setAgent(isag==1);
            temp.setUserName(tuple.get(15));
            //temp.setLogin_pin(tuple.get(16));
            temp.setImagePath(tuple.get(17));
            temp.setLocationAddress(tuple.get(18));
            temp.setAddress1(tuple.get(19));
            temp.setAddress2(tuple.get(20));
//            temp.setDeviceId(tuple.get(21));

            temp.setRecordingList(audioDao.selectall(CommonUtils.toInt(tuple.get(0))));

            customerModels.add(temp);

        }

        return customerModels;
    }

}
