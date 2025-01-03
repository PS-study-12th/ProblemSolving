package backjoon.B_type.study5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class CODETREE_COLORTREE {
    static class Node {
        int id;
        int color;
        int depth;
        Node parent;
        List<Node> child;

        public Node(int id, int color, int depth) {
            this.id = id;
            this.color = color;
            this.depth = depth;
            parent = null;
            child = new ArrayList<>();
        }
    }

    static List<Node> rootNode;
    static Map<Integer, Node> nodeMap;
    
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;
        StringBuilder sb = new StringBuilder();
        nodeMap = new HashMap<>();
        rootNode = new ArrayList<>();
        int iterationCount = Integer.parseInt(bf.readLine());
        for (int i = 0; i < iterationCount; i++) {
            st = new StringTokenizer(bf.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 100) {
                int id = Integer.parseInt(st.nextToken());
                int pId = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                int depth = Integer.parseInt(st.nextToken());
                addNode(id, color, depth, pId);
            } else if (cmd == 200) {
                int id = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                changeColor(id, color);
            } else if (cmd == 300) {
                int id = Integer.parseInt(st.nextToken());
                int color = getColor(id);
                sb.append(color).append('\n');
            } else {
                sb.append(getScore()).append('\n');
            }
        }
        System.out.println(sb);
        bf.close();
    }

    private static void addNode(int id, int color, int depth, int pId) {
        Node node = new Node(id, color, depth);
        if (pId == -1) {
            rootNode.add(node);
            nodeMap.put(id, node);
            return;
        }
        Node parent = nodeMap.get(pId);
        boolean canGoUp = canAdd(parent, node);
        if (canGoUp) {
            parent.child.add(node);
            node.parent = parent;
            nodeMap.put(id, node);
        }
    }

    private static boolean canAdd(Node parent, Node newChild) {

        if (parent.depth == 1) {
            return false;
        }

        int neededDepth = 2;
        Node cur = parent;
        while (cur != null) {
            if (cur.depth < neededDepth) {
                // 만약 필요한 깊이가 현재 노드의 max_depth보다 크다면 모순
                return false;
            }
            neededDepth++;
            cur = cur.parent;
        }

        return true;
    }

    private static int getColor(int id) {
        return nodeMap.get(id).color;
    }

    private static void changeColor(int id, int color) {
        Node root = nodeMap.get(id);
        root.color = color;
        changeSubTreeColor(root, color);
    }

    private static void changeSubTreeColor(Node root, int color) {
        for (Node node : root.child) {
            node.color = color;
            changeSubTreeColor(node, color);
        }
    }

    private static int getScore() {
        int totalScore = 0;
        for (Node node : nodeMap.values()) {
            Set<Integer> distinctColors = new HashSet<>();
            getSubtreeColors(node, distinctColors);
            int value = distinctColors.size();
            totalScore += value * value;
        }
        return totalScore;
    }

    private static void getSubtreeColors(Node node, Set<Integer> distinctColors) {
        if (node == null) {
            return;
        }
        distinctColors.add(node.color);
        for (Node child : node.child) {
            getSubtreeColors(child, distinctColors);
        }
    }


}
