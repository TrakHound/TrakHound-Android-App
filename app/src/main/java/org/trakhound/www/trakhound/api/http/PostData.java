// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound.api.http;

/**
 * Created by Patrick on 4/26/2016.
 */
public class PostData {

    public PostData(String id, String value){
        Id = id;
        Value = value;
    }

    public String Id;
    public String Value;
}