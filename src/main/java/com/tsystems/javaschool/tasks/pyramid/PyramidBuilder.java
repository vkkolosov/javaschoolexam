package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here

        //размер пирамиды
        //1 строка - 1 цифра 1
        //2 строки - 3 цифры 1 + 2
        //3 строки - 6 цифр 3 + 3
        //4 строки - 10 цифр 6 + 4
        //5 строк - 15 цифр 10 + 5

        //а1 = n
        //а2 = а1 + n*a1
        //a3 = a2 + n*a1
        //...
        //an = 0.5*n*(n+1)
        //

        try {
            int[] input = inputNumbers.stream().mapToInt(i -> i).toArray();

            Arrays.sort(input);

            int count = input.length;

            //высота пирамиды
            double n = (-1 + Math.sqrt(1 + 8 * count)) / 2;

            if (n * 10000 != ((int) n) * 10000)
                throw new CannotBuildPyramidException();

            //ширина пирамиды
            double w = 2 * n - 1;

            int[][] result = new int[(int) n][(int) w];

            int leftcorner = 0;
            int rightcorner = 0;

            for (int j = (int) n - 1; j > -1; j--) {

                for (int i = (int) w - rightcorner - 1; i > leftcorner - 1; i = i - 2) {
                    result[j][i] = input[count - 1];
                    count--;
                }

                leftcorner++;
                rightcorner++;
            }

            return result;

        } catch (NullPointerException | OutOfMemoryError e) {
            throw new CannotBuildPyramidException();
        }
    }
}


