/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
