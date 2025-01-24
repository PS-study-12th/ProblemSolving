import java.io.*;
import java.util.*;

public class Main {
	static class Node {
		int l, r, v;
		long sum;
		
		Node() {
			l = -1;
			r = -1;
			v = 0;
			sum = 0;
		}
	}
	
	static HashMap<String, Integer> nameValue;
	static HashMap<Integer, String> valueName;
	static ArrayList<Node> tree;
	static final int MAX = 1_000_000_000;
	
	static void init() {
		nameValue = new HashMap<>();
		valueName = new HashMap<>();
		tree = new ArrayList<>();
		tree.add(new Node());
	}
	
	static void merge(int node) {
		int left = tree.get(node).l == -1 ? 0 : tree.get(tree.get(node).l).v;
		int right = tree.get(node).r == -1 ? 0 : tree.get(tree.get(node).r).v;
		tree.get(node).v = left + right;
		
		long lsum = tree.get(node).l == -1 ? 0 : tree.get(tree.get(node).l).sum;
		long rsum = tree.get(node).r == -1 ? 0 : tree.get(tree.get(node).r).sum;
		tree.get(node).sum = lsum + rsum;
	}
	
	static void update(int node, int s, int e, int idx, int val) {
		if(s == e) {
			tree.get(node).v += val;
			tree.get(node).sum += idx * val;
			return;
		}
		int mid = (s + e) >> 1;
		if(idx <= mid) {
			if(tree.get(node).l == -1) {
				tree.get(node).l = tree.size();
				tree.add(new Node());
			}
			update(tree.get(node).l, s, mid, idx, val);
		} else {
			if(tree.get(node).r == -1) {
				tree.get(node).r = tree.size();
				tree.add(new Node());
			}
			update(tree.get(node).r, mid + 1, e, idx, val);
		}
		merge(node);
	}
	
	static int getKth(int node, int s, int e, int k) {
		if(s == e) return s;
		int lv = tree.get(node).l == -1 ? 0 : tree.get(tree.get(node).l).v;
		int mid = (s + e) >> 1;
		if(k <= lv) {
			return getKth(tree.get(node).l, s, mid, k);
		} else {
			return getKth(tree.get(node).r, mid + 1, e, k - lv);
		}
	}
	
	static long getSum(int node, int s, int e, int ts, int te) {
		if(node == -1) return 0;
		if(e < ts || te < s) return 0;
		if(ts <= s && e <= te) return tree.get(node).sum;
		int mid = (s + e) >> 1;
		return getSum(tree.get(node).l, s, mid, ts, te) + getSum(tree.get(node).r, mid + 1, e, ts, te);
	}
	
	static int insert(String name, int value) {
		if(nameValue.containsKey(name) || valueName.containsKey(value)) return 0;
		update(0, 1, MAX, value, 1);
		nameValue.put(name, value);
		valueName.put(value, name);
		return 1;
	}
	
	static int delete(String name) {
		if(!nameValue.containsKey(name)) return 0;
		int value = nameValue.get(name);
		update(0, 1, MAX, value, -1);
		nameValue.remove(name);
		valueName.remove(value);
		return value;
	}
	
	static String rank(int k) {
		return k > nameValue.size() ? "None" : valueName.get(getKth(0, 1, MAX, k));
	}
	
	static long sum(int k) {
		return getSum(0, 1, MAX, 1, k);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		String cmd, name;
		int value, k;
		int Q = Integer.parseInt(br.readLine());
		while(Q-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = st.nextToken();
			switch(cmd) {
			case "init":
				init();
				break;
			case "insert":
				name = st.nextToken();
				value = Integer.parseInt(st.nextToken());
				sb.append(insert(name, value)).append('\n');
				break;
			case "delete":
				name = st.nextToken();
				sb.append(delete(name)).append('\n');
				break;
			case "rank":
				k = Integer.parseInt(st.nextToken());
				sb.append(rank(k)).append('\n');
				break;
			case "sum":
				k = Integer.parseInt(st.nextToken());
				sb.append(sum(k)).append('\n');
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
