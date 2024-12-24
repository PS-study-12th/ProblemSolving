#include <iostream>
#include <map>
#include <set>
using namespace std;
using ll = long long;
int N;
string comm, arg;

// 1. 앨범 상태를 표현할 앨범 구조체
struct Album {
    Album* parent;
    string name; // 앨범 이름
    map<string, Album*> albs; // 하위 앨범 패키지 관리
    set<string> pics; // 하위 소속 사진들 관리
    int albSize;
    int picSize;

    Album(string name, Album* parent)
        : parent(parent), name(name), albSize(0), picSize(0) {}
};

// alb 관련 명령어
void mkalb(Album *cur, const string& arg) {
    auto pos = cur->albs.lower_bound(arg);

    if (pos != cur->albs.end() && pos->first == arg) {
        cout << "duplicated album name\n";
    } else {
        cur->albs.emplace_hint(pos, arg, new Album(arg, cur));
        while (cur != nullptr) {
            cur->albSize++; // 현재 앨범 사이즈 업데이트
            cur = cur->parent;
        }
    }
}

void rmalb(Album *cur, const string& arg) {
    if (cur->albs.empty()) {
        cout << "0 0\n";
        return;
    }

    int albCnt = 0, picCnt = 0;

    if (arg == "-1") {
        auto pos = cur->albs.begin();
        albCnt = pos->second->albSize + 1;
        picCnt = pos->second->picSize;
        cur->albs.erase(pos);
    }
    else if (arg == "0") {
        albCnt = cur->albSize;
        picCnt = cur->picSize - cur->pics.size();
        cur->albs.clear();
    }
    else if (arg == "1") {
        auto pos = --(cur->albs.end());
        albCnt = pos->second->albSize + 1;
        picCnt = pos->second->picSize;
        cur->albs.erase(pos);
    }
    else {
        auto pos = cur->albs.find(arg);
        if (pos == cur->albs.end()) {
            cout << "0 0\n";
            return;
        }
        albCnt = pos->second->albSize + 1;
        picCnt = pos->second->picSize;
        cur->albs.erase(pos);
    }

    cout << albCnt << " " << picCnt << "\n";

    while (cur != nullptr) { // 앨범, 사진 size update
        cur->albSize -= albCnt;
        cur->picSize -= picCnt;
        cur = cur->parent;
    }
}

Album* ca(Album* cur, Album* root, const string& arg) {
    if (arg == ".." && cur != root) { // 상위 앨범 이동
        return cur->parent;
    }
    else if (arg == "/") { // 최상위 앨범 이동
        return root;
    }
    else { // 하위 앨범 이동
        auto pos = cur->albs.find(arg);
        if (pos != cur->albs.end()) {
            return pos->second;
        }
    }
    return cur; // 이동 실패 시 현재 위치
}

// pics 관련 명령어
void insert(Album *cur, const string& arg) {
    auto pos = cur->pics.lower_bound(arg);

    if (pos != cur->pics.end() && *pos == arg) {
        cout << "duplicated photo name\n";
    } else {
        cur->pics.emplace_hint(pos, arg);
        while (cur != nullptr) { // 사진 사이즈 업데이트
            cur->picSize++;
            cur = cur->parent;
        }
    }
}

void del(Album *cur, const string& arg) {
    if (cur->pics.empty()) {
        cout << "0\n";
        return;
    }

    int picCnt = 0;
    if (arg == "-1") {
        picCnt = 1;
        cur->pics.erase(cur->pics.begin());
    }
    else if (arg == "0") {
        picCnt = cur->pics.size();
        cur->pics.clear();
    }
    else if (arg == "1") {
        picCnt = 1;
        cur->pics.erase(--(cur->pics.end()));
    }
    else {
        auto pos = cur->pics.find(arg);
        if (pos == cur->pics.end()) {
            cout << "0\n";
            return;
        }
        picCnt = 1;
        cur->pics.erase(pos);
    }

    cout << picCnt << "\n";
    while (cur != nullptr) {
        cur->picSize -= picCnt;
        cur = cur->parent;
    }
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N;

    Album* root = new Album("album", 0); // 최상위 앨범 생성
    Album* cur = root; // current 앨범

    while (N--) {
        cin >> comm >> arg;
        if (comm == "mkalb") mkalb(cur, arg);
        else if (comm == "rmalb") rmalb(cur, arg);
        else if (comm == "insert") insert(cur, arg);
        else if (comm == "delete") del(cur, arg);
        else if (comm == "ca") {
            cur = ca(cur, root, arg);  // 앨범 이동 후 cur 갱신
            cout << cur->name << "\n";  // 현재 앨범 출력
        }
    }
    return 0;
}
