#include <string>
#include <vector>
#include <unordered_map>
#include <queue>
#include <iostream>

#define LEN 400'000
#define MAX 1'000'000

using namespace std;

int nodeCnt[2];
unordered_map<int, vector<int>> g;
unordered_map<int, bool> v;
queue<int> q;

void bfs(int s) {
    q.emplace(s);
    v[s] = true;
    if(s % 2 == g[s].size() % 2) ++nodeCnt[0];
    else ++nodeCnt[1];
    
    while(!q.empty()) {
        int cur = q.front(); q.pop();
        for(int nxt : g[cur]) {
            if(v[nxt]) continue;
            q.emplace(nxt);
            v[nxt] = true;
            if(nxt % 2 == g[nxt].size() % 2) ++nodeCnt[0];
            else ++nodeCnt[1];
        }
    }
}

vector<int> solution(vector<int> nodes, vector<vector<int>> edges) {
    for(int node : nodes) {
        v[node] = false;
    }
    
    for(vector<int> e : edges) {
        g[e[0]].emplace_back(e[1]);
        g[e[1]].emplace_back(e[0]);
    }
    
    vector<int> answer;
    answer.resize(2);
    for(int node : nodes) {
        if(v[node]) continue;
        fill(nodeCnt, nodeCnt + 2, 0);
        bfs(node);
        if(nodeCnt[0] == 1) ++answer[0];
        if(nodeCnt[1] == 1) ++answer[1];
    }
    
    return answer;
}
