import java.io.*;
import java.util.*;

public class Main {
	static int n, m;
	static int[][] map; // 0 : 빈칸, 1 : 베이스캠프, -1 : 이동불가
	static class Base {
		int row, col;
		boolean pass;
		
		Base(int row, int col) {
			this.row = row;
			this.col = col;
			this.pass = true;
		}

		@Override
		public String toString() {
			return "Base [row=" + row + ", col=" + col + ", pass=" + pass + "]";
		}
	}
	static class Store {
		int row, col;
		boolean pass;
		
		Store(int row, int col) {
			this.row = row;
			this.col = col;
			this.pass = true;
		}

		@Override
		public String toString() {
			return "Store [row=" + row + ", col=" + col + ", pass=" + pass + "]";
		}
	}
	static class Person {
		int id, row, col;
		Base base;
		Store store;
		
		Person(int id, Base base, Store store) {
			this.id = id;
			this.row = base.row;
			this.col = base.col;
			this.base = base;
			this.store = store;
		}

		@Override
		public String toString() {
			return "Person [id=" + id + ", row=" + row + ", col=" + col + ", base=" + base + ", store=" + store + "]";
		}
	}
	static List<Base> baseList;
	static List<Store> storeList;
	static List<Person> personList;
	static int[] dr = {-1, 0, 0, 1};
	static int[] dc = {0, -1, 1, 0};
	
	static int bfs(int r, int c, Store s) {
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[n][n];
		q.offer(new int[] {r, c, 0});
		visited[r][c] = true;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			if(cur[0] == s.row && cur[1] == s.col) return cur[2];
			for(int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				if(nr < 0 || nr >= n || nc < 0 || nc >= n || visited[nr][nc] || map[nr][nc] == -1) continue;
				visited[nr][nc] = true;
				q.offer(new int[] {nr, nc, cur[2] + 1});
			}
		}
		return Integer.MAX_VALUE;
	}
	
	static void movePerson(Person p) {
		Store s = p.store;
		if(!s.pass) return;
		int r = p.row;
		int c = p.col;
		int d = Integer.MAX_VALUE;
		for(int i = 0; i < 4; i++) {
			int nr = p.row + dr[i];
			int nc = p.col + dc[i];
			if(nr < 0 || nr >= n || nc < 0 || nc >= n || map[nr][nc] == -1) continue;
			int nd = bfs(nr, nc, s);
			if(d > nd) {
				r = nr; c = nc; d = nd;
			}
		}
		p.row = r;
		p.col = c;
		if(p.row == s.row && p.col == s.col) s.pass = false;
	}
	
	static void setPerson(int id) {
		Store store = storeList.get(id);
		Base base = null;
		int curDist = Integer.MAX_VALUE;
		for(Base b : baseList) {
			if(!b.pass) continue;
			int nxtDist = bfs(b.row, b.col, store);
			if(base == null) {
				base = b;
				curDist = nxtDist;
				continue;
			}
			if(curDist > nxtDist) {
				base = b;
				curDist = nxtDist;
			} else if(curDist == nxtDist) {
				if(base.row > b.row) {
					base = b;
					curDist = nxtDist;
				} else if(base.row == b.row) {
					if(base.col > b.col) {
						base = b;
						curDist = nxtDist;
					}
				}
			}
		}
		personList.add(new Person(id, base, store));
		base.pass = false;
	}
	
	static void updateMap() {
		for(Base b : baseList) {
			if(!b.pass) map[b.row][b.col] = -1;
		}
		for(Store s : storeList) {
			if(!s.pass) map[s.row][s.col] = -1;
		}
	}
	
	static boolean check() {
		if(personList.size() < m) return false;
		for(Store s : storeList) {
			if(s.pass) return false;
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		st = new StringTokenizer(br.readLine(), " ");
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		map = new int[n][n];
		baseList = new ArrayList<>();
		storeList = new ArrayList<>();
		personList = new ArrayList<>();
		for(int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for(int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == 1) {
					baseList.add(new Base(i, j));
				}
			}
		}
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int row = Integer.parseInt(st.nextToken()) - 1;
			int col = Integer.parseInt(st.nextToken()) - 1;
			storeList.add(new Store(row, col));
		}
		int time = 0;
		while(true) {
			if(check()) break;
			for(Person p : personList) {
				movePerson(p);
			}
			updateMap();
			if(time < m) {
				setPerson(time);
			}
			updateMap();
			time++;
		}
		System.out.println(time);
		br.close();
	}
}
