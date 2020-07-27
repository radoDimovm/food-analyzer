package bg.sofia.uni.fmi.mjt.analyzer.dto.search;

public class Item {
    private static final String GROUP_WITH_MANU_AND_UPC = "Branded Food Products Database";

    private String group;
    private String name;
    private String ndbno;
    private String manu;
    private String UPC;

    public void setUPC() {
        String[] tokens = name.split(", ");
        this.UPC = tokens[tokens.length-1].substring(5);
    }

    public String getUPC() {
        return UPC;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString(){
        if (group.equals(GROUP_WITH_MANU_AND_UPC)) {
            return String.format("Product name: %s%n"+"ndbno: %s%n"+"group: %s%n"+"manu: %s%n%n", name, ndbno, group, manu);
        }

        return String.format("Product name: %s%n"+"ndbno: %s%n"+"group: %s%n%n", name, ndbno, group);
    }
}
