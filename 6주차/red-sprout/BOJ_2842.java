import java.io.*;
import java.util.*;

public class Main {
	static class Node { char c; int v; }
	
	static int n, k;
	static int[] p, h;
	static Node[][] map;
	
	static int[] dr = {-1, 0, 1, 0, -1, -1, 1, 1};
	static int[] dc = {0, -1, 0, 1, -1, 1, -1, 1};
	
	static boolean bfs(int low, int high) {
		if(map[p[0]][p[1]].v < low || map[p[0]][p[1]].v > high) return false;
		
		boolean[][] visited = new boolean[n][n];
		Queue<int[]> q = new ArrayDeque<>();
		
		visited[p[0]][p[1]] = true;
		q.offer(new int[] {p[0], p[1]});
		
		int cnt = 0;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int r = cur[0], c = cur[1];
			
			if(map[r][c].c == 'K' && ++cnt == k) return true;
			
			for(int i = 0; i < 8; i++) {
				int nr = r + dr[i];
				int nc = c + dc[i];
				
				if(nr < 0 || nr >= n || nc < 0 || nc >= n || visited[nr][nc]) continue;
				
				if(low <= map[nr][nc].v && map[nr][nc].v <= high) {
					visited[nr][nc] = true;
					q.offer(new int[] {nr, nc});
				}
			}
		}
		
		return false;
	}
	
	static int solution() {
		int left = 0, right = 1, res = Integer.MAX_VALUE;
		
		while(right < h.length) {
			int low = h[left], high = h[right];
			
			if(bfs(low, high) && left < right) {
				res = Math.min(res, high - low);
				left++;
			} else {
				right++;
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		n = Integer.parseInt(br.readLine());
		k = 0;
		p = new int[2];
		h = new int[n * n];
		map = new Node[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				map[i][j] = new Node();
			}
		}
		
		for(int i = 0; i < n; i++) {
			String row = br.readLine();
			for(int j = 0; j < n; j++) {
				char c = row.charAt(j);
				map[i][j].c = c;
				if(c == 'P') {
					p[0] = i; p[1] = j;
				} else if(c == 'K') {
					k++;
				}
			}
		}
		
		for(int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < n; j++) {
				map[i][j].v = Integer.parseInt(st.nextToken());
				h[n * i + j] = map[i][j].v;
			}
		}
		
		Arrays.sort(h);
		System.out.println(solution());
		br.close();
	}
}
