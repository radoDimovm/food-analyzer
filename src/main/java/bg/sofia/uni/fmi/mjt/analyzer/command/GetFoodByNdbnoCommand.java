package bg.sofia.uni.fmi.mjt.analyzer.command;

import bg.sofia.uni.fmi.mjt.analyzer.FoodApiClient;
import bg.sofia.uni.fmi.mjt.analyzer.dto.reports.ReportResponse;

import java.util.Map;

public class GetFoodByNdbnoCommand implements Command {
    private static Map<String, ReportResponse> cacheNdbnoSearch;
    private String apiKey;

    public GetFoodByNdbnoCommand(Map<String, ReportResponse> cacheNdbnoSearch, String apiKey) {
        GetFoodByNdbnoCommand.cacheNdbnoSearch = cacheNdbnoSearch;
        this.apiKey = apiKey;
    }

    @Override
    public String execute(String... arguments) {
        String ndbno = arguments[0];
        String responseMessage;

        FoodApiClient foodApiClient = new FoodApiClient(apiKey);

        if (!cacheNdbnoSearch.containsKey(ndbno)) {
            ReportResponse response = foodApiClient.getFoodByNdbno(ndbno);

            if (response != null) {
                cacheNdbnoSearch.putIfAbsent(ndbno, response);
                responseMessage = cacheNdbnoSearch.get(ndbno).toString();
            } else {
                responseMessage = "It's not found a food with such a name into the food analyzer.";
            }
        } else {
            responseMessage = cacheNdbnoSearch.get(ndbno).toString();
        }

        return responseMessage;
    }
}
