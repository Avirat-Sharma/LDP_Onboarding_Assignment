package com.activity.xrs;

import java.util.ArrayList;

import com.iontrading.mkv.enums.MkvFieldType;
import com.iontrading.xrs.api.AbstractXMLStructureModule;
import com.iontrading.xrs.api.DefaultXRSField;
import com.iontrading.xrs.api.IContext;
import com.iontrading.xrs.api.IModuleServiceLocator;
import com.iontrading.xrs.api.IStructureModule;
import com.iontrading.xrs.api.IXRSField;
import com.iontrading.xrs.api.ModuleStatus;
import com.iontrading.xrs.api.XRSStatus;

public class MyStructureModule implements IStructureModule {

    private ArrayList<IXRSField> fields = new ArrayList<IXRSField>();

    @Override
    public void init(IContext context, IModuleServiceLocator locator) {
        fields = new ArrayList<IXRSField>();

        /*
         * We rely on DefaultXRSField defauls and specify only the properties that we are really interested in.
         *
         * In this case we only need to specify the field name and its type.
         */
        DefaultXRSField fld = null;
        // Name
        fld = new DefaultXRSField("Key");
        fld.setType(MkvFieldType.STR);
        fields.add(fld);
        // Name
        fld = new DefaultXRSField("Value");
        fld.setType(MkvFieldType.STR);
        fields.add(fld);
//        // Surname
//        fld = new DefaultXRSField("Value");
//        fld.setType(MkvFieldType.STR);
//        fields.add(fld);
//        // Address
//        fld = new DefaultXRSField("Address");
//        fld.setType(MkvFieldType.STR);
//        fields.add(fld);
//        // Phone
//        fld = new DefaultXRSField("Phone");
//        fld.setType(MkvFieldType.STR);
//        fields.add(fld);
//        // Age
//        fld = new DefaultXRSField("Age");
//        fld.setType(MkvFieldType.INT);
//        fields.add(fld);
//        // Experience
//        fld = new DefaultXRSField("Experience");
//        fld.setType(MkvFieldType.REAL);
//        fields.add(fld);
    }

    @Override
    public String getName() {
        return "The Structure Module";
    }

    @Override
    public ModuleStatus getModuleStatus() {
        /*
         * This sample StructureModule is static, so it's
         * always RUNNING. In a complex scenario the module
         * status could change, e.g. from STARTING to RUNNING.
         *
         * All the changes of the module status must be notified
         * by pushing the proper xRS-event, which leads the xRS
         * to call this method for each module and evaluate the
         * status of the whole context.
         */
        return new ModuleStatus(XRSStatus.RUNNING);
    }

    @Override
    public String getDetails() {
        return "...";
    }

    @Override
    public Iterable<IXRSField> getFieldsStructure() {
        return fields;
    }

    @Override
    public void shutDown() {
    }

}


