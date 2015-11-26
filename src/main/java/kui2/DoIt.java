package kui2;

import java.io.IOException;

class DoIt extends Thread {
    private String urlAddress;
    private String fileName;
    private String folder;
    private int speed;

    public DoIt(String urlAddress, String folder, String fileName, int speed) {
        super();
        this.urlAddress = urlAddress;
        this.folder = folder;
        this.fileName = fileName;
        this.speed = speed;
    }

    public void run() {
        try {
            int threadSize = Write.write32(urlAddress, folder, fileName, speed);
            System.out.println("Size of thread  " + threadSize);
            Take.addSize(threadSize);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}