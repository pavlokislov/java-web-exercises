import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        int[] arr2 = new int[]{1, 2, 3, 4};
        System.out.println(binarySearch(arr2, 5));


        System.out.println(Arrays.binarySearch(arr1, 2) == binarySearch(arr1, 2));
        System.out.println(Arrays.binarySearch(arr1, 1) == binarySearch(arr1, 1));

        System.out.println(Arrays.binarySearch(arr1, 5) == binarySearch(arr1, 5));
        System.out.println(Arrays.binarySearch(arr2, 2) == binarySearch(arr2, 2));
        System.out.println(Arrays.binarySearch(arr2, 1) == binarySearch(arr2, 1));

        System.out.println(-1 == binarySearch(arr2, 5));


    }


    private static int binarySearch(int[] arr, int el) {
        if (arr == null) {
            return -1;
        }

        int starPosition = 0;
        int endPosition = arr.length - 1;

        while (starPosition <= endPosition) {
            int middlePosition = (starPosition + endPosition) / 2;

            if (arr[middlePosition] == el) {
                return middlePosition;
            } else if (arr[middlePosition] < el) {
                starPosition++;
            } else if (arr[middlePosition] > el) {
                endPosition--;
            }

        }
        return -1;
    }


}
