import java.io.*;
import java.util.*;

public class Main {
	static int n, m, p;
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, -1, 0, 1};
	static HashMap<Integer, Long> lazy;
	static HashMap<Integer, Rabbit> map;
	static PriorityQueue<Rabbit> movePQ;
	static HashSet<Integer> moved;
	static Rabbit dummy;
	static class Rabbit {
		int pid, d, row, col, jump;
		long score;
		
		Rabbit(int pid, int d) {
			this.pid = pid;
			this.d = d;
			this.row = 1;
			this.col = 1;
			this.jump = 0;
			this.score = 0;
		}

		@Override
		public String toString() {
			return "Rabbit [pid=" + pid + ", d=" + d + ", row=" + row + ", col=" + col + ", jump=" + jump + ", score="
					+ score + "]";
		}
	}
	static int comp1(Rabbit r1, Rabbit r2) {
		if(r1.jump == r2.jump) {
			return -comp2(r1, r2);
		} else {
			return r1.jump - r2.jump;
		}
	};
	static int comp2(Rabbit r1, Rabbit r2) {
		if(r1.row + r1.col == r2.row + r2.col) {
			if(r1.row == r2.row) {
				if(r1.col == r2.col) {
					return r2.pid - r1.pid;
				} else {
					return r2.col - r1.col;
				}
			} else {
				return r2.row - r1.row;
			}
		} else {
			return (r2.row + r2.col) - (r1.row + r1.col);
		}
	}
	static void init(int N, int M, int P, int[] pid, int[] d) {
		n = N; m = M; p = P;
		lazy = new HashMap<>();
		map = new HashMap<>();
		movePQ = new PriorityQueue<>((r1, r2) -> comp1(r1, r2));
		moved = new HashSet<>();
		dummy = new Rabbit(0, 0);
		for(int i = 0; i < pid.length; i++) {
			Rabbit r = new Rabbit(pid[i], d[i]);
			map.put(pid[i], r);
			movePQ.offer(r);
		}
	}
	static void move(int K, int S) {
		for(int i = 0; i < K; i++) {
			Rabbit r = movePQ.poll();
			setPos(r);
			lazy.put(r.pid, lazy.getOrDefault(r.pid, 0L) + r.row + r.col);
			movePQ.offer(r);
			moved.add(r.pid);
		}
		Rabbit best = dummy;
		for(int pid : moved) {
			if(comp2(best, map.get(pid)) > 0) {
				best = map.get(pid);
			}
		}
		best.score += S;
		moved.clear();
	}
	static void update(int pid, int L) {
		map.get(pid).d *= L;
	}
	static long result() {
		for(int pid : lazy.keySet()) {
			long plus = lazy.get(pid);
			for(int id : map.keySet()) {
				if(id == pid) continue;
				Rabbit r = map.get(id);
				r.score += plus;
			}
		}
		long best = 0;
		for(int pid : map.keySet()) {
			best = Math.max(best, map.get(pid).score);
		}
		return best;
	}
	static void setPos(Rabbit r) {
		int[] result = new int[2];
		for(int i = 0; i < 4; i++) {			
			int row = r.row;
			int col = r.col;
			int d = r.d;
			int limit = 0;
			int dist = 0;
			if(i == 0 || i == 2) {
				limit = n - 1;
				dist = Math.abs(row - 1 + dr[i] * d);
			} else {
				limit = m - 1;
				dist = Math.abs(col - 1 + dc[i] * d);
			}
			int cnt = dist / limit;
			int idx = dist % limit;
			if(i == 0 || i == 2) {
				if(cnt % 2 == 0) {
					row = idx + 1;
				} else {
					row = n - idx;
				}
			} else {
				if(cnt % 2 == 0) {
					col = idx + 1;
				} else {
					col = m - idx;
				}
			}
			if(result[0] + result[1] < row + col) {
				result[0] = row;
				result[1] = col;
			} else if(result[0] + result[1] == row + col) {
				if(result[0] < row) {
					result[0] = row;
					result[1] = col;
				} else if(result[0] == row) {
					if(result[1] < col) {						
						result[0] = row;
						result[1] = col;
					}
				}
			}
		}
		r.row = result[0];
		r.col = result[1];
		r.jump++;
	}
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		int Q = Integer.parseInt(br.readLine());
		for(int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());
			switch(query) {
			case 100:
				int N = Integer.parseInt(st.nextToken());
				int M = Integer.parseInt(st.nextToken());
				int P = Integer.parseInt(st.nextToken());
				int[] pid = new int[P];
				int[] d = new int[P];
				for(int j = 0; j < P; j++) {
					pid[j] = Integer.parseInt(st.nextToken());
					d[j] = Integer.parseInt(st.nextToken());
				}
				init(N, M, P, pid, d);
				break;
			case 200:
				int K = Integer.parseInt(st.nextToken());
				int S = Integer.parseInt(st.nextToken());
				move(K, S);
				break;
			case 300:
				int pidT = Integer.parseInt(st.nextToken());
				int L = Integer.parseInt(st.nextToken());
				update(pidT, L);
				break;
			case 400:
				System.out.println(result());
				break;
			}
		}
		br.close();
	}
}
