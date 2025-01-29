import java.io.*;
import java.util.*;

public class Main {
	static int N, M, size;
	static int[] tree, lazy;
	static Map<Integer, Integer> map;
	
	static void update(int ts, int te) {
		int h = get(ts, te);
		updateTree(1, 0, M - 1, ts, te, h + 1);
	}
	
	static void updateLazy(int node, int s, int e) {
		if(lazy[node] == 0) return;
		tree[node] = lazy[node];
		if(s != e) {
			lazy[2 * node] = lazy[node];
			lazy[2 * node + 1] = lazy[node];
		}
		lazy[node] = 0;
	}
	
	static void updateTree(int node, int s, int e, int ts, int te, int h) {
		updateLazy(node, s, e);
		if(e < ts || te < s) return;
		if(ts <= s && e <= te) {
			tree[node] = h;
			if(s != e) {
				lazy[2 * node] = h;
				lazy[2 * node + 1] = h;
			}
			return;
		}
		int mid = (s + e) / 2;
		updateTree(2 * node, s, mid, ts, te, h);
		updateTree(2 * node + 1, mid + 1, e, ts, te, h);
		tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
	}
	
	static int get(int ts, int te) {
		return get(1, 0, M - 1, ts, te);
	}
	
	static int get(int node, int s, int e, int ts, int te) {
		updateLazy(node, s, e);
		if(e < ts || te < s) return 0;
		if(ts <= s && e <= te) return tree[node];
		int mid = (s + e) / 2;
		int left = get(2 * node, s, mid, ts, te);
		int right = get(2 * node + 1, mid + 1, e, ts, te);
		return Math.max(left, right);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		N = Integer.parseInt(br.readLine());
		map = new HashMap<>();
		TreeSet<Integer> treeSet = new TreeSet<>();
		List<int[]> queries = new ArrayList<>();
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int w = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			treeSet.add(d);
			treeSet.add(d + w - 1);
			queries.add(new int[] {d, d + w - 1});
		}
		
		M = treeSet.size();
		size = 1 << ((int) Math.ceil(Math.log(M) / Math.log(2)) + 1);
		tree = new int[size];
		lazy = new int[size];
		int idx = 0;
		for(int x : treeSet) {
			map.put(x, idx++);
		}
		
		for(int[] q : queries) {
			int ts = map.get(q[0]);
			int te = map.get(q[1]);
			update(ts, te);
		}
		
		System.out.println(get(0, M - 1));
		br.close();
	}
}
