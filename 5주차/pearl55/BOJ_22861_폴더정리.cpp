#include <iostream>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <sstream>

using namespace std;
using pii = pair<int, int>;
using vi = vector<int>;
int N, M, K, Q;

struct Folder{
    string fName;
    Folder * parent;
    unordered_set<Folder*> cFolders;
    unordered_set<string> cFiles;

    Folder(){
        fName = "";
        cFolders.clear();
        cFiles.clear();
    }

    Folder (string name){
        fName = name;
        parent = nullptr;
    }

    ~Folder () {
        cFolders.clear();
        unordered_set<Folder*>().swap(cFolders);

        cFiles.clear();
        unordered_set<string>().swap(cFiles);

        fName.clear();
        parent = nullptr;
    }
};

Folder *root = new Folder("main");
unordered_map<string, Folder*> DB;

void dfs(Folder *cur, unordered_map<string, int>& ret){
    for(string file: cur->cFiles) ret[file]++;
    for(Folder* nxt: cur->cFolders) dfs(nxt, ret);
}

void addFolder(string parent, string name){
    Folder *folder = nullptr;
    //포함 되지 않았을 때
    if(DB.find(name) == DB.end()){
         folder = new Folder(name);
         DB[name] = folder;
    }
    folder = DB[name];

    Folder *parentFolder = nullptr;
    if(DB.find(parent) == DB.end()){
        parentFolder = new Folder(parent);
        DB[parent] = parentFolder;
    }
    parentFolder = DB[parent];

    // 부모, 자식 사이 포인터, set 관계 연결
    folder->parent = parentFolder;
    parentFolder->cFolders.insert(folder);
}

// set에 File 추가하기
void addFile(string parent, string file) {
    Folder * parentFolder;
    if (DB.find(parent) == DB.end()) {
        parentFolder = new Folder(parent);
        DB[parent] = parentFolder;
    }
    parentFolder = DB[parent];
    parentFolder->cFiles.insert(file);
}

void mergeFolder(string st, string ed) {
    Folder* from = DB[st];
    Folder* to = DB[ed];

    to->cFiles.insert(from->cFiles.begin(), from->cFiles.end());
    to->cFolders.insert(from->cFolders.begin(), from->cFolders.end());

    from->parent->cFolders.erase(from);
    for (Folder* childFolder: from->cFolders) {
        childFolder->parent = to;
    }
    from->~Folder();
}

pii printRet(string name) {
    Folder* folder = DB[name];
    unordered_map<string, int> ret;
    dfs(folder, ret);

    int sum = 0;
    for (auto [_, count]: ret) sum += count;
    return { ret.size(), sum };
}

int main(){
    cin.tie(0)->sync_with_stdio(0);
    cin >> N >> M;
    string P, F;
    int C;

    for(int i=0; i<N+M; ++i){
        cin >> P >> F >> C;
        if(C == 1) addFolder(P, F);
        else addFile(P, F);
    }

    cin >> K;
    for(int i=0; i<K; ++i){
        string q1, q2;
        cin >> q1 >> q2;
        istringstream iss1(q1), iss2(q2);
        string buf1, buf2;

        while (getline(iss1, buf1, '/')) {}
        while (getline(iss2, buf2, '/')) {}
        mergeFolder(buf1, buf2);
    }

    cin >> Q;
    for(int i=0; i<Q; ++i){
        string q;
        cin >> q;

        istringstream iss(q);
        string buf;
        while (getline(iss, buf, '/')) {}

        pii ret = printRet(buf);
        cout << ret.first << " " << ret.second << "\n";
    }
    return 0;
}
