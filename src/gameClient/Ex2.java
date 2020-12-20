package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {

    private static MyFrame _win;
    private static Arena Ar;
    private static int counter;
    private static int scenario;
    private static int[][] Path;
    private static String[] args_Data;


    public static void main(String[] args) {
        Arr(args);
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        int id;
        if (args_Data.length == 0) {
            Login_Frame f = new Login_Frame();
            while (f.Flag() != true) {
                System.out.print("");
            }
            String[] Data = f.GetData().split(",");
            id = Integer.parseInt(Data[0]);
            scenario = Integer.parseInt(Data[1]);
        } else {
            id = Integer.parseInt(args_Data[0]);
            scenario = Integer.parseInt(args_Data[1]);
        }
        game_service game = Game_Server_Ex2.getServer(scenario); // you have [0,23] games
        game.login(id);
        String g = game.getGraph();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.load(g);
        directed_weighted_graph gg = g1.getGraph();
        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: scenario number: "+ scenario);
        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            moveAgents(game, gg, counter);
            try {
                if (ind % 3 == 0) {
                    _win.update(Ar);
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }
    /**
     *At first we'll create data structure's of the agent and the pokemon,
     * to every agent / pokemon there is priority queue that compute the best match
     * based on the distance and value.
     * at the end the function will update the finally destination to every agent.
     * this function based on the Gale–Shapley algorithm.
     *
     * @param game a game_service type.
     * @param g a directed_weighted_graph type.
     * @param counter a game_service type.
     */
    private void moveAgents(game_service game, directed_weighted_graph g, int counter) {
        if (counter == 0) {
            Next_Node(game, g);
        }
        String lg = game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg, g);
        Ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        Ar.setPokemons(ffs);
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
        for (int a = 0; a < cl_fs.size(); a++) {
            Arena.updateEdge(cl_fs.get(a), g);
        }
        Ar.setPokemons(cl_fs);
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        ArrayList<CL_Agent> AL = (ArrayList<CL_Agent>) Ar.getAgents();
        for (int i = 0; i < AL.size(); i++) {
            if (AL.get(Path[0][i]).getNextNode() == -1) {
                List<node_data> L = g1.shortestPath(AL.get(Path[0][i]).getSrcNode(), Path[1][i]);
                if (AL.get(Path[0][i]).getSrcNode() != Path[1][i]) {
                    if (L.size() <= 1) {
                        this.counter = 0;
                        break;
                    }
                    if (L.size() <= 4) {
                        this.counter = 0;
                    }
                    int next_node = L.get(1).getKey();
                    game.chooseNextEdge(Path[0][i], next_node);
                    System.out.println("Agent: " + Path[0][i] + ", val: " + AL.get(i).getValue() + " from node: " + AL.get(Path[0][i]).getSrcNode() + "   turned to node: " + next_node + " 1the dest is: " + this.Path[1][i]);
                } else {
                    ArrayList<CL_Pokemon> PL = (ArrayList<CL_Pokemon>) Ar.getPokemons();
                    int new_dest = 0;
                    if (PL.get(i).getType() == 1) {
                        new_dest = Math.min(PL.get(i).get_edge().getDest(), PL.get(i).get_edge().getSrc());
                    } else {
                        new_dest = Math.max(PL.get(i).get_edge().getDest(), PL.get(i).get_edge().getSrc());
                    }
                    int[][] new_path = this.Path;
                    new_path[1][i] = new_dest;
                    this.Path = new_path;
                    List<node_data> L2 = g1.shortestPath(AL.get(Path[0][i]).getSrcNode(), Path[1][i]);
                    if (L2.size() <= 1) {
                        this.counter = 0;
                        break;
                    }
                    if (L2.size() <= 4) {
                        this.counter = 0;
                    } else {
                        this.counter = 1;
                    }
                    int next_node = L2.get(1).getKey();
                    game.chooseNextEdge(Path[0][i], next_node);
                    System.out.println("Agent: " + Path[0][i] + ", val: " + AL.get(i).getValue() + " from node: " + AL.get(Path[0][i]).getSrcNode() + "   turned to node: " + next_node + " 2the dest is: " + this.Path[1][i]);
                }
            }
        }
        game.move();
    }
    /**
     *At first we'll create data structure's of the agent and the pokemon,
     * to every agent / pokemon there is priority queue that compute the best match
     * based on the distance and value.
     * at the end the function will update the finally destination to every agent.
     * this function based on the Gale–Shapley algorithm.
     *
     * @param game a game_service type.
     */

    private void Next_Node(game_service game, directed_weighted_graph g) {
        String lg = game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg, g);
        Ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        Ar.setPokemons(ffs);
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
        for (int a = 0; a < cl_fs.size(); a++) {
            Arena.updateEdge(cl_fs.get(a), g);
        }
        Ar.setPokemons(cl_fs);
        Comparator<Agent_Node> AgComp = new Agent_Comp();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(g);
        ArrayList<CL_Agent> AL = (ArrayList<CL_Agent>) Ar.getAgents();
        HashMap<Integer, PriorityQueue> A = new HashMap<Integer, PriorityQueue>();
        PriorityQueue<Agent_Node> A1 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A2 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A3 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A4 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A5 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A6 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A7 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A8 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A9 = new PriorityQueue<>(10, AgComp);
        PriorityQueue<Agent_Node> A10 = new PriorityQueue<>(10, AgComp);
        if (AL.size() > 0) {
            A.put(AL.get(0).getID(), A1);
        }
        if (AL.size() > 1) {
            A.put(AL.get(1).getID(), A2);
        }
        if (AL.size() > 2) {
            A.put(AL.get(2).getID(), A3);
        }
        if (AL.size() > 3) {
            A.put(AL.get(3).getID(), A4);
        }
        if (AL.size() > 4) {
            A.put(AL.get(4).getID(), A5);
        }
        if (AL.size() > 5) {
            A.put(AL.get(5).getID(), A6);
        }
        if (AL.size() > 6) {
            A.put(AL.get(6).getID(), A7);
        }
        if (AL.size() > 7) {
            A.put(AL.get(7).getID(), A8);
        }
        if (AL.size() > 8) {
            A.put(AL.get(8).getID(), A9);
        }
        if (AL.size() > 9) {
            A.put(AL.get(9).getID(), A10);
        }

//        ArrayList<PriorityQueue> P = new ArrayList<PriorityQueue>();
        ArrayList<CL_Pokemon> PL = (ArrayList<CL_Pokemon>) Ar.getPokemons();
        HashMap<Integer, PriorityQueue> P = new HashMap<Integer, PriorityQueue>();
        Comparator<Poke_Node> PokComp = new Poke_Comp();
        PriorityQueue<Poke_Node> P1 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P2 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P3 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P4 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P5 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P6 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P7 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P8 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P9 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P10 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P11 = new PriorityQueue<>(10, PokComp);
        PriorityQueue<Poke_Node> P12 = new PriorityQueue<>(10, PokComp);
        if (PL.size() > 0) {
            int dest = PL.get(0).get_edge().getDest();
            int src = PL.get(0).get_edge().getSrc();
            if (PL.get(0).getType() == 1) {
                P.put(Math.max(src, dest), P1);
            } else {
                P.put(Math.min(src, dest), P1);
            }
        }

        if (PL.size() > 1) {
            int dest = PL.get(1).get_edge().getDest();
            int src = PL.get(1).get_edge().getSrc();
            if (PL.get(1).getType() == 1) {
                P.put(Math.max(src, dest), P2);
            } else {
                P.put(Math.min(src, dest), P2);
            }
        }

        if (PL.size() > 2) {
            int dest = PL.get(2).get_edge().getDest();
            int src = PL.get(2).get_edge().getSrc();
            if (PL.get(2).getType() == 1) {
                P.put(Math.max(src, dest), P3);
            } else {
                P.put(Math.min(src, dest), P3);
            }
        }

        if (PL.size() > 3) {
            int dest = PL.get(3).get_edge().getDest();
            int src = PL.get(3).get_edge().getSrc();
            if (PL.get(3).getType() == 1) {
                P.put(Math.max(src, dest), P4);
            } else {
                P.put(Math.min(src, dest), P4);
            }
        }

        if (PL.size() > 4) {
            int dest = PL.get(4).get_edge().getDest();
            int src = PL.get(4).get_edge().getSrc();
            if (PL.get(4).getType() == 1) {
                P.put(Math.max(src, dest), P5);
            } else {
                P.put(Math.min(src, dest), P5);
            }
        }

        if (PL.size() > 5) {
            int dest = PL.get(5).get_edge().getDest();
            int src = PL.get(5).get_edge().getSrc();
            if (PL.get(5).getType() == 1) {
                P.put(Math.max(src, dest), P6);
            } else {
                P.put(Math.min(src, dest), P6);
            }
        }

        if (PL.size() > 6) {
            int dest = PL.get(6).get_edge().getDest();
            int src = PL.get(6).get_edge().getSrc();
            if (PL.get(6).getType() == 1) {
                P.put(Math.max(src, dest), P7);
            } else {
                P.put(Math.min(src, dest), P7);
            }
        }

        if (PL.size() > 7) {
            int dest = PL.get(7).get_edge().getDest();
            int src = PL.get(7).get_edge().getSrc();
            if (PL.get(7).getType() == 1) {
                P.put(Math.max(src, dest), P8);
            } else {
                P.put(Math.min(src, dest), P8);
            }
        }
        if (PL.size() > 8) {
            int dest = PL.get(8).get_edge().getDest();
            int src = PL.get(8).get_edge().getSrc();
            if (PL.get(8).getType() == 1) {
                P.put(Math.max(src, dest), P9);
            } else {
                P.put(Math.min(src, dest), P9);
            }
        }
        if (PL.size() > 9) {
            int dest = PL.get(9).get_edge().getDest();
            int src = PL.get(9).get_edge().getSrc();
            if (PL.get(9).getType() == 1) {
                P.put(Math.max(src, dest), P10);
            } else {
                P.put(Math.min(src, dest), P10);
            }
        }
        if (PL.size() > 10) {
            int dest = PL.get(10).get_edge().getDest();
            int src = PL.get(10).get_edge().getSrc();
            if (PL.get(10).getType() == 1) {
                P.put(Math.max(src, dest), P11);
            } else {
                P.put(Math.min(src, dest), P11);
            }
        }
        if (PL.size() > 11) {
            int dest = PL.get(11).get_edge().getDest();
            int src = PL.get(11).get_edge().getSrc();
            if (PL.get(11).getType() == 1) {
                P.put(Math.max(src, dest), P12);
            } else {
                P.put(Math.min(src, dest), P12);
            }
        }

        for (int i = 0; i < AL.size(); i++) {
            int src = AL.get(i).getSrcNode();
            for (int j = 0; j < PL.size(); j++) {
                int dest = 0;
                int src2 = 0;
                if (PL.get(j).getType() == 1) {
                    dest = Math.max(PL.get(j).get_edge().getSrc(), PL.get(j).get_edge().getDest());
                    src2 = Math.min(PL.get(j).get_edge().getSrc(), PL.get(j).get_edge().getDest());
                } else {
                    dest = Math.min(PL.get(j).get_edge().getSrc(), PL.get(j).get_edge().getDest());
                    src2 = Math.max(PL.get(j).get_edge().getSrc(), PL.get(j).get_edge().getDest());
                }
                int id_dest = dest;
                List<node_data> L = g1.shortestPath(src, dest);
                if (!L.contains(g.getNode(src2))) {
                    dest = src2;
                }
                double dist = g1.shortestPathDist(src, dest);
                double val = PL.get(j).getValue();
                Poke_Node poke_node = new Poke_Node(AL.get(i).getID(), dist, val);
                P.get(id_dest).add(poke_node);
                Agent_Node agent_node = new Agent_Node(dest, dist, val);
                A.get(AL.get(i).getID()).add(agent_node);
            }
        }
        int[][] AG = new int[3][AL.size()];
        for (int i = 0; i < AL.size(); i++) {
            Agent_Node agent_node = (Agent_Node) A.get(AL.get(i).getID()).poll();
            AG[0][i] = AL.get(i).getID();
            AG[1][i] = agent_node.getDest();
        }
        boolean flag1 = true; // check if two agent point on the same pokemon
        if (AL.size() != 1) {
            for (int i = 1; i < AL.size(); i++) {
                if (AG[1][i] == AG[1][i - 1]) {
                    flag1 = false;
                }
            }
        }
        if (flag1 == false) {
            for (int i = 1; i < AL.size(); i++) {
                if (AG[1][i] == AG[1][i - 1]) {
                    PriorityQueue<Poke_Node> CP = P.get(AG[1][i]);
                    int A01 = AG[0][i]; // agent id
                    int A02 = AG[0][i - 1]; // agent id
                    boolean flag2 = false;
                    while (flag2 != true && CP != null) {
                        Poke_Node b = CP.poll();
                        if (b == null) {
                            break;
                        }
                        int c = b.getA_Id();
                        if (c == A01 && A.get(AG[0][i]).size() > 1) {
                            Agent_Node new_dest = (Agent_Node) A.get(AG[0][i]).poll();
                            AG[1][i] = new_dest.getDest();
                            flag2 = true;
                            i = 0; // to check again
                        }
                        if (c == A02 && A.get(AG[0][i - 1]).size() > 1) {
                            Agent_Node new_dest = (Agent_Node) A.get(AG[0][i - 1]).poll();
                            AG[1][i - 1] = new_dest.getDest();
                            flag2 = true;
                            i = 0; // to check again
                        }
                    }
                }
            }
        }
        for (int i = 0; i < AL.size(); i++) {
            if (AL.get(AG[0][i]).getSrcNode() == AG[1][i] && PL.size() > 1) {
                Agent_Node agent_node = (Agent_Node) A.get(AL.get(i).getID()).poll();
                AG[1][i] = agent_node.getDest();
                i = 0;
            }
        }
        this.Path = AG;
        this.counter = 1;
    }
    /**
     *Gets all the data from the server,
     * update the Arena and the frame.
     * give the agent their first location.
     *
     * @param game a game_service type.
     */
    private void init(game_service game) {
        Ar = new Arena();
        String g = game.getGraph();
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.load(g);
        Ar.setGraph(g1.getGraph());
        String fs = game.getPokemons();
        Ar.setPokemons(Arena.json2Pokemons(fs));
//        Ar.setPokemonsE();
        _win = new MyFrame("Ex2 - OOP: scenario number: "+ scenario);
        _win.setSize(1000, 700);
        _win.update(Ar);
        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), g1.getGraph());
            }
            Comparator<CL_Pokemon> P_Comp = new CL_Comp();
            PriorityQueue<CL_Pokemon> P = new PriorityQueue<>(10, P_Comp);
            for (CL_Pokemon c : cl_fs) {
                P.add(c);
            }
            for (int a = 0; a < rs; a++) {
                CL_Pokemon c = P.poll();
                int start_node = 0;
                if (c.getType() == 1) {
                    start_node = Math.min(c.get_edge().getSrc(), c.get_edge().getDest());
                } else {
                    start_node = Math.max(c.get_edge().getSrc(), c.get_edge().getDest());
                }
                game.addAgent(start_node);
            }
            System.out.println(game.getAgents());
            String Ag = game.getAgents();
            Ar.setAgents(Arena.getAgents(Ag, g1.getGraph()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class Agent_Node {
        private int dest; // pokemon's edge dest
        private double w; // distance between the agent and the pokemon
        private double val; // pokemon value

        Agent_Node(int dest, double w, double val) {
            this.dest = dest;
            this.w = w;
            this.val = val;
        }

        public int getDest() {
            return this.dest;
        }

        public double getWeight() {
            return this.w;
        }

        public double getVal() {
            return this.val;
        }

    }

    public static class Agent_Comp implements Comparator<Agent_Node> {
        @Override
        public int compare(Agent_Node o1, Agent_Node o2) {
            double val1 = o1.getVal();
            double val2 = o2.getVal();
            double weight1 = o1.getWeight();
            double weight2 = o2.getWeight();
            if (val1 - val2 >= 14 && weight1 - weight2 <= 5) {
                return -1;
            }
            if (val2 - val1 >= 14 && weight2 - weight1 <= 5) {
                return 1;
            }
            if (o1.getWeight() < o2.getWeight()) {
                return -1;
            } else if (o2.getWeight() < o1.getWeight()) {
                return 1;
            }
            return 0;
        }
    }

    private static class Poke_Node {
        private double w;
        private double val;
        private int A_Id; // agent id

        Poke_Node(int A_Id, double w, double val) {
            this.A_Id = A_Id;
            this.w = w;
            this.val = val;
        }

        public int getA_Id() {
            return this.A_Id;
        }

        public double getWeight() {
            return this.w;
        }

        public double getVal() {
            return this.val;
        }
    }

    public static class Poke_Comp implements Comparator<Poke_Node> {
        @Override
        public int compare(Poke_Node o1, Poke_Node o2) {
            double val1 = o1.getVal();
            double val2 = o2.getVal();
            double weight1 = o1.getWeight();
            double weight2 = o2.getWeight();
            if (val1 - val2 >= 14 && weight1 - weight2 <= 5) {
                return -1;
            }
            if (val2 - val1 >= 14 && weight2 - weight1 <= 5) {
                return 1;
            }
            if (o1.getWeight() < o2.getWeight()) {
                return -1;
            } else if (o2.getWeight() < o1.getWeight()) {
                return 1;
            }
            return 0;
        }
    }

    public static class CL_Comp implements Comparator<CL_Pokemon> {
        @Override
        public int compare(CL_Pokemon o1, CL_Pokemon o2) {
            if (o1.getValue() < o2.getValue()) {
                return -1;
            } else if (o2.getValue() < o1.getValue()) {
                return 1;
            }
            return 0;
        }
    }

    public static void Arr(String[] a) {
        args_Data = a;
    }
}
