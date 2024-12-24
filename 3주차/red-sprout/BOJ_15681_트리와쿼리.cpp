#include <iostream>
#include <vector>
#define MAX 101010

using namespace std;

int N, R, Q;
int depth[MAX];
int sz[MAX];
vector<int> tree[MAX];

void init() {
    cin >> N >> R >> Q;
    int u, v;
    for(int i = 0; i < N - 1; i++) {
        cin >> u >> v;
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
}

int dfs(int cur, int d) {
    depth[cur] = d;
    sz[cur] = 1;
    for(auto nxt : tree[cur]) {
        if(depth[nxt] == 0) {
            sz[cur] += dfs(nxt, d + 1);
        }
    }
    return sz[cur];
}

void solution() {
    dfs(R, 1);
    int u;
    for(int i = 0; i < Q; i++) {
        cin >> u;
        cout << sz[u] << '\n';
    }
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    init();
    solution();
    return 0;
}
