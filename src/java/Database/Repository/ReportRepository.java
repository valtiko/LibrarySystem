package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Person;
import models.entities.Report;

import java.util.ArrayList;
import java.util.List;


public class ReportRepository {
    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance();
    private static ReportRepository instance;

    public static ReportRepository getInstance() {
        if (instance == null) {
            instance = new ReportRepository();
        }
        return instance;
    }

    public ReportRepository() {
        this.databaseAccess = DatabaseAccess.getInstance();
    }

    public List<Report> getAllReports() {
        String query = String.format("from Report");
        List<Report> reportsList = reportList(databaseAccess.getQuery(query));
        return reportsList;
    }

    private List<Report> reportList(List<Object> objects) {
        List<Report> reports = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Report) {
                reports.add((Report) o);
            }
        }
        return reports;
    }

    public void createReport(Report report) {
        databaseAccess.save(report);
    }

    public void deleteReport(Report report) {
        databaseAccess.delete(report);
    }
}
