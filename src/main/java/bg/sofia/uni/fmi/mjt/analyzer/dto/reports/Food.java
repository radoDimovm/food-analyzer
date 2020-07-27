package bg.sofia.uni.fmi.mjt.analyzer.dto.reports;

import java.util.Set;

public class Food {
    private String name;
    private Set<Nutrient> nutrients;

    public Food(){}

    @Override
    public String toString(){
        StringBuilder nutrientsString = new StringBuilder();
        nutrients.forEach(n -> nutrientsString.append(n.toString()));

        return String.format("PRODUCT: %s%n%n"+"Nutrient\t\t\t\t\t\t\t\tquantity per 100g product %n%s%n", name, nutrientsString.toString());
    }
}
