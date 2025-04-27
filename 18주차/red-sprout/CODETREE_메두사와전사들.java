// [CODETREE] 메두사와 전사들, 15 MB, 215 ms
import java.io.*;
import java.util.*;

public class Main {
	static class Warrior {
		int id, row, col;
		boolean isStun, isExist;
		
		Warrior(int id, int row, int col) {
			this.id = id;
			this.row = row;
			this.col = col;
			this.isStun = false;
			this.isExist = true;
		}

		@Override
		public String toString() {
			return "Warrior [id=" + id + ", row=" + row + ", col=" + col + ", isStun=" + isStun + ", isExist=" + isExist
					+ "]";
		}
	}
	
	static int N, M, attack;
	static int[] S, E;
	static int[][] roadMap, watchMap, dist;
	static int[][][] before; 
	static Warrior[] warrior;
	static int[] dr = {-1, 1, 0, 0};
	static int[] dc = {0, 0, -1, 1};
	
	static int bfs() {
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				dist[i][j] = -1;
				before[i][j][0] = -1;
				before[i][j][1] = -1;
			}
		}
		q.offer(new int[] {S[0], S[1], 0});
		visited[S[0]][S[1]] = true;
		dist[S[0]][S[1]] = 0;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			if(cur[0] == E[0] && cur[1] == E[1]) return cur[2];
			for(int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				if(!isInRange(nr, nc) || visited[nr][nc] || roadMap[nr][nc] == 1) continue;
				q.offer(new int[] {nr, nc, cur[2] + 1});
				visited[nr][nc] = true;
				dist[nr][nc] = cur[2] + 1;
				before[nr][nc][0] = cur[0];
				before[nr][nc][1] = cur[1];
			}
		}
		return -1;
	}
	
	static int[][] getPath(int d) {
		int[][] path = new int[d][2];
		Deque<int[]> stack = new ArrayDeque<>();
		stack.push(new int[] {E[0], E[1]});
		int idx = d - 1;
		while(idx >= 0) {
			int[] b = stack.pop();
			path[idx--] = b;
			for(int i = 0; i < 4; i++) {
				stack.push(before[b[0]][b[1]]);
			}
		}
		return path;
	}
	
	static void simulation(int d, int[][] path) {
		for(int i = 0; i < d - 1; i++) {
			int row = path[i][0];
			int col = path[i][1];
			initSimul(row, col);
			int cnt = setWatchMap(row, col);
			int move = moveWarrior(row, col);
			System.out.println(move + " " + cnt + " " + attack);
		}
		System.out.println(0);
	}
	
	static int cntWarrior() {
		int cnt = 0;
		for(Warrior w : warrior) {
			if(w.isExist) cnt++;
		}
		return cnt;
	}
	
	static void initStun() {
		for(Warrior w : warrior) {
			w.isStun = false;
		}
	}
	
	static void initSimul(int row, int col) {
		attack = 0;
		initStun();
		for(Warrior w : warrior) {
			if(!w.isExist) continue;
			if(w.row == row && w.col == col) w.isExist = false;
		}
	}
	
	static int setWatchMap(int row, int col) {
		int cnt = -1;
		int[][] tmpWatch = new int[N][N];
		boolean[] lazy = new boolean[M];
		for(int i = 0; i < 4; i++) {
			int tmpCnt = 0;
			boolean[] stunned = new boolean[M];
			Queue<int[]> q = new ArrayDeque<>();
			int[][] visited = new int[N][N];
			q.offer(new int[] {row, col});
			while(!q.isEmpty()) {
				int[] cur = q.poll();
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				tmpCnt += updateQ(row, col, nr, nc, q, visited, i, stunned);
				switch(i) {
				case 0: // -1 0
				case 1: // 1 0
					nc = cur[1] - 1;
					tmpCnt += updateQ(row, col, nr, nc, q, visited, i, stunned);
					nc = cur[1] + 1;
					tmpCnt += updateQ(row, col, nr, nc, q, visited, i, stunned);
					break;
				case 2: // 0 -1
				case 3: // 0 1
					nr = cur[0] - 1;
					tmpCnt += updateQ(row, col, nr, nc, q, visited, i, stunned);
					nr = cur[0] + 1;
					tmpCnt += updateQ(row, col, nr, nc, q, visited, i, stunned);
					break;
				}
			}
			if(cnt < tmpCnt) {
				lazy = stunned;
				cnt = tmpCnt;
				tmpWatch = visited;
			}
		}
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				watchMap[i][j] = (tmpWatch[i][j] == 1) ? 1 : 0;
			}
		}
		for(int i = 0; i < M; i++) {
			warrior[i].isStun = lazy[i];
		}
		return cnt;
	}
	
	static int updateQ(int mr, int mc, int row, int col, Queue<int[]> q, int[][] visited, int direction, boolean[] stunned) {
		if(!isInRange(row, col) || visited[row][col] != 0) return 0;
		int cnt = 0;
		for(Warrior w : warrior) {
			if(!w.isExist) continue;
			if(w.row == row && w.col == col) {
				stunned[w.id] = true;
				block(row, col, visited, direction, getPos(mr, mc, row, col, direction));
				cnt++;
			}
		}
		q.offer(new int[] {row, col});
		visited[row][col] = 1;
		return cnt;
	}
	
	static int[][] getPos(int mr, int mc, int row, int col, int direction) {
		switch(direction) {
		case 0: // -1 0
			if(col < mc) return new int[][] {{-1, -1}, {-1, 0}};
			else if(mc < col) return new int[][] {{-1, 0}, {-1, 1}};
			else return new int[][] {{-1, 0}};
		case 1: // 1 0
			if(col < mc) return new int[][] {{1, -1}, {1, 0}};
			else if(mc < col) return new int[][] {{1, 0}, {1, 1}};
			else return new int[][] {{1, 0}};
		case 2: // 0 -1
			if(row < mr) return new int[][] {{0, -1}, {-1, -1}};
			else if(mr < row) return new int[][] {{0, -1}, {1, -1}};
			else return new int[][] {{0, -1}};
		case 3: // 0 1
			if(row < mr) return new int[][] {{0, 1}, {-1, 1}};
			else if(mr < row) return new int[][] {{0, 1}, {1, 1}};
			else return new int[][] {{0, 1}};
		}
		return null;
	}
	
	static void block(int row, int col, int[][] visited, int direction, int[][] pos) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] {row, col});
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			for(int[] p : pos) {
				int nr = cur[0] + p[0];
				int nc = cur[1] + p[1];
				if(isInRange(nr, nc) && visited[nr][nc] == 0) {
					visited[nr][nc] = -1;
					q.offer(new int[] {nr, nc});
				}
			}
		}
	}
	
	static int moveWarrior(int row, int col) {
		int total = 0;
		for(Warrior w : warrior) {
			if(!w.isExist || w.isStun) continue;
			if(move(row, col, w, 0)) {
				total++;
			}
			if(move(row, col, w, 2)) {
				total++;
			}
		}
		return total;
	}
	
	static boolean move(int row, int col, Warrior w, int idx) {
		if(!w.isExist || w.isStun) return false;
		int wr = w.row;
		int wc = w.col;
		int initial = getDist(row, col, wr, wc);
		for(int i = 0; i < 4; i++) {
			int nr = w.row + dr[(i + idx) % 4];
			int nc = w.col + dc[(i + idx) % 4];
			int nDist = getDist(row, col, nr, nc);
			if(isInRange(nr, nc) && watchMap[nr][nc] == 0 && initial > nDist) {
				wr = nr;
				wc = nc;
				initial = nDist;
			}
		}
		if(wr == w.row && wc == w.col) return false;
		w.row = wr;
		w.col = wc;
		if(wr == row && wc == col) {
			attack++;
			w.isExist = false;
		}
		return true;
	}
	
	static boolean isInRange(int row, int col) {
		return 0 <= row && row < N && 0 <= col && col < N;
	}

	static int getDist(int r1, int c1, int r2, int c2) {
		return Math.abs(r1 - r2) + Math.abs(c1 - c2);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		roadMap = new int[N][N];
		watchMap = new int[N][N];
		dist = new int[N][N];
		before = new int[N][N][2];
		warrior = new Warrior[M];
		
		st = new StringTokenizer(br.readLine(), " ");
		S = new int[2];
		E = new int[2];
		S[0] = Integer.parseInt(st.nextToken());
		S[1] = Integer.parseInt(st.nextToken());
		E[0] = Integer.parseInt(st.nextToken());
		E[1] = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine(), " ");
		for(int i = 0; i < M; i++) {
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			warrior[i] = new Warrior(i, row, col);
		}
		
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < N; j++) {
				roadMap[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int d = bfs();
		if(d != -1) {
			simulation(d, getPath(d));
		} else {
			System.out.println(-1);
		}
		br.close();
	}
}
