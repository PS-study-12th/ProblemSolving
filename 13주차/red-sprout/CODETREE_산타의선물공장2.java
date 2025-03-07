import java.io.*;
import java.util.*;

public class Main {
	static class Node {
		int l = -1, r = -1;
		
		@Override
		public String toString() {
			return "[" + l + ", " + r + "]";
		}
	}
	
	static int n, m;
	static Node[] node;
	static int[] start, end, sz;
	
	static void connect(int n1, int n2) {
		if(n1 == -1 || n2 == -1) return;
		node[n1].r = n2;
		node[n2].l = n1;
	}
		
	static void cmd100(int[] bNum) {
		node = new Node[m + 1];
		for(int i = 1; i <= m; i++) {
			node[i] = new Node();
		}
		
		start = new int[n + 1];
		end = new int[n + 1];
		sz = new int[n + 1];
		Arrays.fill(start, -1);
		Arrays.fill(end, -1);
		
		for(int i = 1; i <= m; i++) {
			int b = bNum[i];
			if(sz[b] == 0) {
				start[b] = i;
				end[b] = i;
			} else {
				node[end[b]].r = i;
				node[i].l = end[b];
				end[b] = i;
			}
			sz[b]++;
		}
	}
	
	static int cmd200(int mSrc, int mDst) {
		if(sz[mSrc] == 0) return sz[mDst];
		
		connect(end[mSrc], start[mDst]);
		
		start[mDst] = start[mSrc];
		if(sz[mDst] == 0) end[mDst] = end[mSrc];
		sz[mDst] += sz[mSrc];
		
		start[mSrc] = -1;
		end[mSrc] = -1;
		sz[mSrc] = 0;
		
		return sz[mDst];
	}
	
	static int cmd300(int mSrc, int mDst) {
		int src = start[mSrc];
		int dst = start[mDst];
		int ns = -1, nd = -1;
		
		if(src != -1) {
			ns = node[src].r;			
			node[src].r = -1;
		}
		
		if(dst != -1) {
			nd = node[dst].r;
			node[dst].r = -1;
		}
		
		if(src != -1 && dst != -1) {
			connect(src, nd);
			connect(dst, ns);
			start[mSrc] = dst;
			start[mDst] = src;
			if(sz[mSrc] == 1) end[mSrc] = dst; 
			if(sz[mDst] == 1) end[mDst] = src;
		} else if(src != -1) {
			start[mSrc] = ns;
			if(ns != -1) node[ns].l = -1;
			if(sz[mSrc] == 1) end[mSrc] = -1;
			start[mDst] = src;
			end[mDst] = src;
			sz[mSrc]--;
			sz[mDst]++;
		} else if(dst != -1) {
			start[mDst] = nd;
			if(nd != -1) node[nd].l = -1;
			if(sz[mDst] == 1) end[mDst] = -1;
			start[mSrc] = dst;
			end[mSrc] = dst;
			sz[mDst]--;
			sz[mSrc]++;
		}
		
		return sz[mDst];
	}
	
	static int cmd400(int mSrc, int mDst) {
		int half = sz[mSrc] / 2;
		if(half == 0) return sz[mDst];
		
		int idx = start[mSrc];
		for(int i = 1; i < half; i++) {
			idx = node[idx].r;
		}
		
		int nIdx = node[idx].r;
		node[idx].r = -1;
		node[nIdx].l = -1;
		connect(idx, start[mDst]);
		
		start[mDst] = start[mSrc];
		if(sz[mDst] == 0) end[mDst] = idx;
		sz[mDst] += half;
		
		start[mSrc] = nIdx;
		sz[mSrc] -= half;
		return sz[mDst];
	}
	
	static int cmd500(int pNum) {
		int a = node[pNum].l;
		int b = node[pNum].r;
		return a + 2 * b;
	}
	
	static int cmd600(int bNum) {
		int a = start[bNum];
		int b = end[bNum];
		int c = sz[bNum];
		return a + 2 * b + 3 * c;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		int q = Integer.parseInt(br.readLine());
		int cmd, mSrc, mDst, pNum, bNum;
		int[] bNumArr;
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) {
			case 100:
				n = Integer.parseInt(st.nextToken());
				m = Integer.parseInt(st.nextToken());
				bNumArr = new int[m + 1];
				for(int i = 1; i <= m; i++) {
					bNumArr[i] = Integer.parseInt(st.nextToken());
				}
				cmd100(bNumArr);
				break;
			case 200:
				mSrc = Integer.parseInt(st.nextToken());
				mDst = Integer.parseInt(st.nextToken());
				sb.append(cmd200(mSrc, mDst)).append('\n');
				break;
			case 300:
				mSrc = Integer.parseInt(st.nextToken());
				mDst = Integer.parseInt(st.nextToken());
				sb.append(cmd300(mSrc, mDst)).append('\n');
				break;
			case 400:
				mSrc = Integer.parseInt(st.nextToken());
				mDst = Integer.parseInt(st.nextToken());
				sb.append(cmd400(mSrc, mDst)).append('\n');
				break;
			case 500:
				pNum = Integer.parseInt(st.nextToken());
				sb.append(cmd500(pNum)).append('\n');
				break;
			case 600:
				bNum = Integer.parseInt(st.nextToken());
				sb.append(cmd600(bNum)).append('\n');
				break;
			}
		}
		
		System.out.print(sb);
		br.close();
	}
}
