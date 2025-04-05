#include <iostream>
#include <unordered_set>
using namespace std;

struct Rectangle {
    int x[2], y[2];
};

int N;
Rectangle rec[1001];
int parent[1001];
int urank[1001];
unordered_set<int> sets;

bool isNotOverlap(int idx1, int idx2) {
    bool totalOut = rec[idx1].x[1] < rec[idx2].x[0] || rec[idx2].x[1] < rec[idx1].x[0]
        || rec[idx1].y[1] < rec[idx2].y[0] || rec[idx2].y[1] < rec[idx1].y[0];
    bool totalIn = (rec[idx1].x[0] < rec[idx2].x[0] && rec[idx1].y[0] < rec[idx2].y[0]
        && rec[idx2].x[1] < rec[idx1].x[1] && rec[idx2].y[1] < rec[idx1].y[1])
        || (rec[idx2].x[0] < rec[idx1].x[0] && rec[idx2].y[0] < rec[idx1].y[0]
            && rec[idx1].x[1] < rec[idx2].x[1] && rec[idx1].y[1] < rec[idx2].y[1]);

    return totalOut || totalIn;
}

int find(int x) {
    if (x == parent[x]) return x;
    return parent[x] = find(parent[x]);
}

void Union(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) return;

    if (urank[a] == urank[b]) ++urank[a];
    if (urank[a] > urank[b]) parent[b] = a;
    else parent[a] = b;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> N;
    for (int i = 0; i <= N; ++i) parent[i] = i;

    rec[0] = { 0, 0, 0, 0 };
    for (int i = 1; i <= N; ++i) {
        cin >> rec[i].x[0] >> rec[i].y[0] >> rec[i].x[1] >> rec[i].y[1];
    }

    for (int i = 1; i <= N; ++i) {
        for (int j = 0; j < i; ++j) {
            if (!isNotOverlap(i, j)) Union(i, j);
        }
    }

    for (int i = 0; i <= N; ++i) {
        sets.insert(find(i));
    }

    cout << sets.size() - 1;
    return 0;
}