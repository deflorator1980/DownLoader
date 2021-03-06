package kui2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Write {
    public static int write32(String urlAddress, String folder, String fileName, int speed)
            throws IOException, InterruptedException {
        InputStream is;
        FileOutputStream file;
        BufferedInputStream bi;
        BufferedOutputStream bo;
        URL url = new URL(urlAddress);
        URLConnection urlCon = url.openConnection();

        try {
            Files.createDirectories(Paths.get(folder));
        } catch (FileAlreadyExistsException ignored) {}

        file = new FileOutputStream(folder + "/" + fileName);
        is = urlCon.getInputStream();
        bi = new BufferedInputStream(is);
        bo = new BufferedOutputStream(file);
        int data;

        int delta;
        int bytesProSecond = 1024 * speed;
        int secondVolume = 0;
        int threadVolume = 0;
        long secondBegun = System.currentTimeMillis();
        while ((data = bi.read()) != -1) {
            bo.write(data);
            secondVolume++;
            threadVolume++;
            if (secondVolume >= bytesProSecond) {
                delta = (int) (System.currentTimeMillis() - secondBegun);
                if (delta < 1000) {
//                    Thread.sleep(1000 - delta);
                    TimeUnit.MILLISECONDS.sleep(1000 - delta);
                    secondVolume = 0;
                    secondBegun = System.currentTimeMillis();
                }
            }
        }
        return threadVolume;
    }

}
