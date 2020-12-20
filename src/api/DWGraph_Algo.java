package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private static directed_weighted_graph G;

    //constructors
    public DWGraph_Algo(directed_weighted_graph graph) {
        this.G = graph;
    }

    public DWGraph_Algo() {
        this.G = null;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.G = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return G;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        if (G.getV() == null) {
            return null;
        }
        directed_weighted_graph g2 = new DWGraph_DS();
        for (node_data n : G.getV()) { // first copy all the nodes from the graph to the new graph.
            g2.addNode(n);
            g2.getNode(n.getKey()).setInfo(n.getInfo()); // copy info
            g2.getNode(n.getKey()).setTag(n.getTag()); // copy tag
            g2.getNode(n.getKey()).setWeight(n.getWeight()); // Copy weight
            g2.getNode(n.getKey()).setLocation(n.getLocation()); // copy location
        }
        for (node_data n : G.getV()) { //make all the connection that is in the graph to the new graph.
            for (edge_data ed : G.getE(n.getKey())) {
                g2.connect(n.getKey(), ed.getDest(), ed.getWeight()); //copy the edges
            }
        }
        return g2; // return the copy graph
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        Reset_Val(G); // reset the tag and info for all nodes
        if (G.getV().size() <= 1) { // empty graph or one node graph is connected
            return true;
        }
        node_data first = G.getV().iterator().next(); // get some node from the graph
        DFS(first); // run dfs and set the tags to 1
        for (node_data nodes : G.getV()) {
            if (nodes.getTag() == 0) { // if one of the node's tag is 0 its mean that our node is not connected to all nodes
                return false;
            }
        }
        Reset_Val(G); // rest the tag and info to all nodes
        TDFS(first); //do dfs to the transpose edges graph
        for (node_data nd : G.getV()) {
            if (nd.getTag() == 0) { // check if our node still gets to all nodes
                return false;
            }
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Collection<node_data> l = G.getV();
        if (!G.getV().contains(G.getNode(src)) || !G.getV().contains(G.getNode(dest))) { // if on of the node aren't in the graph return -1
            return -1;
        }
        if (src == dest) { // if its the same node the path is 0
            G.getNode(src).setInfo(String.format("%d", src));
            return 0;
        }
        if (G.getNode(src) == null || G.getNode(dest) == null) {// if one of the nodes are null return -1
            return -1;
        }
        node_data src1 = G.getNode(src);
        node_data dest1 = G.getNode(dest);
        PriorityQueue<node_data> queue = new PriorityQueue<node_data>(new Comparator<node_data>() { // create priority queue with comparator of weight
            @Override
            public int compare(node_data o1, node_data o2) {
                if (o1.getWeight() < o2.getWeight()) {
                    return -1;
                } else if (o2.getWeight() < o1.getWeight()) {
                    return 1;
                }
                return 0;
            }
        });
        Reset_Weight(G); // reset all nodes weight to infinity
        src1.setWeight(0);
        src1.setInfo("");
        queue.add(src1);
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                node_data node = queue.poll(); // get the lightest node
                if (node == null) {
                    return -1;
                }
                if (node == dest1) { //if the node that we polled is our dest finish.
                    String I = node.getInfo();
                    I += String.format(",%d", node.getKey());
                    node.setInfo(I);
                    return node.getWeight();
                }
                for (edge_data E : G.getE(node.getKey())) {
                    if (G.getNode(E.getDest()).getWeight() > node.getWeight() + E.getWeight()) { // check if the weight of the next node is more than the src node and the edge
                        G.getNode(E.getDest()).setWeight(node.getWeight() + E.getWeight()); // set the weight to be the src weight and the edge weight
                        String I = G.getNode(E.getSrc()).getInfo();
                        if (E.getSrc() == src) {
                            I += String.format("%d", E.getSrc());
                        } else {
                            I += String.format(",%d", E.getSrc());
                        }
                        G.getNode(E.getDest()).setInfo(I);
                        queue.add(G.getNode(E.getDest()));
                    }
                }
            }
        }
        return -1; // id the dijkstra's algorithm doesnt work return -1
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        LinkedList<node_data> path = new LinkedList<node_data>(); // contains the nodes
        if (src == dest) { // if its the same node the path is 0
            node_data src1 = G.getNode(src);
            path.addLast(src1);
            return path;
        }
        if (G.getNode(src) == null || G.getNode(dest) == null) {// if one of the nodes are null return null
            return null;
        }
        double dis = shortestPathDist(src, dest);
        if (dis == -1) { // if there is not path return null
            return null;
        }
        node_data dest1 = G.getNode(dest);
        String[] array = dest1.getInfo().split(",", -1); // the list of the path is in the info after shortestPathDist
        for (int i = 0; i < array.length; i++) {
            path.add(G.getNode((Integer.parseInt(array[i]))));
        }
        return path;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        JsonObject graph = new JsonObject();// create new json object
        JsonArray Nodes = new JsonArray();
        JsonArray Edges = new JsonArray();
        for (node_data n : G.getV()) {
            JsonObject node = new JsonObject();
            ArrayList<Double> p2 = new ArrayList<Double>();
            if (n.getLocation() != null) {
                String P = String.format("%f,%f,%f", n.getLocation().x(), n.getLocation().y(), n.getLocation().z()); // string of the location
                node.addProperty("pos", P); // add the location
            }
            node.addProperty("id", n.getKey()); // add the id
            Nodes.add(node); // add the node to the node list
            for (edge_data e : G.getE(n.getKey())) {
                JsonObject edg = new JsonObject();
                edg.addProperty("src", e.getSrc());
                edg.addProperty("w", e.getWeight());
                edg.addProperty("dest", e.getDest());
                Edges.add(edg); // add the edge to the edges array
            }
        }
        graph.add("Edges", Edges);
        graph.add("Nodes", Nodes);
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // create gson
        String json = gson.toJson(graph); // convert the json to gson
        try {
            String[] array = file.split("/", -1);
            PrintWriter pw;
            if (array.length >1 ) { // choose path
                pw = new PrintWriter(new File(file));
            }
            else {
                pw = new PrintWriter(new File("src/Tests/" + file + ".json"));
            }
            pw.write(json); // write the file.
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        directed_weighted_graph graph = new DWGraph_DS();
        G = graph;
        try {
            JSONObject jsonObject = new JSONObject(file);
            JSONObject nodes = new JSONObject();
            JSONObject edges = new JSONObject();
            JSONArray NodesArray = jsonObject.getJSONArray("Nodes");
            for (int i = 0; i < NodesArray.length(); i++) {
                nodes = NodesArray.getJSONObject(i);
                String pos = (String) nodes.get("pos");
                int id = (int) nodes.get("id");
                node_data n = new DWGraph_DS.Node_Data(id, pos);
                graph.addNode(n);
            }
            JSONArray EdgesArray = jsonObject.getJSONArray("Edges");
            for (int i = 0; i < EdgesArray.length(); i++) {
                edges = EdgesArray.getJSONObject(i);
                int src = (int) edges.get("src");
                double w = (double) edges.get("w");
                int dest = (int) edges.get("dest");
                graph.connect(src, dest, w);
            }
            this.G = graph;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * reset the tag and info to all nodes in the graph.
     *
     * @param g
     */
    public void Reset_Val(directed_weighted_graph g) {
        for (node_data n : g.getV()) {
            n.setTag(0);
            n.setInfo("");
        }
    }
    /**
     * set the weight to all nodes in the graph to be infinity.
     *
     * @param g
     */
    public void Reset_Weight(directed_weighted_graph g) {
        for (node_data n : g.getV()) {
            n.setWeight(Double.POSITIVE_INFINITY);
        }
    }
    /**
     * do dfs from one node to all node,
     * to every node that we get we set the tag to be 1.
     *
     * @param n
     */
    public void DFS(node_data n) {
        n.setTag(1);
        if (G.getE(n.getKey()) != null) {
            for (edge_data ed : G.getE(n.getKey())) {
                node_data nd = G.getNode(ed.getDest());
                if (nd != null && nd.getTag() == 0 && G.getE(nd.getKey()) != null) {
                    DFS(nd);
                }
            }
        }
    }
    /**
     * do dfs from one node to all node in the opposite way,
     * to every node that we get we set the tag to be 1.
     *
     * @param n
     */
    public void TDFS(node_data n) {
        n.setTag(1);
        if (((DWGraph_DS) G).getReverse_E(n.getKey()) != null) {
            for (edge_data ed : ((DWGraph_DS) G).getReverse_E(n.getKey())) {
                node_data nd = G.getNode(ed.getDest());
                if (nd != null && nd.getTag() == 0 && G.getE(nd.getKey()) != null) {
                    TDFS(nd);
                }
            }
        }
    }
}
