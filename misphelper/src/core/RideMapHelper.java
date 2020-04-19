package core;

import core.Ride;

import java.util.Map;

public class RideMapHelper {

    public Map<Long, Ride> rideMap;


    RideMapHelper(Map<Long,Ride> rideMap){
        this.rideMap = rideMap;
    }



    public  boolean containsMore(int i, State state){
        long availableRides = 0L;
        for (Map.Entry<Long,Ride> entry : rideMap.entrySet()){
            if (entry.getValue().getState() == state){
                availableRides++;
            }
            if (availableRides > i){
                return true;
            }
        }
        return false;
    }


    public  boolean containsLess(int i, State state){
        long availableRides = 0L;
        for (Map.Entry<Long,Ride> entry : rideMap.entrySet()){
            if (entry.getValue().getState() == state){
                availableRides++;
            }
            if (availableRides < i){
                return true;
            }
        }
        return false;
    }


    public Ride pickAvailable(){
        for (Map.Entry<Long,Ride> entry : rideMap.entrySet()){
            if (entry.getValue().getState() == State.AVAILABLE){
                return entry.getValue();
            }

        }
        return null;
    }


}
