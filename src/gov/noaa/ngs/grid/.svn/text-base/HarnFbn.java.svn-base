/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.noaa.ngs.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * A bean that provides mapping of HARN,FBN, and NAD83 realizations by state
 * @author Krishna.Tadepalli
 */
public class HarnFbn {
    public static final String[] states = {"Alabama","Arizona","Arkansas",
        "California","Colorado","Connecticut","Delaware","District of Columbia",
        "Florida","Georgia","Idaho","Illinois","Indiana",
        "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
        "Michigan","Minnesota","Mississippi","Missouri","Montana",
        "Nebraska","Nevada","New Hampshire","New Jersey",
        "New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma",
        "Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota",
        "Tennessee","Texas","Utah","Vermont","Virginia",
        "Washington","West Virginia","Wisconsin","Wyoming"
        
    };
    public static final String[] harns = {"NAD 83(1992)","NAD 83(1992)","NAD 83(1997)",
        "NAD 83(1992)","NAD 83(1992)","NAD 83(1992)","NAD 83(1991)","NAD 83(1991)",
        "NAD 83(1990)","NAD 83(1994)","NAD 83(1992)","NAD 83(1997)","NAD 83(1997)",
        "NAD 83(1996)","NAD 83(1997)","NAD 83(1993)","NAD 83(1992)","NAD 83(1992)","NAD 83(1991)","NAD 83(1992)",
        "NAD 83(1994)","NAD 83(1996)","NAD 83(1993)","NAD 83(1997)","NAD 83(1992)",
        "NAD 83(1995)","NAD 83(1994)","NAD 83(1992)","NAD 83(1992)",
        "NAD 83(1992)","NAD 83(1992)","NAD 83(1995)","NAD 83(1996)","NAD 83(1995)","NAD 83(1993)",
        "NAD 83(1991)","NAD 83(1992)","NAD 83(1992)","NAD 83(1995)","NAD 83(1996)",
        "NAD 83(1990)","NAD 83(1993)","NAD 83(1994)","NAD 83(1992)","NAD 83(1993)",
        "NAD 83(1991)","NAD 83(1995)","NAD 83(1991)","NAD 83(1993)"
        
    };
    public static final String[] fbns = {"","","",
        "NAD 83(1998)","","NAD 83(1996)","","",
        "NAD 83(1999)","","NAD 83(1999)","","",
        "","","","","NAD 83(1996)","","NAD 83(1996)",
        "","","","","NAD 83(1999)",
        "","NAD 83(1999)","NAD 83(1996)","NAD 83(1996)",
        "","NAD 83(1996)","NAD 83(2001)","","","",
        "NAD 83(1998)","","NAD 83(1996)","NAD 83(2001)","",
        "NAD 83(1995)","","","NAD 83(1996)","",
        "NAD 83(1998)","","NAD 83(1997)",""
    };
    private String state;
    private String harn;
    private String fbn;
    private List<HarnFbn> hfList;
    public HarnFbn(){
        
    }
    public HarnFbn(String state,String harn, String fbn){
        this.state = state;
        this.harn = harn;
        this.fbn = fbn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getFbn() {
        return fbn;
    }

    public void setFbn(String fbn) {
        this.fbn = fbn;
    }
       public String getHarn() {
        return harn;
    }

    public void setHarn(String harn) {
        this.harn = harn;
    }
    public List<HarnFbn> getHF(){
        hfList = new ArrayList<>();
        for (int i=0;i<states.length;i++){
            hfList.add(new HarnFbn(states[i],harns[i],fbns[i]));
        }
        return hfList;
    } 
    public static void main(String[] args){
        HarnFbn hf = new HarnFbn();
        List<HarnFbn> list = hf.getHF();
        for (int i=0; i<list.size();i++){
            HarnFbn h = list.get(i);
            System.out.println(h.getState()+"|"+h.getHarn()+"|"+h.getFbn());
        }
//        for (int i=0;i<states.length;i++){
//            System.out.println(states[i]+"|"+harns[i]+"|"+fbns[i]);
//        }
    }
    
}
