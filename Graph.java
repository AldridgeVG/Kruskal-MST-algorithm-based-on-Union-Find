package KMA;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Graph {

    int V, E;   //number of vertex and edge


    //provide Comparable interface to make class Graph comparable
    class Edge implements Comparable<Edge> {
        int u, v, weight;   //vertex u,v and weight of edge

        public int compareTo(Edge compareEdge) {
            return this.weight - compareEdge.weight;
        }
    }

    Edge[] edge;//edge set

    //subset[i] = i means i is root; subset[i] = j means j is parent of i(and rank j = 1)
    class subset {
        int parent, rank;
    }

    //construction function
    Graph(int v, int e) {
        V = v;
        E = e;
        edge = new Edge[E];
        int i;
        for (i = 0; i < e; i++) {
            edge[i] = new Edge();
        }
    }

    //find in which set an element are,(return set root)
    int find(subset subsets[], int i) {
        //until self is parent -- find root of this set
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    //unite trees, attach smaller trees to the bigger ones
    void union(subset subsets[], int x, int y) {
        int root_of_x = find(subsets, x);
        int root_of_y = find(subsets, y);

        //union by rank, attach smaller rank tree to root of higher rank tree
        if (subsets[root_of_x].rank < subsets[root_of_y].rank) {
            subsets[root_of_x].parent = root_of_y;
        } else if (subsets[root_of_x].rank > subsets[root_of_y].rank) {
            subsets[root_of_y].parent = root_of_x;
        }
        //if 2 tree have the same rank, attach x to y, vice versa
        else {
            subsets[root_of_y].parent = root_of_x;
            subsets[root_of_x].rank++;
        }
    }

    //Kruskal function member
    void Kruskal() {
        Edge[] solution = new Edge[V];  //initiate solution set
        int i = 0;
        for (i = 0; i < V; ++i) {
            solution[i] = new Edge();
        }

        //step1:sort all edge in non-decreasing order by weight
        //
        Arrays.sort(edge);

        subset[] sub = new subset[V];
        for (i = 0; i < V; ++i) {
            sub[i] = new subset();
        }
        for (i = 0; i < V; ++i) {
            sub[i].parent = i;  //initiate each subset contain only one vertex itself
            sub[i].rank = 0;  //initiate each one element subset rank as 0
        }

        //step2:pick smallest weight edge and add its index for next iteration
        //
        int nextedge = 0;
        int e = 0;

        //only need V-1 edge to form MST
        while (e < V - 1) {
            Edge next = new Edge();
            next = edge[nextedge];
            nextedge++;
            int x = find(sub, next.u);
            int y = find(sub, next.v);
            //
            //--TEST INFO--System.out.println("in edge" + nextedge + " u,v belongs to subsets " + x + " " + y);
            //
            //x != y means next.u and next.v doesn't belong to same subset, add next doesn't form cycle
            if (x != y) {
                solution[e++] = next;   //add next to solution edge set
                union(sub, x, y);         //connect x,y to form a bigger subset
                //
                //--TEST INFO--System.out.println("union success");
                //
            }
            //if x=y->form a cycle, e doesn't ++ to discard this edge
        }

        System.out.println("solution Edge set contains:");
        for (i = 0; i < e; ++i) {
            System.out.println(solution[i].u + "----" + solution[i].v + "   weight: " + solution[i].weight);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int c;

        System.out.println("inupt an integer to start the programme, 0 or invalid input to quit");
        c = in.nextInt();
        while (c != 0) {
            //input number of vertex and edges
            System.out.println("Input Vertex number:");
            int V = in.nextInt();
            System.out.println("V = " + V);
            System.out.println();
            System.out.println("Input Edge number:");
            int E = in.nextInt();
            System.out.println("E = " + E);
            System.out.println();

            //initiate graph
            Graph graph = new Graph(V, E);

            //input info of edges
            int i;
            for (i = 0; i < E; i++) {
                System.out.println("vertex no1 of edge" + i + ": ");
                graph.edge[i].u = in.nextInt();
                System.out.println("vertex no2 of edge" + i + ": ");
                graph.edge[i].v = in.nextInt();
                System.out.println("weight of edge" + i + ": ");
                graph.edge[i].weight = in.nextInt();
                System.out.println("confirm edge" + i + " : " + graph.edge[i].u + "----" + graph.edge[i].v + "   weight: " + graph.edge[i].weight);
            }

            //MST
            graph.Kruskal();
            System.out.println();
            System.out.println("inupt an integer to restart the programme, 0 or invalid input to quit");
            c = in.nextInt();
        }
    }
}
