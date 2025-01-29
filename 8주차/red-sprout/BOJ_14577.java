import java.io.*;
import java.util.*;

public class Main {
	static class Node {
		long val = 0;
		int l = -1, r = -1;
	}
	
	static int N, M, size;
	static final long MAX_VALUE = (long) 1e18;
	static long[] arr;
	static List<Node> tree;
	
	static void update(long idx, long val) {
		update(0, 0, MAX_VALUE, idx, val);
	}
	
	static long getTotal(long l, long r) {
		return getTotal(0, 0, MAX_VALUE, l, r);
	}
	
	static long getTth(long t) {
		return getTth(0, 0, MAX_VALUE, t);
	}
	
	static void update(int node, long s, long e, long idx, long val) {
		if(s == e) {
			tree.get(node).val += val;
			return;
		}
		long mid = (s + e) / 2;
		if(idx <= mid) {
			if(tree.get(node).l < 0) {
				tree.add(new Node());
				tree.get(node).l = size++;
			}
			update(tree.get(node).l, s, mid, idx, val);
		} else {
			if(tree.get(node).r < 0) {
				tree.add(new Node());
				tree.get(node).r = size++;
			}
			update(tree.get(node).r, mid + 1, e, idx, val);
		}
		int left = tree.get(node).l;
		int right = tree.get(node).r;
		long lval = left < 0 ? 0 : tree.get(left).val;
		long rval = right < 0 ? 0 : tree.get(right).val;
		tree.get(node).val = lval + rval;
	}
	
	static long getTotal(int node, long s, long e, long ts, long te) {
		if(node < 0) return 0;
		if(e < ts || te < s) return 0;
		if(ts <= s && e <= te) return tree.get(node).val;
		long mid = (s + e) / 2;
		long left = getTotal(tree.get(node).l, s, mid, ts, te);
		long right = getTotal(tree.get(node).r, mid + 1, e, ts, te);
		return left + right;
	}
	
  static long getTth(int node, long s, long e, long t) {
    if (s == e) return s;
    long mid = (s + e) / 2;
    long lval = (tree.get(node).l < 0) ? 0 : tree.get(tree.get(node).l).val;
    if (lval >= t) return getTth(tree.get(node).l, s, mid, t);
    return getTth(tree.get(node).r, mid + 1, e, t - lval);
  }
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		size = 1;
		arr = new long[N + 1];
		tree = new ArrayList<>();
		tree.add(new Node());
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 1; i <= N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
			update(arr[i], 1);
		}
		
		int q, idx;
		long val, l, r, t;
		while(M-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			q = Integer.parseInt(st.nextToken());
			switch(q) {
			case 1:
				idx = Integer.parseInt(st.nextToken());
				val = Long.parseLong(st.nextToken());
				update(arr[idx], -1);
				arr[idx] += val;
				update(arr[idx], 1);
				break;
			case 2:
				idx = Integer.parseInt(st.nextToken());
				val = Long.parseLong(st.nextToken());
				update(arr[idx], -1);
				arr[idx] -= val;
				update(arr[idx], 1);
				break;
			case 3:
				l = Long.parseLong(st.nextToken());
				r = Long.parseLong(st.nextToken());
				sb.append(getTotal(l, r)).append('\n');
				break;
			case 4:
				t = Long.parseLong(st.nextToken());
				sb.append(getTth(N - t + 1)).append('\n');
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
