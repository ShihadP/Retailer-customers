package com.example.retailer_customers.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.retailer_customers.OnCompleteCallBack;
import com.example.retailer_customers.R;
import com.example.retailer_customers.Recording;
import com.example.retailer_customers.api.ApiService;
import com.example.retailer_customers.db.AudioDao;
import com.example.retailer_customers.db.MemberDao;
import com.example.retailer_customers.models.MemberModel;
import com.example.retailer_customers.utils.CommonUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private int RECORD_AUDIO_REQUEST_CODE = 123;
    int PLACE_PICKER_REQUEST = 100;
    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    ImageButton imageButton;
    private SeekBar seekBar;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private String audioName = null;
    private String uri = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    Double latitude,longitude;




    private RecyclerView recyclerViewRecordings;
    private ArrayList<Recording> recordingArraylist=new ArrayList<>();
    private RecordingListAdapter recordingAdapter;
    private TextView textViewNoRecordings,location;
    String address;
    String imagePath;

    EditText name, district, email, phone, pincode, birthdate, address1, address2, username, password;
    Button add;
   MemberDao memberDao;
    AudioDao audioDao;

    int memberId;
    boolean isFromApprove;
    //private SweetAlertDialog sweetAlertDialog;
    int CHECK_LOCATION_REQUEST = 200;
    ApiService web;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerform);
        recordingArraylist=new ArrayList<>();
        Intent intent = getIntent();
        isFromApprove = intent != null && intent.hasExtra("isFromApprove") ? intent.getBooleanExtra("isFromApprove", false) : false;
        memberId = intent != null && intent.hasExtra("memberId") ? intent.getIntExtra("memberId", 0) : 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio();
        }

        intDb();
        initUi();

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if (mPlayer != null) {
            int mCurrentPosition = mPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }


    @Override
    public void onClick(View view) {
        if (view == imageViewRecord) {
            prepareforRecording();
            startRecording();
        } else if (view == imageViewStop) {
            prepareforStop();
            stopRecording();
        } else if (view == imageViewPlay) {
            if (!isPlaying && fileName != null) {
                isPlaying = true;
                startPlaying();
            } else {
                isPlaying = false;
                stopPlaying();
            }
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult");
        if (requestCode == PLACE_PICKER_REQUEST) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            address=data.getStringExtra("address");
            location.setText(data.getStringExtra("address"));


        }else {


            File root = android.os.Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath() + "/Members/Images/");
            if (!file.exists()) {
                file.mkdirs();
            }
            String imge=String.valueOf(System.currentTimeMillis() + ".png");
           // uri = root.getAbsolutePath() + "/Members/Images/" + imge;

            //Uri uriSavedImage= Uri.fromFile(new File(root.getAbsolutePath() + "/Members/Images/imge"));


            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            File files = new File(root.getAbsolutePath() + "/Members/Images/"+imge);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(files);
                if (fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePath=files.getAbsolutePath();
            imageButton.setImageBitmap(bitmap);
            }

    }

    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }


    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory named VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/Members/AudioRecordings/");
        if (!file.exists()) {
            file.mkdirs();
        }
        audioName=String.valueOf(System.currentTimeMillis() + ".mp3");
        uri = root.getAbsolutePath() + "/Members/AudioRecordings/" + audioName;
        fileName = root.getAbsolutePath() + "/Members/AudioRecordings/" + audioName;
        Log.d("filename", fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    private void stopPlaying() {
        try {
            mPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_launcher_background);
        chronometer.stop();

    }


    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        //linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {

        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Recording recording=new Recording();
        recording.setFileName(audioName);
        recording.setUri(uri);
        recordingArraylist.add(recording);
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
        //fetchRecordings();
        setAdaptertoRecyclerView(recordingArraylist);
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        Log.d("instartPlaying", fileName);
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_login_bk);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_launcher_background);
                isPlaying = false;
                chronometer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void intDb() {
        memberDao = new MemberDao(getApplicationContext());
        audioDao=new AudioDao(getApplicationContext());

    }

    private void initUi() {

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        location=(TextView)findViewById(R.id.txtlocation);
        location.setVisibility(View.GONE);
        recyclerViewRecordings = (RecyclerView) findViewById(R.id.recyclerViewRecordings);
        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerViewRecordings.setHasFixedSize(true);

        //textViewNoRecordings = (TextView) findViewById(R.id.textViewNoRecordings);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);


//            playbtn = (Button) findViewById(R.id.btnPlay);
//            stopplay = (Button) findViewById(R.id.btnStopPlay);
//            stopbtn.setEnabled(false);
//            playbtn.setEnabled(false);
//            stopplay.setEnabled(false);
//            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//            mFileName += "/AudioRecording.3gp";

        name = (EditText) findViewById(R.id.txt_name);
        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_password);
        address1 = (EditText) findViewById(R.id.txt_address);
        email = (EditText) findViewById(R.id.txt_email);
        phone = (EditText) findViewById(R.id.txt_phoneno);
        district = (EditText) findViewById(R.id.txt_district);
        birthdate = (EditText) findViewById(R.id.txt_birthDate);
        pincode = (EditText) findViewById(R.id.txt_pincode);
        address2 = (EditText) findViewById(R.id.txt_address2);
        add = (Button) findViewById(R.id.add_custoner_button);

        if (memberId > 0) {
            MemberModel memberModel = memberDao.getMember(memberId);
            name.setText(memberModel.getName());
            //district.setText(memberModel.getDistrict());
            username.setText(memberModel.getUserName());
            password.setText(memberModel.getLogin_pin());
            address1.setText(memberModel.getAddress1());
            address2.setText(memberModel.getAddress2());
            email.setText(memberModel.getEmail());
            phone.setText(memberModel.getPhone_no());
            district.setText(memberModel.getDistrict());
            birthdate.setText(CommonUtils.getDate(memberModel.getBirth_date()));
            pincode.setText(CommonUtils.toString(memberModel.getPin_code()));
            imagePath=memberModel.getImagePath();
            add.setText("Approve");
            location.setText(memberModel.getLocationAddress());
            linearLayoutRecorder.setVisibility(View.GONE);
            latitude=Double.valueOf(memberModel.getLatitude());
            longitude=Double.valueOf(memberModel.getLoungitude());
            setAdaptertoRecyclerView(memberModel.getRecordingList());
            File file=saveImage(memberModel.getAudio());

            if (memberModel.getImagePath()!=null) {

                File imgFile = new File(memberModel.getImagePath());

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                    imageButton.setImageBitmap(myBitmap);

                }
            }
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()){

//                    Intent intent=new Intent(RegisterActivity.this, RootMapActivity.class);
//                    intent.putExtra("isApprove",isFromApprove?true:false);
//                    intent.putExtra("latitude",latitude);
//                    intent.putExtra("longitude",longitude);
//                    intent.putExtra("address",address);
//                    startActivityForResult(intent,PLACE_PICKER_REQUEST);

            }else {
                    requestLocationPermissions();
                }

        }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
                startActivityForResult(intent, 0);
            }
        });





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastProgress = 0;
                seekBar.setProgress(0);
                //stopPlaying();
                //starting the chronometer
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });


    }

    public static File saveImage( final String imageData) {
        final byte[] imgBytesData = Base64.decode(imageData,
                Base64.DEFAULT);

        File file = null;
        File root = android.os.Environment.getExternalStorageDirectory();
        file = new File(root.getAbsolutePath() + "/Members/Images/gfffg.mp3");
        //file = File.createTempFile("image", null, context.getCacheDir());
        final FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }



    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestLocationPermissions() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                CHECK_LOCATION_REQUEST);
