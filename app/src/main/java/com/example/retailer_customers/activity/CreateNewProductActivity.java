//package com.example.retailer_customers.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.provider.MediaStore;
//import android.transition.TransitionManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class CreateNewProductActivity extends AppCompatActivity implements View.OnClickListener, LoginCallback.RetailCallback {
//
//
//    private int RECORD_AUDIO_REQUEST_CODE = 123;
//    int PLACE_PICKER_REQUEST = 100;
//    private Chronometer chronometer;
//    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
//    private SeekBar seekBar;
//    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
//    private MediaRecorder mRecorder;
//    private MediaPlayer mPlayer;
//    private String fileName = null;
//    private String audioName = null;
//    private String uri = null;
//    private int lastProgress = 0;
//    private Handler mHandler = new Handler();
//    private boolean isPlaying = false;
//    Double latitude, longitude;
//
//
//    private RecyclerView recyclerViewRecordings;
//    private ArrayList<Recording> recordingArraylist = new ArrayList<>();
//    private RecordingListAdapter recordingAdapter;
//    private TextView textViewNoRecordings, location;
//    String address;
//
//    EditText productname, mrp, statndardprice, description, hsccode, tax, productcode,groupcode;
//    Button add;
//   // ProductDao productDao;
//    //AudioDao audioDao;
//    ImageButton imageButton;
//    int productId;
//    boolean isFromApprove;
//    //private SweetAlertDialog sweetAlertDialog;
//    int CHECK_LOCATION_REQUEST = 200;
//    ApiService web;
//    String imagePath;
//    Spinner spinner;
//    List<CategoryModel> categoryModelList=new ArrayList<>();
//    CategoryModel selectedcategory;
//    Spinner category_spinner;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_new_product);
//        recordingArraylist = new ArrayList<>();
//        Intent intent = getIntent();
//        isFromApprove = intent != null && intent.hasExtra("isFromApprove") ? intent.getBooleanExtra("isFromApprove", false) : false;
//        productId = intent != null && intent.hasExtra("productId") ? intent.getIntExtra("productId", 0) : 0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getPermissionToRecordAudio();
//        }
//
//        web = new ApiService(getApplicationContext());
//
//        web.GetAllCAtegories(new OnCompleteCallBack() {
//            @Override
//            public void onCompleteCallBack(Object data) {
//                categoryModelList=(List<CategoryModel>)data;
//            }
//
//            @Override
//            public void onErrorCallBAck() {
//
//            }
//        });
//
//        intDb();
//        initUi();
//
//    }
//
//    private void setCategorySpinner() {
//
//        List<String>category=new ArrayList<>();
//        for (CategoryModel temp:categoryModelList){
//            category.add(temp.getCategory()!=null?temp.getCategory():"");
//        }
//        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item, category );
//        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
//        category_spinner.setAdapter( aa );
//    }
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            seekUpdation();
//        }
//    };
//
//    private void seekUpdation() {
//        if (mPlayer != null) {
//            int mCurrentPosition = mPlayer.getCurrentPosition();
//            seekBar.setProgress(mCurrentPosition);
//            lastProgress = mCurrentPosition;
//        }
//        mHandler.postDelayed(runnable, 100);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void getPermissionToRecordAudio() {
//        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
//        // checking the build version since Context.checkSelfPermission(...) is only available
//        // in Marshmallow
//        // 2) Always check for permission (even if permission has already been granted)
//        // since the user can revoke permissions at any time through Settings
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // The permission is NOT already granted.
//            // Check if the user has been asked about this permission already and denied
//            // it. If so, we want to give more explanation about why the permission is needed.
//            // Fire off an async request to actually get the permission
//            // This will show the standard permission request dialog UI
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    RECORD_AUDIO_REQUEST_CODE);
//
//        }
//    }
//
//    // Callback with the request from calling requestPermissions(...)
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        // Make sure it's our original READ_CONTACTS request
//        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
//            if (grantResults.length == 3 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//
//                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
//                finishAffinity();
//            }
//        }
//
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        if (view == imageViewRecord) {
//            prepareforRecording();
//            startRecording();
//        } else if (view == imageViewStop) {
//            prepareforStop();
//            stopRecording();
//        } else if (view == imageViewPlay) {
//            if (!isPlaying && fileName != null) {
//                isPlaying = true;
//                startPlaying();
//            } else {
//                isPlaying = false;
//                stopPlaying();
//            }
//        }
//
//    }
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("onActivityResult");
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            latitude = data.getDoubleExtra("latitude", 0);
//            longitude = data.getDoubleExtra("longitude", 0);
//            address = data.getStringExtra("address");
//            location.setText(data.getStringExtra("address"));
//
//
//        }else {
//
//
//            File root = android.os.Environment.getExternalStorageDirectory();
//            File file = new File(root.getAbsolutePath() + "/Members/Images/");
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            String imge=String.valueOf(System.currentTimeMillis() + ".png");
//            // uri = root.getAbsolutePath() + "/Members/Images/" + imge;
//
//            //Uri uriSavedImage= Uri.fromFile(new File(root.getAbsolutePath() + "/Members/Images/imge"));
//
//
//            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
//            File files = new File(root.getAbsolutePath() + "/Members/Images/"+imge);
//
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(files);
//                if (fos != null) {
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//                    fos.close();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imagePath=files.getAbsolutePath();
//            imageButton.setImageBitmap(bitmap);
//        }
//
//    }
//
//    private void prepareforRecording() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.GONE);
//        imageViewStop.setVisibility(View.VISIBLE);
//        linearLayoutPlay.setVisibility(View.GONE);
//    }
//
//
//    private void startRecording() {
//        //we use the MediaRecorder class to record
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        /**In the lines below, we create a directory named VoiceRecorderSimplifiedCoding/Audios in the phone storage
//         * and the audios are being stored in the Audios folder **/
//        File root = android.os.Environment.getExternalStorageDirectory();
//        File file = new File(root.getAbsolutePath() + "/Members/AudioRecordings/");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        audioName = String.valueOf(System.currentTimeMillis() + ".mp3");
//        uri = root.getAbsolutePath() + "/Members/AudioRecordings/" + audioName;
//        fileName = root.getAbsolutePath() + "/Members/AudioRecordings/" + audioName;
//        Log.d("filename", fileName);
//        mRecorder.setOutputFile(fileName);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            mRecorder.prepare();
//            mRecorder.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        lastProgress = 0;
//        seekBar.setProgress(0);
//        stopPlaying();
//        //starting the chronometer
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        chronometer.start();
//    }
//
//
//    private void stopPlaying() {
//        try {
//            mPlayer.release();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mPlayer = null;
//        //showing the play button
//        imageViewPlay.setImageResource(R.drawable.ic_launcher_background);
//        chronometer.stop();
//
//    }
//
//
//    private void prepareforStop() {
//        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
//        imageViewRecord.setVisibility(View.VISIBLE);
//        imageViewStop.setVisibility(View.GONE);
//        //linearLayoutPlay.setVisibility(View.VISIBLE);
//    }
//
//    private void stopRecording() {
//
//        try {
//            mRecorder.stop();
//            mRecorder.release();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mRecorder = null;
//        //starting the chronometer
//        chronometer.stop();
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        //showing the play button
//        Recording recording = new Recording();
//        recording.setFileName(audioName);
//        recording.setUri(uri);
//        recordingArraylist.add(recording);
//        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
//        //fetchRecordings();
//        setAdaptertoRecyclerView(recordingArraylist);
//    }
//
//    private void startPlaying() {
//        mPlayer = new MediaPlayer();
//        Log.d("instartPlaying", fileName);
//        try {
//            mPlayer.setDataSource(fileName);
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IOException e) {
//            Log.e("LOG_TAG", "prepare() failed");
//        }
//        //making the imageview pause button
//        imageViewPlay.setImageResource(R.drawable.ic_login_bk);
//
//        seekBar.setProgress(lastProgress);
//        mPlayer.seekTo(lastProgress);
//        seekBar.setMax(mPlayer.getDuration());
//        seekUpdation();
//        chronometer.start();
//
//        /** once the audio is complete, timer is stopped here**/
//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                imageViewPlay.setImageResource(R.drawable.ic_launcher_background);
//                isPlaying = false;
//                chronometer.stop();
//            }
//        });
//
//        /** moving the track as per the seekBar's position**/
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (mPlayer != null && fromUser) {
//                    //here the track's progress is being changed as per the progress bar
//                    mPlayer.seekTo(progress);
//                    //timer is being updated as per the progress of the seekbar
//                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
//                    lastProgress = progress;
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//
//    private void intDb() {
//        //productDao= new ProductDao(getApplicationContext());
//        //audioDao = new AudioDao(getApplicationContext());
//
//    }
//
//    private void initUi() {
//
//
//        linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
//        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
//        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
//        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
//        linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        location = (TextView) findViewById(R.id.txtlocation);
//        imageButton = (ImageButton) findViewById(R.id.imagebutton);
//        recyclerViewRecordings = (RecyclerView) findViewById(R.id.recyclerViewRecordings);
//        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerViewRecordings.setHasFixedSize(true);
//        category_spinner = (Spinner) findViewById( R.id.category_spinner );
//        //textViewNoRecordings = (TextView) findViewById(R.id.textViewNoRecordings);
//
//        imageViewRecord.setOnClickListener(this);
//        imageViewStop.setOnClickListener(this);
//        imageViewPlay.setOnClickListener(this);
//
//
////            playbtn = (Button) findViewById(R.id.btnPlay);
////            stopplay = (Button) findViewById(R.id.btnStopPlay);
////            stopbtn.setEnabled(false);
////            playbtn.setEnabled(false);
////            stopplay.setEnabled(false);
////            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
////            mFileName += "/AudioRecording.3gp";
//
//        productname = (EditText) findViewById(R.id.txt_name);
//        mrp = (EditText) findViewById(R.id.txt_mrp);
//        statndardprice = (EditText) findViewById(R.id.txt_sp);
//        description = (EditText) findViewById(R.id.txt_description);
//        hsccode = (EditText) findViewById(R.id.txt_hsncode);
//        //groupcode = (EditText) findViewById(R.id.g);
//        productcode = (EditText) findViewById(R.id.txt_productcode);
//        add = (Button) findViewById(R.id.add_custoner_button);
//        spinner = (Spinner) findViewById(R.id.category_spinner);
//
//
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                selectedcategory=categoryModelList.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//
//        if (productId > 0) {
//
//            Intent i = getIntent();
//            ProductModel productModel =(ProductModel)i.getSerializableExtra("sampleObject");
//            productname.setText(productModel.getName());
//            //district.setText(memberModel.getDistrict());
//            productcode.setText(CommonUtils.toString(productModel.getPtc()));
//            mrp.setText(productModel.getMrp()!=null?productModel.getMrp().toString():"");
//            statndardprice.setText(productModel.getSp()!=null?productModel.getSp().toString():"");
//            hsccode.setText(productModel.getHsn());
//            //tax.setText(productModel.getTax()!=null?productModel.getTax().toString():"");
//            description.setText(productModel.getDescription());
//            //groupcode.setText(CommonUtils.toString(productModel.getGroup_code()));
//            add.setText("Approve");
//            linearLayoutRecorder.setVisibility(View.GONE);
//
//            if (productModel.getImagePath()!=null) {
//
//                File imgFile = new File(productModel.getImagePath());
//
//                if (imgFile.exists()) {
//
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//
//                    imageButton.setImageBitmap(myBitmap);
//
//                }
//            }
//             if (productModel.getRecordingList()!=null&&productModel.getRecordingList().size()>0) {
//                 setAdaptertoRecyclerView(productModel.getRecordingList());
//             }
//        }
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
//                startActivityForResult(intent, 0);
//            }
//        });
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveData();
//            }
//        });
//
//        imageViewPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lastProgress = 0;
//                seekBar.setProgress(0);
//                //stopPlaying();
//                //starting the chronometer
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                chronometer.start();
//            }
//        });
//
//
//    }
//
//
//    private boolean checkPermissions() {
//        int permissionState1 = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        int permissionState2 = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION);
//        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestLocationPermissions() {
//
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                CHECK_LOCATION_REQUEST);
////        }
//
//    }
//
//    private void fetchRecordings() {
//
//        File root = android.os.Environment.getExternalStorageDirectory();
//        String path = root.getAbsolutePath() + "/Members/AudioRecordings";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        if (files != null)
//            Log.d("Files", "Size: " + files.length);
//        if (files != null) {
//
//            for (Recording tuple : recordingArraylist) {
//                String recordingUri = root.getAbsolutePath() + "/Members/AudioRecordings/" + tuple.getFileName();
//                Recording recording = new Recording(recordingUri, fileName, false);
//                recordingArraylist.add(recording);
//            }
//
//            for (int i = 0; i < files.length; i++) {
//
//                Log.d("Files", "FileName:" + files[i].getName());
//                String fileName = files[i].getName();
//                String recordingUri = root.getAbsolutePath() + "/Members/AudioRecordings" + fileName;
//                for (Recording tuple : recordingArraylist) {
//
//                    Recording recording = new Recording(recordingUri, fileName, false);
//                    recordingArraylist.add(recording);
//                }
//            }
//
//            //textViewNoRecordings.setVisibility(View.GONE);
//            recyclerViewRecordings.setVisibility(View.VISIBLE);
//            // setAdaptertoRecyclerView();
//
//        } else {
//            //textViewNoRecordings.setVisibility(View.VISIBLE);
//            recyclerViewRecordings.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void setAdaptertoRecyclerView(ArrayList<Recording> recordingList) {
//        recyclerViewRecordings.setVisibility(View.VISIBLE);
//        recordingAdapter = new RecordingListAdapter(this, recordingList);
//        recyclerViewRecordings.setAdapter(recordingAdapter);
//    }
//
//    private void saveData() {
//        if (productname.getText().toString().isEmpty() || productname.getText().toString().equals("")) {
//            productname.setError("please enter name");
//        } else {
//            insertProduct();
//        }
//    }
//
//
//    private void insertProduct() {
//
//        ProductModel memberModel = new ProductModel();
//        memberModel.setName(productname.getText().toString());
//        memberModel.setPtc(CommonUtils.toInt(productcode.getText().toString()));
//        memberModel.setTax(BigDecimal.TEN);
//        memberModel.setHsn(hsccode.getText().toString());
//        memberModel.setSp(CommonUtils.toBigDecimal(statndardprice.getText().toString()));
//        memberModel.setMrp(CommonUtils.toBigDecimal(mrp.getText().toString()));
//        memberModel.setDescription(description.getText().toString());
//        memberModel.setGroup_code(selectedcategory.getId());
//        memberModel.setStatus(productId>0?1:0);
//        memberModel.setImagePath(imagePath);
//        //memberModel.setBirth_date(new Date(birthdate.getText().toString()));
//        memberModel.setImage("");
//        //productDao.delete(productId);
//        //productDao.insertProducts(memberModel, recordingArraylist);
//        web = new ApiService(getApplicationContext(), this);
//        web.syncProducts(memberModel);
//        clearData();
//    }
//
//    private void clearData() {
//        productname.getText().clear();
//        //tax.getText().clear();
//        productcode.getText().clear();
//        statndardprice.getText().clear();
//        hsccode.getText().clear();
//        //groupcode.getText().clear();
//        //district.getText().clear();
//        //purpose.getText().clear();
//        finish();
//    }
//
//    @Override
//    public void CreateNewProducts(ProductModel obj) {
//
//    }
//
//    @Override
//    public void PaymentStatusCallbackFiler() {
//
//    }
//}
