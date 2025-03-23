#include <iostream>
#include <vector>
#include <string>
#include <unordered_set>
using namespace std;

struct Node {
    Node* next[26] = {nullptr};
    bool isEnd = false;
};

class Trie {
private:
    Node* root;

public:
    Trie() {
        root = new Node();
    }

    void insert(const string& word) {
        Node* cur = root;
        for (char c : word) {
            if (cur->next[c - 'a'] == nullptr) {
                cur->next[c - 'a'] = new Node();
            }
            cur = cur->next[c - 'a'];
        }
        cur->isEnd = true;
    }

    bool find(const string& word, const unordered_set<long long>& back) {
        Node* cur = root;
        for (size_t i = 0; i < word.length(); ++i) {
            char c = word[i];
            if (cur->next[c - 'a'] == nullptr) {
                return false;
            }
            cur = cur->next[c - 'a'];
            if (cur->isEnd && back.count(hashing(word, i + 1))) {
                return true;
            }
        }
        return false;
    }

    static long long hashing(const string& word, int start) {
        long long hash = 0;
        const int p = 31;
        for (size_t i = start; i < word.length(); ++i) {
            hash = hash * p + word[i];
        }
        return hash;
    }
};

int c, n;
Trie front;
unordered_set<long long> back;

void init() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);

    cin >> c >> n;
    for (int i = 0; i < c; ++i) {
        string word;
        cin >> word;
        front.insert(word);
    }

    for (int i = 0; i < n; ++i) {
        string word;
        cin >> word;
        back.insert(Trie::hashing(word, 0));
    }
}

void solution() {
    int q;
    cin >> q;
    string word;
    while (q-- > 0) {
        cin >> word;
        cout << (front.find(word, back) ? "Yes" : "No") << '\n';
    }
}

int main() {
    init();
    solution();
    return 0;
}
