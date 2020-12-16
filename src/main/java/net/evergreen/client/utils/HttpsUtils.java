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

import net.evergreen.client.Evergreen;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpsUtils {

    public static void downloadFile(URL url, File destination) throws Exception {
        if (!destination.exists()) {
            URLConnection con = url.openConnection();
            DataInputStream dis = new DataInputStream(con.getInputStream());
            byte[] data = new byte[con.getContentLength()];

            for (int i = 0; i < data.length; i++) {
                data[i] = dis.readByte();
            }

            dis.close();
            FileOutputStream fos = new FileOutputStream(destination);
            fos.write(data);
            fos.close();
        }
    }

    public static String getString(String url) {
        try {
            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");
            httpClient.setUseCaches(false);
            httpClient.setRequestProperty("User-Agent", "Evergreen/" + Evergreen.VERSION);
            httpClient.setReadTimeout(15000);
            httpClient.setConnectTimeout(15000);
            httpClient.setDoOutput(true);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
