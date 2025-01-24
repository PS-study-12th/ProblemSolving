import java.io.*;
import java.util.*;

public class Main {
	static class Point implements Comparable<Point>{
		long x, y, v; int i;
		
		public Point(long x, long y, long v, int i) {
			this.x = x; this.y = y; this.v = v; this.i = i;
		}
		
		@Override
		public String toString() {
			return "[" + x + ", " + y + ", " + v + ", " + i + "]";
		}

		@Override
		public int compareTo(Point p) {
			return this.y == p.y ? Long.compare(this.x, p.x) : Long.compare(this.y, p.y);
		}
	}
	
	static class Pair {
		int idx; long val;
		
		public Pair(int idx, long val) {
			this.idx = idx; this.val = val;
		}

		@Override
		public String toString() {
			return "[" + idx + ", " + val + "]";
		}
	}
	
	static class Node {
		long left, right, mid, all;
	}
	
	static int n, m, size;
	static Node[] tree;
	static Point[] point;
	static Pair[] tmpX, tmpY;
	
	static void compression() {
		tmpX = new Pair[n];
		tmpY = new Pair[n];
		for(Point p : point) {
			tmpX[p.i] = new Pair(p.i, p.x);
			tmpY[p.i] = new Pair(p.i, p.y);
		}
		
		Arrays.sort(tmpX, (o1, o2) -> Long.compare(o1.val, o2.val));
		Arrays.sort(tmpY, (o1, o2) -> Long.compare(o1.val, o2.val));
		
		int ix = 0, iy = 0;
		for(int i = 0; i < n - 1; i++) {
			point[tmpX[i].idx].x = ix;
			if(tmpX[i].val != tmpX[i + 1].val) ix++;
		}
		point[tmpX[n - 1].idx].x = ix;
		
		for(int i = 0; i < n - 1; i++) {
			point[tmpY[i].idx].y = iy;
			if(tmpY[i].val != tmpY[i + 1].val) iy++;
		}
		point[tmpY[n - 1].idx].y = iy;
		
		Arrays.sort(point);
	}
	
	static void init() {
		for(int i = 0; i < size; i++) {
			tree[i].left = tree[i].right = tree[i].mid = (long) (-1e14);
			tree[i].all = 0;
		}
	}
	
	static Node merge(Node n1, Node n2) {
		Node node = new Node();
		node.left = Math.max(n1.left, n1.all + n2.left);
		node.right = Math.max(n2.right, n1.right + n2.all);
		node.mid = Math.max(Math.max(n1.mid, n2.mid), n1.right + n2.left);
		node.all = n1.all + n2.all;
		return node;
	}
	
	static void update(int idx, long val) {
		idx += m;
		tree[idx].all += val;
		tree[idx].left = tree[idx].right = tree[idx].mid = tree[idx].all;
		while((idx >>= 1) > 0) {
			tree[idx] = merge(tree[idx << 1], tree[idx << 1 | 1]);
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		n = Integer.parseInt(br.readLine());
		m = 1 << (int) Math.ceil(Math.log(n) / Math.log(2));
		size = m << 1;
		tree = new Node[size];
		for(int i = 0; i < size; i++) tree[i] = new Node();
		point = new Point[n];
		for(int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			long x = Long.parseLong(st.nextToken());
			long y = Long.parseLong(st.nextToken());
			long v = Long.parseLong(st.nextToken());
			point[i] = new Point(x, y, v, i);
		}
		compression();
		
		long res = 0;
		for(int i = 0; i < n; i++) {
			if(i > 0 && point[i - 1].y == point[i].y) continue;
			init();
			for(int j = i; j < n; j++) {
				update((int) point[j].x, point[j].v);
				if(j == n - 1 || point[j].y != point[j + 1].y) res = Math.max(res, tree[1].mid);
			}
		}
		
		System.out.println(res);
		br.close();
	}
}
