#include <iostream>
#include <vector>
#include <utility>
#include <cstring>
using namespace std;
using ull = unsigned long long;
using pii = pair<int, int>;
using piull = pair<int, ull>;
int N, M;
vector<vector<pii>> tree; // {v, weight}

// LCA를 통한 이동 경로 찾기 + 비용 관리
int depth[100001];
int anc[17][100001];
ull cost[17][100001];

void dfs(int cur, int prev) {
    for(pii nxt : tree[cur]) {
        if(nxt.first == prev) continue;

        anc[0][nxt.first] = cur;
        cost[0][nxt.first] = nxt.second;
        depth[nxt.first] = depth[cur]+1;
        dfs(nxt.first, cur);
    }
}

void fillLCATable() {
    for (int i=1; i <= 16; ++i) {
        for (int j= 1; j <= N; ++j) {
            if (anc[i-1][j]) {
                anc[i][j] = anc[i-1][anc[i-1][j]];
                cost[i][j] = cost[i-1][j] + cost[i-1][anc[i-1][j]];
            }
        }
    }
}

piull findLCA(int u, int v) {
    ull ret = 0; // 이동 비용 합계

    // depth 맞추기
    if (depth[u] < depth[v]) swap(u, v);
    int diff = depth[u] - depth[v];

    for (int i = 16; i >= 0; --i) {
        if ((1 << i) <= diff) {
            ret += cost[i][u];
            u = anc[i][u];
            diff -= (1 << i);
        }
    }

    if (u == v) return {u, ret};

    for (int i = 16; i >= 0; i--) {
        if (anc[i][u] != anc[i][v]) {
            ret += cost[i][v] + cost[i][u];
            u = anc[i][u];
            v = anc[i][v];
        }
    }

    // LCA 바로 아래 조상 처리
    ret += cost[0][u] + cost[0][v];
    return {anc[0][u], ret};
}

int findKth(int u, int v, int k) {
    int lca = findLCA(u, v).first;
    int distU = depth[u] - depth[lca] + 1;

    if(k == distU) return lca;
    else if (k < distU){
        k--;
        for(int i=16; k; --i){
            if(k & (1<<i)){
                u = anc[i][u];
                k -= (1<<i);
            }
        }
        return u;
    }
    else{
        k -= distU;
        k = depth[v]-depth[lca]-k;
        for(int i=16; k; --i){
            if(k & (1<<i)){
                v = anc[i][v];
                k -= (1<<i);
            }
        }
        return v;
    }
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N;
    tree.resize(N+1);
    for(int i=0; i<N-1; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        tree[u].emplace_back(v, w);
        tree[v].emplace_back(u, w);
    }

    memset(anc, 0, sizeof(anc));
    memset(cost, 0, sizeof(cost));
    memset(depth, 0, sizeof(depth));

    depth[1] = 0; // 1번 root 지정
    dfs(1, 0);
    fillLCATable(); // LCA 구하기 위한 전처리

    cin >> M;
    for(int i=0; i<M; i++) {
        int q, u, v, k;
        cin >> q;
        if(q==1) { // u에서 v로 가는 경로 비용
            cin >> u >> v;
            cout << findLCA(u, v).second << "\n";
        }
        else { // u에서 v로 가는 경로에 존재하는 정점 중 kth 정점
            cin >> u >> v >> k;
            cout << findKth(u, v, k) << "\n";
        }
    }
}
