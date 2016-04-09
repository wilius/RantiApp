package org.alexiwilius.ranti_app.location;

/**
 * Created by AlexiWilius on 17.11.2014.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.alexiwilius.ranti_app.util.UIThread;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LocationDetector implements LocationListener {

    private volatile LocationManager locationManager;
    private volatile Hashtable<LocationResult, LocationResult> listeners;

    private volatile Timer gpsTimer, networkTimer;

    private volatile boolean gpsEnabled = false;
    private volatile boolean networkEnabled = false;
    private volatile boolean isStarted = false;

    private final int TIMER_TIMEOUT = 15000;

    private static LocationDetector INSTANCE;

    /**
     * @return LocationDetector
     */
    private LocationDetector() {
        listeners = new Hashtable<LocationResult, LocationResult>();
    }

    /**
     * @return LocationDetector
     */
    private static LocationDetector getInstance() {
        // I use LocationResult callback class to pass location value from MyLocation to user code.
        if (INSTANCE == null) {
            synchronized (LocationDetector.class) {
                //double checking Singleton instance
                if (INSTANCE == null) {
                    INSTANCE = new LocationDetector();
                }
            }
        }
        return INSTANCE;
    }

    public static void registerListener(@NonNull LocationResult listener) throws NoLocationSupplierException, NoActiveLocationSupplier {
        LocationDetector detector = getInstance();
        detector.listeners.put(listener, listener);
        detector.restoreStatus();
    }

    public static void removeListener(@NonNull LocationResult listener) {
        LocationDetector detector = getInstance();
        detector.listeners.remove(listener);
        detector.stop();
    }

    /**
     *
     */
    private synchronized void restoreStatus() throws NoActiveLocationSupplier, NoLocationSupplierException {
        if (listeners.size() > 0) {
            if (!isStarted)
                start();
        } else if (isStarted)
            stop();
    }

    /**
     * Changes location manager of the detector
     *
     * @param locationManager
     */
    public static void setLocationManager(LocationManager locationManager) throws NoLocationSupplierException, NoActiveLocationSupplier {
        LocationDetector locationDetector = getInstance();
        locationDetector.locationManager = locationManager;
        locationDetector.updateProviders();
        locationDetector.restoreStatus();
    }

    /**
     * try to get current location provided gps or network.
     * Firstly try to get gps location for 40 sec.
     * If cannot get location then try to get last know location provided by gps or network
     *
     * @return Boolean
     */
    private synchronized void start() throws NoLocationSupplierException, NoActiveLocationSupplier {
        if (locationManager == null) return;
        List<String> providers = locationManager.getAllProviders();
        boolean haveSupplier = false;
        LocationManager locationManager = getLocationManager();

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            haveSupplier = true;
        }

        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    LocationDetector.this.onLocationChanged(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                    updateProviders();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    updateProviders();
                }
            });
            haveSupplier = true;
        }

        if (!haveSupplier)
            throw new NoLocationSupplierException("Cannot found any location supplier");

        //don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled) {
            onLocationChanged(null);
            throw new NoActiveLocationSupplier("No active location supplier found");
        }

        starTimer();
    }

    /**
     * stops the location updates
     */
    private synchronized void stop() {
        getLocationManager().removeUpdates(this);
        cancelTimer();
        isStarted = false;
    }

    /**
     * starts a timer for gps timeout.
     */
    private void starTimer() {
        if (!gpsEnabled) return;

        gpsTimer = new Timer();
        gpsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                android.location.Location networkLocation = null,
                        gpsLocation = null;

                if (networkEnabled)
                    networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (gpsEnabled)
                    gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (gpsLocation != null && networkLocation != null) {
                    if (gpsLocation.getTime() > networkLocation.getTime())
                        LocationDetector.this.onLocationChanged(gpsLocation);
                    else
                        LocationDetector.this.onLocationChanged(networkLocation);
                } else {
                    if (gpsLocation != null) {
                        LocationDetector.this.onLocationChanged(gpsLocation);
                    } else if (networkLocation != null) {
                        LocationDetector.this.onLocationChanged(networkLocation);
                    } else {
                        LocationDetector.this.onLocationChanged(null);
                    }
                }
            }
        }, TIMER_TIMEOUT);
    }

    /**
     * cancels scheduled task
     */
    private void cancelTimer() {
        clearTimer(gpsTimer);
        clearTimer(networkTimer);
    }

    /**
     * @param timer
     */
    private void clearTimer(Timer timer) {
        if (timer == null) return;

        timer.cancel();
        timer.purge();
    }

    /**
     * when a new location handled, notifies this location to all listeners
     *
     * @param location
     */
    private void notifyLocation(Location location) {
        Enumeration<LocationResult> elements = listeners.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().gotLocation(location);
        }
    }

    private void notifyLocationDisabled() {
        Enumeration<LocationResult> elements = listeners.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().locationDisabled();
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        cancelTimer();
        if (location != null) {
            switch (location.getProvider()) {
                case LocationManager.NETWORK_PROVIDER:
                    notifyLocation(new NetworkLocation(location));
                    break;
                case LocationManager.GPS_PROVIDER:
                    notifyLocation(new GPSLocation(location));
                    break;
            }
        }
        starTimer();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        updateProviders();
    }

    @Override
    public void onProviderDisabled(String provider) {
        updateProviders();
        stop();
    }

    /**
     * @return LocationManager
     * @throws java.lang.NullPointerException if there is no location manager then throws a exception
     */
    public LocationManager getLocationManager() {
        if (locationManager == null)
            throw new NullPointerException("Location Manager couldn't specified");

        return locationManager;
    }


    /**
     * initializes providers according to locationManager changes
     */
    private void updateProviders() {
        LocationManager locationManager = getLocationManager();
        //exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gpsEnabled && !networkEnabled)
            notifyLocationDisabled();
    }


    public interface LocationResult {
        void gotLocation(Location location);

        void locationDisabled();
    }
}


