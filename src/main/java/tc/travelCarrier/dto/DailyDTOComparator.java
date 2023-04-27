package tc.travelCarrier.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DailyDTOComparator implements Comparator<DailyDTO> {

    @Override
    public int compare(DailyDTO o1, DailyDTO o2) {
        // dailyDate를 비교하여 오름차순으로 정렬
        String date1 = o1.getDailyDate().replaceAll("[^0-9]", "");
        String date2 = o2.getDailyDate().replaceAll("[^0-9]", "");
        int compareResult = date1.compareTo(date2);

        // dailyDate가 같으면 attachDailySort를 비교하여 오름차순으로 정렬
        if (compareResult == 0) {
            compareResult = o1.getAttachDailySort() - o2.getAttachDailySort();
        }

        return compareResult;
    }

}
