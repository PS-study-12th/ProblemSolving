import java.io.*;
import java.util.*;

public class Main {
	static class Pair {
		int f, s;

		public Pair(int f, int s) {
			this.f = f;
			this.s = s;
		}

		@Override
		public String toString() {
			return "[f=" + f + ", s=" + s + "]";
		}
	}
	
	static int n, p, k;
	static List<Pair>[] graph;
	
	static boolean connect(int target) {
		int[] cost = new int[n + 1];
		Arrays.fill(cost, Integer.MAX_VALUE);
		Deque<Pair> q = new ArrayDeque<>();
		boolean[] visited = new boolean[n + 1];
		cost[1] = 0;
		q.offerLast(new Pair(1, 0));
		
		while(!q.isEmpty()) {
			Pair cur = q.pollFirst();
			int u = cur.f, cnt = cur.s;
			if(u == n) return true;
			if(visited[u]) continue;
			visited[u] = true;
			for(Pair e : graph[u]) {
				if(visited[e.f]) continue;
				if(e.s <= target && cost[e.f] > cnt) {
					cost[e.f] = cnt;
					q.offerFirst(new Pair(e.f, cnt));
				} else if(cnt < k && cost[e.f] > cnt + 1) {
					cost[e.f] = cnt + 1;
					q.offerLast(new Pair(e.f, cnt + 1));
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		graph = new List[n + 1];
		for(int i = 1; i <= n; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for(int i = 0; i < p; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			graph[u].add(new Pair(v, d));
			graph[v].add(new Pair(u, d));
		}
		
		int left = -1;
		int right = 1_000_001;
		while(left + 1 < right) {
			int mid = (left + right) / 2;
			if(connect(mid)) {
				right = mid;
			} else {
				left = mid;
			}
		}

		System.out.println(connect(right) ? right : -1);
		br.close();
	}
}
