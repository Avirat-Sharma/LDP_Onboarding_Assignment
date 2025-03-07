package com.activity.fcall
package com.iontrading
public class BondPriceBean {
    public static int id=0;
    public String className="BondPriceBean";
    public String bondid;
    public String price;

    BondPriceBean(String bondid,String price){
        this.bondid=bondid;
        this.price=price;
    }
}