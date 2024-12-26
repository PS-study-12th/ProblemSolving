import java.io.*;
import java.util.*;

public class Main {
	static class Problem implements Comparable<Problem> {
		int p, l, g;

		public Problem(int p, int l, int g) {
			this.p = p;
			this.l = l;
			this.g = g;
		}

		@Override
		public String toString() {
			return "Problem [p=" + p + ", l=" + l + ", g=" + g + "]";
		}

		@Override
		public int compareTo(Problem p) {
			return this.l == p.l ? this.p - p.p : this.l - p.l;
		}
	}
	
	static Map<Integer, Problem> map;
	static TreeSet<Problem> setTotal;
	static TreeSet<Problem>[] setL;
	static Problem comp;
	
	static void init() {
		map = new HashMap<>();
		setTotal = new TreeSet<>();
		setL = new TreeSet[101];
		for(int i = 1; i < setL.length; i++) {
			setL[i] = new TreeSet<>();
		}
		comp = new Problem(0, 0, 0);
	}
	
	static int recommend(int g, int x) {
		return x == 1 ? setL[g].last().p : setL[g].first().p;
	}
	
	static int recommend2(int x) {
		return x == 1 ? setTotal.last().p : setTotal.first().p;
	}
	
	static int recommend3(int x, int l) {
		comp.l = l;
		Problem problem;
		if(x == 1) {
			problem = setTotal.ceiling(comp);
			return problem == null ? -1 : problem.p;
		} else {
			problem = setTotal.floor(comp);
			return problem == null ? -1 : problem.p;
		}
	}
	
	static void add(int p, int l, int g) {
		Problem problem = new Problem(p, l, g);
		map.put(p, problem);
		setTotal.add(problem);
		setL[g].add(problem);
	}
	
	static void solved(int p) {
		Problem problem = map.get(p);
		setTotal.remove(problem);
		setL[problem.g].remove(problem);
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = null;
		
		init();
		String query;
		int x, p, l, g, N, M;
		N = Integer.parseInt(br.readLine());
		while(N-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			p = Integer.parseInt(st.nextToken());
			l = Integer.parseInt(st.nextToken());
			g = Integer.parseInt(st.nextToken());
			add(p, l, g);
		}
		
		M = Integer.parseInt(br.readLine());
		StringBuilder sb = new StringBuilder();
		while(M-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			query = st.nextToken();
			switch(query) {
			case "recommend":
				g = Integer.parseInt(st.nextToken());
				x = Integer.parseInt(st.nextToken());
				sb.append(recommend(g, x)).append('\n');
				break;
			case "recommend2":
				x = Integer.parseInt(st.nextToken());
				sb.append(recommend2(x)).append('\n');
				break;
			case "recommend3":
				x = Integer.parseInt(st.nextToken());
				l = Integer.parseInt(st.nextToken());
				sb.append(recommend3(x, l)).append('\n');
				break;
			case "add":
				p = Integer.parseInt(st.nextToken());
				l = Integer.parseInt(st.nextToken());
				g = Integer.parseInt(st.nextToken());
				add(p, l, g);
				break;
			case "solved":
				p = Integer.parseInt(st.nextToken());
				solved(p);
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
