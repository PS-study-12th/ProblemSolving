import java.io.*;
import java.util.*;

public class Main {
	static class Edge {
		int id;
		String feat;
		
		Edge(int id, String feat) {
			this.id = id;
			this.feat = feat;
		}

		@Override
		public String toString() {
			return "[" + id + ", " + feat + "]";
		}
	}
	
	static HashSet<Integer> objects;
	static HashSet<Integer> roots;
	static HashMap<Integer, int[]> weaks;
	static HashMap<Integer, int[]> strongs;
	static HashMap<Integer, Set<Integer>> connections;
	static HashMap<Integer, List<Edge>> graph;
	static Queue<Integer> q, lazy;
	static HashSet<Integer> v;
	
	static final String ROOT = "ROOT";
	static final String WEAK = "->";
	static final String STRONG = "=>";
	
	static void init() {
		objects = new HashSet<>();
		roots = new HashSet<>();
		weaks = new HashMap<>();
		strongs = new HashMap<>();
		connections = new HashMap<>();
		graph = new HashMap<>();
		q = new ArrayDeque<>();
		lazy = new ArrayDeque<>();
		v = new HashSet<>();
	}
	
	static void made(int oId, String isRoot) {
		objects.add(oId);
		graph.put(oId, new ArrayList<>());
		connections.put(oId, new HashSet<>());
		if(isRoot.equals(ROOT)) {
			roots.add(oId);
		}
	}
	
	static void add(int rId, int oId1, String feat, int oId2) {
		graph.get(oId1).add(new Edge(rId, feat));
		connections.get(oId1).add(rId);
		connections.get(oId2).add(rId);
		if(feat.equals(WEAK)) {
			weaks.put(rId, new int[] {oId1, oId2});
		} else {
			strongs.put(rId, new int[] {oId1, oId2});
		}
	}
	
	static void remove(int rId) {
		weaks.remove(rId);
		strongs.remove(rId);
	}
	
	static void fill() {
		q.clear();
		v.clear();
		lazy.clear();
		for(int r : roots) {
			q.offer(r);
			v.add(r);
		}
	}
	
	static void clear() {
		for(int obj : objects) {
			if(!v.contains(obj)) {
				lazy.offer(obj);
			}
		}
		
		while(!lazy.isEmpty()) {
			int obj = lazy.poll();
			objects.remove(obj);
			graph.remove(obj);
			for(int rId : connections.get(obj)) {
				remove(rId);
			}
		}
	}
	
	static int m() {
		fill();
		while(!q.isEmpty()) {
			int cur = q.poll();
			for(Edge e : graph.get(cur)) {
				int rId = e.id;
				int[] ref = e.feat.equals(STRONG) ? strongs.get(rId) : weaks.get(rId);
				if(ref != null && !v.contains(ref[1]) && objects.contains(ref[1])) {
					v.add(ref[1]);
					q.offer(ref[1]);
				}
			}
		}
		clear();
		return objects.size();
	}
	
	static int M() {
		fill();
		while(!q.isEmpty()) {
			int cur = q.poll();			for(Edge e : graph.get(cur)) {
				int rId = e.id;
				int[] ref = strongs.get(rId);
				if(ref != null && !v.contains(ref[1]) && objects.contains(ref[1])) {
					v.add(ref[1]);
					q.offer(ref[1]);
				}
			}
		}
		clear();
		return objects.size();
	}
	
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;
        
        st = new StringTokenizer(br.readLine(), " ");
        int o = Integer.parseInt(st.nextToken());
        int e = Integer.parseInt(st.nextToken());
        init();
        
        String cmd, isRoot, feat;
        int oId, oId1, oId2, rId;
        StringBuilder sb = new StringBuilder();
        while(o-- > 0) {
        	st = new StringTokenizer(br.readLine(), " ");
        	oId = Integer.parseInt(st.nextToken());
        	isRoot = st.nextToken();
        	made(oId, isRoot);
        }
        
        while(e-- > 0) {
        	st = new StringTokenizer(br.readLine(), " ");
        	cmd = st.nextToken();
        	switch(cmd) {
        	case "MADE":
            	oId = Integer.parseInt(st.nextToken());
            	isRoot = st.nextToken();
            	made(oId, isRoot);
        		break;
        	case "ADD":
        		rId = Integer.parseInt(st.nextToken());
        		oId1 = Integer.parseInt(st.nextToken());
        		feat = st.nextToken();
        		oId2 = Integer.parseInt(st.nextToken());
        		add(rId, oId1, feat, oId2);
        		break;
        	case "REMOVE":
        		rId = Integer.parseInt(st.nextToken());
        		remove(rId);
        		break;
        	case "m":
        		sb.append(m()).append('\n');
        		break;
        	case "M":
        		sb.append(M()).append('\n');
        		break;
        	}
        }
        
        System.out.println(sb);
        br.close();
    }
}
