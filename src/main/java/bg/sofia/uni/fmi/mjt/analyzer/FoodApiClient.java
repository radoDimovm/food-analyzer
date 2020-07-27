package bg.sofia.uni.fmi.mjt.analyzer;

import bg.sofia.uni.fmi.mjt.analyzer.dto.reports.ReportResponse;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.Item;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.ListResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FoodApiClient {
    private final String API_URL = "https://api.nal.usda.gov";
    private final String apiKey;

    public FoodApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public ListResponse getFoodByName(String qName) {
        String URL = API_URL + "/fdc/v1/search?generalSearchInput=" + qName + "&api_key=" + apiKey;
        ListResponse response = getJsonBodyInProperClass(URL, ListResponse.class);

        if (response != null) {
            //The point of this loop is to check if any item is with group "Branded Food Products Database"
            //Only items with group "Branded Food Products Database" have UPC field, which has to be set.
            //Other items don't have UPC.
            for (Item item : response.getList().getItems()) {
                System.out.println(item.getGroup());
                if (item.getGroup().equals("Branded Food Products Database")) {
                    item.setUPC();
                }
            }
        }

        return response;
    }

    public ReportResponse getFoodByNdbno(String ndbno) {
        String URL = API_URL + "/ndb/reports/?ndbno=" + ndbno + "&api_key=" + apiKey;

        return getJsonBodyInProperClass(URL, ReportResponse.class);
    }

    private <T> T getJsonBodyInProperClass(String URL, Class<T> classOfT) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        try {
            HttpResponse<String> jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (jsonResponse.statusCode() == 200) {
                Gson gson = new Gson();
                return gson.fromJson(jsonResponse.body(), classOfT);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
