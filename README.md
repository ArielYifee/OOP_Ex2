## Directed Weighted Graph and game

![](https://i.pinimg.com/originals/1d/7e/83/1d7e836aa727aa30038c1fb8bf7fc7b6.gif)

Ex2 is the third exercise in the OPP course of Ariel university.
this exercise will allow you to create a directed and weighted graph and to get information such as the shortest path between nodes, 
which is calculated by the shortest weighted path, copy the graph, remove nodes, or edges, adding nodes, connection node, save and load graph and more.

## How To Installation

to use this exercise you need to import the .java files to your IDE and now you can use this.

## How To Usage

we have here two classes: Graph_DS, this class included NodeInfo class and Graph_Algo. there is an interface to each class that elaborates on each function.
in the implementation of each function, some comments will explain how the function works.
download and put in your favorite IDE (:

in src/API/ in ex2 you can use the GUI or code the game you want in the main.
in src/algorithm/ in algoManager you can change the running algo with one of your own.
in src/GUI in myPanel you can add your own screens.

## Class Method

# class: DWGraph_DS
implements directed_weighted_graph 

private method: 

    HashMap<Integer, HashMap<Integer, edge_data>> Edges white
    HashMap<Integer, HashMap<Integer, edge_data>> Reverse Edges white 
    HashMap<Integer, node_data> Varticals 
    int edgeS
    int MC
	
function:

  * DWGraph_DS
  * getNode
  * getEdge
  * addNode
  * connect
  * getV
  * getE
  * removeNode
  * removeEdge
  * nodeSize
  * edgeSize
  * getMC
    
# class: DWGraph_DS
implements node_data

private method:

    geo_location location
    String Info
    int key 
    int Tag
    int index
    double Weight
    

function:

* Node_Data
* getKey
* getLocation
* setLocation
* getWeight
* setWeight
* getInfo
* setInfo
* getTag
* setTag
	
# class: Edge_Data
implements edge_data

private method:

    int src
    int dest
    int tag
    double weight
    String info 
	
function:

* Edge_Data
* getSrc
* getDest
* getWeight
* getInfo
* setInfo
* getTag
* setTag
   
# class: DWGraph_Algo 
implements dw_graph_algorithms 

private method:

    directed_weighted_graph G;
	
function: 

 * DWGraph_Algo
 * DWGraph_Algo
 * init
 * getGraph
 * copy
 * isConnected
 * shortestPathDist
 * shortestPath
 * save
 * load
 * Reset_Val 
 * Reset_Weight 
 * DFS 
 * TDFS
	
## Links
	
[Shortest path problem](https://en.wikipedia.org/wiki/Shortest_path_problem)
	
## Tests 

Ex2_Testr:

      public directed_weighted_graph graph1()
      public directed_weighted_graph graph2()
      public directed_weighted_graph graph3()

## License

this exercise was made by Ariel Yifee and Moriya Bitton.
