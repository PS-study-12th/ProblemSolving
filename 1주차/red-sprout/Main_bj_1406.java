import java.io.*;
import java.util.*;

public class Main {
    static Deque<Character> left, right;
    static void init(String str) {
        left = new ArrayDeque<>();
        right = new ArrayDeque<>();
        for(int i = 0; i < str.length(); i++) {
            left.offerLast(str.charAt(i));
        }
    }
    static void left() {
        if(left.isEmpty()) return;
        right.offerFirst(left.pollLast());
    }
    static void right() {
        if(right.isEmpty()) return;
        left.offerLast(right.pollFirst());
    }
    static void deleteLeft() {
        if(left.isEmpty()) return;
        left.pollLast();
    }
    static void addLeft(char c) {
        left.offerLast(c);
    }
    static String getStr() {
        StringBuilder sb = new StringBuilder();
        while(!left.isEmpty()) {
            sb.append(left.pollFirst());
        }
        while(!right.isEmpty()) {
            sb.append(right.pollFirst());
        }
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;
        String str = br.readLine();
        int M = Integer.parseInt(br.readLine());
        init(str);
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            char q = st.nextToken().charAt(0);
            switch (q) {
                case 'L': left(); break;
                case 'D': right(); break;
                case 'B': deleteLeft(); break;
                case 'P': addLeft(st.nextToken().charAt(0)); break;
            }
        }
        System.out.println(getStr());
        br.close();
    }
}
