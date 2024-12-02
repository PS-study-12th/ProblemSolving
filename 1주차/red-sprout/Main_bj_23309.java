import java.io.*;
import java.util.*;

public class Main {
    static int N, M;

    static class Node {
        int id;
        Node prev, next;

        Node(int id) {
            this.id = id;
        }
    }

    static Node[] nodes;

    static Node getNode(int id) {
        if(nodes[id] == null) nodes[id] = new Node(id);
        return nodes[id];
    }

    static int buildNext(int i, int j) {
        Node prev = nodes[i];
        Node node = getNode(j);
        Node next = prev.next;
        prev.next = node;
        node.prev = prev;
        next.prev = node;
        node.next = next;
        return next.id;
    }

    static int buildPrev(int i, int j) {
        Node next = nodes[i];
        Node node = getNode(j);
        Node prev = next.prev;
        next.prev = node;
        node.next = next;
        prev.next = node;
        node.prev = prev;
        return prev.id;
    }

    static int closeNext(int i) {
        Node prev = nodes[i];
        Node node = prev.next;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        return node.id;
    }

    static int closePrev(int i) {
        Node next = nodes[i];
        Node node = next.prev;
        Node prev = node.prev;
        next.prev = prev;
        prev.next = next;
        return node.id;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;
        st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        nodes = new Node[1_000_001];
        st = new StringTokenizer(br.readLine(), " ");
        int id = Integer.parseInt(st.nextToken());
        Node root = new Node(id);
        Node node = root;
        nodes[id] = root;
        for (int i = 1; i < N; i++) {
            id = Integer.parseInt(st.nextToken());
            Node next = new Node(id);
            nodes[id] = next;
            node.next = next;
            next.prev = node;
            node = next;
        }
        node.next = root;
        root.prev = node;
        int i, j;
        StringBuilder sb = new StringBuilder();
        for (int q = 0; q < M; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            String query = st.nextToken();
            switch (query) {
                case "BN":
                    i = Integer.parseInt(st.nextToken());
                    j = Integer.parseInt(st.nextToken());
                    sb.append(buildNext(i, j)).append('\n');
                    break;
                case "BP":
                    i = Integer.parseInt(st.nextToken());
                    j = Integer.parseInt(st.nextToken());
                    sb.append(buildPrev(i, j)).append('\n');
                    break;
                case "CN":
                    i = Integer.parseInt(st.nextToken());
                    sb.append(closeNext(i)).append('\n');
                    break;
                case "CP":
                    i = Integer.parseInt(st.nextToken());
                    sb.append(closePrev(i)).append('\n');
                    break;
            }
        }
        System.out.print(sb);
        br.close();
    }
}
