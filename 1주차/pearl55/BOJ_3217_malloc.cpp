#include <iostream>
#include <string>
#include <unordered_map>
#include <set>
using namespace std;
int N;
unordered_map<string, pair<int, int>> varMap;  // 변수 이름, {메모리 시작 위치, 크기}
set<pair<int, int>> freeSgmt = {{1, 100000}};  // 빈 구간 초기화

// malloc: 메모리 할당
int malloc(int size) {
    for (auto it = freeSgmt.begin(); it != freeSgmt.end(); ++it) {
        int start = it->first;
        int end = it->second;
        int availableSize = end - start + 1;

        // 빈 구간에서 할당할 구간 분리
        if (availableSize >= size) {
            freeSgmt.erase(it);

            if (availableSize > size) freeSgmt.insert({start + size, end});
            return start; // 시작 위치 리턴
        }
    }
    return 0; // 할당 실패
}

// free: 메모리 해제
void free(int start, int size) {
    int end = start + size - 1;
    
    // 해제 후 구간 원복 (이전 구간 및 다음 구간과 병합)
    auto it = freeSgmt.lower_bound({start, end});
    if (it != freeSgmt.begin()) {
        auto prev_ = prev(it);
        if (prev_->second + 1 == start) {  // 이전 구간과 병합
            start = prev_->first;
            freeSgmt.erase(prev_);
        }
    }
    if (it != freeSgmt.end() && it->first == end + 1) {  // 다음 구간과 병합
        end = it->second;
        freeSgmt.erase(it);
    }
    freeSgmt.insert({start, end});
}

// 명령어 조건 분기
void checkCommand(string comm) {
    if (comm.find("malloc") != -1) { // malloc 호출 후 변수 정보 추가
        string varName = comm.substr(0, comm.find('='));
        int sizeSt = comm.find('(') + 1;
        int sizeEd= comm.find(')');
        int size = stoi(comm.substr(sizeSt, sizeEd - sizeSt));
        int allocSt = malloc(size);

        if (allocSt > 0) {
            varMap[varName] = {allocSt, size};
        } else {
            varMap[varName] = {0, 0};  // 할당 실패 시 0 초기화
        }

    } else if (comm.find("free") != -1) { // free 호출 후 변수 정보 제거
        string varName = comm.substr(comm.find('(') + 1, 4);

        if (varMap.find(varName) != varMap.end() && varMap[varName].first > 0) {
            free(varMap[varName].first, varMap[varName].second);
            varMap.erase(varName);
        }

    } else if (comm.find("print") != -1) {
        string varName = comm.substr(comm.find('(') + 1, 4);
        if (varMap.find(varName) != varMap.end()) {
            cout << varMap[varName].first << '\n';
        } else {
            cout << "0\n";
        }
    }
}

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin >> N;

    string command;
    for (int i = 0; i < N; i++) {
        cin >> command;
        checkCommand(command);
    }

    return 0;
}
