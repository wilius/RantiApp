package org.alexiwilius.ranti_app.network.request;

import org.w3c.dom.Document;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by AlexiWilius on 6.10.2015.
 */
public class XMLContent extends Content {

    private Document mDocument;
    private String mTextContent;

    public XMLContent(Document document) throws TransformerException {
        setDocument(document);
    }

    @Override
    public String getContent() {
        return mTextContent;
    }

    @Override
    public Integer getContentLength() {
        return mTextContent.length();
    }

    @Override
    public String getContentType() {
        return "text/xml";
    }

    public void setDocument(Document document) throws TransformerException {
        mDocument = document;
        mTextContent = getStringFromDocument(document);
    }

    private String getStringFromDocument(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

    public Document getDocument() {
        return mDocument;
    }
}
