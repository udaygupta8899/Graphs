import java.io.*;
import java.util.*;
public class Main {
    static int n,m;
    static List<List<Integer>> adj,rev,sccGraph;
    static int[] k,component;
    static long[] sum;
    static Stack<Integer> stack = new Stack<>();
    static boolean[] vis;
    static List<Integer> topOrder;
    public static void main(String[] args) throws Exception {
        FastReader sc = new FastReader();
        // FastScanner sc = new FastScanner(System.in);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));    
        n = sc.nextInt();
        m = sc.nextInt();
        adj = new ArrayList<>();
        rev = new ArrayList<>();
        // out.write(n+" "+m+"\n");
        for(int i=0;i<=n;i++){
            adj.add(new ArrayList<>());
            rev.add(new ArrayList<>());
        }
        k = new int[n+1];
        for(int i=1;i<=n;i++){
            k[i]=sc.nextInt();
        }
        for(int i=0;i<m;i++){
            int u = sc.nextInt();
            int v = sc.nextInt();
            adj.get(u).add(v);
            rev.get(v).add(u);
        }

        // TopoSort
        vis = new boolean[n+1];
        for(int i=1;i<=n;i++){
            if(!vis[i]){
                dfs(i);
            }
        }

        // Kosaraju
        component = new int[n+1];
        sum = new long[n+1];
        Arrays.fill(component,-1);
        int comp = 1;
        while(!stack.isEmpty()){
            int node = stack.pop();
            if(component[node] == -1){
                dfs2(node,comp++);
            }
        }
        // Build edges between SCCs
        comp--;
        sccGraph = new ArrayList<>();
        for(int i=0;i<=comp;i++){
            sccGraph.add(new ArrayList<>());
        }
        Arrays.fill(vis,false);
        buildSCCGraph(comp);

        // Apply topoSort on SCC DAG
        topOrder = new ArrayList<>();
        topoSortSCC(comp);

        // DP
        long[] dp = new long[comp+1];
        for(int u=1;u<=comp;u++){
            dp[u]+=sum[u];
            for(int v:sccGraph.get(u)){
                dp[v] = Math.max(dp[v],dp[u]);
            }
        }
        long max=0;
        for(int i=1;i<=comp;i++){
            max = Math.max(max,dp[i]);
        }
        out.write(max+"\n");
        out.flush();
    }
    static void topoSortSCC(int comp){
        int[] indegree = new int[comp+1];
        for(int i=1;i<=comp;i++){
            for(int v:sccGraph.get(i)){
                indegree[v]++;
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        for(int i=1;i<=comp;i++){
            if(indegree[i] == 0){
                queue.offer(i);
            }
        }
        while(!queue.isEmpty()){
            int node = queue.poll();
            topOrder.add(node);
            for(int v:sccGraph.get(node)){
                indegree[v]--;
                if(indegree[v] == 0){
                    queue.offer(v);
                }
            }
        }
    }
    static void buildSCCGraph(int comp){
        sccGraph = new ArrayList<>();
        for(int i=0;i<=comp;i++){
            sccGraph.add(new ArrayList<>());
        }
        for(int u=1;u<=n;u++){
            for(int v:adj.get(u)){
                if(component[u] != component[v]){
                    sccGraph.get(component[u]).add(component[v]);
                }
            }
        }
    }
    static void dfs2(int node,int comp){
        component[node] = comp;
        sum[comp] += (long)k[node];
        for(int it:rev.get(node)){
            if(component[it] == -1){
                dfs2(it,comp);
            }
        }
    }
    static void dfs(int node){
        vis[node]=true;
        for(int v:adj.get(node)){
            if(!vis[v]){
                dfs(v);
            }
        }
        stack.push(node);
    }
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int bId = 0, size = 0;
        private final InputStream in;

        FastReader() { in = System.in; }

        private byte read() throws IOException {
            if (bId == size) {
                size = in.read(buffer);
                bId = 0;
                if (size == -1) return -1;
            }
            return buffer[bId++];
        }

        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            do { c = read(); } while (c <= ' ');
            if (c == '-') { sign = -1; c = read(); }
            for (; c > ' '; c = read()) val = val * 10 + c - '0';
            return val * sign;
        }

        long nextLong() throws IOException {
            int c, sign = 1;
            long val = 0;
            do { c = read(); } while (c <= ' ');
            if (c == '-') { sign = -1; c = read(); }
            for (; c > ' '; c = read()) val = val * 10 + c - '0';
            return val * sign;
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        String next() throws IOException {
            int c;
            do { c = read(); } while (c <= ' ');
            StringBuilder sb = new StringBuilder();
            for (; c > ' '; c = read()) sb.append((char)c);
            return sb.toString();
        }
        String nextLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c = read();

            if (c == '\n' || c == '\r') {
                if (c == '\r') {
                    int next = read();
                    if (next != '\n') bId--; 
                }
                return "";
            }

            while (c != -1 && c != '\n' && c != '\r') {
                sb.append((char)c);
                c = read();
            }
            if (c == '\r') {
                int next = read();
                if (next != '\n') bId--; 
            }
            return sb.toString();
        }

    }
}

class DSU{
    int[] parent;
    int[] rank;
    int[] size;
    private int maxSize;
    private int components;
    DSU(int n){
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        components=n;
        for(int i=0;i<n;i++){
            parent[i] = i;
            size[i]=1;
        }
        maxSize=1;
    }
    public int find(int node){
        if(parent[node] == node) return node;
        return parent[node] = find(parent[node]);
    }
    public void union(int x,int y){
        int parent_x = find(x);
        int parent_y = find(y);
        if(parent_x == parent_y) return;
        components--;
        if(rank[parent_x] > rank[parent_y]){
            parent[parent_y] = parent_x;
            size[parent_x]+=size[parent_y];
            maxSize = Math.max(maxSize,size[parent_x]);
        }else if(rank[parent_x] < rank[parent_y]){
            parent[parent_x] = parent_y;
            size[parent_y]+=size[parent_x];
            maxSize = Math.max(maxSize,size[parent_y]);
        }else{
            parent[parent_y] = parent_x;
            rank[parent_x]++;
            size[parent_x]+=size[parent_y];
            maxSize = Math.max(maxSize,size[parent_x]);
        }
    }
    public int getMaxSize(){
        return maxSize;
    }
    public int getComponents(){
        return components;
    }
}
