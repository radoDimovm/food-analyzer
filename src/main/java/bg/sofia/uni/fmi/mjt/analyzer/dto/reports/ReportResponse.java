package bg.sofia.uni.fmi.mjt.analyzer.dto.reports;

import bg.sofia.uni.fmi.mjt.analyzer.dto.reports.Report;

public class ReportResponse {
    private Report report;

    public ReportResponse(){}

    @Override
    public String toString(){
        return report.toString();
    }
}
