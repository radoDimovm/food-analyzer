package bg.sofia.uni.fmi.mjt.analyzer.dto.reports;

public class Nutrient {
    private String name;
    private String unit;
    private String value;
//    private String group;

    public Nutrient(){};

    @Override
    public String toString(){
//        return String.format("Nutrient: %s%n"+"quantity per 100g product: %s%s%n", name, value, unit);
        return String.format("%-40s%s%s%n", name, value, unit);

    }
}
