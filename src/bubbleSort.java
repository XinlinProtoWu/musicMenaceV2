
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 吴欣霖
 */
public class bubbleSort {
    // Sort by Artist
    public static ArrayList<Music> bubbleArtist(ArrayList<Music> unsortedList) {
        return bubbleArtistRecursive(new ArrayList<>(unsortedList));
    }

    private static ArrayList<Music> bubbleArtistRecursive(ArrayList<Music> list) {
        boolean swapped = false;
        int n = list.size();
        // Loop through the length of the array (all of the unassessed elements)
        for (int j = 0; j < n - 1; j++) {
            // Compare each element to its adjacent element and switch if conditions met
            if (list.get(j).getArtist().compareTo(list.get(j + 1).getArtist()) > 0) {
                swapped = true;
                Music temp = list.get(j);
                list.set(j, list.get(j + 1));
                list.set(j + 1, temp);
            }
        }

        // Continue the recursion if any swaps were made
        if (swapped) {
            return bubbleArtistRecursive(list);
        }

        return list;
    }
}
