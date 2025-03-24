#include <string>
#include <vector>
#include <sstream>
#include <utility>
using namespace std;

string table[51][51];
pair<int, int> parent[51][51];

pair<int, int> find(pair<int, int>& x) {
    if(x == parent[x.first][x.second]) return x;
    return parent[x.first][x.second] = find(parent[x.first][x.second]);
}

void Union(pair<int, int>& p1, pair<int, int>& p2) {
    auto a = find(p1);
    auto b = find(p2);
    if(a == b) return;
    
    if(table[a.first][a.second] == "") {
        table[a.first][a.second] = table[b.first][b.second];
    }
    parent[b.first][b.second] = a;
}

vector<string> solution(vector<string> commands) {
    vector<string> answer;
    int len = commands.size();
    
    for(int i = 1; i <= 50; ++i) {
        for(int j = 1; j <= 50; ++j) {
            parent[i][j] = {i, j};
        }
    }
    
    for(int i = 0; i < len; ++i) {
        int pos = commands[i].find(' ');
        string cmd = commands[i].substr(0, pos);
        string str = commands[i].substr(++pos); 
        
        if(cmd == "UPDATE") {
            int cnt = 0;
            for(char c : str) {
                if(c == ' ') ++cnt;
            }
            
            istringstream iss(str);
            if(cnt == 2) {
                int r, c;
                string value;
                iss >> r >> c >> value;
                pair<int, int> p = {r, c};
                p = find(p);
                table[p.first][p.second] = value;
            }
            else {
                string value1, value2;
                iss >> value1 >> value2;
                if(value1 == value2) continue;
                
                for(int i = 1; i <= 50; ++i) {
                    for(int j = 1; j <= 50; ++j) {
                        pair<int, int> p = {i, j};
                        p = find(p);
                        if(table[p.first][p.second] == value1)
                            table[p.first][p.second] = value2;
                    }
                }
            }
        }
        else if(cmd == "MERGE") {
            istringstream iss(str);
            int r1, c1, r2, c2;
            iss >> r1 >> c1 >> r2 >> c2;
            
            pair<int, int> p1 = {r1, c1}, p2 = {r2, c2};
            Union(p1, p2);
        }
        else if(cmd == "UNMERGE") {
            istringstream iss(str);
            int r, c;
            iss >> r >> c;
            
            pair<int, int> p = {r, c};
            p = find(p);
            string val = table[p.first][p.second];
            vector<pair<int, int>> coord;
            
            for(int i = 1; i <= 50; ++i) {
                for(int j = 1; j <= 50; ++j) {
                    pair<int, int> tmp = {i, j};
                    if(find(tmp) == p) {
                        coord.emplace_back(i, j);
                        table[i][j] = "";
                    }
                }
            }
            
            for(const auto& p : coord) {
                parent[p.first][p.second] = p;
            }
            
            table[r][c] = val;
        }
        else {
            istringstream iss(str);
            int r, c;
            iss >> r >> c;
            
            pair<int, int> p = {r, c};
            p = find(p);
            if(table[p.first][p.second].empty()) 
                answer.push_back("EMPTY");
            else 
                answer.push_back(table[p.first][p.second]);
        }
    }
    return answer;
}