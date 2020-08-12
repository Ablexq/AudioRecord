package com.af.audiodemo.view;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.af.audiodemo.MainContract;
import com.af.audiodemo.MainPresenter;
import com.af.audiodemo.R;
import com.af.audiodemo.view.widget.RecordAudioButton;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

public class MainActivity<T extends MainContract.Presenter> extends AppCompatActivity implements MainContract.View {
    private static final String TAG = "MainActivity===========";

    LinearLayout mRoot;
    RecyclerView mRvMsg;//消息列表
    RecordAudioButton mBtnVoice;//底部录制按钮

    private Context mContext;
    private VideoAdapter mAdapter = new VideoAdapter();//适配器
    private RecordVoicePopWindow mRecordVoicePopWindow;//提示
    private MainContract.Presenter mPresenter;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPresenter = new MainPresenter<MainContract.View>(this, this);
        setContentView(R.layout.activity_main);
        requestPermission();//请求麦克风权限
        initView();//初始化布局
        mPresenter.init();


        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(headsetReceiver, intentFilter);
    }


    private int current = -1;
    //-1 : 外放
    // 0 : 蓝牙
    // 1 : 有线耳机

    //AudioSource.DEFAULT:默认音频来源
    //AudioSource.MIC:麦克风（常用）
    //AudioSource.VOICE_UPLINK:电话上行
    //AudioSource.VOICE_DOWNLINK:电话下行
    //AudioSource.VOICE_CALL:电话、含上下行
    //AudioSource.CAMCORDER:摄像头旁的麦克风
    //AudioSource.VOICE_RECOGNITION:语音识别
    //AudioSource.VOICE_COMMUNICATION:语音通信

    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e(TAG, "HEADSET device -> " + device);
                    int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
                    Log.e(TAG, "HEADSET STATE -> " + state);
                    if (state == BluetoothProfile.STATE_CONNECTING) {
                        Log.d(TAG, "onReceive: STATE_CONNECTING");
                    } else if (state == BluetoothProfile.STATE_CONNECTED) {
                        if (device == null) {
                            return;
                        }
                        // mBluetoothDevice = device;
                        Log.d(TAG, "onReceive: STATE_CONNECTED 蓝牙耳机连接");
                        current = 0;
                    } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                        // mBluetoothDevice = null;
                        Log.d(TAG, "onReceive: STATE_DISCONNECTED 蓝牙耳机断开");
                        current = -1;
                    } else if (state == BluetoothProfile.STATE_DISCONNECTING) {
                        Log.d(TAG, "onReceive: STATE_DISCONNECTING ");
                    }
                } else if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                    if (intent.hasExtra("state")) {
                        int state = intent.getIntExtra("state", 0);
                        if (state == 1) {
                            Log.d(TAG, "onReceive: 有线耳机 连接");
                            current = 1;
                        } else if (state == 0) {
                            Log.d(TAG, "onReceive: 有线耳机 断开");
                            current = -1;
                        }
                    }
                }

                //连接有线/蓝牙：会默认优先有线耳机
                //连接有线/蓝牙，当断开有线，直接外放
                //连接有线/蓝牙，当断开蓝牙，直接外放
                switch (current) {
                    case 0:
                        Log.i(TAG, "----------- onReceive: 使用蓝牙");
                        break;
                    case 1:
                        Log.i(TAG, "----------- onReceive: 使用有线");
                        break;
                    default:
                        Log.i(TAG, "----------- onReceive: 外放");
                        break;
                }

                //todo 检查有线耳机是否被连接或没有。
                if (mAudioManager.isWiredHeadsetOn()) {
                    Log.d(TAG, "=================: isWiredHeadsetOn true");
                } else {
                    Log.d(TAG, "=================: isWiredHeadsetOn false");
                }

                //指示在当前平台支持使用SCO的用于电话会议使用情况。
                // 应用程序想使用蓝牙SCO音频时手机没有呼叫必须先调用此方法，以确保该平台支持此功能。
                if (mAudioManager.isBluetoothScoAvailableOffCall()) {
                    Log.d(TAG, "=================: isBluetoothScoAvailableOffCall true");
                } else {
                    Log.d(TAG, "=================: isBluetoothScoAvailableOffCall false");
                }

                //是否使用蓝牙SCO通信。
                if (mAudioManager.isBluetoothScoOn()) {
                    Log.d(TAG, "=================: isBluetoothScoOn true");
                } else {
                    Log.d(TAG, "=================: isBluetoothScoOn false");
                }

                //检查扬声器是否打开或关闭。
                if (mAudioManager.isSpeakerphoneOn()) {
                    Log.d(TAG, "=================: isSpeakerphoneOn true");
                } else {
                    Log.d(TAG, "=================: isSpeakerphoneOn false");
                }

                //检查蓝牙A2DP音频外设是否连接与否。
                //todo 蓝牙耳机连上会变为true
                if (mAudioManager.isBluetoothA2dpOn()) {
                    Log.d(TAG, "=================: isBluetoothA2dpOn true");
                } else {
                    Log.d(TAG, "=================: isBluetoothA2dpOn false");
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (AudioDeviceInfo device : mAudioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)) {
                    Log.e(TAG, "当前所有的音频输入设备：" + device.getProductName() +
                            " type:" + device.getType() +
                            " id:" + device.getId());
                }
            }
            //当前所有的音频输入设备：BKL-AL00 type:15 id:7
            //当前所有的音频输入设备：BKL-AL00 type:15 id:8
            //当前所有的音频输入设备：BKL-AL00 type:3 id:5287
            //当前所有的音频输入设备：BKL-AL00 type:18 id:9
            //当前所有的音频输入设备：Timekettle M2 type:7 id:5291
            //type  ==
            // 3:耳机mic组合,有线耳机
            // 15：手机内置mic
            // 18：电话mic
            // 7：蓝牙sco ，如 ： M2
            // ( 见 AudioDeviceInfo )

            Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    };


    private void initView() {
        mRoot = findViewById(R.id.root);
        mRvMsg = findViewById(R.id.rvMsg);
        mBtnVoice = findViewById(R.id.btnVoice);
        mBtnVoice.setOnVoiceButtonCallBack(new RecordAudioButton.OnVoiceButtonCallBack() {
            @Override
            public void onStartRecord() {
                mPresenter.startRecord();
            }

            @Override
            public void onStopRecord() {
                mPresenter.stopRecord();
            }

            @Override
            public void onWillCancelRecord() {
                mPresenter.willCancelRecord();
            }

            @Override
            public void onContinueRecord() {
                mPresenter.continueRecord();
            }
        });
        mRvMsg.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (R.id.iv_voice == view.getId()) {
                    mPresenter.startPlayRecord(position);
                }
            }
        });
        mRvMsg.setAdapter(mAdapter);
    }

    private void requestPermission() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_PRIVILEGED,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                )
                .request();
    }

    @Override
    public void showList(List<File> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showNormalTipView() {
        if (mRecordVoicePopWindow == null) {
            mRecordVoicePopWindow = new RecordVoicePopWindow(mContext);
        }
        mRecordVoicePopWindow.showAsDropDown(mRoot);
    }

    @Override
    public void showTimeOutTipView(int remainder) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showTimeOutTipView(remainder);
        }
    }

    @Override
    public void showRecordingTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordingTipView();
        }
    }

    @Override
    public void showRecordTooShortTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordTooShortTipView();
        }
    }

    @Override
    public void showCancelTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showCancelTipView();
        }
    }

    @Override
    public void hideTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.dismiss();
        }
    }

    @Override
    public void updateCurrentVolume(int db) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.updateCurrentVolume(db);
        }
    }

    @Override
    public void startPlayAnim(int position) {
        mAdapter.startPlayAnim(position);
    }

    @Override
    public void stopPlayAnim() {
        mAdapter.stopPlayAnim();
    }
}
