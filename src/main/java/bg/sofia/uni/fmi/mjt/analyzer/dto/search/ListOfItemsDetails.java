package bg.sofia.uni.fmi.mjt.analyzer.dto.search;

import java.util.List;

public class ListOfItemsDetails {
    private String q;
    private List<Item> items;

    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        items.forEach(n -> itemsString.append(n.toString()));

        return itemsString.toString();
    }

    public String getQ() {
        return q;
    }

    public List<Item> getItems() {
        return items;
    }
}
