package com.example.root.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    ListView imageList;
    ArrayList<String> listOfImages;
    private static final int CAMERA_CAPTURE = 20;
    public Bitmap bitmap;
    private static final long TWO_MINUTES = 40 * 1000L;
    private AlarmManager mAlarmManager;
    private PendingIntent mSelfiePendingIntent;
    private Intent mSelfieNotificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageList = (ListView) findViewById(R.id.imageList);
        DisplayCapturedImagesFromCamera();
        startPendingIntents();
        startSelfieReminders();
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
                start();
            }
        });
    }



    private void DisplayCapturedImagesFromCamera() {

        File myPath = getExternalFilesDir(null);
        listOfImages = new ArrayList<String>();

        try {

            for (File f : myPath.listFiles()) {
                listOfImages.add(f.getAbsolutePath());
            }

            PhotoAdapter adapter = new PhotoAdapter(MainActivity.this, listOfImages);
            imageList.setAdapter(adapter);
        } catch (Exception ex)
        {
            Log.w("Error", ex.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_camera) {
            startCapture();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCapture() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = CreateImageFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CAMERA_CAPTURE);
            }
        }
    }

    private File CreateImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Image_" + timeStamp + "_";

        File storageDirectory = getExternalFilesDir("");
        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        return image;

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CAMERA_CAPTURE:
                if (resultCode == RESULT_OK) {
                    DisplayCapturedImagesFromCamera();
                }
                break;
        }
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bitmap = BitmapFactory.decodeFile(listOfImages.get(position));
                start();


            }
        });



    }

    private void startSelfieReminders() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Broadcast the notification intent at specified intervals
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                , System.currentTimeMillis() + TWO_MINUTES
                , TWO_MINUTES
                , mSelfiePendingIntent);

    }

    private void startPendingIntents() {
        // Create the notification pending intent
        mSelfieNotificationIntent = new Intent(MainActivity.this, Notification.class);
        mSelfiePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mSelfieNotificationIntent, 0);
    }


    //for showing the pictures
    public void start()
  {
      Intent intent;
      intent = new Intent(this , ImageLoaderActivity.class );
      ByteArrayOutputStream bs = new ByteArrayOutputStream();
     // intent.putExtra("Image", bitmap);
      bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
      intent.putExtra("byteArray", bs.toByteArray());
       startActivity(intent);
  }
}

