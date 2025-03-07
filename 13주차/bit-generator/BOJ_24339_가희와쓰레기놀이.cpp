#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <utility>
#define STRONG    1
#define WEAK    0
using namespace std;

int O, E, object_id, object_id2, ref_id;
string is_root, cmd, linked;
vector<int> roots;
unordered_map<int, int> links;
unordered_map<int, unordered_map<int, int>> graph, rev;
unordered_set<int> objects;
unordered_set<int> check;

void checkLink(int state) {
    for (const int& r : roots) {
        queue<int> q;
        q.push(r);
        check.insert(r);

        while (!q.empty()) {
            int cur = q.front();
            q.pop();

            for (auto it = graph[cur].begin(); it != graph[cur].end(); ) {
                int id = it->first;
                int nxt = it->second;
                
                if(check.find(nxt) != check.end()) {
                    ++it;
                    continue;
                }

                if (links.find(id) == links.end()) {
                    links.erase(id);
                    it = graph[cur].erase(it);
                    continue;
                }

                if (links[id] >= state) {
                    check.insert(nxt);
                    q.push(nxt);
                }

                ++it;
            }
        }
    }
}

void removeObject() {
    for (auto it = objects.begin(); it != objects.end(); ) {
        int n = *it;
        if (check.find(n) == check.end()) {
            for (const auto& p : graph[n]) {
                links.erase(p.first);
            }

            for (const auto& p : rev[n]) {
                graph[p.second].erase(p.first);
                links.erase(p.first);
            }

            graph.erase(n);
            rev.erase(n);
            it = objects.erase(it);
        }
        else {
            ++it;
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> O >> E;
    while (O--) {
        cin >> object_id >> is_root;
        if (is_root == "ROOT") {
            roots.push_back(object_id);
        }

        objects.insert(object_id);
    }

    while (E--) {
        cin >> cmd;
        if (cmd == "MADE") {
            cin >> object_id >> is_root;
            if (is_root == "ROOT") {
                roots.push_back(object_id);
            }

            objects.insert(object_id);
        }
        else if (cmd == "ADD") {
            cin >> ref_id >> object_id >> linked >> object_id2;
            if (linked == "=>") {
                links.emplace(ref_id, STRONG);
            }
            else {
                links.emplace(ref_id, WEAK);
            }

            graph[object_id].emplace(ref_id, object_id2);
            rev[object_id2].emplace(ref_id, object_id);
        }
        else if (cmd == "REMOVE") {
            cin >> ref_id;
            links.erase(ref_id);
        }
        else if (cmd == "M") {
            check.clear();
            checkLink(1);
            removeObject();
            cout << check.size() << '\n';
        }
        else {
            check.clear();
            checkLink(0);
            removeObject();
            cout << check.size() << '\n';
        }
    }

    return 0;
}