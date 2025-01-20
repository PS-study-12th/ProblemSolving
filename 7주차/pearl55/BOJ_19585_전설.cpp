#include <iostream>
#include <vector>
#include <unordered_set>
using namespace std;

struct TrieNode{
    int children[26];
    bool finished;

    TrieNode(){
        for(int i=0; i<26; ++i) children[i] = -1;
        finished = false;
    }
};

int C, N, Q;
vector<TrieNode> trie;
unordered_set<string> names;

int init(){
    TrieNode root;
    trie.push_back(root);
    return (int)trie.size()-1;
}

void add(int idx, int pos, const string &str){
    if(idx == str.size()){
        trie[pos].finished = true;
        return;
    }
    int alpIdx = str[idx] - 'a';

    if(trie[pos].children[alpIdx] == -1){
        int nxtNode = init();
        trie[pos].children[alpIdx] = nxtNode;
    }

    int child = trie[pos].children[alpIdx];
    add(idx+1, child, str);
}

string solve(const string &team){
    int len = team.size();
    int pos = 0;

    for(int i=0; i<len; ++i) {
        int alpIdx = team[i] - 'a';
        if(trie[pos].children[alpIdx] == -1) return "No";
        pos = trie[pos].children[alpIdx];

        if(trie[pos].finished) {
            string tmp = team.substr(i+1);
            if(names.find(tmp) != names.end()) return "Yes";
        }
    }
    return "No";
}

int main(void){
    cin.tie(0)->sync_with_stdio(0);
    cin >> C >> N;

    int root = init();
    for(int i=0; i<C; ++i){
        string color;
        cin >> color;
        add(0, root, color);
    }

    for(int i=0; i<N; ++i){
        string name;
        cin >> name;
        names.insert(name);
    }

    cin >> Q;
    for(int i=0; i<Q; ++i){
        string team;
        cin >> team;
        cout << solve(team) << "\n";
    }
    return 0;
}
