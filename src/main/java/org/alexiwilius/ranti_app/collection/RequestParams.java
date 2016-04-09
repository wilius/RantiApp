package org.alexiwilius.ranti_app.collection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by AlexiWilius on 18.11.2014.
 */
public class RequestParams extends Hashtable<CharSequence, CharSequence> {

    public String toString() {
        Iterator<Entry<CharSequence, CharSequence>> iterator = entrySet().iterator();

        StringBuilder response = new StringBuilder();
        Entry<CharSequence, CharSequence> item;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (response.length() > 0)
                response.append('&');
            try {
                response.append(URLEncoder.encode(item.getKey().toString(), "utf-8"));
                response.append("=");
                response.append(URLEncoder.encode(item.getValue().toString(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }
}
