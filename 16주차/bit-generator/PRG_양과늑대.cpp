#include <string>
#include <vector>
#include <queue>
using namespace std;

struct Node {
    int mask;
    int wolves;
    int sheep;
};

vector<int> graph[18];
bool check[1 << 17];

int solution(vector<int> info, vector<vector<int>> edges) {
    int ans = 1;
    int sz = info.size();
    
    for(const auto& v : edges) {
        graph[v[0]].push_back(v[1]);
    }
    
    queue<Node> q;
    check[1] = true;
    q.push({1, 0, 1});
    
    while(!q.empty()) {
        Node cur = q.front();
        q.pop();
        
        for(int i = 0; i < sz; ++i) {
            if(cur.mask & (1 << i)) {
                for(auto n : graph[i]) {
                    Node nxt = cur;
                    if(check[nxt.mask | (1 << n)]) continue;
                    if(nxt.wolves + info[n] >= nxt.sheep) continue;
                    
                    if(info[n]) ++nxt.wolves;
                    else ++nxt.sheep;
                    nxt.mask |= (1 << n);
                    
                    if(ans < nxt.sheep) ans = nxt.sheep;
                    q.push(nxt);
                    check[nxt.mask] = true;
                }
            }
        }
    }
    
    return ans;
}