#include <iostream>
#include <set>
#include <map>
#include <string>

using namespace std;

const int MAX_MEMORY = 100000; // 최대 메모리 크기
typedef pair<int, int> ci;


set<ci> memories;  // {시작주소, 크기}
map<string, ci>arr;  // 변수명 -> 마지막으로 할당된 (시작주소, 크기)

void merge() {

    auto it = memories.begin();
    while (it != memories.end()) {
        auto nextIdx = next(it);
        auto curit = it;
        //현재주소+할당량이 다음 블록의 첫주소와 같다면 
        if (nextIdx != memories.end() && it->first + it->second == nextIdx->first) {
            // 두 블록이 인접하면 병합

            int na = it->first;
            int ns = it->second + nextIdx->second;
            //병합된거 추가 
            memories.insert({ na,ns });
            it = curit;
            //둘다 삭제 
            memories.erase(it);
            memories.erase(nextIdx);
            it = memories.find({ na, ns }); //it=curit 하면 왜 안됨?
             
            continue;  //현재 병합한 블록 처음 위치부터 다시 탐색

        }
        else { //다르면 그다음거 확인 
            it++;
        }
    }
}
      
  
int malloc(int size) {
        // 할당할 메모리 크기를 맞는 공간을 찾아서 할당
        int startAddress = 1;

        //빈공간이 있는 memories 내 블록들을 맨처음부터 모두 탐색 
        for (auto it = memories.begin(); it != memories.end(); it++) {
            //memories: {시작,할당가능량) 
            //할당 가능 
            if (it->second >= size) {
                startAddress = it->first;

                //정확히 같은 크기로 할당가능하면 
                if (it->second == size) {
                    memories.erase(it);  //일단 삭제
                }
                else { //메모리가 남는경우 
                    int na = it->first + size;
                    int ns = it->second - size;
                    memories.erase(it);  // 기존 블록을 삭제
                    memories.insert({ na,ns });  // 남은 공간을 다시 삽입
                    //메모리를 쓰느거니까 여기서는 병합할 일이 안생김 
                }
                return startAddress;
          
            }
        }
        // 메모리 할당 불가 
        return 0;
}

void free(string var) {
        // 해당 변수에 할당된 메모리를 해제
        

        int startAddress = arr[var].first;
        int size = arr[var].second;
        //변수에 시작값,메모리 없애기(초기화) 
        arr.erase(var);
        //쓸수 있는 공간 추가해주기 
        memories.insert({ startAddress , size });


        //서로 맞닿아서 연결되는 부분 합치기 
        merge();
}



int main() {
    
    ios::sync_with_stdio(false);
    cin.tie(0);

    int n;
    cin >> n;
    

    memories.insert({ 1, MAX_MEMORY });
   
    string cmd;

    while (n--) {
        cin >> cmd;

        
        if (cmd.find("malloc") != string::npos) {// malloc(size) 처리
            
            string var="";
            //변수는 4자리 
            for (int i = 0; i < 4; i++) {
                var += cmd[i]; 
            }
            int size;
            auto fidx = cmd.find('(');
            auto eidx = cmd.find(')');
            size =stoi(cmd.substr(fidx + 1, eidx - fidx - 1));
         
            int address = malloc(size);


            if (address != 0) { //할당 가능, 변수에 시작 주소 저장하기 
                arr[var] = { address, size };
            }
            else { //메모리 공간이 부족해서 할당 안됨 
                arr[var] = { 0, 0 };  //이전에 할당된건? 
            }
        }
        else if (cmd.find("free") != string::npos) { // free(var) 처리
            string var;
            size_t equal_pos = cmd.find('(');
            size_t end_pos = cmd.find(')');
            var = cmd.substr(equal_pos + 1, end_pos - equal_pos - 1);

            free(var);
        }
        else if (cmd.find("print") != string::npos) { // print(var) 처리
            string var="";
            for (int i = 6; i < 10; i++) {
                var += cmd[i];
            }

            cout << arr[var].first<<"\n";
        }
    }

    return 0;
}
