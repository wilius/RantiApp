package org.alexiwilius.ranti_app.location;

/**
 * Created by AlexiWilius-WS on 11.1.2016.
 */
public class NoActiveLocationSupplier extends Exception {
    public NoActiveLocationSupplier(String s) {
        super(s);
    }
}
