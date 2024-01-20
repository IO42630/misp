package com.olexyn.misp.reverse.runnable;

import com.olexyn.misp.reverse.Reverse;
import com.olexyn.misp.reverse.Tools;
import lombok.Getter;
import org.json.JSONObject;

import static com.olexyn.misp.reverse.Reverse.FORWARD_URL;

public class CheckSuppyR implements Runnable {

    @Getter
    private int available;

    public int CHECK_SUPPLY_INTERVAL_MILLI = 100;

    private Reverse reverse;

    public CheckSuppyR(Reverse reverse) {
        this.reverse = reverse;
    }


    @Override
    public void run() {
        while (true) {
            JSONObject obj = new JSONObject().put("available", 0);

            try {
                String result = Tools.send("POST", FORWARD_URL, obj.toString());

                JSONObject resultObj = new JSONObject(result);

                available = resultObj.getInt("available");

                Thread.sleep(CHECK_SUPPLY_INTERVAL_MILLI);

            } catch (Exception ignored) { }
        }


    }

}
