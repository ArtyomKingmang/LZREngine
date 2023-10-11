package com.kingmang.LZREngine.utils.tools;

import java.util.Random;

public class RandTool {
    public final static Random random = new Random();

    public static int weightedIndex(int[] weights){
        return RandTool.weightedIndex(random, weights);
    }
    public static int weightedIndex(Random random, int[] weights){
        int sum = 0;
        int i;
        for (i = 0; i < weights.length; i++) sum += weights[i];
        int selection = random.nextInt(sum);
        int count = 0;
        for (i = 0; i < weights.length - 1; i++)
        {
            count += weights[i];
            if (selection < count)
                return i;
        }
        return weights.length - 1;

    }
}
