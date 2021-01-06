package com.cashiar.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.cashiar.R;
import com.cashiar.models.UserModel;
import com.cashiar.preferences.Preferences;
import com.cashiar.remote.Api;
import com.cashiar.tags.Tags;
import com.cashiar.ui.activity_welcome.WelcomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireBaseMessaging extends FirebaseMessagingService {

    Preferences preferences = Preferences.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("keys", key + "    value " + map.get(key));
        }

        if (getSession().equals(Tags.session_login)) {
            if (map.get("trader_id") != null) {
                int to_id = Integer.parseInt(map.get("trader_id"));

                if (getCurrentUser_id() == to_id) {
                    manageNotification(map);
                }
            }
        }
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (getSession().equals(Tags.session_login)) {
            updateTokenFireBase(s);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

       if (not_type.equals("newTraders")) {

            String title = map.get("title");
            String body = map.get("body");

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);
           Intent intent = null;

               intent = new Intent(this, WelcomeActivity.class);


           intent.putExtra("not", true);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

           TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
           taskStackBuilder.addNextIntent(intent);

           PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


//
            builder.setContentIntent(pendingIntent);

            builder.setContentTitle(title);
            builder.setContentText(Jsoup.parse(body).text());
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Jsoup.parse(body).text()));
UserModel userModel=new UserModel();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
           EventBus.getDefault().post(userModel);

           if (manager != null) {
                EventBus.getDefault().post(userModel);

                manager.createNotificationChannel(channel);

                manager.notify(Tags.not_tag, Tags.not_id, builder.build());


            }


        }

    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map) {


        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");

        if (not_type.equals("newTraders")) {

            String title = map.get("title");
            String body = map.get("body");

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

//            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
//
//            channel.setShowBadge(true);
//            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
//                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//                    .build()
//            );
            Intent intent = null;

            intent = new Intent(this, WelcomeActivity.class);


            intent.putExtra("not", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);

            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


//
            builder.setContentIntent(pendingIntent);
            builder.setContentTitle(title);
            builder.setContentText(Jsoup.parse(body).text());
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Jsoup.parse(body).text()));
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            //    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

            //  PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            //builder.setContentIntent(pendingIntent);


            builder.setContentTitle(title);
            builder.setContentText(body);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
            UserModel userModel=new UserModel();
            EventBus.getDefault().post(userModel);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                EventBus.getDefault().post(userModel);

                // manager.createNotificationChannel(channel);
                manager.notify(Tags.not_tag, Tags.not_id, builder.build());


            }


        }
    }





    private int getCurrentUser_id() {
        return preferences.getUserData(this).getId();
    }



    private String getSession() {
        return preferences.getSession(this);
    }
    private void updateTokenFireBase(String token) {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                try {

                    Api.getService(Tags.base_url)
                            .updatePhoneToken( token, preferences.getUserData(this).getId(), "android")
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Log.e("token", "updated successfully");
                                    } else {
                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("error", t.getMessage());
                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                              //  Toast.makeText(FireBaseMessaging.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(FireBaseMessaging.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {


                }

            }
        });
    }


}
