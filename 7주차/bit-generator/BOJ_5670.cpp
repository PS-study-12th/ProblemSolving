#include <iostream>
#include <string>
#include <cstring>
using namespace std;

constexpr size_t MAX_SIZE = 1'000'005;
struct TrieNode {
    bool is_terminal;
    TrieNode* child[26];
    int cnt[26];

    TrieNode() {
        is_terminal = false;
        memset(child, 0, sizeof(TrieNode*) * 26);
        memset(cnt, 0, sizeof(int) * 26);
    }

    ~TrieNode() {
        for (int i = 0; i < 26; ++i) {
            if (child[i] != nullptr) {
                delete child[i];
            }
        }
    }
};
TrieNode* root;
int N;
string str[100000];

void insert(string& s) {
    TrieNode* cur = root;

    for (char c : s) {
        if (cur->child[c - 'a'] == nullptr) {
            cur->child[c - 'a'] = new TrieNode;
        }

        ++cur->cnt[c - 'a'];
        cur = cur->child[c - 'a'];
    }

    cur->is_terminal = true;
}

int find(string& s) {
    TrieNode* cur = root->child[s[0] - 'a'];
    int cnt = root->cnt[s[0] - 'a'];
    int typeCnt = 1;

    for (int i = 1; i < s.length(); ++i) {
        if (cnt != cur->cnt[s[i] - 'a']) {
            ++typeCnt;
            cnt = cur->cnt[s[i] - 'a'];
        }

        cur = cur->child[s[i] - 'a'];
    }

    return typeCnt;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    while (true) {
        cin >> N;
        if (cin.eof()) break;

        root = new TrieNode;
        for (int i = 0; i < N; ++i) {
            cin >> str[i];
            insert(str[i]);
        }

        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += find(str[i]);
        }

        cout << fixed;
        cout.precision(2);
        cout << (double)sum / N << '\n';
        delete root;
    }

    return 0;
}