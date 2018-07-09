package com.qsj.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 利用动态规划求最短路径问题
 */
public class CalMinDistance {
    /**
     * 计算最短的距离
     */
    public static int[] calMinDistance(int[][] distances) {
        int[] dist = new int[distances.length];
        dist[0] = 0;
        for (int i = 1; i < distances.length; i++) {
            int k = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                if (distances[j][i] != 0) {
                    if ((dist[j] + distances[j][i]) < k) {
                        k = dist[j] + distances[j][i];
                    }
                }
            }
            dist[i] = k;
        }
        return dist;
    }

    /**
     * 计算路径
     */

    public static String calTheRoute(int[][] distances, int[] dist) {
        StringBuffer buf = new StringBuffer();
        Stack<Integer> st = new Stack<>();
        int j = distances.length - 1;
        st.add(j);
        while (j > 0) {
            for (int i = 0; i < j; i++) {
                if (distances[i][j] != 0) {
                    if (dist[j] - distances[i][j] == dist[i]) {
                        st.add(i);
                    }
                }
            }
            j = st.peek();
        }
        while (!st.empty()) {
            buf.append(st.pop()).append("-->");
        }
        return buf.toString();
    }

    /**
     * 从文件读取路径图
     */
    public static int[][] readTheFile(File file) {
        Reader input = null;
        try {
            input = new FileReader(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(input);
        List<String> list = new ArrayList<>();
        try {
            String str = bufferedReader.readLine();
            while (str != null) {
                list.add(str);
                str = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] distances = new int[11][11];
        for (String str : list) {
            String[] str1 = str.split(",");
            int i = Integer.parseInt(str1[0]);
            int j = Integer.parseInt(str1[1]);
            distances[i - 1][j - 1] = Integer.parseInt(str1[2]);
        }
        return distances;
    }

    public static void main(String[] args) {
        File f = new File("D:" + File.separator + "distance_1.csv");
		int distance[][] = readTheFile(f);
		int dist[] = calMinDistance(distance);
		System.out.println("最短路径长度为：" + dist[distance.length - 1]);
		System.out.println("最短路径为：" + calTheRoute(distance, dist));
    }
}