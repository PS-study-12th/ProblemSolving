import java.io.*;
import java.util.*;

public class Main {
  static int N, M, size;
  static int[] depth;
  static long[] cost;
  static int[][] parent;
  static List<int[]>[] tree;

  static void dfs(int cur, int d, long c) {
    depth[cur] = d;
    cost[cur] = c;
    for (int[] edge : tree[cur]) {
      if (depth[edge[0]] == 0) {
        dfs(edge[0], d + 1, c + edge[1]);
        parent[0][edge[0]] = cur;
      }
    }
  }

  static int lca(int u, int v) {
    int d, n1, n2;
    if (depth[u] > depth[v]) {
      d = depth[u] - depth[v];
      n1 = u;
      n2 = v;
    } else {
      d = depth[v] - depth[u];
      n1 = v;
      n2 = u;
    }

    for (int i = size; i >= 0; i--) {
      if ((d & (1 << i)) != 0) {
        n1 = parent[i][n1];
      }
    }

    if (n1 == n2)
      return n1;

    for (int i = size; i >= 0; i--) {
      if (parent[i][n1] != parent[i][n2]) {
        n1 = parent[i][n1];
        n2 = parent[i][n2];
      }
    }

    return parent[0][n1];
  }

  static long getDist(int u, int v) {
    int l = lca(u, v);
    return cost[u] + cost[v] - 2 * cost[l];
  }

  static int getKth(int u, int v, int k) {
    int l = lca(u, v), d, node;
    int mid = depth[u] - depth[l] + 1;
    if(mid == k) return l;
    if (mid > k) {
      d = k - 1;
      node = u;
    } else {
      d = mid + depth[v] - depth[l] - k;
      node = v;
    }

    for (int i = size; i >= 0; i--) {
      if ((d & (1 << i)) != 0) {
        d ^= (1 << i);
        node = parent[i][node];
      }
    }

    return node;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = null;

    N = Integer.parseInt(br.readLine());
    size = (int) Math.ceil(Math.log(N) / Math.log(2));
    depth = new int[N + 1];
    cost = new long[N + 1];
    parent = new int[size + 1][N + 1];
    tree = new List[N + 1];
    for (int i = 1; i <= N; i++) {
      tree[i] = new ArrayList<>();
    }

    for (int i = 0; i < N - 1; i++) {
      st = new StringTokenizer(br.readLine(), " ");
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      int w = Integer.parseInt(st.nextToken());
      tree[u].add(new int[] { v, w });
      tree[v].add(new int[] { u, w });
    }

    dfs(1, 1, 0);
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= N; j++) {
        parent[i][j] = parent[i - 1][parent[i - 1][j]];
      }
    }
    
    M = Integer.parseInt(br.readLine());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < M; i++) {
      st = new StringTokenizer(br.readLine(), " ");
      int q = Integer.parseInt(st.nextToken());
      int u = Integer.parseInt(st.nextToken());
      int v = Integer.parseInt(st.nextToken());
      if (q == 1) {
        sb.append(getDist(u, v)).append('\n');
      } else {
        int k = Integer.parseInt(st.nextToken());
        sb.append(getKth(u, v, k)).append('\n');
      }
    }

    System.out.print(sb);
    br.close();
  }
}
