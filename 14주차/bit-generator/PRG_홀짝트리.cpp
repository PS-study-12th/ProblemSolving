#include <string>
#include <vector>
#include <queue>
using namespace std;

vector<int> graph[1000001];
bool check[1000001];
int nodeCnt[2];

void bfs(int n) {
    queue<int> q;
    nodeCnt[0] = 0; nodeCnt[1] = 0;
    
    q.push(n);
    check[n] = true;
    
    if(graph[n].size() % 2 == n % 2) ++nodeCnt[0];
    else ++nodeCnt[1];
    
    while(!q.empty()) {
        int cur = q.front();
        q.pop();
        
        for(int nxt : graph[cur]) {
            if(check[nxt]) continue;
            check[nxt] = true;
            q.push(nxt);
            
            if(graph[nxt].size() % 2 == nxt % 2) ++nodeCnt[0];
            else ++nodeCnt[1];
        }
    }
}

vector<int> solution(vector<int> nodes, vector<vector<int>> edges) {
    vector<int> answer;
    answer.resize(2);
    
    for(int i = 0; i < (int)edges.size(); ++i) {
        graph[edges[i][0]].push_back(edges[i][1]);
        graph[edges[i][1]].push_back(edges[i][0]);
    }
    
    for(int i = 0; i < (int)nodes.size(); ++i) {
        if(check[nodes[i]]) continue;
        bfs(nodes[i]);
        if(nodeCnt[0] == 1) ++answer[0];
        if(nodeCnt[1] == 1) ++answer[1];
    }
    
    return answer;
}