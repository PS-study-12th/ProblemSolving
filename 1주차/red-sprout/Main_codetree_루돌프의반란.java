import java.io.*;
import java.util.*;

public class Main {
	static int N, M, P, C, D;
	static class Rudolph {
		int row, col;
		
		@Override
		public String toString() {
			return "Rudolph - row : " + row + " , col : " + col;
		}
	}
	static class Santa {
		int id, row, col, time, score;
		boolean isExist;
		
		Santa(int p, int r, int c) {
			id = p; row = r; col = c; time = 0; score = 0; isExist = true;
		}
		
		@Override
		public String toString() {
			return "Santa(" + id + ") - row : " + row 
					+ " , col : " + col
					+ " , time : " + time
					+ " , score : " + score
					+ " , " + (isExist ? "ALIVE" : "DEAD");
		}
	}
	static Rudolph rudolph;
	static Santa[] santa;
	static int[][] map;
	static int[] dr = {-1, 0, 1, 0, -1, 1, -1, 1};
	static int[] dc = {0, 1, 0, -1, -1, -1, 1, 1};
	static int[] reverse = {2, 3, 0, 1};
	static int moveR() {
		updateMap();
		int id = -1, dist = Integer.MAX_VALUE, d = -1;
		for(int i = 1; i <= P; i++) {
			Santa newSanta = santa[i];
			if(!santa[i].isExist) continue;
			int rr = rudolph.row;
			int rc = rudolph.col;
			int sr = newSanta.row;
			int sc = newSanta.col;
			int newDist = getDist(rr, rc, sr, sc);
			if(dist > newDist) {
				id = i; dist = newDist;
			} else if(dist == newDist) {
				if(id == -1) {
					id = i; dist = newDist;
					continue;
				}
				Santa origin = santa[id];
				if(origin.row < newSanta.row) {
					id = i; dist = newDist;
				} else if(origin.row == newSanta.row) {
					if(origin.col < newSanta.col) id = i; dist = newDist;
				}
			}
		}
		for(int i = 0; i < 8; i++) {
			int rr = rudolph.row + dr[i];
			int rc = rudolph.col + dc[i];
			int sr = santa[id].row;
			int sc = santa[id].col;
			if(isOutOfRange(rr, rc)) continue;
			int newDist = getDist(rr, rc, sr, sc);
			if(dist > newDist) {
				dist = newDist; d = i;
			}
		}
		rudolph.row += dr[d];
		rudolph.col += dc[d];
		return d;
	}
	static int moveS(int id) {
		updateMap();
		Santa s = santa[id];
		if(s.time > 0 || !s.isExist) return -1;
		int rr = rudolph.row;
		int rc = rudolph.col;
		int sr = s.row;
		int sc = s.col;
		int dist = getDist(rr, rc, sr, sc), d = -1;
		for(int i = 0; i < 4; i++) {
			sr = s.row + dr[i];
			sc = s.col + dc[i];
			if(isOutOfRange(sr, sc) || map[sr][sc] > 0) continue;
			int newDist = getDist(rr, rc, sr, sc);
			if(dist > newDist) {
				dist = newDist; d = i;
			}
		}
		if(d != -1) {
			s.row += dr[d];
			s.col += dc[d];
		}
		return d;
	}
	static int getDist(int rr, int rc, int sr, int sc) {
		int drow = (rr - sr);
		int dcol = (rc - sc);
		return drow * drow + dcol * dcol;
	}
	static boolean isOutOfRange(int r, int c) {
		return r < 0 || r >= N || c < 0 || c >= N;
	}
	static void rtos(int d) {
		updateMap();
		int r = rudolph.row;
		int c = rudolph.col;
		Santa s = santa[map[r][c]];
		if(s == null) return;
		int id = s.id;
		s.row += dr[d] * C;
		s.col += dc[d] * C;
		s.score += C;
		s.time = 2;
		if(isOutOfRange(s.row, s.col)) {
			s.isExist = false;
			return;
		}
		stos(id, d);
	}
	static void stor(int id, int d) {
		updateMap();
		Santa s = santa[id];
		int r = s.row;
		int c = s.col;
		if(rudolph.row != r || rudolph.col != c) return;
		d = reverse[d];
		s.row += dr[d] * D;
		s.col += dc[d] * D;
		s.score += D;
		s.time = 2;
		if(isOutOfRange(s.row, s.col)) {
			s.isExist = false;
			return;
		}
		stos(id, d);
	}
	static void stos(int id, int d) {
		Santa now = santa[id];
		int r = now.row;
		int c = now.col;
		if(map[r][c] == 0) return;
		Santa next = santa[map[r][c]];
		next.row += dr[d];
		next.col += dc[d];
		if(isOutOfRange(next.row, next.col)) {
			next.isExist = false;
			return;
		}
		stos(next.id, d);
	}
	static void updateMap() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				map[i][j] = 0;
			}
		}
		for(int i = 1; i <= P; i++) {
			Santa s = santa[i];
			if(!s.isExist) continue;
			int r = s.row;
			int c = s.col;
			map[r][c] = s.id;
		}
	}
	static void updateStun() {
		for(int i = 1; i <= P; i++) {
			if(!santa[i].isExist) continue;
			if(santa[i].time > 0) santa[i].time--;
		}
	}
	static boolean hasNext() {
		for(int i = 1; i <= P; i++) {
			if(santa[i].isExist) return true;
		}
		return false;
	}
	static void updateNextScore() {
		for(int i = 1; i <= P; i++) {
			if(santa[i].isExist) santa[i].score++;
		}
	}
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		rudolph = new Rudolph();
		st = new StringTokenizer(br.readLine(), " ");
		rudolph.row = Integer.parseInt(st.nextToken()) - 1;
		rudolph.col = Integer.parseInt(st.nextToken()) - 1;
		santa = new Santa[P + 1];
		for(int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int id = Integer.parseInt(st.nextToken());
			int row = Integer.parseInt(st.nextToken()) - 1;
			int col = Integer.parseInt(st.nextToken()) - 1;
			santa[id] = new Santa(id, row, col);
		}
		map = new int[N][N];
		int d = 0;
		for(int i = 0; i < M; i++) {
			d = moveR();
			rtos(d);
			for(int j = 1; j <= P; j++) {
				d = moveS(j);
				if(d != -1) stor(j, d);
			}
			updateMap();
			updateStun();
			if(hasNext()) {
				updateNextScore();
			} else  {
				break;
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i <= P; i++) {
			sb.append(santa[i].score).append(' ');
		}
		System.out.println(sb);
		br.close();
	}
}
