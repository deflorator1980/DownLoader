package kui2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Take {
    static int commonSize = 0;

    public static void main(String[] args) throws IOException,
            InterruptedException {

        String urlAddress;
        String fileName;
        String dataFile = null;
        String folder = null;
        StringTokenizer tok;
        String speedArgument = null;
        int threads = 0;

        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i])) {
                folder = args[i + 1];
            }
            if ("-f".equals(args[i])) {
                dataFile = args[i + 1];
            }
            if ("-n".equals(args[i])) {
                threads = Integer.parseInt(args[i + 1]);
            }
            if ("-l".equals(args[i])) {
                speedArgument = args[i + 1];
            }
        }

        if (folder == null) {
            System.out.println("no foder");
            return;
        }
        if (dataFile == null) {
            System.out.println("no file");
            return;
        }
        if (threads <= 0) {
            System.out.println("no threads");
            return;
        }
        if (speedArgument == null) {
            System.out.println("no speed");
            return;
        }

        int speed = speedParser(speedArgument);
        if (speed == 0) {
            System.out.println("Enter correct speed");
            return;
        }

        List<String> lines = Files.readAllLines(Paths.get(dataFile));

        ExecutorService exe = Executors.newFixedThreadPool(threads);
        System.out.println("Downliading...");
        long startTime = System.currentTimeMillis();

        for (String line : lines) {
            tok = new StringTokenizer(line, " ");
            urlAddress = tok.nextToken();
            fileName = tok.nextToken();
            exe.execute(new DoIt(urlAddress, folder, fileName, speed));
        }
        exe.shutdown();
        exe.awaitTermination(2, TimeUnit.MINUTES);
        System.out.println("Time "
                + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("Size " + commonSize + " bytes");

    }

    public static int speedParser(String speedArgument) {
        char rate = speedArgument.charAt(speedArgument.length() - 1);
        int speed = Integer.parseInt(speedArgument.replaceAll("[km]", ""));
        if (rate == 'm')
            speed *= 1024;
        else if (rate != 'k') {
            System.out.println("Enter speed suffix");
            return 0;
        }
        return speed;
    }


    public static synchronized void addSize(int threadSize) {
        commonSize += threadSize;
    }

}


