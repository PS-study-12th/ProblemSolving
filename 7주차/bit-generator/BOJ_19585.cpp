#include <iostream>
#include <string>
#include <cstring>
#include <unordered_map>
#include <unordered_set>
using namespace std;

int C, N, Q;
string str;

struct TrieNode {
    bool is_terminal;
    unordered_map<char, TrieNode*> child;
    
    TrieNode() {
        is_terminal = false;
    }
};

TrieNode* root;
unordered_set<string> nickname;

void insert(string& s) {
    TrieNode* cur = root;
    
    for(char c : s) {
        if(cur->child.find(c) == cur->child.end()) {
            cur->child[c] = new TrieNode;
        }
        
        cur = cur->child[c];
    }
    
    cur->is_terminal = true;
}

bool find(string& s) {
    TrieNode* cur = root;
    
    for(int i = 0; i < (int)s.length(); ++i) {
        if(cur->is_terminal) {
            if(nickname.find(s.substr(i)) != nickname.end()) {
                return true;
            }
        }
        
        if(cur->child.find(s[i]) == cur->child.end()) {
            return false;
        }
        
        cur = cur->child[s[i]];
    }
    
    return false;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> C >> N;
    root = new TrieNode;
    
    while(C--) {
        cin >> str;
        insert(str);
    }
    
    while(N--) {
        cin >> str;
        nickname.insert(str);
    }
    
    cin >> Q;
    while(Q--) {
        cin >> str;
        if(find(str)) cout << "Yes\n";
        else cout << "No\n";
    }
    
    return 0;
}