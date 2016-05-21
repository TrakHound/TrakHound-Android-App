// Copyright (c) 2016 Feenux LLC, All Rights Reserved.

// This file is subject to the terms and conditions defined in
// file 'LICENSE.txt', which is part of this source code package.

package org.trakhound.www.trakhound;

/**
 * Created by Patrick on 5/16/2016.
 */
public class TH_Tools {

    public static String capitalizeFirst(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
