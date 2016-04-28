package org.trakhound.www.trakhound.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Patrick on 4/26/2016.
 */
public class Requests {

    public static String POST (String url, PostData[] postDatas) {

        String result = null;

        try{

            // Format PostData array
            String postData = FormatPostData(postDatas);

            // Open Connection
            URL u = new URL(url);
            URLConnection conn = u.openConnection();

            if (postData != null) {
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( postData );
                wr.flush();
            }

            // Read Response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Build response string
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            result = sb.toString();

        }
        catch(Exception e) { e.printStackTrace(); }

        return result;
    }

    private static String FormatPostData(PostData[] postDatas) {

        String result = null;
        if (postDatas != null) {

            result = "";

            for (int i = 0; i < postDatas.length; i++)  {
                result += EncodePostData(postDatas[i]);
                if (i < postDatas.length - 1) result += "&";
            }

        }

        return result;
    }

    private static String EncodePostData(PostData postData) {

        String result = null;

        try {

            String id = postData.Id;
            String value = postData.Value;

            result = URLEncoder.encode(id, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");

        } catch (Exception ex) { }

        return result;
    }

}