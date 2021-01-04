/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.utils;

import net.evergreen.client.utils.json.BetterJsonObject;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static List<String> getLinesFromFile(File file) throws FileNotFoundException {
        BufferedReader f = new BufferedReader(new FileReader(file));
        return f.lines().collect(Collectors.toList());
    }

    public static List<String> getLinesFromStream(InputStream is) {
        BufferedReader f = new BufferedReader(new InputStreamReader(is));
        return f.lines().collect(Collectors.toList());
    }

    public static BetterJsonObject getJsonFromFile(File file) throws FileNotFoundException {
        List<String> lines = getLinesFromFile(file);
        if (lines.isEmpty()) return null;
        String builder = String.join("", lines);
        if (builder.trim().length() > 0)
            return new BetterJsonObject(builder.trim());
        return null;
    }

}
