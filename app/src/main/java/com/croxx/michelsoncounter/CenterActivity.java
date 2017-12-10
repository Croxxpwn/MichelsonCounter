package com.croxx.michelsoncounter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;


public class CenterActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    private static final String TAG = "SampleCameraFrameAccessActivity";
    protected CameraBridgeViewBase cameraPreview;
    protected Mat mRgba;
    protected Mat mGray;
    protected Mat cannyEdges;
    protected Mat circles;
    protected Mat mCap;
    protected double[] parameters;
    protected static int RMAX = 10000000;
    protected int counter;
    protected Boolean flag;
    protected List<Mat> channels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        cameraPreview = (CameraBridgeViewBase) findViewById(R.id.camera_view_center);
        cameraPreview.setCvCameraViewListener(this);
        cameraPreview.setMaxFrameSize(640, 480);
        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                counter = 0;
                return false;
            }
        });
        if (ContextCompat.checkSelfPermission(CenterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            cameraPreview.enableView();
            cannyEdges = new Mat();
            circles = new Mat();
            counter = 0;
            flag = true;
            channels = new LinkedList<>();
        }

    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        Core.split(mRgba, channels);
        channels.get(0).copyTo(mGray);
        for(Mat mat:channels){
            mat.release();
        }

        mGray.convertTo(mGray, CvType.CV_8UC1, 2, -128);
        Imgproc.blur(mGray, mGray, new Size(7, 7));
        Imgproc.threshold(mGray, mGray, 128, 255, CvType.CV_8UC1);
        int ava = 0;
        int c = 0;
        for (int i = mRgba.cols() / 2 - 5; i <= mRgba.cols() / 2 + 5; i++) {
            for (int j = mRgba.rows() / 2 - 5; j <= mRgba.rows() / 2 + 5; j++) {
                ava += (int) (mGray.get(j, i)[0]);
                c++;
            }
        }
        ava /= c;
        if (flag && ava > 128) {
            counter++;
            flag = false;
        }
        if (!flag && ava < 125) {
            flag = true;
        }

        Imgproc.circle(mRgba, new Point(mRgba.cols() / 2, mRgba.rows() / 2), 5, new Scalar(255, 255, 255), 2);
        Imgproc.circle(mRgba, new Point(mRgba.cols() / 2, mRgba.rows() / 2), 60, new Scalar(255, 255, 255), 2);
        Imgproc.putText(mRgba, "" + counter, new Point(mRgba.cols() / 2 - 10, mRgba.rows() / 2 - 80), Core.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 255, 0), 2);
        Imgproc.putText(mRgba, "Touch to reset counter.", new Point(mRgba.cols() / 2 - 150, mRgba.rows() / 2 + 80), Core.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 255, 0), 2);

        return mRgba;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        /*
        if (cameraPreview != null) {
            cameraPreview.disableView();
        }
        */
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        // TODO Auto-generated method stub
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        // TODO Auto-generated method stub
        mRgba.release();
        mGray.release();
        cannyEdges.release();
        circles.release();
    }
}