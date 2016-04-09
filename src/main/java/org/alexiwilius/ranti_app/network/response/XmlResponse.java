package org.alexiwilius.ranti_app.network.response;

import org.alexiwilius.ranti_app.network.request.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by AlexiWilius on 6.10.2015.
 */
public class XmlResponse extends Response {

    public XmlResponse(Request request, HttpURLConnection conn) throws IOException {
        super(request, conn, conn.getInputStream());
    }

    @Override
    public Object getData() {
        try {
            return DocumentBuilderFactory
                    .newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(toString().getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
