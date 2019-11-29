package com.techproof.appinstaller;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import app.PrintLog;
import app.fcm.FCMController;
import app.fcm.GCMPreferences;
import app.fcm.MapperUtils;
import app.fcm.NotificationUIResponse;
import app.receiver.FirebaseAlarmReceiver;
import app.rest.request.DataRequest;
import app.server.v2.DataHubConstant;
import app.server.v2.DataHubHandler;
import app.server.v2.DataHubPreference;
import app.serviceprovider.Utils;
import app.socket.EngineApiController;
import app.socket.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private DataHubHandler mHandler;
    private DataHubPreference preference;
    private GCMPreferences gcmPreferences;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        System.out.println("MyFirebaseMessagingService.onNewToken " + token);
        gcmPreferences = new GCMPreferences(this);
        gcmPreferences.setGCMID(token);
        mHandler = new DataHubHandler();
        requestGCM(token);
    }

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        System.out.println("152 get message ");
        System.out.println("152 get message getData " + remoteMessage.getData());
        mHandler = new DataHubHandler();
        gcmPreferences = new GCMPreferences(this);
        preference = new DataHubPreference(this);

        try {
            /*for (String key : remoteMessage.getData().keySet()) {
                String value = remoteMessage.getData().get(key);
                doNotificationRequest(value);
            }*/

            /*
             * here reqvalueData get with key reqvalue and
             * spilt with # to get reqvalue, ifdelay and delaytime...
             */

            String reqvalueData = remoteMessage.getData().get("reqvalue");
            System.out.println("152 get message reqvalue " + reqvalueData);
            if (reqvalueData != null) {
                if (reqvalueData.contains("#")) {
                    String[] reqValueArr = reqvalueData.split("#");
                    String reqvalue = reqValueArr[0];
                    String ifdelay = reqValueArr[1];
                    String delaytime = reqValueArr[2];

                    if (ifdelay != null && ifdelay.equalsIgnoreCase("yes")) {
                        gcmPreferences.setFCMNotificationId(reqvalue);
                        setFCMAlarm(this, Integer.parseInt(delaytime));

                    } else {
                        doNotificationRequest(reqvalue);
                    }
                }


            }


        } catch (Exception e) {
            System.out.println("exception 152 get here is the notification exception" + " " + e);
        }
    }

    private void showFCMNotification(String response) {
        Gson gson = new Gson();
        NotificationUIResponse notiResponse = gson.fromJson(response, NotificationUIResponse.class);

        if (notiResponse.status.equalsIgnoreCase("0") && notiResponse.type != null) {
            if (notiResponse.type.equalsIgnoreCase(MapperUtils.MATER_UPDATE)) {
                doMasterRequest(this);
            } else {
                new FCMController(getApplicationContext(), notiResponse);
            }
        }
    }


    private void doMasterRequest(final Context mContext) {
        DataRequest mMasterRequest = new DataRequest();
        EngineApiController apiController = new EngineApiController(mContext, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                PrintLog.print("response master OK from firebase" + " " + response.toString() + " :" + responseType);
                mHandler.parseMasterData(mContext, response.toString());

            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                PrintLog.print("response master Failed  from firebase" + " " + errormsg + " :type" + " " + responseType);
                if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                    mHandler.parseMasterData(mContext, preference.getAdsResponse());
                } else {
                    mHandler.parseMasterData(mContext, new DataHubConstant(mContext).parseAssetData());
                }
            }
        }, EngineApiController.MASTER_SERVICE_CODE);
        apiController.getMasterData(mMasterRequest);


    }


    private void doNotificationRequest(String value) {
        DataRequest request = new DataRequest();
        EngineApiController mApiController = new EngineApiController(getApplicationContext(),
                new Response() {
                    @Override
                    public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                        new DataHubHandler().parseNotificationData(response.toString(), new DataHubHandler.NotificationListener() {
                            @Override
                            public void pushFCMNotification(String json) {
                                if (json != null)
                                    showFCMNotification(json);
                            }
                        });
                    }

                    @Override
                    public void onErrorObtained(String errormsg, int responseType) {
                        System.out.println("response on notification ERROR" + " " + errormsg);
                    }
                }, EngineApiController.NOTIFICATION_ID_CODE);
        mApiController.setNotificatioID(value);
        mApiController.getNotificationIDRequest(request);
    }

    private void requestGCM(String mToken) {
        DataRequest mRequest = new DataRequest();
        EngineApiController mApiController = new EngineApiController(getApplicationContext(), new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                mHandler.parseFCMData(getApplicationContext(), response.toString());

            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                System.out.println("response GCM Failed receiver" + " " + errormsg);
                gcmPreferences.setGCMRegister(false);
            }
        }, EngineApiController.GCM_SERVICE_CODE);
        mApiController.setFCMTokens(mToken);
        mApiController.getGCMIDRequest(mRequest);
    }

    private void setFCMAlarm(Context context, int delayTime) {
        int dtime = Utils.getRandomNo(delayTime);
        System.out.println("152 get message setFCMAlarm " + dtime);
        gcmPreferences.setFCMRandomDelay(dtime);
        Intent myIntent = new Intent(context, FirebaseAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis() + dtime, pendingIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + dtime, pendingIntent);

        } else {
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + dtime, pendingIntent);
        }

    }
}

