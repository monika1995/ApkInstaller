package app.adshandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import app.PrintLog;
import app.fcm.FCMTopicResponse;
import app.fcm.GCMPreferences;
import app.receiver.TopicAlarmReceiver;
import app.rest.request.DataRequest;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;
import app.server.v2.DataHubHandler;
import app.server.v2.DataHubPreference;
import app.serviceprovider.Utils;
import app.socket.EngineApiController;
import app.socket.Response;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by quantum4u1 on 25/04/18.
 */

class EngineHandler {

    private DataHubPreference preference;
    private DataHubHandler mHandler;
    private DataHubConstant mConstant;
    private GCMPreferences gcmPreference;

    private Context mContext;

    EngineHandler(Context context) {
        preference = new DataHubPreference(context);
        mHandler = new DataHubHandler();
        mConstant = new DataHubConstant(context);
        gcmPreference = new GCMPreferences(context);
        this.mContext = context;
    }


    void initServices(boolean fetchFromServer) {
        if (fetchFromServer) {
            doVersionRequest();
        } else {
            PrintLog.print("get pref data" + " " + new DataHubPreference(mContext).getAdsResponse());
            new DataHubHandler().parseMasterData(mContext, new DataHubPreference(mContext).getAdsResponse());
        }
    }

    private void doVersionRequest() {
        DataRequest dataRequest = new DataRequest();

        EngineApiController mApiController = new EngineApiController(mContext, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                PrintLog.print("response version OK" + " " + response);

                mHandler.parseVersionData(mContext, response.toString(), new DataHubHandler.MasterRequestListener() {
                    @Override
                    public void callMasterService() {
                        PrintLog.print("checking version flow domasterRequest");
                        doMasterRequest();
                    }
                });


            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                PrintLog.print("response version ERROR" + " " + errormsg);
                if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                    mHandler.parseMasterData(mContext, preference.getAdsResponse());
                } else {
                    mHandler.parseMasterData(mContext, mConstant.parseAssetData());
                }
            }
        }, EngineApiController.VERSION_ID_CODE);
        mApiController.getVersionRequest(dataRequest);

        doReferrerRequest();
    }


    private void doMasterRequest() {
        DataRequest mMasterRequest = new DataRequest();

        EngineApiController apiController = new EngineApiController(mContext, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                PrintLog.print("response master OK" + " " + response.toString() + " :" + responseType);

                mHandler.parseMasterData(mContext, response.toString());

            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                PrintLog.print("response master Failed" + " " + errormsg + " :type" + " " + responseType);

                if (!preference.getAdsResponse().equalsIgnoreCase(DataHubConstant.KEY_NA)) {
                    mHandler.parseMasterData(mContext, preference.getAdsResponse());
                } else {
                    mHandler.parseMasterData(mContext, mConstant.parseAssetData());
                }
            }
        }, EngineApiController.MASTER_SERVICE_CODE);
        apiController.getMasterData(mMasterRequest);


    }


    void doGCMRequest() {
        System.out.println("353 Logs >> 00");
        if (RestUtils.isVirtual(mContext)
                || !gcmPreference.getGCMRegister()
                || !gcmPreference.getGCMID().equalsIgnoreCase("NA")) {
            System.out.println("353 Logs >> 01");
            DataRequest mRequest = new DataRequest();
            EngineApiController mApiController = new EngineApiController(mContext, new Response() {
                @Override
                public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                    mHandler.parseFCMData(mContext, response.toString());

                }

                @Override
                public void onErrorObtained(String errormsg, int responseType) {
                    System.out.println("response GCM Failed receiver" + " " + errormsg);
                    gcmPreference.setGCMRegister(false);
                }
            }, EngineApiController.GCM_SERVICE_CODE);
            mApiController.setFCMTokens(gcmPreference.getGCMID());
            mApiController.getGCMIDRequest(mRequest);

            System.out.println("EngineHandler.doGCMRequest already register");
        }

    }

    private void doReferrerRequest() {
        if (!gcmPreference.getReferalRegister() && !gcmPreference.getreferrerId().equalsIgnoreCase("NA")) {
            DataRequest mRequest = new DataRequest();
            EngineApiController mController = new EngineApiController(mContext, new Response() {
                @Override
                public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                    PrintLog.print("response referal success" + " ");
                    mHandler.parseReferalData(mContext, response.toString());
                }

                @Override
                public void onErrorObtained(String errormsg, int responseType) {
                    PrintLog.print("response referal Failed app launch 1" + " " + errormsg);
                    gcmPreference.setReferalRegister(false);
                }
            }, EngineApiController.REFERRAL_ID_CODE);
            mController.getReferralRequest(mRequest);
        }
    }

    void doTopicsRequest() {
        /*
         *creating topic for first time or when app version change.
         */
        if (!RestUtils.getVersion(mContext).equalsIgnoreCase(String.valueOf(gcmPreference.getTopicAppVersion()))
                || !gcmPreference.getregisterAllTopics()) {
            createTopics(mContext);
        }
    }

    private ArrayList<String> topics, allsubscribeTopic;
    private String version;

    private void createTopics(Context context) {
        String country = "C_" + RestUtils.getCountryCode(context);
        version = "AV_" + RestUtils.getVersion(context);
        String osVersion = "OS_" + RestUtils.getOSVersion(context);
        String deviceVersion = "DV_" + RestUtils.getDeviceVersion(context);
        String date = "DT_" + RestUtils.getDateofLos_Angeles();
        String month = "DT_" + RestUtils.getMonthofLos_Angeles();

        if (!RestUtils.validateJavaDate(RestUtils.getDateofLos_Angeles())) {
            date = "DT_" + RestUtils.getDate(System.currentTimeMillis());
            System.out.println("EngineHandler.createTopics not valid " + date);
        }

        topics = new ArrayList<>();
        topics.add("all");
        topics.add(country);
        topics.add(version);
        topics.add(osVersion);
        topics.add(deviceVersion);
        topics.add(date);
        topics.add(month);

        allsubscribeTopic = new ArrayList<>();
        System.out.println("EngineHandler.createTopics " + gcmPreference.getAllSubscribeTopic());
        System.out.println("EngineHandler.createTopics topic ver " + version + " " + gcmPreference.getTopicAppVersion());

        /*
         * subscribe all topic first time and boolen getAllSubscribeTopic help to know
         * that is all topic subscribe successfully or not.
         */
        if (!gcmPreference.getAllSubscribeTopic()) {
            for (int i = 0; i < topics.size(); i++) {
                subscribeToTopic(topics.get(i));
            }

        } else if (!version.equalsIgnoreCase(gcmPreference.getTopicAppVersion())) {
            /*
             * here when app version updated, we unsubsribe last topic which we saved on setTopicAppVersion preference
             * and send to server new topic
             */
            System.out.println("EngineHandler.createTopics not equal");
            unsubscribeTopic(gcmPreference.getTopicAppVersion(), version);

        } else {
            System.out.println("EngineHandler.createTopics hi meeenuuu ");
            /*
             * here when server response failed and all topics are already subscribed successfully.
             */
            if (!gcmPreference.getregisterAllTopics()) {
                doFCMTopicRequest(topics);
            }
        }
    }


    private void subscribeToTopic(final String topicName) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        allsubscribeTopic.add(topicName);
                        if (topics.size() == allsubscribeTopic.size()) {
                            System.out.println("task successfull for all topics");
                            doFCMTopicRequest(allsubscribeTopic);
                            gcmPreference.setAllSubscribeTopic(true);
                            gcmPreference.setTopicAppVersion(version);
                        }
                        System.out.println("Subscribed to " + topicName + " topic");
                    } else {
                        System.out.println("Failed to subscribe to " + topicName + " topic");
                    }

                }
            });
        } catch (Exception e) {
            System.out.println("Subscribed to " + topicName + " topic failed " + e.getMessage());
        }

    }

    private void unsubscribeTopic(final String topicName, final String currentVersion) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("EngineHandler.createTopics unsubscribeTopic " + topicName);
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic(currentVersion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("EngineHandler.createTopics subscribeTopic " + currentVersion);
                doFCMTopicRequest(topics);
            }
        });
    }

    private void doFCMTopicRequest(ArrayList<String> list) {
        DataRequest mRequest = new DataRequest();
        EngineApiController mApiController = new EngineApiController(mContext, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("response FCM topic " + response);
                new DataHubHandler().parseFCMTopicData(response.toString(), new DataHubHandler.NotificationListener() {
                    @Override
                    public void pushFCMNotification(String json) {
                        if (json != null) {
                            showFCMTopicResponse(json);
                        }
                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                System.out.println("response FCM topic Failed receiver" + " " + errormsg);
                gcmPreference.setregisterAllTopics(false);
            }
        }, EngineApiController.FCM_TOPIC_CODE);
        mApiController.setAllTopics(list);
        mApiController.getFCMTopicData(mRequest);
    }

    private void showFCMTopicResponse(String response) {
        Gson gson = new Gson();
        FCMTopicResponse fcmTopicResponse = gson.fromJson(response, FCMTopicResponse.class);

        if (fcmTopicResponse.status.equalsIgnoreCase("0")) {
            gcmPreference.setregisterAllTopics(true);
            gcmPreference.setTopicAppVersion(version);

            if (fcmTopicResponse.pushData != null) {
                try {
                    if (fcmTopicResponse.pushData.reqvalue != null && fcmTopicResponse.pushData.reqvalue.contains("#")) {
                        String[] reqValueArr = fcmTopicResponse.pushData.reqvalue.split("#");
                        String reqvalue = reqValueArr[0];
                        String ifdelay = reqValueArr[1];
                        String delaytime = reqValueArr[2];
                        if (ifdelay != null && ifdelay.equalsIgnoreCase("yes")) {
                            gcmPreference.setOnBoardNotificationId(reqvalue);
                            setFCMAlarm(mContext, Integer.parseInt(delaytime));
                        } else {
                            Intent myIntent = new Intent(mContext, TopicAlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, myIntent, 0);
                            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void setFCMAlarm(Context context, int delayTime) {
        int dtime = Utils.getRandomNo(delayTime);
        gcmPreference.setFCMRandomOnboard(dtime);
        System.out.println("response FCM topic setFCMAlarm " + dtime);
        Intent myIntent = new Intent(context, TopicAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, myIntent, 0);
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
