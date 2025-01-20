import java.io.*;
import java.util.*;

public class Main {
	static int hp, wp, hm, wm;
	static long original, hash;
	static long MAX_X1, MAX_X2;
	static char[][] paint, total;
	static long[][] row, table;
	
	static final long X1 = 31;
	static final long X2 = 8191;
	static final long P = 1_000_000_007;
	
	static long setMax(long x, int size) {
		long res = 1;
		for(int i = 0; i < size; i++) {
			res = res * x % P;
		}
		return res;
	}
	
	static long v(char c) {
		return c == 'o' ? 1 : 0;
	}
	
	static void rowHash(char[][] map, int h, int w, int r) {
		for(int i = 0; i <= w - wp; i++) {
			if(i == 0) {
				for(int j = 0; j < wp; j++) {
					row[r][i] = (row[r][i] + v(map[r][i + j])) % P;
					row[r][i] = (row[r][i] * X1) % P;
				}
			} else {
				row[r][i] = X1 * ((row[r][i - 1] + P - v(map[r][i - 1]) * MAX_X1 % P + v(map[r][i + wp - 1])) % P) % P;
			}
		}
	}
	
	static void colHash(char[][] map, int h, int w) {
		for(int i = 0; i <= h - hp; i++) {
			for(int c = 0; c <= w - wp; c++) {
				if(i == 0) {
					for(int r = 0; r < hp; r++) {
						table[i][c] = (table[i][c] + row[i + r][c]) % P;
						table[i][c] = (table[i][c] * X2) % P;
					}
				} else {
					table[i][c] = X2 * ((table[i - 1][c] + P - row[i - 1][c] * MAX_X2 % P + row[i + hp - 1][c]) % P) % P;
				}
			}
		}
	}
	
	static void init() {
		MAX_X1 = setMax(X1, wp);
		MAX_X2 = setMax(X2, hp);
		
		for(int i = 0; i < hp; i++) {
			rowHash(paint, hp, wp, i);
		}
		colHash(paint, hp, wp);
		original = table[0][0];
		
		row = new long[hm][wm];
		table = new long[hm][wm];
		for(int i = 0; i < hm; i++) {
			rowHash(total, hm, wm, i);
		}
		colHash(total, hm, wm);
	}
	
	static int solution() {
		int res = 0;
		for(int i = 0; i <= hm - hp; i++) {
			for(int j = 0; j <= wm - wp; j++) {
				if(table[i][j] == original) res++;
			}
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		st = new StringTokenizer(br.readLine(), " ");
		hp = Integer.parseInt(st.nextToken());
		wp = Integer.parseInt(st.nextToken());
		hm = Integer.parseInt(st.nextToken());
		wm = Integer.parseInt(st.nextToken());
		paint = new char[hp][];
		total = new char[hm][];
		row = new long[hm][wm];
		table = new long[hm][wm];
		
		for(int i = 0; i < hp; i++) {
			paint[i] = br.readLine().toCharArray();
		}
		
		for(int i = 0; i < hm; i++) {
			total[i] = br.readLine().toCharArray();
		}
		
		init();
		System.out.println(solution());
		br.close();
	}
}
