package com.activity.bondprice;

public class BondPriceBean {
    public static int id=0;
    private final String className="BondPriceBean";
    public String bondid;
    public String price;

    BondPriceBean(String bondid,String price){
        this.bondid=bondid;
        this.price=price;
    }

    public String getClassName(){
        return className;
    }
}