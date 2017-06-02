package com.example.yu.login;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Quan on 4/10/2017.
 */

public class Constants {
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 30;

    public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Moscow South", new LatLng(37.783888,-122.4009012));

        // Googleplex.
        LANDMARKS.put("Japantown", new LatLng(37.785281,-122.4296384));

        // Test
        LANDMARKS.put("SFO", new LatLng(37.621313,-122.378955));

        // UW Red Square
        LANDMARKS.put("UW Red Square", new LatLng(47.655917, -122.309666));

        // Starbucks on the ave near jjs
        LANDMARKS.put("Starbucks Ave & 42nd", new LatLng(47.658246, -122.313364));

        // Quan's apt
        LANDMARKS.put("Quan's apt", new LatLng(47.658056, -122.315207));

        // Trader Joe's
        LANDMARKS.put("Trader Joe's", new LatLng(47.662706, -122.317748));

        // EE Buiulding's
        LANDMARKS.put("EE Building", new LatLng(47.653261, -122.305837));


    }


    // TODO: 5/4/17
    // Implement this geofencing strategy:
    // http://stackoverflow.com/questions/31847977/android-build-polygonal-shape-geofence/32133568#32133568
    // The polygon of Washington state
    public static final List<LatLng> POLYGON_WA = new ArrayList<LatLng>();
    static {
        POLYGON_WA.add(new LatLng(49.0023, -123.3208));
        POLYGON_WA.add(new LatLng(49.0027, -123.0338));
        POLYGON_WA.add(new LatLng(49.0018, -122.0650));
        POLYGON_WA.add(new LatLng(48.9973, -121.7491));
        POLYGON_WA.add(new LatLng(48.9991, -121.5912));
        POLYGON_WA.add(new LatLng(49.0009, -119.6082));
        POLYGON_WA.add(new LatLng(49.0005, -118.0378));
        POLYGON_WA.add(new LatLng(48.9996, -117.0319));
        POLYGON_WA.add(new LatLng(47.9614, -117.0415));
        POLYGON_WA.add(new LatLng(46.5060, -117.0394));
        POLYGON_WA.add(new LatLng(46.4274, -117.0394));
        POLYGON_WA.add(new LatLng(46.3498, -117.0621));
        POLYGON_WA.add(new LatLng(46.3384, -117.0277));
        POLYGON_WA.add(new LatLng(46.2848, -116.9879));
        POLYGON_WA.add(new LatLng(46.2388, -116.9577));
        POLYGON_WA.add(new LatLng(46.2022, -116.9659));
        POLYGON_WA.add(new LatLng(46.1722, -116.9254));
        POLYGON_WA.add(new LatLng(46.1432, -116.9357));
        POLYGON_WA.add(new LatLng(46.1009, -116.9584));
        POLYGON_WA.add(new LatLng(46.0785, -116.9762));
        POLYGON_WA.add(new LatLng(46.0537, -116.9433));
        POLYGON_WA.add(new LatLng(45.9960, -116.9165));
        POLYGON_WA.add(new LatLng(46.0008, -118.0330));
        POLYGON_WA.add(new LatLng(45.9998, -118.9867));
        POLYGON_WA.add(new LatLng(45.9320, -119.1302));
        POLYGON_WA.add(new LatLng(45.9278, -119.1708));
        POLYGON_WA.add(new LatLng(45.9402, -119.2559));
        POLYGON_WA.add(new LatLng(45.9354, -119.3047));
        POLYGON_WA.add(new LatLng(45.9220, -119.3644));
        POLYGON_WA.add(new LatLng(45.9172, -119.4386));
        POLYGON_WA.add(new LatLng(45.9067, -119.4894));
        POLYGON_WA.add(new LatLng(45.9249, -119.5724));
        POLYGON_WA.add(new LatLng(45.9196, -119.6013));
        POLYGON_WA.add(new LatLng(45.8565, -119.6700));
        POLYGON_WA.add(new LatLng(45.8479, -119.8052));
        POLYGON_WA.add(new LatLng(45.8278, -119.9096));
        POLYGON_WA.add(new LatLng(45.8245, -119.9652));
        POLYGON_WA.add(new LatLng(45.7852, -120.0710));
        POLYGON_WA.add(new LatLng(45.7623, -120.1705));
        POLYGON_WA.add(new LatLng(45.7258, -120.2110));
        POLYGON_WA.add(new LatLng(45.7057, -120.3628));
        POLYGON_WA.add(new LatLng(45.6951, -120.4829));
        POLYGON_WA.add(new LatLng(45.7469, -120.5942));
        POLYGON_WA.add(new LatLng(45.7460, -120.6340));
        POLYGON_WA.add(new LatLng(45.7143, -120.6924));
        POLYGON_WA.add(new LatLng(45.6721, -120.8558));
        POLYGON_WA.add(new LatLng(45.6409, -120.9142));
        POLYGON_WA.add(new LatLng(45.6572, -120.9471));
        POLYGON_WA.add(new LatLng(45.6419, -120.9787));
        POLYGON_WA.add(new LatLng(45.6529, -121.0645));
        POLYGON_WA.add(new LatLng(45.6078, -121.1469));
        POLYGON_WA.add(new LatLng(45.6083, -121.1847));
        POLYGON_WA.add(new LatLng(45.6721, -121.2177));
        POLYGON_WA.add(new LatLng(45.7057, -121.3392));
        POLYGON_WA.add(new LatLng(45.6932, -121.4010));
        POLYGON_WA.add(new LatLng(45.7263, -121.5328));
        POLYGON_WA.add(new LatLng(45.7091, -121.6145));
        POLYGON_WA.add(new LatLng(45.6947, -121.7361));
        POLYGON_WA.add(new LatLng(45.7067, -121.8095));
        POLYGON_WA.add(new LatLng(45.6452, -121.9338));
        POLYGON_WA.add(new LatLng(45.6088, -122.0451));
        POLYGON_WA.add(new LatLng(45.5833, -122.1089));
        POLYGON_WA.add(new LatLng(45.5838, -122.1426));
        POLYGON_WA.add(new LatLng(45.5660, -122.2009));
        POLYGON_WA.add(new LatLng(45.5439, -122.2641));
        POLYGON_WA.add(new LatLng(45.5482, -122.3321));
        POLYGON_WA.add(new LatLng(45.5756, -122.3795));
        POLYGON_WA.add(new LatLng(45.5636, -122.4392));
        POLYGON_WA.add(new LatLng(45.6006, -122.5676));
        POLYGON_WA.add(new LatLng(45.6236, -122.6891));
        POLYGON_WA.add(new LatLng(45.6582, -122.7647));
        POLYGON_WA.add(new LatLng(45.6817, -122.7750));
        POLYGON_WA.add(new LatLng(45.7613, -122.7619));
        POLYGON_WA.add(new LatLng(45.8106, -122.7962));
        POLYGON_WA.add(new LatLng(45.8642, -122.7839));
        POLYGON_WA.add(new LatLng(45.9120, -122.8114));
        POLYGON_WA.add(new LatLng(45.9612, -122.8148));
        POLYGON_WA.add(new LatLng(46.0160, -122.8587));
        POLYGON_WA.add(new LatLng(46.0604, -122.8848));
        POLYGON_WA.add(new LatLng(46.0832, -122.9034));
        POLYGON_WA.add(new LatLng(46.1028, -122.9597));
        POLYGON_WA.add(new LatLng(46.1556, -123.0579));
        POLYGON_WA.add(new LatLng(46.1865, -123.1210));
        POLYGON_WA.add(new LatLng(46.1893, -123.1664));
        POLYGON_WA.add(new LatLng(46.1446, -123.2810));
        POLYGON_WA.add(new LatLng(46.1470, -123.3703));
        POLYGON_WA.add(new LatLng(46.1822, -123.4314));
        POLYGON_WA.add(new LatLng(46.2293, -123.4287));
        POLYGON_WA.add(new LatLng(46.2691, -123.4946));
        POLYGON_WA.add(new LatLng(46.2582, -123.5557));
        POLYGON_WA.add(new LatLng(46.2573, -123.6209));
        POLYGON_WA.add(new LatLng(46.2497, -123.6875));
        POLYGON_WA.add(new LatLng(46.2691, -123.7404));
        POLYGON_WA.add(new LatLng(46.2350, -123.8729));
        POLYGON_WA.add(new LatLng(46.2383, -123.9292));
        POLYGON_WA.add(new LatLng(46.2677, -123.9711));
        POLYGON_WA.add(new LatLng(46.2924, -124.0212));
        POLYGON_WA.add(new LatLng(46.2653, -124.0329));
        POLYGON_WA.add(new LatLng(46.2596, -124.2444));
        POLYGON_WA.add(new LatLng(46.4312, -124.2691));
        POLYGON_WA.add(new LatLng(46.8386, -124.3529));
        POLYGON_WA.add(new LatLng(47.1832, -124.4380));
        POLYGON_WA.add(new LatLng(47.4689, -124.5616));
        POLYGON_WA.add(new LatLng(47.8012, -124.7566));
        POLYGON_WA.add(new LatLng(48.0423, -124.8679));
        POLYGON_WA.add(new LatLng(48.2457, -124.8679));
        POLYGON_WA.add(new LatLng(48.3727, -124.8486));
        POLYGON_WA.add(new LatLng(48.4984, -124.7539));
        POLYGON_WA.add(new LatLng(48.4096, -124.4174));
        POLYGON_WA.add(new LatLng(48.3599, -124.2389));
        POLYGON_WA.add(new LatLng(48.2964, -124.0116));
        POLYGON_WA.add(new LatLng(48.2795, -123.9141));
        POLYGON_WA.add(new LatLng(48.2247, -123.5413));
        POLYGON_WA.add(new LatLng(48.2539, -123.3998));
        POLYGON_WA.add(new LatLng(48.2841, -123.2501));
        POLYGON_WA.add(new LatLng(48.4233, -123.1169));
        POLYGON_WA.add(new LatLng(48.4533, -123.1609));
        POLYGON_WA.add(new LatLng(48.5548, -123.2220));
        POLYGON_WA.add(new LatLng(48.5902, -123.2336));
        POLYGON_WA.add(new LatLng(48.6901, -123.2721));
        POLYGON_WA.add(new LatLng(48.7675, -123.0084));
        POLYGON_WA.add(new LatLng(48.8313, -123.0084));
        POLYGON_WA.add(new LatLng(49.0023, -123.3215));
    }
}
