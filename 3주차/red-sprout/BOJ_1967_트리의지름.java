import java.io.*;
import java.util.*;

public class Main {
	static int n;
	static int[] dist;
	static List<int[]>[] tree;
	static boolean[] visited;
		
	static void dfs(int cur, int d) {
		dist[cur] = Math.max(dist[cur], d);
		visited[cur] = true;
		for(int[] edge : tree[cur]) {
			if(!visited[edge[0]]) {
				dfs(edge[0], d + edge[1]);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		n = Integer.parseInt(br.readLine());
		tree = new List[n + 1];
		for(int i = 1; i <= n; i++) tree[i] = new ArrayList<>();
		
		for(int i = 0; i < n - 1; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			tree[a].add(new int[] {b, c});
			tree[b].add(new int[] {a, c});
		}
		
		dist = new int[n + 1];
		visited = new boolean[n + 1];
		for(int i = 1; i <= n; i++) {
			Arrays.fill(visited, false);
			if(tree[i].size() == 1) dfs(i, 0);
		}
		
		int answer = 0;
		for(int i = 1; i <= n; i++) {
			answer = Math.max(answer, dist[i]);
		}

		System.out.println(answer);
		br.close();
	}
}
