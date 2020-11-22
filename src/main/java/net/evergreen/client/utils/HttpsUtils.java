package net.evergreen.client.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
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

}
