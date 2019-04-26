package com.example.newsaggregator.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class GetData {
    private static final String TAG = "rssDB";

//    Переходим к коду. Прежде всего нужно помнить две вещи: нам требуется наличие подключения к интернету и обеспечить загрузку страницы в отдельном потоке.
//
//    Начнём с проверки подключения к интернету.
//    public void onClick(View view) {
//        ConnectivityManager myConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkinfo = myConnMgr.getActiveNetworkInfo();
//
//        if (networkinfo != null && networkinfo.isConnected()) {
//            new DownloadPageTask().execute(bestUrl); // запускаем в новом потоке
//        } else {
//            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
//        }
//    }

    static public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("http://google.comm"); //You can replace it with your name
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    static public String getData(String encoding, String url) {
        HttpURLConnection connection = null;

        StringBuilder sb = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);

            connection.setReadTimeout(3250);
            connection.connect();


            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                Log.d(TAG, "");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                int count = 0;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("/n");
                    count++;
                    Log.d(TAG, count + " " + sb.toString());
                }
                Log.d(TAG, " " + count);
                Log.d(TAG, " " + sb.toString());
            }

        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    //TODO закрыть соединения.
}