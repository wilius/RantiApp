package org.alexiwilius.ranti_app.network.request;

import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;

/**
 * Created by AlexiWilius on 7.10.2015.
 */
public class SOAPContent extends XMLContent {

    private final String mAction;

    public SOAPContent(Document document, String action) throws TransformerException {
        super(document);
        mAction = action;
    }

    @Override
    protected void setRequest(Request request) {
        super.setRequest(request);
        request.getConnection().setRequestProperty("SOAPAction", mAction);
    }
}
