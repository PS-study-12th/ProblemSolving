#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using vi = vector<int>;

int N, M, K;
vi blue, parent;

void init() { // idx 기반 parent 배열 관리
    parent.resize(M+1);
    for (int i = 0; i < M+1; i++) parent[i] = i;
}

int findParent(int node) {
    if (node == parent[node]) return node;
    else return parent[node] = findParent(parent[node]);
}

void unionParent(int u, int v) {
    int pu = findParent(u);
    int pv = findParent(v);
    if (pu != pv) parent[pu] = pv;
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N >> M >> K;
    init();

    for(int i=0; i<M; ++i) {
        int num;
        cin >> num;
        blue.push_back(num);
    }
    sort(blue.begin(), blue.end());

    for(int i=0; i<K; ++i) {
        int num;
        cin >> num;
        int pos = upper_bound(blue.begin(), blue.end(), num) - blue.begin();
        int cardIdx = findParent(pos);
        cout << blue[cardIdx] << "\n";
        unionParent(cardIdx, cardIdx+1);
    }
    return 0;
}
