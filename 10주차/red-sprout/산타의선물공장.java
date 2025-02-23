import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        int id, w, b;
        int prev, next;

        Node(int id, int w, int b) {
            this.id = id;
            this.w = w;
            this.b = b;
        }

        @Override
        public String toString() {
            return "[" + id + ", " + w + ", " + b + "]";
        }
    }

    static int n, m;
    static HashMap<Integer, Node> idMap;
    static int[] head, tail;
    static int[] parent;
    static boolean[] broken;

    static void connect(int n1, int n2) {
        connect(idMap.get(n1), idMap.get(n2));
    }

    static void disconnect(int n1, int n2) {
        disconnect(idMap.get(n1), idMap.get(n2));
    }

    static void remove(int n1) {
        remove(idMap.get(n1));
    }

    static void connect(Node n1, Node n2) {
        if(n1 == null || n2 == null) return;
        n1.next = n2.id;
        n2.prev = n1.id;
    }

    static void disconnect(Node n1, Node n2) {
        if(n1 == null || n2 == null) return;
        n1.next = 0;
        n2.prev = 0;
    }

    static void remove(Node n1) {
        int prv = n1.prev, nxt = n1.next;
        disconnect(prv, n1.id);
        disconnect(n1.id, nxt);
        connect(prv, nxt);
        idMap.remove(n1.id);
    }

    static void printAll() {
        for(int i = 1; i <= m; i++) {
            printBelt(i);
        }
    }

    static void printBelt(int b_num) {
        Node h = idMap.get(head[b_num]);
        System.out.print("Belt " + b_num + " : ");
        if(h == null) {
            System.out.println("Empty!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(Node node = h; node != null; node = idMap.get(node.next)) {
            sb.append(node).append(' ');
        }
        System.out.println(sb);
    }

    static void printEnd() {
        for(int i = 1; i <= m; i++) {
            System.out.println("Belt " + i + " : [" + head[i] + ", " + tail[i] + "]");
        }
    }

    static int find(int x) {
        if(x == parent[x]) return x;
        return parent[x] = find(parent[x]);
    }

    static void union(int x, int y) {
        x = find(x);
        y = find(y);
        parent[x] = y;
    }

    static void cmd100(int[] idArr, int[] wArr) {
        idMap = new HashMap<>();
        head = new int[m + 1];
        tail = new int[m + 1];
        parent = new int[m + 1];
        broken = new boolean[m + 1];
        for(int i = 1; i <= m; i++) {
            parent[i] = i;
        }

        int sz = n / m;

        int idx = 0;
        for(int i = 1; i <= m; i++) {
            for(int j = 0; j < sz; j++) {
                idMap.put(idArr[idx], new Node(idArr[idx], wArr[idx], i));
                if(j == 0) {
                    head[i] = idArr[idx];
                    idx++;
                } else {
                    if(j == sz - 1) {
                        tail[i] = idArr[idx];
                    }
                    connect(idArr[idx - 1], idArr[idx]);
                    idx++;
                }
            }
        }
    }

    static long cmd200(int w_max) {
        long res = 0;
        for(int i = 1; i <= m; i++) {
            Node node = idMap.get(head[i]);
            if(node == null) continue;
            if(node.w <= w_max) {
                res += node.w;
                head[i] = node.next;
                remove(node);
                if(head[i] == tail[i]) tail[i] = 0;
            } else {
                if(head[i] == tail[i]) continue;
                head[i] = node.next;
                disconnect(node.id, node.next);
                connect(tail[i], node.id);
                tail[i] = node.id;
            }
        }
        return res;
    }

    static int cmd300(int id) {
        Node node = idMap.get(id);
        if(node == null) return -1;
        int b = find(node.b);
        if(head[b] == id) {
            head[b] = node.next;
        }
        if(tail[b] == id) {
            tail[b] = node.prev;
        }
        remove(id);
        return id;
    }

    static int cmd400(int id) {
        Node node = idMap.get(id);
        if(node == null) return -1;
        int b = find(node.b);
        int prv = node.prev;
        if(prv != 0) {
            disconnect(prv, node.id);
            connect(tail[b], head[b]);
        }
        head[b] = id;
        tail[b] = prv;
        return b;
    }

    static int cmd500(int bNum) {
        if(broken[bNum]) return -1;
        broken[bNum] = true;
        for(int b = bNum + 1; b <= m; b++) {
            if(broken[b]) continue;
            connect(tail[b], head[bNum]);
            union(bNum, b);
            tail[b] = tail[bNum];
            head[bNum] = 0;
            tail[bNum] = 0;
            return bNum;
        }
        for(int b = 1; b <= bNum - 1; b++) {
            if(broken[b]) continue;
            connect(tail[b], head[bNum]);
            union(bNum, b);
            tail[b] = tail[bNum];
            head[bNum] = 0;
            tail[bNum] = 0;
            return bNum;
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = null;

        int q = Integer.parseInt(br.readLine());
        int cmd, id, w_max, b_num;
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch(cmd) {
                case 100:
                    n = Integer.parseInt(st.nextToken());
                    m = Integer.parseInt(st.nextToken());
                    int[] idArr = new int[n];
                    int[] wArr = new int[n];
                    for(int i = 0; i < n; i++) {
                        idArr[i] = Integer.parseInt(st.nextToken());
                    }
                    for(int i = 0; i < n; i++) {
                        wArr[i] = Integer.parseInt(st.nextToken());
                    }
                    cmd100(idArr, wArr);
                    break;
                case 200:
                    w_max = Integer.parseInt(st.nextToken());
                    sb.append(cmd200(w_max)).append('\n');
                    break;
                case 300:
                    id = Integer.parseInt(st.nextToken());
                    sb.append(cmd300(id)).append('\n');
                    break;
                case 400:
                    id = Integer.parseInt(st.nextToken());
                    sb.append(cmd400(id)).append('\n');
                    break;
                case 500:
                    b_num = Integer.parseInt(st.nextToken());
                    sb.append(cmd500(b_num)).append('\n');
                    break;
            }
        }

        System.out.print(sb);
        br.close();
    }
}
