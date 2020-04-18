public class Main {





    public  static void main(String... args){

        MockSet mockSet = new MockSet();

        Runnable publicRunnable = new PublicMock(mockSet);
        Runnable bridgeRunable = new BridgeRunnable(mockSet);
        Runnable clientRunnable = new ClientRunnable(mockSet);
        Runnable appRunnable = new AppMock(mockSet);

        Thread publicThread = new Thread(publicRunnable);
        Thread bridgeThread = new Thread(bridgeRunable);
        Thread clientThread = new Thread(clientRunnable);
        Thread appThread = new Thread(appRunnable);

        publicThread.start();
        bridgeThread.start();
        clientThread.start();
        appThread.start();

        int br =0;
    }
}



