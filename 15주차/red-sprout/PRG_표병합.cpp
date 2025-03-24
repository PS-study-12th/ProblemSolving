#include <string>
#include <vector>
#include <sstream>
using namespace std;

int parent[2501];
string val[50][50];

int hashing(int r, int c) {
    return r * 50 + c;
}

int find_parent(int x) {
    if (parent[x] == x) return x;
    return parent[x] = find_parent(parent[x]);
}

void merge_parent(int x, int y) {
    x = find_parent(x);
    y = find_parent(y);
    if (x == y) return;
    if (val[x / 50][x % 50] == "") {
        val[x / 50][x % 50] = val[y / 50][y % 50];
    }
    parent[y] = x;
}

vector<string> solution(vector<string> commands) {
    vector<string> answer;
    int len = commands.size();
    for (int i = 0; i < 2501; i++) {
        parent[i] = i;
    }
    
    for (int i = 0; i < len; ++i) {
        int pos = commands[i].find(' ');
        string cmd = commands[i].substr(0, pos);
        string str = commands[i].substr(pos + 1);
        
        if (cmd == "UPDATE") {
            int cnt = 0;
            for (char c : str) {
                if (c == ' ') cnt++;
            }
            istringstream iss(str);
            if (cnt == 2) {
                int r, c;
                string value;
                iss >> r >> c >> value;
                int pos = hashing(--r, --c);
                pos = find_parent(pos);
                val[pos / 50][pos % 50] = value;
            } else {
                string value1, value2;
                iss >> value1 >> value2;
                for (int i = 0; i < 50; ++i) {
                    for (int j = 0; j < 50; ++j) {
                        int pos = hashing(i, j);
                        pos = find_parent(pos);
                        if (val[pos / 50][pos % 50] == value1) {
                            val[pos / 50][pos % 50] = value2;
                        }
                    }
                }
            }
        } else if (cmd == "MERGE") {
            istringstream iss(str);
            int r1, c1, r2, c2;
            iss >> r1 >> c1 >> r2 >> c2;
            int p1 = hashing(--r1, --c1);
            int p2 = hashing(--r2, --c2);
            merge_parent(p1, p2);
        } else if (cmd == "UNMERGE") {
            istringstream iss(str);
            int r, c;
            iss >> r >> c;
            int pos = hashing(--r, --c);
            int root = find_parent(pos);
            string saved_val = val[root / 50][root % 50];
            vector<int> coords;
            for (int i = 0; i < 50; ++i) {
                for (int j = 0; j < 50; ++j) {
                    int cur = hashing(i, j);
                    if (find_parent(cur) == root) {
                        coords.push_back(cur);
                        val[i][j] = "";
                    }
                }
            }
            for (int p : coords) {
                parent[p] = p;
            }
            val[r][c] = saved_val;
        } else {
            istringstream iss(str);
            int r, c;
            iss >> r >> c;
            r--; c--;
            int pos = hashing(r, c);
            pos = find_parent(pos);
            string result = val[pos / 50][pos % 50];
            if (result == "") result = "EMPTY";
            answer.push_back(result);
        }
    }
    return answer;
}
