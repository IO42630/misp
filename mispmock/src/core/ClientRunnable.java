package core;

import core.ClientMock;

/**
 * Pass the MockSet.
 * Provide a Runnable.
 */
public class ClientRunnable implements Runnable  {

    private MockSet mockSet;

    public ClientRunnable(MockSet mockSet){
        super();
        this.mockSet = mockSet;
    }

    @Override
    public void run() {
        new ClientMock(mockSet);
    }
}
