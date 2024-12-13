import java.io.*;
import java.util.*;

public class Main {
	static int M, N, L;
	static int[] hunter;
	static int dist(int x, int a, int b) {
		return Math.abs(x - a) + b;
	}
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		M = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		L = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine(), " ");
		hunter = new int[M];
		for(int i = 0; i < M; i++) {
			hunter[i] = Integer.parseInt(st.nextToken());
		}
		Arrays.sort(hunter);
		
		int cnt = 0;
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int idx = Arrays.binarySearch(hunter, a);
			int d = Integer.MAX_VALUE;
			if(idx < 0) {
				idx = Math.abs(idx) - 2;
				if(idx >= 0) {
					d = Math.min(d, dist(hunter[idx], a, b));
				}
				if(idx + 1 < M) {					
					d = Math.min(d, dist(hunter[idx + 1], a, b));
				}
			} else {
				d = dist(hunter[idx], a, b);
			}
			if(d <= L) cnt++;
		}
		System.out.println(cnt);
		br.close();
	}
}
