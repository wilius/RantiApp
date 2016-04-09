package org.alexiwilius.ranti_app.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AlexiWilius on 23.12.2014.
 */
public abstract class Location {
    android.location.Location location;

    protected Location(android.location.Location location) {
        this.location = location;
    }

    protected Location(LatLng position) {
        location = new android.location.Location("");
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);
    }

    private Location(Double lat, Double lng) {
        this(new LatLng(lat, lng));
    }

    public long getTime() {
        return location.getTime();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public double getSpeed() {
        return Math.floor(location.getSpeed() * 3.6);
    }

    public float getBearing() {
        return location.getBearing();
    }

    public LatLng getPosition() {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void setPosition(LatLng position) {
        location.setLatitude(position.latitude);
        location.setLongitude(position.longitude);
    }

    public float distance(Location b) {
        return distance(b.getPosition());
    }

    public float distance(LatLng b) {
        return distance(getPosition(), b);
    }

    public static float distance(LatLng a, LatLng b) {
        double lat_a = a.latitude, lat_b = b.latitude,
                lng_a = a.longitude, lng_b = b.longitude;

        double earthRadius = 3958.75,
                latDiff = Math.toRadians(lat_b - lat_a),
                lngDiff = Math.toRadians(lng_b - lng_a),
                tmp = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                        Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                                Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2),
                c = 2 * Math.atan2(Math.sqrt(tmp), Math.sqrt(1 - tmp)),
                distance = earthRadius * c;

        int meterConversion = 1609;

        return Math.abs(new Float(distance * meterConversion));
    }

    public static Location create(double lat, double lng) {
        return new Location(lat, lng) {
        };
    }
}
