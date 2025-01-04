import java.io.*;
import java.util.*;

public class Main {
	static int MAX, SIZE;
	static int[] tree;
	
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
	
	static int get(int node, int s, int e, int k) {
		if(s == e) return s;
		int mid = (s + e) >> 1;
		int left = tree[node << 1];
		if(k <= left) {
			return get(node << 1, s, mid, k);
		} else {
			return get(node << 1 | 1, mid + 1, e, k - left);
		}
	}
	
	static void update(int idx, int val) {
		update(1, 1, MAX, idx, val);
	}
	
	static int get(int k) {
		int idx = get(1, 1, MAX, k);
		update(1, 1, MAX, idx, -1);
		return idx;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int n = Integer.parseInt(br.readLine());
		MAX = 1_000_000;
		SIZE = 1 << ((int) Math.ceil(Math.log(MAX) / Math.log(2)) + 1);
		tree = new int[SIZE];
		
		int a, b, c;
		while(n-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			a = Integer.parseInt(st.nextToken());
			if(a == 1) {
				b = Integer.parseInt(st.nextToken());
				sb.append(get(b)).append('\n');
			} else {
				b = Integer.parseInt(st.nextToken());
				c = Integer.parseInt(st.nextToken());
				update(b, c);
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
