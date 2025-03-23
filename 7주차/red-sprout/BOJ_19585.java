import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        Node[] next = new Node[26];
        boolean isEnd = false;
    }

    static class Trie {
        Node root = new Node();

        void insert(String word) {
            Node cur = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if(cur.next[c - 'a'] == null) cur.next[c - 'a'] = new Node();
                cur = cur.next[c - 'a'];
            }
            cur.isEnd = true;
        }

        boolean find(String word) {
            Node cur = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if(cur.next[c - 'a'] == null) return false;
                cur = cur.next[c - 'a'];
                if(cur.isEnd && back.contains(word.substring(i + 1))) return true;
            }
            return false;
        }
    }

    static int c, n;
    static Trie front;
    static HashSet<String> back;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        c = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        front = new Trie();
        back = new HashSet<>();

        for (int i = 0; i < c; i++) {
            String word = br.readLine();
            front.insert(word);
        }

        for(int i = 0; i < n; i++) {
            String word = br.readLine();
            back.add(word);
        }

        int q = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while(q-- > 0) {
            String word = br.readLine();
            sb.append(front.find(word) ? "Yes" : "No").append('\n');
        }

        System.out.print(sb);
        br.close();
    }
}
