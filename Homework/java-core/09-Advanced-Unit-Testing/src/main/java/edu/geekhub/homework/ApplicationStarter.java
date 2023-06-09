package edu.geekhub.homework;

import edu.geekhub.homework.analytics.AnalyticsService;
import edu.geekhub.homework.domain.LosesStatisticService;

public class ApplicationStarter {

    public static void main(String[] args) {
        var losesStatisticService = new LosesStatisticService();

        var losesStatistic = losesStatisticService.getById(9);
        losesStatisticService.deleteById(1);
        losesStatisticService.create(losesStatistic);

        var analyticsService = new AnalyticsService();

        print(analyticsService.totalCountOfLosesForStatistic(losesStatisticService.getById(1)));
        print(analyticsService.totalCountOfLosesForAllStatistics(losesStatisticService.getAll()));
        print(analyticsService.findStatisticWithMinLosesAmounts(losesStatisticService.getAll()));
        print(analyticsService.findStatisticWithMaxLosesAmounts(losesStatisticService.getAll()));

        losesStatisticService.deleteAll();
    }

    static void print(Object objectToPrint) {
        System.out.println(objectToPrint);
    }
}
