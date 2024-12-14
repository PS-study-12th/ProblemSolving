import java.io.*;
import java.util.*;

public class Main {
	static int N, R, Q;
	static int[] depth, size;
	static List<Integer>[] tree;
	
	static int dfs(int cur, int d) {
		depth[cur] = d;
		size[cur] = 1;
		for(int nxt : tree[cur]) {
			if(depth[nxt] == 0) {
				size[cur] += dfs(nxt, d + 1);
			}
		}
		return size[cur];
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		R = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		depth = new int[N + 1];
		size = new int[N + 1];
		tree = new List[N + 1];
		for(int i = 1; i <= N; i++) {
			tree[i] = new ArrayList<>();
		}
		
		for(int i = 0; i < N - 1; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			tree[u].add(v);
			tree[v].add(u);
		}
		
		dfs(R, 1);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < Q; i++) {
			int u = Integer.parseInt(br.readLine());
			sb.append(size[u]).append('\n');
		}
		
		System.out.print(sb);
		br.close();
	}
}