//        }

    }

    private void Recording() {
        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/Members/AudioRecordings";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files!=null)
            Log.d("Files", "Size: "+ files.length);
        if( files!=null ){
            for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/Members/AudioRecordings" + fileName;
                for (Recording tuple:recordingArraylist) {

                    Recording recording = new Recording(recordingUri, fileName, false);
                    recordingArraylist.add(recording);
                }
            }
        }
    }
    private void fetchRecordings() {



        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/Members/AudioRecordings";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files!=null)
          Log.d("Files", "Size: "+ files.length);
        if( files!=null ){

            for (Recording tuple:recordingArraylist) {
                String recordingUri = root.getAbsolutePath() + "/Members/AudioRecordings/" + tuple.getFileName();
                Recording recording = new Recording(recordingUri, fileName, false);
                recordingArraylist.add(recording);
            }

                for (int i = 0; i < files.length; i++) {

                Log.d("Files", "FileName:" + files[i].getName());
                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/Members/AudioRecordings" + fileName;
                for (Recording tuple:recordingArraylist) {

                    Recording recording = new Recording(recordingUri, fileName, false);
                    recordingArraylist.add(recording);
                }
            }

            //textViewNoRecordings.setVisibility(View.GONE);
            recyclerViewRecordings.setVisibility(View.VISIBLE);
           // setAdaptertoRecyclerView();

        }else{
            //textViewNoRecordings.setVisibility(View.VISIBLE);
            recyclerViewRecordings.setVisibility(View.GONE);
        }

    }

    private void setAdaptertoRecyclerView(ArrayList<Recording> recordingList) {
        recyclerViewRecordings.setVisibility(View.VISIBLE);
        recordingAdapter = new RecordingListAdapter(this,recordingList);
        recyclerViewRecordings.setAdapter(recordingAdapter);
    }

    private void saveData() {
        if (name.getText().toString().isEmpty() || name.getText().toString().equals("")) {
            name.setError("please enter name");
        } else {
            insertMember();
        }
    }


    private void insertMember()  {

        MemberModel memberModel = new MemberModel();
        memberModel.setId(memberId);
        memberModel.setName(name.getText().toString());
        memberModel.setAddress1(address1.getText().toString());
        memberModel.setAddress2(address2.getText().toString());
        memberModel.setPhone_no(phone.getText().toString());
        memberModel.setDistrict(district.getText().toString());
        memberModel.setUserName(username.getText().toString());
        memberModel.setLogin_pin(password.getText().toString());
        //memberModel.setBirth_date(new Date(birthdate.getText().toString()));
        memberModel.setPin_code(CommonUtils.toInt(pincode.getText().toString()));
        memberModel.setEmail(email.getText().toString());
        memberModel.setLatitude(String.valueOf(latitude));
        memberModel.setLoungitude(String.valueOf(longitude));
        memberModel.setImagePath(imagePath);
        memberModel.setLocationAddress(location.getText().toString());
        memberModel.setStatus(memberId>0?"1":"0");
        memberModel.setDeviceId(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        memberDao.delete(memberId);//delete if already exsist

        memberDao.insertmember(memberModel,recordingArraylist);
        web = new ApiService(getApplicationContext());
        File recordFile = null;
        if(recordingArraylist!=null&&recordingArraylist.size()>0){
            recordFile = new File(recordingArraylist.get(0).getUri());
        }



        web.createNewMember(memberModel,recordFile!=null&&recordFile.exists()?recordFile:null, new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
             memberDao.updateStatus(memberDao.getNewMemberId(),"1");
            }

            @Override
            public void onErrorCallBAck() {

            }
        });
        clearData();
    }

    private void clearData() {
        name.getText().clear();
        address1.getText().clear();
        address2.getText().clear();
        phone.getText().clear();
        pincode.getText().clear();
        email.getText().clear();
        district.getText().clear();
        //purpose.getText().clear();
        finish();
    }
}