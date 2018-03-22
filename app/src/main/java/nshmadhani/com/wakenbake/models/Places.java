package nshmadhani.com.wakenbake.models;


import java.util.List;

public class Places {
    public String name;

    public Places() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addToMasterList(String name) {
        this.name = name;
    }

}
