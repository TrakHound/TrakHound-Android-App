// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Patrick on 4/26/2016.
 */
public class Requests {

    public static String POST = "POST";
    public static String GET = "GET";

    public static String post (String url, PostData[] postDatas) {

        return runRequest(url, postDatas, POST);
    }

    public static String get (String url) {

        return runRequest(url, null, GET);
    }

    public static Bitmap getImage(String url) {

        try {

            HttpURLConnection conn = getConnection(url, GET);

            InputStream input = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            conn.disconnect();

            return bitmap;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


    private static String formatPostData(PostData[] postDatas) {

        String result = null;
        if (postDatas != null) {

            result = "";

            for (int i = 0; i < postDatas.length; i++)  {
                result += encodePostData(postDatas[i]);
                if (i < postDatas.length - 1) result += "&";
            }

        }

        return result;
    }

    private static String encodePostData(PostData postData) {

        String result = null;

        try {

            String id = postData.Id;
            String value = postData.Value;

            result = URLEncoder.encode(id, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");

        } catch (Exception ex) { }

        return result;
    }

    private static String runRequest(String url, PostData[] postDatas, String method) {

        String result = null;

        // Format PostData array
        String postData = formatPostData(postDatas);

        InputStream stream = null;

        try {

            HttpURLConnection conn = getConnection(url, method);
            if (conn != null) {

                if (postData != null) {

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( postData );
                    wr.flush();
                    wr.close();
                }

                stream = conn.getInputStream();
                result = readTextStream(stream);

                conn.disconnect();
            }
        }
        catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    private static HttpURLConnection getConnection(String url, String method) {

        try {

            // Open Connection
            URL u = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod(method);

            conn.setDoInput(true);
            conn.setDoOutput(method.equals(POST));

            //conn.setRequestMethod(method);

            conn.connect();

            return conn;

        }
        catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    private static String readTextStream(InputStream stream) throws IOException {

        String result = null;

        if (stream != null) {

            InputStreamReader streamReader = new InputStreamReader(stream);
            if (streamReader != null) {

                BufferedReader reader = new BufferedReader(streamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Build response string
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                result = sb.toString();

                // Close IO Readers
                reader.close();
                streamReader.close();
            }
        }

        return result;
    }

}