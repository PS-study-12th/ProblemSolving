// [PRG] 매출 하락 최소화, 98.8MB, 166.36ms
#include <string>
#include <vector>
#include <cstring>

using namespace std;

int n;
vector<int> tree[300'001];
int sale[300'001];
int dp[300'001][2];

int dfs(int cur, int d) {
    if(tree[cur].empty()) return 0;
    if(dp[cur][d] != -1) return dp[cur][d];
    
    int* res = &dp[cur][d];
    *res = 0;
    bool flag = true;
    int diff = 1'000'000'000;
    for(int nxt : tree[cur]) {
        int ns = dfs(nxt, 0);
        int s = dfs(nxt, 1) + sale[nxt];
        *res += min(ns, s);
        if(d == 0) {
            if(ns > s) {
                flag = false;
            } else {
                diff = min(diff, s - ns);
            }
        } else {
            flag = false;
        }
    }
    
    if(flag) {
        *res += diff;
    }
    return *res;
}

void init(vector<int> sales, vector<vector<int>> links) {
    n = sales.size();
    for(int i = 0; i < n; ++i) {
        sale[i + 1] = sales[i];
    }
    for(vector<int> l : links) {
        tree[l[0]].emplace_back(l[1]);
    }
}

int solution(vector<int> sales, vector<vector<int>> links) {
    init(sales, links);
    memset(dp, -1, sizeof(dp));
    int res = dfs(1, 0);
    memset(dp, -1, sizeof(dp));
    res = min(res, dfs(1, 1) + sale[1]);
    return res;
}
