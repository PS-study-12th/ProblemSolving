#include <string>
#include <vector>

using namespace std;

int n, ans;
vector<int> _info;
vector<vector<int>> _edges;
bool v[17];

void dfs(int sheep, int wolf) {
    if(sheep > wolf) {
        ans = max(ans, sheep);
    } else {
        return;
    }
    
    for(vector<int> e : _edges) {
        if(v[e[0]] && !v[e[1]]) {
            v[e[1]] = true;
            if(_info[e[1]] == 0) {
                dfs(sheep + 1, wolf);
            } else {
                dfs(sheep, wolf + 1);
            }
            v[e[1]] = false;
        }
    }
}

int solution(vector<int> info, vector<vector<int>> edges) {
    n = info.size();
    _info = info;
    _edges = edges;
    fill(v, v + n, false);
    ans = 1;
    v[0] = true;
    dfs(1, 0);
    return ans;
}
