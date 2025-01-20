#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using ll = long long;
using pii = pair<int, int>;
int N;

struct TrieNode{
    int children[26];
    bool finished;

    // 생성자 초기화
    TrieNode(){
        for(int i=0; i<26; ++i) children[i] = -1;
        finished = false;
    }
};

vector<TrieNode> trie;

// 초기화 함수
int init(){
    TrieNode root;
    trie.push_back(root);
    return (int)trie.size()-1;
}

// 사전에 단어 추가
void add(int idx, int pos, const string &str){
    // 현재 인덱스와 문자열 사이즈가 같으면 return
    if(idx == str.size()){
        trie[pos].finished = true;
        return;
    }

    int alpIdx = str[idx] -'a';

    // 만약, 다음 알파벳 자리에 연결된 트라이 노드가 없을 경우 생성
    if(trie[pos].children[alpIdx] == -1){
        int nxtNode = init();
        trie[pos].children[alpIdx] = nxtNode;
    }

    int childNode = trie[pos].children[alpIdx];
    add(idx+1, childNode, str);
}

// trie 트리를 전체 순회하면서 typeCnt와 wordCnt 체크
pii traversal(int pos){
    int typeCnt = 0;
    int wordCnt = 0;
    int nxtChildCnt = 0;

    for(int i=0; i<26; ++i){
        int child = trie[pos].children[i];

        if(child != -1){
            nxtChildCnt++;
            auto [type, word] = traversal(child);
            typeCnt += type;
            wordCnt += word;
        }
    }

    // 만약 다음으로 갈 수 있는 선택지가 없거나, child가 하나만 있으면 자동 타이핑 되는데,
    // 아래 조건문에서는 자동 타이핑이 안되는 경우 wordCnt를 추가해준다.
    if (pos == 0 || !(nxtChildCnt == 0 ||
    (nxtChildCnt == 1 && !trie[pos].finished))) typeCnt += wordCnt;

    // finished가 켜져있으면 wordCnt 무조건 1개 추가
    if(trie[pos].finished) wordCnt++;

    return make_pair(typeCnt, wordCnt);
}

int main(){
    cin.tie(0)->sync_with_stdio(0);
    cout.setf(ios::fixed);
    cout.precision(2);

    while(cin >> N){
        trie.clear();
        int root = init();
        for(int i=0; i<N; ++i){
            string word;
            cin >> word;
            add(0, root, word);
        }
        cout << (double)traversal(0).first/N << "\n";
    }
    return 0;
}
