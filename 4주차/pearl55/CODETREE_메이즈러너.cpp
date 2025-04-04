#include <bits/stdc++.h>
using namespace std;
using ll = long long;
using pii = pair<int, int>;
using pll = pair<ll, ll>;
using tiii = tuple<int, int, int>;

const int INF=1e9+1;
int n,m,k;
int dx[4]={1,-1,0,0};
int dy[4]={0,0,1,-1};

struct runner {
    int x, y; // 위치
    bool finished; // 탈출 여부
};

// 전체 러너 탈출 체크
bool isfinished(vector<runner>& runners) {
    for(auto it : runners) {
        if(!it.finished) return false;
    }
    return true;
}

// 가장 작은 사각형 범위를 찾는 함수
// tuple 리턴 값 (square start x, square start y, size)
tiii findSquare(vector<vector<int>>& board, vector<runner>& runners, pii exit) {
    int size = 1;
    vector<vector<int>> tmp(board.size(),vector<int>(board.size()));
    for(auto r : runners) {
        if(r.finished) continue;
        tmp[r.x][r.y]=1;
    }
    
    while((++size) < board.size()) {
        for(int r=1; r<=board.size()-size; r++) {
            for(int c=1; c<=board.size()-size; c++) {
                // 러너가 포함되었는지, 탈출구가 포함되었는지
                bool findRunner = false;
                bool findExit = false;
                
                for(int x=r; x<r+size; x++) {
                    for(int y=c; y<c+size; y++) {
                        if(x==exit.first && y==exit.second) findExit=1;
                        if(tmp[x][y]) findRunner=1;
                    }
                }
                if(findRunner && findExit) return {r,c,size};
            }
        }
    }
    return {0,0,0};
}

// 회전 함수
void rotate(vector<vector<int>>& board, vector<runner>& runners, pii& exit) {
    auto [startx,starty,size] = findSquare(board,runners,exit);
    
    vector<vector<int>> tmp(size, vector<int>(size));
    map<pii,pii> rot;
    for(int i=0;i<size;i++) {
        for(int j=0;j<size;j++) {
            rot[{startx+size-j-1,starty+i}]={startx+i,starty+j};
            tmp[i][j]=board[startx+size-j-1][starty+i];
        }
    }
    
    // 범위 내에 있는 러너들과 탈출구를 회전시킨다
    for(auto& [x,y,g] : runners) {
        if(!g && rot.find({x,y})!=rot.end()) {
            pii rotated=rot[{x,y}];
            x=rotated.first, y=rotated.second;
        }
    }
    exit = rot[exit];
    // board를 회전시키고 벽의 내구도를 1 감소시킨다
    for(int i=0;i<size;i++) {
        for(int j=0;j<size;j++) board[startx+i][starty+j]=tmp[i][j]>0 ? tmp[i][j]-1 : tmp[i][j];
    }
    return;
}

int move(vector<vector<int>>& board, vector<runner>& runners, pii exit) {
    // 러너들이 움직이는 함수
    int dist=0;
    for(auto& r : runners) {
        if(r.finished) continue;
        for(int i=0;i<4;i++) {
            int nx=r.x+dx[i];
            int ny=r.y+dy[i];
            
            if(nx>=1 && nx<board.size() && ny>=1 && ny<board.size() && !board[nx][ny]) {
                if(exit.first==nx && exit.second==ny) {
                    r.finished = true;
                    dist++;
                    break;
                }
                if(abs(exit.first-nx)+abs(exit.second-ny)==abs(exit.first-r.x)+abs(exit.second-r.y)-1) {
                    // 거리가 1작은 경우 움직인다
                    r.x=nx;
                    r.y=ny;
                    dist++;
                    break;
                }
            }
        }
    }
    return dist;
}


int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin>>n>>m>>k;
    
    vector<vector<int>> board(n+1,vector<int>(n+1));
    for(int i=1;i<=n;i++) {
        for(int j=1;j<=n;j++) cin>>board[i][j];
    }
    
    // runners 담은 배열 초기화
    vector<runner> runners(m);
    for(auto& it : runners) {
        cin >> it.x >> it.y;
        it.finished = false;
    }
    
    pii exit; cin>>exit.first>>exit.second;
    int totalDistance=0;
    
    while(k--) {
        totalDistance+=move(board,runners,exit);
        if(isfinished(runners)) break;
        rotate(board,runners,exit);
    }
    
    cout << totalDistance << endl;
    cout << exit.first << ' ' << exit.second << "\n";
    return 0;
}
