import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static class Node {
        String var;
        int start, end;
        Node prev, next;
        Node(String var, int start, int end) {
            this.var = var;
            this.start = start;
            this.end = end;
        }
    }
    static Node start = new Node("start", 0, 0);
    static Node end = new Node("end", 100001, 100001);
    static HashMap<String, Node> map = new HashMap<>();
    static void init() {
        map.put("start", start);
        map.put("end", end);
        start.next = end;
        end.prev = start;
    }
    static void malloc(String var, int size) {
        for(Node node = start; node != end; node = node.next) {
            int s = node.end + 1;
            int e = node.next.start - 1;
            int length = e - s + 1;
            if(size <= length) {
                Node newNode = new Node(var, s, s + size - 1);
                Node next = node.next;
                node.next = newNode;
                newNode.prev = node;
                next.prev = newNode;
                newNode.next = next;
                map.put(var, newNode);
                return;
            }
        }
        map.put(var, new Node(var, 0, 0));
        return;
    }
    static void free(String var) {
        Node node = map.getOrDefault(var, start);
        if(node.prev == null || node.next == null) return;
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        node.prev = null;
        next.prev = prev;
        node.next = null;
        node.start = 0;
        node.end = 0;
    }
    static int print(String var) {
        Node node = map.getOrDefault(var, start);
        return node.start;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        // StringTokenizer st = null;
        init();
        N = Integer.parseInt(br.readLine());
        for(int i = 0; i < N; i++) {
            String query = br.readLine();
            if(query.contains("malloc")) {
                String var = query.substring(0,4);
                int size = Integer.parseInt(query.substring(12, query.length() - 2));
                malloc(var, size);
            } else if(query.contains("free")) {
                String var = query.substring(5, query.length() - 2);
                free(var);
            } else {
                String var = query.substring(6, query.length() - 2);
                sb.append(print(var)).append('\n');
            }
        }
        System.out.print(sb);
        br.close();
    }
}
