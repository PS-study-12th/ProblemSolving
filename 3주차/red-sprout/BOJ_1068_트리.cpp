#include <iostream>
#include <vector>
#include <algorithm>
#define MAX 51

using namespace std;

struct Node {
    int id;
    vector<int> children;
};

int N, R, D;
Node node[MAX];
int parent[MAX];

void init() {
    cin >> N;
    for (int i = 0; i < N; i++) {
        cin >> parent[i];
        node[i].id = i;
        if (parent[i] == -1) {
            R = i;
        } else {
            node[parent[i]].children.push_back(i);
        }
    }
}

int dfs(int cur) {
    if (cur == D) return 0;
    if (node[cur].children.empty()) return 1;
    int result = 0;
    for (int child : node[cur].children) {
        result += dfs(child);
    }
    return result;
}

void solution() {
    cin >> D;
    if (D == R) {
        cout << 0 << '\n';
        return;
    }
    for (int i = 0; i < N; i++) {
        auto& children = node[i].children;
        children.erase(remove(children.begin(), children.end(), D), children.end());
    }
    cout << dfs(R) << '\n';
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    init();
    solution();
    return 0;
}
