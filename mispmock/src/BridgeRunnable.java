

/**
 * Pass the MockSet.
 * Provide a Runnable.
 */
public class BridgeRunnable implements Runnable {

    MockSet mockSet;

    public BridgeRunnable(MockSet mockSet){
        super();
        this.mockSet = mockSet;
    }

    @Override
    public void run() {
        new BridgeMock(mockSet);
    }

}
