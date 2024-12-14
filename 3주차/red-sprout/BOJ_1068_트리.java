import java.io.*;
import java.util.*;

public class Main {
	static class Node {
		int id;
		Node parent;
		List<Node> children;
		
		Node(int id) {
			this.id = id;
			this.children = new ArrayList<>();
		}
	}
	
	static int N;
	static int[] parent;
	static Node root;
	static Node[] node;
	
	static int dfs(Node cur) {
		if(cur.children.isEmpty()) return 1;
		int result = 0;
		for(Node nxt : cur.children) {
			result += dfs(nxt);
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		N = Integer.parseInt(br.readLine());
		parent = new int[N];
		node = new Node[N];
		for(int i = 0; i < N; i++) {
			node[i] = new Node(i);
		}
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < N; i++) {
			parent[i] = Integer.parseInt(st.nextToken());
			if(parent[i] == -1) {
				root = node[i];
			} else {
				node[i].parent = node[parent[i]];
				node[parent[i]].children.add(node[i]);
			}
		}
		
		int num = Integer.parseInt(br.readLine());
		if(num == root.id) {
			System.out.println(0);
		} else {			
			node[num].parent = null;
			node[parent[num]].children.remove(node[num]);
			parent[num] = -1;
			System.out.println(dfs(root));
		}
		
		br.close();
	}
}
