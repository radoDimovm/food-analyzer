package bg.sofia.uni.fmi.mjt.analyzer.dto.reports;

public class Report {
    Food food;

    public Report(){}

    @Override
    public String toString(){
        return food.toString();
    }
}
