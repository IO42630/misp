package com.olexyn.misp.mock;

/**
 * Pass the MockSet.
 * Provide a Runnable.
 */
public class AdapterRunnable implements Runnable  {

    private MockSet mockSet;

    public AdapterRunnable(MockSet mockSet){
        super();
        this.mockSet = mockSet;
    }

    @Override
    public void run() {
        new AdapterMock(mockSet);
    }
}
