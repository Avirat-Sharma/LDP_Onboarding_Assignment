package com.activity.xrs;

import java.util.Hashtable;

import com.iontrading.xrs.api.IContext;
import com.iontrading.xrs.api.IXRSObject;

/**
 * This object represents the data managed by the context.
 * <p>
 * An IXRSObject is just a map: field name vs field value. The implementation details are therefore completely up to the
 * developer.
 * <p>
 * The SnapshotModule and RealTimeModule can determine the set of the required values in a IXRSObject based on the user
 * request, thus an IXRSObject can be also "partially filled".
 * <p>
 */

public class MyObject implements IXRSObject {

    private IContext ctx;
    private String id = "";
    private Hashtable<String, Comparable<?>> fields = new Hashtable<String, Comparable<?>>();

    public MyObject(String id, IContext context) throws NullPointerException {
        if (id == null || id.isEmpty()) {
            throw new NullPointerException("Empty value for MyObject Id is not allowed");
        } else {
            this.id = id;
        }
        ctx = context;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public synchronized Object getFieldValue(String fieldName) {
        return fields.containsKey(fieldName) ? fields.get(fieldName) : null;
    }

    @Override
    public boolean isFieldSet(String fieldName) {
        return fields.containsKey(fieldName);
    }

    private synchronized void set(String fieldName, Comparable<?> value) {
        fields.put(fieldName, value);
    }

    public void setFieldValue(String fieldName, Integer value) {
        set(fieldName, value);
    }

    public void setFieldValue(String fieldName, Double value) {
        set(fieldName, value);
    }

    public void setFieldValue(String fieldName, String value) {
        set(fieldName, value);
    }

    @Override
    public MyObject clone() {
        MyObject o = new MyObject(getId(), ctx);
        for (String k : fields.keySet()) {
            o.fields.put(k, fields.get(k));
        }
        return o;
    }
}


