#include <iostream>
#include <vector>
#include <unordered_map>
#include <set>
#include <queue>
using namespace std;
int O, E;

struct Node {
    int id;
    bool isRoot;
    bool isAlive;

    Node(int _id, bool _isRoot) {
        id = _id;
        isRoot = _isRoot;
        isAlive = true;
    }
};

struct Edge {
    int id;
    int st, dst;
    bool isStrong;
    bool isAlive;

    Edge(int _id, int _st, int _dst, bool _isStrong) {
        id = _id;
        st = _st;
        dst = _dst;
        isStrong = _isStrong;
        isAlive = true;
    }
};

unordered_map<int, int> nodeIdx;
unordered_map<int, int> edgeIdx;
vector<Node> nodes;
vector<Edge> edges;
vector<set<int>> adj;

// BFS
int bfs(bool isStrong) {
    queue<int> q;
    vector<bool> alive(nodes.size(), false);

    for (int i = 0; i < nodes.size(); i++) {
        if (nodes[i].isAlive && nodes[i].isRoot) {
            q.push(i);
            alive[i] = true;
        }
    }

    while (!q.empty()) {
        int cur = q.front();
        q.pop();

        for (int edgeId : adj[cur]) {
            Edge &e = edges[edgeId];

            if (!e.isAlive) continue;
            if (isStrong && !e.isStrong) continue;
            if (!nodes[nodeIdx[e.dst]].isAlive) continue;

            int next = nodeIdx[e.dst];
            if (!alive[next]) {
                alive[next] = true;
                q.push(next);
            }
        }
    }

    int cnt = 0;
    vector<int> rmNodes;
    vector<int> rmEdges;

    for (int i = 0; i < nodes.size(); i++) {
        if (alive[i]) cnt++;
        else {
            nodes[i].isAlive = false;
            rmNodes.push_back(i);
        }
    }

    for (int edgeId = 0; edgeId < edges.size(); edgeId++) {
        Edge &e = edges[edgeId];
        if (!e.isAlive) continue;

        if (!nodes[nodeIdx[e.st]].isAlive || !nodes[nodeIdx[e.dst]].isAlive) {
            rmEdges.push_back(edgeId);
        }
    }

    for (int i : rmNodes) {
        adj[i].clear();
        nodeIdx.erase(nodes[i].id);
    }

    for (int edgeId : rmEdges) {
        edges[edgeId].isAlive = false;
        edgeIdx.erase(edges[edgeId].id);
    }

    return cnt;
}

void command() {
    for (int i = 0; i < E; i++) {
        string cmd;
        cin >> cmd;

        if (cmd == "M") {
            cout << bfs(true) << "\n";
        }
        else if (cmd == "m") {
            cout << bfs(false) << "\n";
        }
        else if (cmd == "MADE") {
            int objId;
            string isRoot;
            cin >> objId >> isRoot;

            bool rootStatus = (isRoot == "ROOT");

            int newIdx = nodes.size();
            nodes.emplace_back(objId, rootStatus);
            nodeIdx[objId] = newIdx;
            adj.emplace_back();
        }
        else if (cmd == "ADD") {
            int edgeId, obj1, obj2;
            string type;
            cin >> edgeId >> obj1 >> type >> obj2;

            bool strong = (type == "=>");
            int idx1 = nodeIdx[obj1];
            int idx2 = nodeIdx[obj2];

            int newIdx = edges.size();
            edges.emplace_back(edgeId, obj1, obj2, strong);
            edgeIdx[edgeId] = newIdx;
            adj[idx1].insert(newIdx);
        }
        else if (cmd == "REMOVE") {
            int edgeId;
            cin >> edgeId;

            if (edgeIdx.find(edgeId) == edgeIdx.end()) continue;

            int idx = edgeIdx[edgeId];
            Edge &e = edges[idx];

            adj[nodeIdx[e.st]].erase(idx);
            edgeIdx.erase(edgeId);
            edges[idx].isAlive = false;
        }
    }
}

void init() {
    cin >> O >> E;
    adj.resize(O + 1);

    for (int i = 0; i < O; i++) {
        int objId;
        string isRoot;
        cin >> objId >> isRoot;

        bool rootStatus = (isRoot == "ROOT");
        int newIdx = nodes.size();
        nodes.emplace_back(objId, rootStatus);
        nodeIdx[objId] = newIdx;
    }
    command();
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    init();
    return 0;
}
