package com.example.saudiestate.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Golden Code on 3/9/2018.
 */

public class Estates {
   public String KeyEstate , Type , Name , EstateType , Image , Age  , Floors , NoOfRooms , NoOfpaths , City , Price , NearServices , Lat, Lng , KeyUser;
    public Map<String, Object> toMap() {
        HashMap<String, Object> resault = new HashMap<>();
        resault.put("KeyEstate", KeyEstate);
        resault.put("Type", Type);
        resault.put("Name", Name);
        resault.put("EstateType", EstateType);
        resault.put("Image", Image);
        resault.put("Age", Age);
        resault.put("Floors", Floors);
        resault.put("NoOfRooms", NoOfRooms);
        resault.put("NoOfpaths", NoOfpaths);
        resault.put("City", City);
        resault.put("Price", Price);
        resault.put("NearServices", NearServices);
        resault.put("Lat", Lat);
        resault.put("Lng", Lng);
        resault.put("KeyUser", KeyUser);
        return resault;

    }
}
