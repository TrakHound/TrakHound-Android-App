package org.trakhound.www.trakhound.devices;

import java.util.ArrayList;

/**
 * Created by Patrick on 4/27/2016.
 */
public class Table {

    private static String TABLE_DELIMITER_START = "\\^\\^\\^";
    private static String TABLE_DELIMITER_END = "~~~";

    public String Name;
    public String Data;

    public static Table[] get(String s) {

        String[] tables = splitTables(s);
        if (tables != null) {

            ArrayList<Table> tableList = new ArrayList<>();

            for (int i = 0; i < tables.length; i++) {

                Table table = getTable(tables[i]);
                if (table != null) tableList.add(table);
            }

            Table[] tableArray = new Table[tableList.size()];
            return tableList.toArray(tableArray);
        }

        return null;
    }

    private static String[] splitTables(String s) {

        String[] tables = s.split(TABLE_DELIMITER_START);
        if (tables != null && tables.length > 0) {

            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < tables.length; i++) {

                if (tables[i] != null && tables[i].length() > 0) {

                    result.add(tables[i]);

                }
            }

            String[] a = new String[result.size()];
            return result.toArray(a);

        } else {

            return null;
        }

    }

    private static Table getTable(String s) {

        Table result = null;

        int delimiter = s.indexOf(TABLE_DELIMITER_END);
        if (delimiter >= 0) {

            result = new Table();
            result.Name = s.substring(0, delimiter);
            result.Data = s.substring(delimiter + TABLE_DELIMITER_END.length());

        }

        return result;
    }


}
