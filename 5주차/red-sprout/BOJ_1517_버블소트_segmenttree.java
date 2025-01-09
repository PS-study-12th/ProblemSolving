import java.io.*;
import java.util.*;

public class Main {
	static int n, size;
	static int[] arr, tree;
	static HashMap<Integer, Integer> order;
	
	static void init() {
		List<int[]> list = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			list.add(new int[] {i, arr[i]});
		}
		Collections.sort(list, (o1, o2) -> o1[1] - o2[1]);
		
		int idx = 0;
		for(int i = 0; i < n; i++) {
			int key = list.get(i)[1];
			if(!order.containsKey(key)) order.put(key, idx++);
		}
	}
	
	static long get(int x) {
		return get(1, 0, n - 1, order.get(x) + 1, n - 1);
	}
	
	static long get(int node, int s, int e, int ts, int te) {
		if(e < ts || te < s) return 0;
		if(ts <= s && e <= te) return tree[node];
		int mid = (s + e) >> 1;
		return get(node << 1, s, mid, ts, te) + get(node << 1 | 1, mid + 1, e, ts, te);
	}
	
	static void update(int x) {
		update(1, 0, n - 1, order.get(x), 1);
	}
	
	static void update(int node, int s, int e, int idx, int val) {
		if(e < idx || idx < s) return;
		if(s == e) {
			tree[node] += val;
			return;
		}
		int mid = (s + e) >> 1;
		update(node << 1, s, mid, idx, val);
		update(node << 1 | 1, mid + 1, e, idx, val);
		tree[node] = tree[node << 1] + tree[node << 1 | 1];
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		n = Integer.parseInt(br.readLine());
		size = 1 << ((int) Math.ceil(Math.log(n) / Math.log(2)) + 1);
		arr = new int[n];
		tree = new int[size];
		order = new HashMap<>();
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		init();
		long cnt = 0;
		for(int i = 0; i < n; i++) {
			cnt += get(arr[i]);
			update(arr[i]);
		}
		
		System.out.println(cnt);
		br.close();
	}
}
