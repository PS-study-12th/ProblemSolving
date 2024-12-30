package backjoon.B_type.study4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class BOJ_21944 {

    private static class Problem implements Comparable<Problem> {
        int idx;
        int tier;

        public Problem(int idx, int tier) {
            this.idx = idx;
            this.tier = tier;
        }

        @Override
        public int compareTo(Problem o) {
            if (o.tier == this.tier) {
                return Integer.compare(this.idx, o.idx);
            } else {
                return Integer.compare(this.tier, o.tier);
            }
        }

        @Override
        public String toString() {
            return "Problem{" +
                    "idx=" + idx +
                    ", tier=" + tier +
                    '}';
        }
    }

    static Map<Integer, TreeSet<Problem>> typeProblems;
    static Map<Integer, Problem> dict;
    static TreeSet<Problem> problems;
    static Map<Integer, Integer> typeMap;

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int N = Integer.parseInt(bf.readLine());
        problems = new TreeSet();
        typeMap = new HashMap<>();
        typeProblems = new HashMap<>();
        dict = new HashMap<>();
        // 입력
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(bf.readLine());
            addProblem(st);
        }
        // 명령어 수행
        int M = Integer.parseInt(bf.readLine());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(bf.readLine());
            executeCommand(st);
        }
        bf.close();
    }

    private static void executeCommand(StringTokenizer st) {
        String cmd = st.nextToken();
        if (cmd.equals("add")) {
            addProblem(st);
        }
        if (cmd.equals("recommend")) {
            recommend1(st);
        }
        if (cmd.equals("recommend2")) {
            recommend2(st);
        }
        if (cmd.equals("recommend3")) {
            recommend3(st);
        }
        if (cmd.equals("solved")) {
            deleteProblem(st);
        }
    }


    private static void addProblem(StringTokenizer st) {
        int idx = Integer.parseInt(st.nextToken());
        int tier = Integer.parseInt(st.nextToken());
        int type = Integer.parseInt(st.nextToken());
        Problem create = new Problem(idx, tier);
        if (dict.containsKey(idx)) {
            int prevType = typeMap.get(idx);
            Problem prev = dict.get(idx);
            TreeSet<Problem> problems1 = typeProblems.get(prevType);
            problems1.remove(prev);
            typeProblems.put(prevType, problems1);
            problems.remove(prev);
        }
        TreeSet<Problem> orDefault = typeProblems.getOrDefault(type, new TreeSet<>());
        dict.put(idx, create);
        problems.add(create);
        typeMap.put(idx, type);
        orDefault.add(create);
        typeProblems.put(type, orDefault);
    }


    // 알고리즘 유형 중 난이도 최고, 최저 문제 찾기
    private static void recommend1(StringTokenizer st) {
        int type = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        TreeSet<Problem> types = typeProblems.get(type);
        if (x == 1) {
            Problem last = types.last();
            System.out.println(last.idx);
        } else {
            Problem first = types.first();
            System.out.println(first.idx);
        }
    }

    // 알고리즘 유형 보지 않고 최고, 최저 문제 찾기.
    private static void recommend2(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        Problem result;
        if (x == 1) {
            result = problems.last();
        } else {
            result = problems.first();
        }
        System.out.println(result.idx);
    }

    private static void recommend3(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        int tierLimit = Integer.parseInt(st.nextToken());
        Problem result;
        if (x == 1) {
            result = problems.ceiling(new Problem(-1, tierLimit));
        } else {
            result = problems.floor(new Problem(-1, tierLimit));
        }
        if (result == null) {
            System.out.println(-1);
        } else {
            System.out.println(result.idx);
        }

    }

    private static void deleteProblem(StringTokenizer st) {
        int idx = Integer.parseInt(st.nextToken());
        int type = typeMap.get(idx);
        Problem problem = dict.get(idx);
        typeMap.remove(idx);
        TreeSet<Problem> problems1 = typeProblems.get(type);
        problems1.remove(problem);
        typeProblems.put(type, problems1);
        problems.remove(problem);
        dict.remove(idx);
    }
}
