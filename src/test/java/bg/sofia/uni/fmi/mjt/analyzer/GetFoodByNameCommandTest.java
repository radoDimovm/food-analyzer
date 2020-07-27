package bg.sofia.uni.fmi.mjt.analyzer;

import bg.sofia.uni.fmi.mjt.analyzer.command.GetFoodByNameCommand;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.ListResponse;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFoodByNameCommandTest {

    private Map<String, ListResponse> cacheNameSearch = new ConcurrentHashMap<>();

    private GetFoodByNameCommand command = new GetFoodByNameCommand(cacheNameSearch, "");

    @Test
    public void testResponseMessageWhenFoodNotFound() {
        FoodApiClient foodApiClientMock = mock(FoodApiClient.class);
        ListResponse response = mock(ListResponse.class);

        when(foodApiClientMock.getFoodByName(any(String.class))).thenReturn(null);

        String actualResponseMessage = command.execute("");

        assertEquals("It's not found a food with such a name into the food analyzer.", actualResponseMessage);
    }

}
