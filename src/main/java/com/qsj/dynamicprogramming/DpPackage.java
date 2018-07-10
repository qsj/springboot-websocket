package com.qsj.dynamicprogramming;

/**
 * 01背包问题，M件物品的体积不同，带来的价值不同，在给定最大体积下，找出能带来最大价值的组合
 * 状态转移方程：f(i,j)=max{f(i-1,j-wi)+vi,f(i-1,j)}
 */
public class DpPackage {
    /**
     * 物品的体积数组
     */
    int[] volumeArr = new int[] { 1, 3, 5, 7, 9, 11 };
    /**
     * 物品的价值数组
     */
    int[] valueArr = new int[] { 122, 4, 6, 10, 8, 12 };
    /**
     * 背包的最大体积
     */
    int packageMaxVolume = 24;

    public int getValue() {
        int goodsCount = volumeArr.length - 1;
        int[][] maxValue = new int[goodsCount + 1][packageMaxVolume + 1];
        for (int i = 1; i < goodsCount + 1; i++) {
            for (int j = 1; j < packageMaxVolume + 1; j++) {
                if (j >= volumeArr[i]) {
                    maxValue[i][j] = Math.max(maxValue[i - 1][j - volumeArr[i]] + valueArr[i], maxValue[i - 1][j]);
                } else {
                    maxValue[i][j] = maxValue[i - 1][j];
                }
            }
        }
        outputTwoArr(maxValue, goodsCount, packageMaxVolume);
        return maxValue[goodsCount][packageMaxVolume];
    }

    private void outputTwoArr(int[][] maxValue, int rowNum, int cloumnNum) {
        for (int i = 0; i <= rowNum; i++) {
            String row = "";
            for (int j = 0; j <= cloumnNum; j++) {
                row = row + maxValue[i][j] + ",";
            }
            System.out.println(row);
        }
    }

    public static void main(String[] args) {
        DpPackage dpPackage = new DpPackage();
        System.out.println(dpPackage.getValue());
    }
}