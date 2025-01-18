package com.chuyue.usercenter.utils;

import java.util.List;
import java.util.Objects;

public class EditDistanceAlgorithm {

    /**
     * edit distance/ minimum difference between two tag lists
     * Could refer to Edit Distance on leetcode ~~
     * @param tagListOne
     * @param tagListTwo
     * @return the min distance making tagListOne to tagListTwo
     */
    public static int minDistanceBetweenTags(List<String> tagListOne, List<String> tagListTwo) {
        int tagListOneLength = tagListOne.size();
        int tagListTwoLength = tagListTwo.size();

        //If any of them are empty list
        if(tagListOneLength * tagListTwoLength == 0) {
            return tagListOneLength + tagListTwoLength;
        }

        int[][] dp = new int[tagListOneLength + 1][tagListTwoLength + 1];

        //Initialize the first column
        for (int i = 0; i < tagListOneLength + 1; i++) {
            dp[i][0] = i;
        }
        //Initialize the first row
        for (int i = 0; i < tagListTwoLength + 1; i++) {
            dp[0][i] = i;
        }

        for(int i = 1; i <= tagListOneLength; i++) {
            for(int j = 1; j <= tagListTwoLength; j++) {
                if(Objects.equals(tagListOne.get(i - 1), tagListTwo.get(j - 1))) {
                    dp[i][j] = dp[i-1][j-1];
                }else{
                    dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i][j-1], dp[i-1][j])) + 1;
                }
            }
        }
        return dp[tagListOneLength][tagListTwoLength];
    }
}
