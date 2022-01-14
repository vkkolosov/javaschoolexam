package com.tsystems.javaschool.tasks.subsequence;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")

    public boolean find(List x, List y) {

        boolean result = true;

        if (y == null)
        throw new IllegalArgumentException();

        try {
            if (x.size() == 0) {
                return true;
            }

            //Integer
            if (x.get(0) instanceof Integer) {

                int[] components = x.stream().mapToInt(i -> (int) i).toArray();
                int[] set = y.stream().mapToInt(i -> (int) i).toArray();

                int k = 0;

                for (int i = 0; i < components.length; i++) {

                    int count = 0;

                    for (int j = k; j < set.length; j++) {
                        if (components[i] == set[j]) {
                            count++;
                            k = j + 1;
                            break;
                        }
                    }
                    if (count == 0)
                        return false;
                }
                return result;

                //String
            } else if (x.get(0) instanceof String) {

                String[] components = new String[x.size()];
                int index = 0;

                for (Object value : x) {
                    components[index] = (String) value;
                    index++;
                }

                String[] set = new String[y.size()];
                index = 0;

                for (Object value : y) {
                    set[index] = (String) value;
                    index++;
                }

                int k = 0;

                for (int i = 0; i < components.length; i++) {

                    int count = 0;

                    for (int j = k; j < set.length; j++) {
                        if (components[i] == set[j]) {
                            count++;
                            k = j + 1;
                            break;
                        }
                    }
                    if (count == 0) {
                        return false;
                    }
                }
                return result;

            } else {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) { throw new IllegalArgumentException();}
    }
}
