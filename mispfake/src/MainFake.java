public class MainFake {



    public static BridgeFake  bridgeFake;
    public static ClientFake clientFake;


    public  static void main(String... args){




        Runnable bridgeRunable = new BridgeFake();
        Runnable clientRunnable = new ClientFake();

        Thread bridgeThread = new Thread(bridgeRunable);
        Thread clientThread = new Thread(clientRunnable);
        bridgeThread.start();
        clientThread.start();

    }
}
