/*
시간: 684ms
메모리: 140736kb
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static int N;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String line;
//		StringTokenizer st=new StringTokenizer(br.readLine());
		
		while((line=br.readLine())!=null&&!line.isEmpty()) {
			N=Integer.parseInt(line.trim());
			ArrayList<String> words=new ArrayList<>();
			Trie tree=new Trie();
			int totalSum=0;
			
			for (int i = 0; i < N; i++) {
				String s=br.readLine();
				words.add(s);
				tree.add(s);
			}
			
			for (String s : words) {
				int num=tree.search(s);
				totalSum+=num;
			}
			
			System.out.printf("%.2f\n",(double) totalSum/(double) words.size());
		}
	}
}

class Trie{
	Node root;

	public Trie() {
		root=new Node('*', false);
	}
	
	public void add(String s) {
		ArrayList<Node> nxt=root.next;
		boolean flag=false;
		for (int i = 0; i < s.length();) {
			flag=false;
			for (Node node : nxt) {
				if(node.c.equals(s.charAt(i))) {
					nxt=node.next;
					flag=true;
					if(i==s.length()-1) {
						node.end=true;
					}
					break;
				}
			}
			if(flag==false) {
				Node newNode;
				if(i==s.length()-1) {
					newNode=new Node(s.charAt(i),true);
				}
				else {
					newNode=new Node(s.charAt(i),false);
				}
				nxt.add(newNode);
				nxt=newNode.next;
			}
			i++;
		}
	}
	
	public int search(String s) {
		ArrayList<Node> nxt=root.next;
		Node cur=root;
		int sum=0;
		for (int i = 0; i < s.length(); i++) {
			if(nxt.size()>1||i==0||cur.end) {
				sum++;
			}
			for (Node node : nxt) {
				if(node.c.equals(s.charAt(i))) {
					nxt=node.next;
					cur=node;
					break;
				}
			}
		}
		return sum;
	}
}

class Node{
	Character c;
	ArrayList<Node> next;
	boolean end;
	public Node(Character c, boolean end) {
		this.c = c;
		this.end=end;
		next=new ArrayList<Node>();
	}
}
