package com.olexyn.misp.reverse.runnable;

import com.olexyn.misp.reverse.Reverse;
import com.olexyn.misp.reverse.Tools;
import org.json.JSONObject;

public class CheckSuppyR implements Runnable {

    private int available;
    public int CHECK_SUPPLY_INTERVAL = 100;

    private Reverse reverse;

    public CheckSuppyR(Reverse reverse) {
        this.reverse = reverse;
    }


    @Override
    public void run() {
        while (true) {
            JSONObject obj = new JSONObject().put("available", 0);

            try {
                String result = Tools.send("POST", reverse.FORWARD_URL, obj.toString());

                JSONObject resultObj = new JSONObject(result);

                available = resultObj.getInt("available");

                Thread.sleep(CHECK_SUPPLY_INTERVAL);

            } catch (Exception ignored) { }
        }


    }


    public int getAvailable() { return available; }
}
