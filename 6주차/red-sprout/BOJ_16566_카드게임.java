import java.io.*;
import java.util.*;

public class Main {
	static int n, m, k;
	static int[] arr;
	static int[] parent;
	
	static int find(int x) {
		if(parent[x] == x) return x;
		return parent[x] = find(parent[x]);
	}
	
	static void union(int x, int y) {
		if(y >= m) return;
		x = find(x);
		y = find(y);
		if(x != y) parent[x] = y;
	}
	
	static int query(int x) {
		int left = -1;
		int right = m;
		while(left + 1 < right) {
			int mid = (left + right) / 2;
			if(arr[mid] > x) {
				right = mid;
			} else {
				left = mid;
			}
		}
		int idx = find(right);
		union(idx, idx + 1);
		return arr[idx];
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		arr = new int[m];
		parent = new int[m];
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < m; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
			parent[i] = i;
		}
		Arrays.sort(arr);
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < k; i++) {
			sb.append(query(Integer.parseInt(st.nextToken()))).append('\n');
		}
		
		System.out.print(sb);
		br.close();
	}
}
