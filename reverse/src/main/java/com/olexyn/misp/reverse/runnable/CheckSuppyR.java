package com.olexyn.misp.reverse.runnable;

import com.olexyn.misp.reverse.Tools;
import lombok.Getter;
import org.json.JSONObject;

import static com.olexyn.misp.helper.Constants.POST;
import static com.olexyn.misp.reverse.Reverse.CHECK_SUPPLY_INTERVAL_MS;
import static com.olexyn.misp.reverse.Reverse.FORWARD_URL;
import static com.olexyn.misp.helper.Constants.AVAILABLE;

public class CheckSuppyR implements Runnable {

    @Getter
    private int available;

    @Override
    public void run() {
        while (true) {
            JSONObject obj = new JSONObject().put(AVAILABLE, 0);

            try {
                String result = Tools.send(POST, FORWARD_URL, obj.toString());

                JSONObject resultObj = new JSONObject(result);

                available = resultObj.getInt(AVAILABLE);

                Thread.sleep(CHECK_SUPPLY_INTERVAL_MS);

            } catch (Exception ignored) { }
        }


    }

}
