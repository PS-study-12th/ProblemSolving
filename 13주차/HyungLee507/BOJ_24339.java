package backjoon.B_type.study13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class BOJ_24339 {
    static int O, E;
    static Set<Integer> roots;
    static Map<Integer, Node> nodes;
    static Map<Integer, Edge> edges;

    static class Node {
        int id;
        boolean isRoot;
        List<Integer> weakEdges;
        List<Integer> strongEdges;

        public Node(int id, String rootInfo) {
            this.id = id;
            this.isRoot = rootInfo.equals("ROOT");
            weakEdges = new ArrayList<>();
            strongEdges = new ArrayList<>();
        }
    }

    static class Edge {
        int id;
        int from;
        int to;
        boolean isStrong; // true: STRONG (=>), false: WEAK (->)

        public Edge(int id, int from, int to, boolean isStrong) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.isStrong = isStrong;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bf.readLine());
        O = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        init();

        for (int i = 0; i < O; i++) {
            st = new StringTokenizer(bf.readLine());
            int nodeId = Integer.parseInt(st.nextToken());
            String rootInfo = st.nextToken();
            createNode(nodeId, rootInfo);
        }

        for (int i = 0; i < E; i++) {
            String command = bf.readLine();
            if (command.startsWith("MADE")) {
                createNode(command);
            } else if (command.startsWith("ADD")) {
                addEdge(command);
            } else if (command.startsWith("REMOVE")) {
                removeEdge(command);
            } else if (command.charAt(0) == 'M') {
                System.out.println(performBFS(false)); // STRONG 관계만 사용
            } else if (command.charAt(0) == 'm') {
                System.out.println(performBFS(true)); // WEAK 관계도 포함
            }
        }

        bf.close();
    }

    private static void init() {
        roots = new HashSet<>();
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    private static void createNode(int id, String rootInfo) {
        if (nodes.containsKey(id)) {
            return;
        }
        nodes.put(id, new Node(id, rootInfo));
        if (rootInfo.equals("ROOT")) {
            roots.add(id);
        }
    }

    private static void createNode(String command) {
        StringTokenizer st = new StringTokenizer(command.substring(5));
        int id = Integer.parseInt(st.nextToken());
        String rootInfo = st.nextToken();
        createNode(id, rootInfo);
    }

    private static void addEdge(String command) {
        StringTokenizer st = new StringTokenizer(command.substring(4));
        int edgeId = Integer.parseInt(st.nextToken());
        int from = Integer.parseInt(st.nextToken());
        String type = st.nextToken();
        int to = Integer.parseInt(st.nextToken());

        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return;
        }

        Edge newEdge = new Edge(edgeId, from, to, type.equals("=>"));
        edges.put(edgeId, newEdge);

        if (newEdge.isStrong) {
            nodes.get(from).strongEdges.add(edgeId);
        } else {
            nodes.get(from).weakEdges.add(edgeId);
        }
    }

    private static void removeEdge(String command) {
        int edgeId = Integer.parseInt(command.substring(7));
        if (!edges.containsKey(edgeId)) {
            return;
        }

        Edge edge = edges.get(edgeId);
        int from = edge.from;
        nodes.get(from).strongEdges.remove(Integer.valueOf(edgeId));
        nodes.get(from).weakEdges.remove(Integer.valueOf(edgeId));

        edges.remove(edgeId);
    }

    private static int performBFS(boolean followWeak) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // ROOT에서 BFS 시작
        for (int root : roots) {
            if (nodes.containsKey(root)) {
                visited.add(root);
                queue.offer(root);
            }
        }

        while (!queue.isEmpty()) {
            int cur = queue.poll();
            Node currentNode = nodes.get(cur);
            if (currentNode == null) {
                continue;
            }

            // STRONG 관계 탐색
            for (int edgeId : currentNode.strongEdges) {
                Edge edge = edges.get(edgeId);
                if (edge == null || visited.contains(edge.to)) {
                    continue;
                }
                visited.add(edge.to);
                queue.offer(edge.to);
            }

            // WEAK 관계 탐색 (m() 실행 시에만 포함)
            if (followWeak) {
                for (int edgeId : currentNode.weakEdges) {
                    Edge edge = edges.get(edgeId);
                    if (edge == null || visited.contains(edge.to)) {
                        continue;
                    }
                    visited.add(edge.to);
                    queue.offer(edge.to);
                }
            }
        }

        // 방문하지 않은 객체 삭제
        removeUnvisitedNodes(visited);

        return nodes.size();
    }

    private static void removeUnvisitedNodes(Set<Integer> visited) {
        Iterator<Map.Entry<Integer, Node>> iterator = nodes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Node> entry = iterator.next();
            if (!visited.contains(entry.getKey())) {
                iterator.remove();
            }
        }

        // 사용되지 않는 edge 제거
        edges.values().removeIf(edge -> !nodes.containsKey(edge.from) || !nodes.containsKey(edge.to));
    }
}
