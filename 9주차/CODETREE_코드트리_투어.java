import java.io.*;
import java.util.*;

public class Main {
	static class Edge implements Comparable<Edge> {
		int u, w;
		
		public Edge(int u, int w) {
			this.u = u;
			this.w = w;
		}

		@Override
		public String toString() {
			return "[" + u + ", " + w + "]";
		}

		@Override
		public int compareTo(Edge e) {
			return this.w - e.w;
		}
	}
	
	static class Tour implements Comparable<Tour> {
		int id, revenue, dest;
		
		public Tour(int id, int revenue, int dest) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
		}

		@Override
		public String toString() {
			return "[" + id + ", " + revenue + ", " + dest + "]";
		}

		@Override
		public int compareTo(Tour t) {
			int g1 = this.revenue - cost[this.dest];
			int g2 = t.revenue - cost[t.dest];
			if(g1 == g2) return this.id - t.id;
			return g2 - g1;
		}
	}
	
	static int n, m;
	static int[] cost;
	static List<Edge>[] g;
	static HashMap<Integer, Tour> map;
	static TreeSet<Integer> set;
	
	static void dijkstra(int s) {
		Arrays.fill(cost, 1_000_000_000);
		boolean[] visited = new boolean[n];
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		
		cost[s] = 0;
		pq.offer(new Edge(s, 0));
		while(!pq.isEmpty()) {
			Edge cur = pq.poll();
			
			if(visited[cur.u]) continue;
			visited[cur.u] = true;
			
			for(Edge e : g[cur.u]) {
				if(!visited[e.u] && cost[e.u] > cur.w + e.w) {
					cost[e.u] = cur.w + e.w;
					pq.offer(new Edge(e.u, cost[e.u]));
				}
			}
		}
	}
	
	static void init(StringTokenizer st) {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		cost = new int[n];
		g = new List[n];
		map = new HashMap<>();
		set = new TreeSet<>((i1, i2) -> map.get(i1).compareTo(map.get(i2)));
		for(int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		
		for(int i = 0; i < m; i++) {
			int v = Integer.parseInt(st.nextToken());
			int u = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			g[v].add(new Edge(u, w));
			g[u].add(new Edge(v, w));
		}
		
		updateStart(0);
	}
	
	static void create(int id, int revenue, int dest) {
		map.put(id, new Tour(id, revenue, dest));
		set.add(id);
	}
	
	static void cancel(int id) {
		if(!map.containsKey(id)) return;
		set.remove(id);
		map.remove(id);
	}
	
	static int sell() {
		Integer id = set.pollFirst();
		if(id == null || map.get(id).revenue < cost[map.get(id).dest]) return -1;
		map.remove(id);
		return id;
	}
	
	static void updateStart(int s) {
		dijkstra(s);
		set.clear();
		for(int id : map.keySet()) {
			set.add(id);
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int Q = Integer.parseInt(br.readLine());
		int cmd, id, revenue, dest, s;
		while(Q-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) {
			case 100:
				init(st);
				break;
			case 200:
				id = Integer.parseInt(st.nextToken());
				revenue = Integer.parseInt(st.nextToken());
				dest = Integer.parseInt(st.nextToken());
				create(id, revenue, dest);
				break;
			case 300:
				id = Integer.parseInt(st.nextToken());
				cancel(id);
				break;
			case 400:
				id = sell();
				sb.append(id).append('\n');
				cancel(id);
				break;
			case 500:
				s = Integer.parseInt(st.nextToken());
				updateStart(s);
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}

