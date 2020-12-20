package Tests.api;

import api.*;
import gameClient.util.Point3D;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class Ex2_Test {
    @Test
    @DisplayName("check empty graph")
    void test0() throws Exception{
        directed_weighted_graph g = new DWGraph_DS();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        int n = g.nodeSize();
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        assertEquals(0,n,"empty graph node size");
        assertEquals(0,e,"empty graph edge size");
        assertEquals(true,c,"empty graph connection");
    }
    @Test
    @DisplayName("check one graph")
    void test1() throws Exception {
        directed_weighted_graph g = new DWGraph_DS();
        node_data A = new DWGraph_DS.Node_Data();
        g.addNode(A);
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        int n = g.nodeSize();
        int e = g.edgeSize();
        boolean c = g1.isConnected();
        double d = g1.shortestPathDist(0, 0);
        g.removeNode(2);
        assertNull(g.getEdge(0,4));
        assertNull(g.getEdge(2,4));
        assertEquals(1,n,"one node graph node size");
        assertEquals(0,e,"one node graph edge size");
        assertEquals(true,c,"one node graph connection");
        assertEquals(0,d,"one node graph edge weight");
    }

    @Test
    @DisplayName("connected graph test")
    void test2()throws Exception{
        directed_weighted_graph g = graph2();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        boolean c = g1.isConnected();
        int mc = g.getMC();
        assertEquals(true,c,"graph2 connection");
        assertEquals(6,mc,"Mode count test");
    }
    @Test
    @DisplayName("connected graph test")
    void test3()throws Exception{
        directed_weighted_graph g = graph3();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        boolean c = g1.isConnected();
        int mc = g.getMC();
        assertEquals(false,c,"graph2 connection");
        assertEquals(6,mc,"Mode count test");
    }
    @Test
    @DisplayName("remove edge and node test")
    void test4() throws Exception{
        directed_weighted_graph g = graph3();
        g.removeNode(2);
        int n = g.nodeSize();
        int e = g.edgeSize();
        assertEquals(2,n,"test 4 node size");
        assertEquals(1,e,"test 4 edge size");
        g.removeEdge(0,1);
        int e2 = g.edgeSize();
        assertEquals(0,e2,"test 4 edge size2");
    }

    @Test
    @DisplayName("check remove edge and node function, path and path distance")
    void test5() throws Exception {
        directed_weighted_graph g = graph1();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        int n1 = g.nodeSize();
        int e0 = g.edgeSize();
        boolean c = g1.isConnected();
        double d = g1.shortestPathDist(0, 6);
        int m = g.getMC();
        List<node_data> L = g1.shortestPath(0, 6);
        int Ls = L.size();
        g.removeNode(1);
        assertEquals(9,n1,"graph1 node size");
        assertEquals(12,e0,"graph1 edge size");
        assertEquals(18,d,"graph1 distance");
        assertEquals(6,Ls,"graph1 node list");
        assertEquals(21,m,"graph1 mode count");
        assertEquals(false,c,"graph1 connection");
    }

    @Test
    @DisplayName("save test")
    void test6() throws Exception{
        directed_weighted_graph g = graph1();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        g1.save("graph");
    }

    @Test
    @DisplayName("load test")
    void test7() throws Exception{
        Scanner scanner = new Scanner( new File("src/Tests/graph.json") );
        String jsonString = scanner.useDelimiter("\\A").next();
        scanner.close();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.load(jsonString);
    }
    @Test
    @DisplayName("check data graph connection")
    void test8() throws Exception{
        Scanner scanner = new Scanner( new File("data/A4") );
        String jsonString = scanner.useDelimiter("\\A").next();
        scanner.close();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.load(jsonString);
        boolean c = g1.isConnected();
        assertEquals(true,c,"data graph connection");
    }


    public directed_weighted_graph graph0(){
        directed_weighted_graph g = new DWGraph_DS();
        node_data A = new DWGraph_DS.Node_Data();
        node_data B = new DWGraph_DS.Node_Data();
        geo_location A1 = new Point3D(33,44);
        geo_location B1 = new Point3D(55,66);
        A.setLocation(A1);
        B.setLocation(B1);
        g.addNode(A);
        g.addNode(B);
        g.connect(A.getKey(),B.getKey(),30);
        return g;
    }

    public directed_weighted_graph graph1() {
        directed_weighted_graph g = new DWGraph_DS();
        node_data S0 = new DWGraph_DS.Node_Data(); // 0
        geo_location S = new Point3D(33,44);
        S0.setLocation(S);
        node_data A1  = new DWGraph_DS.Node_Data();
        geo_location A = new Point3D(33,44);
        A1.setLocation(A);
        node_data B2 = new DWGraph_DS.Node_Data();
        geo_location B = new Point3D(33,44);
        B2.setLocation(B);
        node_data C3 = new DWGraph_DS.Node_Data();
        geo_location C = new Point3D(33,44);
        C3.setLocation(C);
        node_data D4 = new DWGraph_DS.Node_Data();
        geo_location D = new Point3D(33,44);
        D4.setLocation(D);
        node_data E5 = new DWGraph_DS.Node_Data();
        geo_location E = new Point3D(33,44);
        E5.setLocation(E);
        node_data F6 = new DWGraph_DS.Node_Data();
        geo_location F = new Point3D(33,44);
        F6.setLocation(F);
        node_data G7 = new DWGraph_DS.Node_Data(); // 7
        geo_location G = new Point3D(33,44);
        G7.setLocation(G);
        node_data H8 = new DWGraph_DS.Node_Data();
        geo_location H = new Point3D(33,44);
        H8.setLocation(H);
        g.addNode(S0);
        g.addNode(A1);
        g.addNode(B2);
        g.addNode(C3);
        g.addNode(D4);
        g.addNode(E5);
        g.addNode(F6);
        g.addNode(G7);
        g.addNode(H8);
        g.connect(S0.getKey(), A1.getKey(), 2);
        g.connect(S0.getKey(), D4.getKey(), 20);
        g.connect(A1.getKey(), E5.getKey(), 3);
        g.connect(E5.getKey(), B2.getKey(), 1);
        g.connect(E5.getKey(), H8.getKey(), 4);
        g.connect(E5.getKey(), G7.getKey(), 6);
        g.connect(B2.getKey(), C3.getKey(), 7);
        g.connect(H8.getKey(), E5.getKey(), 2);
        g.connect(H8.getKey(), G7.getKey(), 1);
        g.connect(G7.getKey(), D4.getKey(), 2);
        g.connect(C3.getKey(), F6.getKey(), 5);
        g.connect(F6.getKey(), B2.getKey(), 1);
        return g;
    }

    public directed_weighted_graph graph2(){
        directed_weighted_graph g = new DWGraph_DS();
        node_data A = new DWGraph_DS.Node_Data();
        node_data B = new DWGraph_DS.Node_Data();
        node_data D = new DWGraph_DS.Node_Data();
        geo_location A1 = new Point3D(11,22);
        geo_location B1 = new Point3D(33,44);
        geo_location D1 = new Point3D(55,66);
        A.setLocation(A1);
        B.setLocation(B1);
        D.setLocation(D1);
        g.addNode(A);
        g.addNode(B);
        g.addNode(D);
        g.connect(A.getKey(),B.getKey(),30);
        g.connect(B.getKey(),D.getKey(),40);
        g.connect(D.getKey(),A.getKey(),50);
        return g;
    }
    public directed_weighted_graph graph3(){
        directed_weighted_graph g = new DWGraph_DS();
        node_data A = new DWGraph_DS.Node_Data();
        node_data B = new DWGraph_DS.Node_Data();
        node_data D = new DWGraph_DS.Node_Data();
        geo_location A1 = new Point3D(11,22);
        geo_location B1 = new Point3D(33,44);
        geo_location D1 = new Point3D(55,66);
        A.setLocation(A1);
        B.setLocation(B1);
        D.setLocation(D1);
        g.addNode(A);
        g.addNode(B);
        g.addNode(D);
        g.connect(A.getKey(),B.getKey(),30);
        g.connect(A.getKey(),D.getKey(),40);
        g.connect(D.getKey(),B.getKey(),50);
        return g;
    }
}