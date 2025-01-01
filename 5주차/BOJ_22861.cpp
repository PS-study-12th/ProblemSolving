#include <iostream>
#include <string>
#include <set>
#include <map>
#include <sstream>
using namespace std;

struct Folder {
	set<Folder*> subfolders;  // 하위 폴더들
	set<string> files;        // 파일들
	string name;
	Folder(string name) : name(name) {}
};

map<string, Folder*> folderMap; // 폴더 이름 -> 폴더 객체 맵

// 폴더 내 파일 종류 및 갯수 세기
void dfs(Folder* folder, set<string>& ans, int& cnt) {
	// 파일 갯수 카운트
	cnt += folder->files.size();

	// 파일 목록 ans에 추가 (중복을 자동으로 처리)
	for (const string& file : folder->files) {
		ans.insert(file);  // 중복을 피하기 위해 set에 삽입
	}
	if (folder->subfolders.size() == 0)return;
	// 하위 폴더 탐색
	for (Folder* subfolder : folder->subfolders) {
		dfs(subfolder, ans, cnt);
	}
}

int main() {
	// main 폴더 생성
	Folder* mains = new Folder("main");
	folderMap["main"] = mains;

	int n, m;
	cin >> n >> m;  // n: main 하위 폴더 개수, m: 파일 수

	string p, f;
	int c;
	// 폴더 및 파일 정보 입력
	for (int i = 0; i < n + m; i++) {
		cin >> p >> f >> c;  // 상위 폴더, 폴더 또는 파일, 폴더인지 파일인지

		if (c == 1) {  // 폴더
			// 상위 폴더가 아직 생성되지 않았으면 생성
			if (folderMap.find(p) == folderMap.end()) {
				folderMap[p] = new Folder(p);
			}

			// 하위 폴더 생성
			Folder* child = new Folder(f);
			folderMap[f] = child;  // 하위 폴더 맵에 추가
			folderMap[p]->subfolders.insert(child);  // 상위 폴더에 하위 폴더 추가
		}

		if (c == 0) {  // 파일
			// 파일을 상위 폴더에 추가
			if (folderMap.find(p) == folderMap.end()) {
				folderMap[p] = new Folder(p);
			}
			folderMap[p]->files.insert(f);
		}
	}
	//이동하는 폴더 
	int move;
	cin >> move;
	string a, b;
	for (int i = 0; i < move; i++) {
		cin >> a >> b; 
		//a경로에 있는것을 b경로에 이동시킴 
		stringstream sa(a);
		string segmenta;
		string currenta;
		while (getline(sa, segmenta, '/')) {
			// 해당 하위 폴더 찾기
			currenta = segmenta;
		}
		Folder* currentFoldera = folderMap[currenta];
		stringstream sb(b);
		string segmentb;
		string currentb;
		while (getline(sb, segmentb, '/')) {
			// 해당 하위 폴더 찾기
			currentb = segmentb;
		}
		Folder* currentFolderb = folderMap[currentb];

		//옮기기 
		//currentfolderb에 파일, 폴더 추가하고 
		for (auto f : currentFoldera->subfolders) {
			currentFolderb->subfolders.insert(f);
		}
		for (auto f : currentFoldera->files) {
			currentFolderb->files.insert(f);
		}

		//currentfoldera 지우기 
		currentFoldera->subfolders.clear();
		currentFoldera->files.clear();
	}



	int q;
	cin >> q;  // 쿼리 수

	// 각 쿼리에 대해 폴더 경로를 파싱하고, 파일 종류와 갯수 출력
	for (int i = 0; i < q; i++) {
		string query;
		cin >> query;

		// 경로를 '/'로 나누기 위해 stringstream 사용
		stringstream ss(query);
		string segment;
		string current;
		
		//맨 마지막 폴더 경로로 탐색
		while (getline(ss, segment, '/')) {
			// 해당 하위 폴더 찾기
			current = segment;
		}
		Folder* currentFolder = folderMap[current]; 
		// 현재 폴더에 있는 파일 종류와 갯수 세기
		set<string> ans;
		int cnt = 0;
		//cout << currentFolder->name << "탐색중\n";
		dfs(currentFolder, ans, cnt);

		// 파일 종류의 갯수와 파일 갯수 출력
		cout << ans.size() << " " << cnt << "\n";
	}

	return 0;
}
