import java.io.*;
import java.util.*;

class Node{
	boolean visited;
	boolean isDeleted;
	public Node() {
		super();
		this.visited = false;
		this.isDeleted = false;
	}
}

class Edge{
	int src;
	int dst;
	boolean isDeleted;
	public Edge(int src, int dst) {
		super();
		this.src = src;
		this.dst = dst;
		this.isDeleted=false;
	}
}

class UserSolution {
    
    static HashMap<Integer, Integer> objectMapper;
    static HashMap<Integer, Integer> edgeMapper;
    static ArrayList<Integer> roots;
    static ArrayList<Node> objects;
    static ArrayList<Edge> edges;
    static HashMap<Integer, int[]> strongs;
    static HashMap<Integer, HashSet<Integer>> goes;
    static HashMap<Integer, HashSet<Integer>> comes;
    static ArrayDeque<Integer> q;
    
    public static void main(String[] args) throws Exception {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        objectMapper=new HashMap<>();
        edgeMapper=new HashMap<>();
        roots=new ArrayList<>();
        objects=new ArrayList<>();
        edges=new ArrayList<>();
        strongs=new HashMap<>();
        goes=new HashMap<>();
        comes=new HashMap<>();
        q=new ArrayDeque<>();

        int o=Integer.parseInt(st.nextToken());
        int q=Integer.parseInt(st.nextToken());
        
        for (int oSize = 0; oSize < o; oSize++) {
            st=new StringTokenizer(br.readLine());
            int id=Integer.parseInt(st.nextToken());
            String root=st.nextToken();
            if(root.equals("ROOT")) {
                roots.add(oSize);
            }
            objectMapper.put(id, oSize);
            objects.add(new Node());
        }

        for (int i = 0; i < q; i++) {
            st=new StringTokenizer(br.readLine());
            String cmd=st.nextToken();
            
            switch(cmd) {
                case "MADE":
                    made(st);
                    break;
                case "ADD":
                	add(st);
                	break;
                case "REMOVE":
                	remove(st);
                	break;
                case "M":
                	System.out.println(M());
                	break;
                case "m":
                	System.out.println(m());
                	break;
                default:
                    break;
            }
        }
        
    }
    
    static void made(StringTokenizer st) {
    	int oId=Integer.parseInt(st.nextToken());
        String root=st.nextToken();
        int oSize=objects.size();
        if(root.equals("ROOT")) {
            roots.add(oSize);
        }
        objectMapper.put(oId, oSize);
        objects.add(new Node());
    }
    
    static void add(StringTokenizer st) {
    	int rId=Integer.parseInt(st.nextToken());
    	int oId1=Integer.parseInt(st.nextToken());
    	String relation=st.nextToken();
    	int oId2=Integer.parseInt(st.nextToken());
    	int eId=edges.size();
    	
    	int idx1=objectMapper.get(oId1);
    	int idx2=objectMapper.get(oId2);
    	edgeMapper.put(rId, eId);
    	
    	if(!goes.containsKey(idx1)) {
    		goes.put(idx1, new HashSet<>());
    	}
    	goes.get(idx1).add(eId);
    	if(!comes.containsKey(idx2)) {
    		comes.put(idx2, new HashSet<>());
    	}
    	comes.get(idx2).add(eId);
    	
    	if(relation.equals("=>")) {
    		strongs.put(eId, new int[] {idx1, idx2});
    	}
    	edges.add(new Edge(idx1, idx2));
    }
    
    static void remove(StringTokenizer st) {
    	int rId=Integer.parseInt(st.nextToken());
    	int eId=edgeMapper.get(rId);
    	edges.get(eId).isDeleted=true;
    	strongs.remove(eId);
    }
    
    static void inputRoot() {
    	q.clear();
    	for (int root : roots) {
    		q.add(root);
		}
    }
    
    static int M() {
    	inputRoot();
    	
    	while(!q.isEmpty()) {
    		int start=q.poll();
    		objects.get(start).visited=true;
    		
    		HashSet<Integer> goSets= goes.get(start);
    		if(goSets==null) continue;
    		
    		for (int eId : goes.get(start)) {
				if(!strongs.containsKey(eId)) continue;
				int nxt=strongs.get(eId)[1];
				if(objects.get(nxt).visited || objects.get(nxt).isDeleted) continue;
				q.add(nxt);
			}
    	}
    	return clear();
    }
    
    static int m() {
    	inputRoot();
    	
    	while(!q.isEmpty()) {
    		int start=q.poll();
    		objects.get(start).visited=true;
    		
    		HashSet<Integer> goSets= goes.get(start);
    		if(goSets==null) continue;
    		
    		for (int eId : goSets) {
    			if(edges.get(eId).isDeleted) continue;
				int nxt=edges.get(eId).dst;
				if(objects.get(nxt).visited || objects.get(nxt).isDeleted) continue;
				q.add(nxt);
			}
    	}
    	return clear();
    }
    
    static int clear() {
    	int ans=0;
    	
    	for (int i = 0; i < objects.size(); i++) {
    		Node node=objects.get(i);
    		if(node.visited) {
    			ans++;
    			node.visited=false;
    		}
			else {
				node.isDeleted=true;
				
				HashSet<Integer> goSets= goes.get(i);
	    		if(goSets==null) continue;
				for(int eId : goSets) {
					strongs.remove(eId);
					edges.get(eId).isDeleted=true;
				}
				
				HashSet<Integer> comeSets= comes.get(i);
	    		if(comeSets==null) continue;
				for (int eId : comeSets) {
					strongs.remove(eId);
					edges.get(eId).isDeleted=true;
				}
			}
		}
    	
    	return ans;
    	
    }
}
