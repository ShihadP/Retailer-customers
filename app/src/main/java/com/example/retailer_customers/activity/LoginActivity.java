package com.example.retailer_customers.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.retailer_customers.R;
import com.example.retailer_customers.models.MemberModel;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText user_name, pass_word;
    TextView signup;
    //MemberDao memberDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        //user = new User( getApplicationContext() );
        //memberDao=new MemberDao(getApplicationContext());
        submit = (Button) findViewById( R.id.login );
        signup = (TextView) findViewById( R.id.btn_signup );
        user_name = (EditText) findViewById( R.id.username );
        pass_word = (EditText) findViewById( R.id.password );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent( LoginActivity.this, ProductListActivity.class );
                //intent.putExtra( "memberId", memberModel.getMember_id() );
               // LoginActivity.this.startActivity( intent );
                //login(memberDao.getMember(user_name.getText().toString()));

            }
        } );
        signup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, ProductListActivity1.class );
                //intent.putExtra( "role_name", userModel.getRole_name() );
                LoginActivity.this.startActivity( intent );
            }
        } );
    }

    private void login(MemberModel memberModel) {
        if (memberModel!=null) {
            if (memberModel.getLogin_pin().equalsIgnoreCase( pass_word.getText().toString() )) {
                 if(memberModel.getName().equalsIgnoreCase("Agent")){
                     Intent intent = new Intent(LoginActivity.this, MembersListActivity.class);
                     intent.putExtra("memberId", memberModel.getMember_id());
                     LoginActivity.this.startActivity(intent);
                 }else {
                     if (memberModel.getStatus().equalsIgnoreCase("1")) {
                         Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                         intent.putExtra("memberId", memberModel.getMember_id());
                         LoginActivity.this.startActivity(intent);
                     }else {
                         user_name.setError("User Not Approved");
                     }
                 }
            }else {
                pass_word.setError("Invalid Password");
            }
        }else {
            user_name.setError("User Not Fount");
        }
    }


//    public void makeLfogin() {
//        final String url = "https://planet-customerdiary.herokuapp.com/user/getalluserdetails";
//        JsonObjectRequest req = new DiaryBookJsonObjectRequest(this, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            VolleyLog.v("Response:%n %s", response.toString(4));
//                            Log.d("Response", response.toString());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//
//            }
//        });
//
//        AppController.getInstance().submitServerRequest(req,"submitShipmet");
//    }
}

