// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.users;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import org.trakhound.www.trakhound.R;
import org.trakhound.www.trakhound.http.Requests;

/**
 * Created by Patrick on 5/16/2016.
 */
public class GetUserImage extends AsyncTask<String,Void,Bitmap> {

    private View context;

    private UserConfiguration userConfig;

    public GetUserImage(View context, UserConfiguration userConfig) {

        this.context = context;
        this.userConfig = userConfig;
    }

    protected void onPreExecute(){

    }

    @Override
    protected Bitmap doInBackground(String... arg0) {

        if (userConfig.Image_Url != null) {

            String url = "http://www.feenux.com/trakhound/users/files/" + userConfig.Image_Url;

            return Requests.getImage(url);
        }

        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap){

        View v = context.findViewById(R.id.UserImage);
        if (v != null) {

            if (bitmap != null) {

                ((ImageView)v).setImageBitmap(bitmap);

                v = context.findViewById(R.id.UserImageBorder);
                if (v != null) v.setVisibility(View.VISIBLE);

            } else {

                v = context.findViewById(R.id.UserImageBorder);
                if (v != null) v.setVisibility(View.INVISIBLE);
            }
        }

    }

}
