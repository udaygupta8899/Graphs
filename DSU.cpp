#include<bits/stdc++.h>
using namespace std;

class Graph{
	private:
		vector<list<pair<int,int>>> adj;
		int v;
	public:
		Graph(int v){
			this->v=v;
			adj.resize(v);
		}
		void addEdge(int src,int dest,int wt){
			adj[src].push_back({dest,wt});
			adj[dest].push_back({src,wt});
		}
		//DSU Code
		vector<int> parent;
		vector<int> rank;
		    
		int find (int x) {
			if (x == parent[x]) 
			    return x;
		    
			return parent[x] = find(parent[x]);
		  	}
		    
		    void Union (int x, int y) {
			int x_parent = find(x);
			int y_parent = find(y);
		    
			if (x_parent == y_parent) 
			    return;
		    
			if(rank[x_parent] > rank[y_parent]) {
			    parent[y_parent] = x_parent;
			} else if(rank[x_parent] < rank[y_parent]) {
			    parent[x_parent] = y_parent;
			} else {
			    parent[x_parent] = y_parent;
			    rank[y_parent]++;
			}
		 }
		    
		 void kruskals(){
		 	parent.resize(v);
		 	rank.resize(v,0);
		 	for(int i=0;i<v;i++){
		 		parent[i]=i;
		 	}
		 	vector<vector<int>> vec;
		 	for(int i=0;i<v;i++){
		 		for(auto temp:adj[i]){
		 			int u=i;
		 			int d=temp.first;
		 			int wt=temp.second;
		 			vec.push_back({i,d,wt});
		 		}
		 	}
		 	auto lambda=[&](auto &v1,auto &v2){
		 		return v1[2]<v2[2];
		 	};
		 	sort(begin(vec),end(vec),lambda);
		 	int sum=0;
		 	for(auto &temp:vec){
		 		int u=temp[0];
		 		int d=temp[1];
		 		int wt=temp[2];
		 		
		 		int parent_u=find(u);
		 		int parent_d=find(d);
		 		
		 		if(parent_u!=parent_d){
		 			Union(u,d);
		 			sum+=wt;
		 		}
		 	}
		 	cout<<"Sum: "<<sum<<endl;
		 }
};

int main(){
	int v;
	ifstream file("inputMST.txt");
	file>>v;
	Graph grp(v);
	int src,dest,wt;
	while(file>>src>>dest>>wt){
		grp.addEdge(src,dest,wt);
	}
	grp.kruskals();
	return 0;
}
