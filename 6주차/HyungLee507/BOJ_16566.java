package backjoon.B_type.study6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BOJ_16566 {

    static int[] pointer;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int[] arr = new int[M];
        pointer = new int[M];
        for (int i = 0; i < M; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            pointer[i] = i;
        }
        Arrays.sort(arr);
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < K; i++) {
            int num = Integer.parseInt(st.nextToken());
            int index = binarySearch(arr, num);
            index = find(index);
            union(index);
            sb.append(arr[index]).append('\n');
        }
        System.out.println(sb);
    }

    private static int find(int k) {
        if (pointer[k] == k) {
            return k;
        }
        return pointer[k] = find(pointer[k]);
    }

    private static int binarySearch(int[] arr, int num) {
        // 여기서 left right 는 인덱스를 나타냄
        int left = -1;
        int right = pointer.length;
        int mid = 0;
        while (left + 1 < right) {
            mid = (left + right) / 2;
            if (arr[pointer[mid]] <= num) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return left + 1;
    }

    private static void union(int x) {
        if (x == pointer.length - 1) {
            return;
        }
        int a = find(x);
        int b = find(x + 1);
        if (a > b) {
            pointer[b] = a;
        } else {
            pointer[a] = b;
        }
    }
}
