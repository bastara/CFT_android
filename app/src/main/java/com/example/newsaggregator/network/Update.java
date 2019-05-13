package com.example.newsaggregator.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.newsaggregator.DBRequest;
import com.example.newsaggregator.MainActivity;
import com.example.newsaggregator.R;
import com.example.newsaggregator.data.Contract;
import com.example.newsaggregator.data.db.DBAdapter;
import com.example.newsaggregator.data.db.ParseXML;
import com.example.newsaggregator.data.preference.Preference;

import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Update {

    public void upDate(final Context context, boolean fromMain) {
        String cs = Context.CONNECTIVITY_SERVICE;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            if (fromMain) {
                Toast.makeText(context, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Cursor cursor;
        DBRequest dbRequest = new DBRequest(context);
        cursor = dbRequest.getCursorRefreshNews();

        int max = cursor.getCount();
        cursor.moveToFirst();

        Preference preference = new Preference(context);
        if (preference.getNotification()) {
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.setAction("ru.startandroid.notifications.action_delete");
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId")
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("Обновление новостей")
                    .setContentText("Подготовка")
                    .setProgress(max, 0, true)
                    .setAutoCancel(true)
                    .addAction(android.R.drawable.ic_delete, "Прервать", resultPendingIntent)
                    .setContentIntent(resultPendingIntent);

            notificationManager.notify(1, builder.build());

            //оставил для наглядности, как напоминание о Значимости выполняемого действия,
            // в финале естественное этого не будет и уж тем более не в UI
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int progress = 0;
            for (int i = 0; i < max; i++) {
                Log.d(Contract.Entry.TAG, "Обновление новостей-" + i);
                //оставил для наглядности, в дальнейшем удалю, иначе все быстро отрабатывает и не видно прогрессбара.
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress += 1;
                builder.setProgress(max, progress, false)
                        .setContentText(progress + " из " + max);
                notificationManager.notify(1, builder.build());

                String url = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_URL));
                ParseXML.parseXML(url, context);
                cursor.moveToNext();
            }
            builder.setProgress(0, 10, false)
                    .setContentText("Готово");
            notificationManager.notify(1, builder.build());
        } else {
            for (int i = 0; i < max; i++) {
                String url = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_URL));
                ParseXML.parseXML(url, context);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }
}
