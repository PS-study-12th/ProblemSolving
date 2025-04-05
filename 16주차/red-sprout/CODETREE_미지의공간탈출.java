import java.io.*;
import java.util.*;

public class Main {
	static class Strange {
		int row, col, d, v;
		
		Strange(int row, int col, int d, int v) {
			this.row = row;
			this.col = col;
			this.d = d;
			this.v = v;
		}
	}
	
	static int N, M, F;
	static int[][] map, copy, meet; // 0 : 빈칸, 1 : 장애물, 2 : 출발, 3 : cube, 4 : 탈출구, 5 : 이상현상
	static int[][][] cube;
	static Strange[] strange;
	static int[] dr = {0, 0, 1, -1};
	static int[] dc = {1, -1, 0, 0};
	
	static int[] transfer(int d, int r, int c) {
		int[] result = {d, r, c};
		if(d == 0) {
			if(r == -1) {
				result[0] = 4; result[1] = M - 1 - c; result[2] = M - 1;
			} else if(r == M) {
				result[1] = -2; // meet
			} else if(c == -1) {
				result[0] = 2; result[1] = r; result[2] = M - 1;
			} else if(c == M) {
				result[0] = 3; result[1] = r; result[2] = 0;
			}
		} else if(d == 1) {
			if(r == -1) {
				result[0] = 4; result[1] = c; result[2] = 0;
			} else if(r == M) {
				result[1] = -2; // meet
			} else if(c == -1) {
				result[0] = 3; result[1] = r; result[2] = M - 1;
			} else if(c == M) {
				result[0] = 2; result[1] = r; result[2] = 0;
			}
		} else if(d == 2) {
			if(r == -1) {
				result[0] = 4; result[1] = M - 1; result[2] = c;
			} else if(r == M) {
				result[1] = -2; // meet
			} else if(c == -1) {
				result[0] = 1; result[1] = r; result[2] = M - 1;
			} else if(c == M) {
				result[0] = 0; result[1] = r; result[2] = 0;
			}
		} else if(d == 3) {
			if(r == -1) {
				result[0] = 4; result[1] = 0; result[2] = M - 1 - c;
			} else if(r == M) {
				result[1] = -2; // meet
			} else if(c == -1) {
				result[0] = 0; result[1] = r; result[2] = M - 1;
			} else if(c == M) {
				result[0] = 1; result[1] = r; result[2] = 0;
			}
		} else if(d == 4) {
			if(r == -1) {
				result[0] = 3; result[1] = 0; result[2] = M - 1 - c;
			} else if(r == M) {
				result[0] = 2; result[1] = 0; result[2] = c;
			} else if(c == -1) {
				result[0] = 1; result[1] = 0; result[2] = r;
			} else if(c == M) {
				result[0] = 0; result[1] = 0; result[2] = M - 1 - r;
			}
		}
		return result;
	}
	
	static int cubeBFS(int sr, int sc) {
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][][] visited = new boolean[5][M][M];
		q.offer(new int[] {4, sr, sc, 0});
		visited[4][sr][sc] = true;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			for(int i = 0; i < 4; i++) {
				int[] pos = transfer(cur[0], cur[1] + dr[i], cur[2] + dc[i]);
				if(pos[1] == -2) {
					if(meet[pos[0]][pos[2]] == 0) return cur[3] + 1;
					continue;
				}
				if(visited[pos[0]][pos[1]][pos[2]] || cube[pos[0]][pos[1]][pos[2]] != 0) continue;
				visited[pos[0]][pos[1]][pos[2]] = true;
				q.offer(new int[] {pos[0], pos[1], pos[2], cur[3] + 1});
			}
		}
		return -1;
	}
	
	static int mapBFS(int time, int mr, int mc) {
		if(time == -1 || !check(time, mr, mc)) return -1;
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];
		q.offer(new int[] {time, mr, mc});
		visited[mr][mc] = true;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			if(map[cur[1]][cur[2]] == 4) return cur[0];
			for(int i = 0; i < 4; i++) {
				int nt = cur[0] + 1;
				int nr = cur[1] + dr[i];
				int nc = cur[2] + dc[i];
				if(nr < 0 || nr >= N || nc < 0 || nc >= N || visited[nr][nc]) continue;
				if(check(nt, nr, nc)) {
					q.offer(new int[] {nt, nr, nc});
					visited[nr][nc] = true;
				}
			}
		}
		return -1;
	}
	
	static boolean check(int time, int r, int c) {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				copy[i][j] = map[i][j];
			}
		}
		for(Strange s : strange) {
			int row = s.row;
			int col = s.col;
			int k = time / s.v;
			while(copy[row][col] != 1 && copy[row][col] != 4 && k-- >= 0) {
				copy[row][col] = 5;
				row += dr[s.d];
				col += dc[s.d];
				if(row < 0 || row >= N || col < 0 || col >= N) break;
			}
		}
		return copy[r][c] != 1 && copy[r][c] != 5;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		F = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		copy = new int[N][N];
		meet = new int[4][M];
		cube = new int[5][M][M];
		int r = -1, c = -1, sr = -1, sc = -1;
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == 3) {					
					if(r == -1 && c == -1) {
						r = i; c = j;
					}
					map[i][j] = 1;
				}
			}
		}
		
		int mr = -1, mc = -1;
		for(int i = 0; i < 4; i++) Arrays.fill(meet[i], 1);
		if(c + M < N - 1) {
			for(int i = 0; i < M; i++) {
				meet[0][i] = map[r + M - 1 - i][c + M];
				if(meet[0][i] != 1) {
					mr = r + M - 1 - i;
					mc = c + M;
				}
			}
		}
		if(c > 0) {
			for(int i = 0; i < M; i++) {
				meet[1][i] = map[r + i][c - 1];
				if(meet[1][i] != 1) {
					mr = r + i;
					mc = c - 1;
				}
			}
		}
		if(r + M < N - 1) {
			for(int i = 0; i < M; i++) {
				meet[2][i] = map[r + M][c + i];
				if(meet[2][i] != 1) {
					mr = r + M;
					mc = c + i;
				}
			}
		}
		if(r > 0) {
			for(int i = 0; i < M; i++) {
				meet[3][i] = map[r - 1][c + M - 1 - i];
				if(meet[3][i] != 1) {
					mr = r - 1;
					mc = c + M - 1 - i;
				}
			}
		}
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < M; j++) {
				st = new StringTokenizer(br.readLine(), " ");
				for(int k = 0; k < M; k++) {
					cube[i][j][k] = Integer.parseInt(st.nextToken());
					if(cube[i][j][k] == 2) {
						sr = j; sc = k;
					}
				}
			}
		}
		
		strange = new Strange[F];
		for(int i = 0; i < F; i++) {
			st = new StringTokenizer(br.readLine());
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			strange[i] = new Strange(row, col, d, v);
		}
		
		System.out.println(mapBFS(cubeBFS(sr, sc), mr, mc));
		br.close();
	}
}
