package com.activity.xrs;

import com.iontrading.mkv.enums.MkvFieldType;
import com.iontrading.xrs.api.*;

import java.util.ArrayList;

public class MyStructureModule implements IStructureModule {
    private ArrayList<IXRSField> fields = new ArrayList<IXRSField>();
    private final String schema = "ID(STR)";
    @Override
    public void init(IContext context, IModuleServiceLocator locator){
        fields = new ArrayList<IXRSField>();
        DefaultXRSField fld = new DefaultXRSField("Id");
        fld.setType(MkvFieldType.STR);
        fields.add(fld);
        System.out.println("Field Created");
    }

    @Override
    public void shutDown() {
        System.out.println("Shut down ? IDK what to put here except this sout statement");
    }

    @Override
    public String getName() {
        return "MyStructureModule";
    }

    @Override
    public Iterable<IXRSField> getFieldsStructure(){
        return fields;
    }

    @Override
    public ModuleStatus getModuleStatus(){
        return new ModuleStatus(XRSStatus.RUNNING);
    }

    @Override
    public String getDetails() {
        return schema;
    }
}
