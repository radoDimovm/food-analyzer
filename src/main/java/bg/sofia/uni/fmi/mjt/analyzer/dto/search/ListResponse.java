package bg.sofia.uni.fmi.mjt.analyzer.dto.search;

public class ListResponse {
    private ListOfItemsDetails list;

    public ListOfItemsDetails getList() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
