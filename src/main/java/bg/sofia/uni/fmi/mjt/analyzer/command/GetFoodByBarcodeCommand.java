package bg.sofia.uni.fmi.mjt.analyzer.command;

import bg.sofia.uni.fmi.mjt.analyzer.ImageHandler;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.Item;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.ListResponse;

import java.util.Map;
import java.util.Optional;

public class GetFoodByBarcodeCommand implements Command {
    private static Map<String, ListResponse> cacheNameSearch;

    public GetFoodByBarcodeCommand(Map<String, ListResponse> cacheNameSearch) {
        GetFoodByBarcodeCommand.cacheNameSearch = cacheNameSearch;
    }

    @Override
    public String execute(String... arguments) {
        String responseMessage = null;

        String[] tokensUPC = arguments[0].split("=");
        String commandArgument = tokensUPC[0];
        String UPC;

        if (commandArgument.equals("--upc")) {
            UPC = tokensUPC[1];
        }
        else {
            ImageHandler imageHandler = new ImageHandler();
            UPC = imageHandler.receiveUPCCode(tokensUPC[1]);
        }

        final String UPCFinal = UPC;

        boolean thereIsSuchaUPC = false;
        for (ListResponse listResponse : cacheNameSearch.values()) {
            Optional<Item> optional = listResponse.getList()
                    .getItems()
                    .stream()
                    .filter(e -> UPCFinal.equals(e.getUPC()))
                    .findFirst();

            if (optional.isPresent()) {
                responseMessage = optional.get().toString();
                thereIsSuchaUPC = true;
                break;
            }
        }

        if (!thereIsSuchaUPC) {
            responseMessage = "It's not found a food with such an UPC into the food analyzer.";
        }

        return responseMessage;
    }
}
