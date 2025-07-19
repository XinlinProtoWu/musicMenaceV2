
import java.util.ArrayList;

public class quickSort {
    //Sort by title
    //This method is used to get the first and last index of the unsorted array initially
    public static ArrayList<Music> quickSortTitle(ArrayList<Music> unsortedList) {
        if (unsortedList == null || unsortedList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return quickSortTitle(new ArrayList<>(unsortedList), 0, unsortedList.size() - 1);
    }
    //overload the previous method
    private static ArrayList<Music> quickSortTitle(ArrayList<Music> list, int start, int end) {
        //to make sure the start index is not greater than the end
        if (start < end) {
            //to get the partition index
            int partitionIndex = partitionTitle(list, start, end);
            //quicksort left and right sides
            quickSortTitle(list, start, partitionIndex - 1);
            quickSortTitle(list, partitionIndex + 1, end);
        }
        return list;
    }
    //split
    private static int partitionTitle(ArrayList<Music> list, int start, int end) {
        Music pivot = list.get(end);
        int i = start - 1;

        for (int j = start; j < end; j++) {
            //to check if greater or lesser than the compared object
            if (list.get(j).getTitle().toLowerCase().compareTo(pivot.getTitle().toLowerCase()) < 0) {
                i++;
                swap(list, i, j);
            }
        }
        //swap
        swap(list, i + 1, end);
        //return partition index
        return i + 1;
    }
    //swap
    private static void swap(ArrayList<Music> list, int i, int j) {
        Music temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    //duration
        public static ArrayList<Music> quickSortDuration(ArrayList<Music> unsortedList) {
        if (unsortedList == null || unsortedList.isEmpty()) {
            return new ArrayList<>();
        }

        return quickSortDuration(new ArrayList<>(unsortedList), 0, unsortedList.size() - 1);
    }

    private static ArrayList<Music> quickSortDuration(ArrayList<Music> list, int start, int end) {
        if (start < end) {
            int partitionIndex = partitionDuration(list, start, end);
            quickSortDuration(list, start, partitionIndex - 1);
            quickSortDuration(list, partitionIndex + 1, end);
        }
        return list;
    }

    private static int partitionDuration(ArrayList<Music> list, int start, int end) {
        Music pivot = list.get(end);
        int i = start - 1;

        for (int j = start; j < end; j++) {
            if (list.get(j).getDuration() < pivot.getDuration()) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, end);
        return i + 1;
    }
}