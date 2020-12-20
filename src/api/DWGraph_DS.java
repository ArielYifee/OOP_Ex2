package api;

import gameClient.util.Point3D;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, HashMap<Integer, edge_data>> E; // edges wight
    private HashMap<Integer, HashMap<Integer, edge_data>> Reverse_E; // revers edges wight
    private HashMap<Integer, node_data> V; // nodes

    private static int edgeS, MC;

    public DWGraph_DS(){
        this.V = new HashMap<Integer, node_data>();
        this.E = new HashMap<Integer, HashMap<Integer, edge_data>>();
        this.Reverse_E = new HashMap<Integer, HashMap<Integer, edge_data>>();
        this.MC =0 ;
        this.edgeS = 0;
        Node_Data.index = 0;
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (V.containsKey(key)) { // check if the node is in the graph by it key and if it is it will return the node itself
            return V.get(key);
        }
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(E.containsKey(src)) {// check if the src node is in the graph, if it's in the graph return the edge
            return E.get(src).get(dest);
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        V.put(n.getKey(),n); //put the node in the node list
        E.put(n.getKey(),new HashMap<Integer, edge_data>()); // declare the node id in the edges list
        Reverse_E.put(n.getKey(),new HashMap<Integer, edge_data>());// declare the node id in the revers edges list
        MC ++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        // check if both of the node are in the graph
        if(V.get(src) != null && V.get(dest) != null ){
            edge_data edge = new Edge_Data(src,dest,w); // create the edge
            edge_data edge_Trn = new Edge_Data(dest,src,w);// create revers edge
            // if there is no edge between them will creat new one and connect
            if (!E.get(src).containsKey(dest)){// check if the edge is already exist
                E.get(src).put(dest,edge);// update the edge
                Reverse_E.get(dest).put(src,edge_Trn);// update the revers list
                edgeS++; // new edge
                MC++;
            }
            else{ // there is already edge we just update the weight
                E.get(src).put(dest, edge);
                Reverse_E.get(dest).put(src,edge_Trn);
                MC++;
            }
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */

    @Override
    public Collection<edge_data> getE(int node_id) {
        return E.get(node_id).values();
    }

    /**
     *
     * This method returns a pointer (shallow copy) for the
     * collection representing all the revers edges getting out of
     * the given node (all the edges starting (source) at the given node).
     *
     * @param node_id
     * @return Collection<edge_data>
     */

    public Collection<edge_data> getReverse_E(int node_id) {
        return Reverse_E.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */

    @Override
    public node_data removeNode(int key) {
        if(!V.containsKey(key)){ // check if the node is in the graph
            return null;
        }
        node_data N = V.get(key); //copy the node
        //remove all the edges that
        int Ecounter = E.get(key).size(); // edge counter
        //remove the edges and the node
        E.remove(key);
        V.remove(key);
        for(HashMap edge : E.values()){
            if(edge.containsKey(key)){
                edge.remove(key);
                Ecounter ++;
            }
        }
        edgeS -= Ecounter;
        MC ++;
        return N;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */

    @Override
    public edge_data removeEdge(int src, int dest) {
        edgeS --;
        MC ++;
        Reverse_E.get(dest).remove(src);
        return E.get(src).remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return V.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edgeS;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return MC;
    }

    public static class Node_Data implements node_data{

        private geo_location location;
        private String Info;
        private int key ,Tag;
        private double Weight;
        public static int index;


        //constructor
        public Node_Data() {
            this.Weight = 0;
            this.key = index++;
            this.Tag = 0;
            this.Info = "";
        }
        //constructor
        public Node_Data(int n, String s) {
            String[] array = s.split(",",-1);
            double x = Double.parseDouble(array[0]);
            double y = Double.parseDouble(array[1]);
            double z = Double.parseDouble(array[2]);
            this.location = new Point3D(x,y,z);
            this.Weight = 0;
            this.key = n;
            this.Tag = 0;
            this.Info = s;
        }

        /**
         * Returns the key (id) associated with this node.
         *
         * @return
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * Returns the location of this node, if
         * none return null.
         *
         * @return
         */
        @Override
        public geo_location getLocation() {
            return location;
        }

        /**
         * Allows changing this node's location.
         *
         * @param p - new new location  (position) of this node.
         */
        @Override
        public void setLocation(geo_location p) {
            this.location = new Point3D(p.x(),p.y(),p.z());
        }

        /**
         * Returns the weight associated with this node.
         *
         * @return
         */
        @Override
        public double getWeight() {
            return Weight;
        }

        /**
         * Allows changing this node's weight.
         *
         * @param w - the new weight
         */
        @Override
        public void setWeight(double w) {
            this.Weight = w;
        }

        /**
         * Returns the remark (meta data) associated with this node.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return this.Info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.Info=s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return this.Tag;
        }

        /**
         * Allows setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this.Tag = t;
        }
    }
    static class Edge_Data implements edge_data{

        private int src;
        private double weight;
        private int dest;
        private int tag=0;
        private String info ="";

        //constructor
        public Edge_Data (int Source, int Dest, double  weight){
            this.src = Source;
            this.dest = Dest;
            this.weight = weight;
        }

        /**
         * The id of the source node of this edge.
         *
         * @return
         */
        @Override
        public int getSrc() {
            return this.src;
        }

        /**
         * The id of the destination node of this edge
         *
         * @return
         */
        @Override
        public int getDest() {
            return this.dest;
        }

        /**
         * @return the weight of this edge (positive value).
         */
        @Override
        public double getWeight() {
            return this.weight;
        }

        /**
         * Returns the remark (meta data) associated with this edge.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * Allows changing the remark (meta data) associated with this edge.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return this.tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking an edge - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            this.tag = t;
        }
    }
}
