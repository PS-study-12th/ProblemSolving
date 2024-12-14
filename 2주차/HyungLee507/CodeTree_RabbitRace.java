package backjoon.B_type.study2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class CodeTree_RabbitRace {
    static int N, M, P;
    static Map<Integer, Integer> rabbitDist = new HashMap<>(); // 고유번호 -> 점프 거리
    static Map<Integer, Integer> rabbitNum = new HashMap<>(); // 고유번호 -> 인덱스
    static PriorityQueue<int[]> rabbitList = new PriorityQueue<>((a, b) -> {
        if (a[0] != b[0]) {
            return Integer.compare(a[0], b[0]); // 점프 횟수
        }
        if (a[1] != b[1]) {
            return Integer.compare(a[1], b[1]); // 행+열 합
        }
        if (a[2] != b[2]) {
            return Integer.compare(a[2], b[2]); // 행
        }
        if (a[3] != b[3]) {
            return Integer.compare(a[3], b[3]); // 열
        }
        return Integer.compare(a[4], b[4]); // 고유번호
    });
    static int[] row;
    static int[] col;
    static int[] rabbitScore;

    public static void init(int n, int m, int p, int[] rabbitData) {
        N = n;
        M = m;
        P = p;
        rabbitDist.clear();
        rabbitNum.clear();
        rabbitList.clear();
        rabbitScore = new int[P];
        row = new int[2 * N - 2];
        col = new int[2 * M - 2];

        for (int i = 0; i < N; i++) {
            row[i] = i;
        }
        for (int i = N - 2, j = N; i >= 1; i--, j++) {
            row[j] = i;
        }
        for (int i = 0; i < M; i++) {
            col[i] = i;
        }
        for (int i = M - 2, j = M; i >= 1; i--, j++) {
            col[j] = i;
        }

        for (int i = 0; i < P; i++) {
            int num = rabbitData[i * 2];
            int dist = rabbitData[i * 2 + 1];
            rabbitDist.put(num, dist);
            rabbitNum.put(num, i);
            rabbitList.add(new int[]{0, 0, 0, 0, num}); // 점프 횟수, 합, 행, 열, 고유번호
        }
    }

    private static int positiveModulo(int value, int mod) {
        return ((value % mod) + mod) % mod;
    }

    public static void race(int K, int S) {
        Set<Integer> movedRabbits = new HashSet<>();

        for (int k = 0; k < K; k++) {
            int[] top = rabbitList.poll();
            int jumpCnt = top[0];
            int x = top[2];
            int y = top[3];
            int num = top[4];
            int jumpDist = rabbitDist.get(num);

            // 이동 가능한 위치를 계산
            int newX = row[positiveModulo(x + jumpDist, row.length)];
            int newY = y;
            int[] posUp = new int[]{-(newX + newY), -newX, -newY};

            newX = row[positiveModulo(x - jumpDist, row.length)];
            int[] posDown = new int[]{-(newX + newY), -newX, -newY};

            newX = x;
            newY = col[positiveModulo(y + jumpDist, col.length)];
            int[] posRight = new int[]{-(newX + newY), -newX, -newY};

            newY = col[positiveModulo(y - jumpDist, col.length)];
            int[] posLeft = new int[]{-(newX + newY), -newX, -newY};

            List<int[]> posList = Arrays.asList(posUp, posDown, posRight, posLeft);
            posList.sort(Comparator.comparingInt((int[] a) -> a[0])
                    .thenComparingInt(a -> a[1])
                    .thenComparingInt(a -> a[2]));

            int[] bestPos = posList.get(0);
            int bestX = -bestPos[1];
            int bestY = -bestPos[2];

            // 모든 토끼 점수 업데이트
            for (int i = 0; i < P; i++) {
                rabbitScore[i] += (bestX + bestY);
            }
            rabbitScore[rabbitNum.get(num)] -= (bestX + bestY);

            // 이동한 토끼 업데이트
            rabbitList.add(new int[]{jumpCnt + 1, -(bestX + bestY), bestX, bestY, num});
            movedRabbits.add(num);
        }

        // 이동한 토끼 중 점수 부여
        PriorityQueue<int[]> movedQueue = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(b[0], a[0]); // 점수
            }
            if (a[1] != b[1]) {
                return Integer.compare(b[1], a[1]); // 행
            }
            if (a[2] != b[2]) {
                return Integer.compare(b[2], a[2]); // 열
            }
            return Integer.compare(b[3], a[3]); // 고유번호
        });

        for (int num : movedRabbits) {
            int[] top = rabbitList.stream().filter(r -> r[4] == num).findFirst().orElse(null);
            if (top != null) {
                int x = top[2];
                int y = top[3];
                movedQueue.add(new int[]{x + y, x, y, num});
            }
        }

        int[] bestRabbit = movedQueue.poll();
        if (bestRabbit != null) {
            int bestNum = bestRabbit[3];
            rabbitScore[rabbitNum.get(bestNum)] += S;
        }
    }

    public static void changeDist(int num, int L) {
        rabbitDist.put(num, rabbitDist.get(num) * L);
    }

    public static void bestRabbit() {
        int maxScore = Arrays.stream(rabbitScore).max().orElse(0);
        System.out.println(maxScore);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int Q = sc.nextInt();

        for (int q = 0; q < Q; q++) {
            int order = sc.nextInt();
            if (order == 100) {
                int n = sc.nextInt();
                int m = sc.nextInt();
                int p = sc.nextInt();
                int[] rabbitData = new int[p * 2];
                for (int i = 0; i < p * 2; i++) {
                    rabbitData[i] = sc.nextInt();
                }
                init(n, m, p, rabbitData);
            } else if (order == 200) {
                int K = sc.nextInt();
                int S = sc.nextInt();
                race(K, S);
            } else if (order == 300) {
                int num = sc.nextInt();
                int L = sc.nextInt();
                changeDist(num, L);
            } else if (order == 400) {
                bestRabbit();
            }
        }

        sc.close();
    }
}
