package bg.sofia.uni.fmi.mjt.analyzer.command;

import bg.sofia.uni.fmi.mjt.analyzer.FoodApiClient;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.ListResponse;

import java.util.Map;

public class GetFoodByNameCommand implements Command {
    private static Map<String, ListResponse> cacheNameSearch;
    private String apiKey;

    public GetFoodByNameCommand(Map<String, ListResponse> cacheNameSearch, String apiKey) {
        GetFoodByNameCommand.cacheNameSearch = cacheNameSearch;
        this.apiKey = apiKey;
    }

    @Override
    public String execute(String... arguments) {
        String qName = formQName(arguments);
        String responseMessage;

        if (!cacheNameSearch.containsKey(qName)) {
            FoodApiClient foodApiClient = new FoodApiClient(apiKey);

            ListResponse response = foodApiClient.getFoodByName(qName);

            if (response != null) {
                cacheNameSearch.putIfAbsent(qName, response);
                responseMessage = response.toString();
            } else {
                responseMessage = "It's not found a food with such a name into the food analyzer.";
            }
        } else {
            responseMessage = cacheNameSearch.get(qName).toString();
        }

        return responseMessage;
    }

    private String formQName(String[] arguments) {
        StringBuilder sb = new StringBuilder();
        for (String arg: arguments) {
            sb.append(arg).append("%20");
        }

        return sb.toString().substring(0, sb.length() - 3);
    }
}
