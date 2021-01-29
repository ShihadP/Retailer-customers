package com.example.retailer_customers.utils;

import android.os.StrictMode;
import android.provider.Settings;

import com.example.retailer_customers.db.DatabaseHandlerController;
import com.example.retailer_customers.models.MemberModel;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;


public class AppController extends MultiDexApplication {


    private final String TAG="AppController";
    private static AppController mInstance;

    @Override
    public void onCreate() {
        boolean DEVELOPER_MODE = BuildConfig.DEBUG && Settings.Secure.getInt(this.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) > 0;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
//                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        mInstance = this;
       DatabaseHandlerController.getSqliteVersion(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    MemberModel loggedMember;
    public int getLoggedUserId() {
        MemberModel userModel = getLoggedUser();
        return userModel != null ? userModel.getId() : 0;
    }
    public MemberModel getLoggedUser() {
        if(loggedMember != null)
            return loggedMember;
        //MemberDao memberDao = new MemberDao(getApplicationContext());
        if(loggedMember == null) {
            int loggerUserId = Preference.getLoggedMemberId();
            //loggedMember = memberDao.getMember(loggerUserId);
        }

        return loggedMember;
    }

    public void setLoggedUser(MemberModel memberModel) {
        this.loggedMember = memberModel;
        Preference.setLoggedMemberId(memberModel.getId());
    }

}


