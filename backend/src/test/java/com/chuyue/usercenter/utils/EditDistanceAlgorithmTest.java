package com.chuyue.usercenter.utils;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
class EditDistanceAlgorithmTest {
    @Test
    void testCompareTags() {
        List<String> tagList1 = Arrays.asList("Java", "大一", "男");
        List<String> tagList2 = Arrays.asList("Java", "大一", "女");
        List<String> tagList3 = Arrays.asList("Python", "大二", "女");
        // 1
        int score1 = EditDistanceAlgorithm.minDistanceBetweenTags(tagList1, tagList2);
        // 3
        int score2 = EditDistanceAlgorithm.minDistanceBetweenTags(tagList1, tagList3);
        System.out.println(score1);
        System.out.println(score2);
    }
}