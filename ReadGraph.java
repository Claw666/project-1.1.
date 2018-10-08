import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}
		
public class ReadGraph
		{
		
		public final static boolean DEBUG = true;
		
		public final static String COMMENT = "//";
		
		public static void main( String args[] )
			{
			if( args.length < 1 )
				{
				System.out.println("Error! No filename specified.");
				System.exit(0);
				}

				
			String inputfile = args[0];
			
			boolean seen[] = null;
			
			//! n is the number of vertices in the graph
			int n = -1;
			
			//! m is the number of edges in the graph
			int m = -1;
			
			//! e will contain the edges of the graph
			ColEdge e[] = null;
			
			try 	{ 
			    	FileReader fr = new FileReader(inputfile);
			        BufferedReader br = new BufferedReader(fr);

			        String record = new String();
					
					//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
					//! These comments are only allowed at the top of the file.
					
					//! -----------------------------------------
			        while ((record = br.readLine()) != null)
						{
						if( record.startsWith("//") ) continue;
						break; // Saw a line that did not start with a comment -- time to start reading the data in!
						}
	
					if( record.startsWith("VERTICES = ") )
						{
						n = Integer.parseInt( record.substring(11) );					
						if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
						}

					seen = new boolean[n+1];	
						
					record = br.readLine();
					
					if( record.startsWith("EDGES = ") )
						{
						m = Integer.parseInt( record.substring(8) );					
						if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
						}

					e = new ColEdge[m];	
												
					for( int d=0; d<m; d++)
						{
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
						record = br.readLine();
						String data[] = record.split(" ");
						if( data.length != 2 )
								{
								System.out.println("Error! Malformed edge line: "+record);
								System.exit(0);
								}
						e[d] = new ColEdge();
						
						e[d].u = Integer.parseInt(data[0]);
						e[d].v = Integer.parseInt(data[1]);

						seen[ e[d].u ] = true;
						seen[ e[d].v ] = true;
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
				
						}
									
					String surplus = br.readLine();
					if( surplus != null )
						{
						if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
						}
					
					}
			catch (IOException ex)
				{ 
		        // catch possible io errors from readLine()
			    System.out.println("Error! Problem reading file "+inputfile);
				System.exit(0);
				}

			for( int x=1; x<=n; x++ )
				{
				if( seen[x] == false )
					{
					if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
					}
				}

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//! 
			//! there will be n vertices in the graph, numbered 1 to n

			//! INSERT YOUR CODE HERE!
			
			
			System.out.println();
			
			// connect is an array with the number of edges every vertice does
			
			int[] connect = new int[n];
			for (int i=0; i<m; i++){
				connect[e[i].u-1]++;
				connect[e[i].v-1]++;
			}
			int mincount = connect[0];
			for (int i=0; i<n; i++){
				System.out.print(connect[i]+" ");
				if (connect[i] < mincount)
					mincount = connect[i];		//mincount is a Lower
			}
			
			int[][] ConnectionList = new int[2][n];
			for (int i=0; i<n; i++){
				ConnectionList[0][i] = connect[i];
				ConnectionList[1][i] = i;
			}
			sorting(ConnectionList,1);
			//Line [1] of ConnectionList is a sorted array of the nods by the ascending number of their connections 	
				
			System.out.println();
			System.out.println();
			int[][] graph = new int[n][n];
			fillMatrix(graph,e,n,m);
		
			//Draws the graph Matrix
			//Matrix is to visualize the connections between nods
			
			for (int i=0; i<n; i++){
				for (int j=0; j<n ;j++)
					System.out.print(graph[i][j]+" ");
				System.out.println();
			}
	
			int[] colour = new int[n];
			for (int i=0; i<colour.length; i++)
				colour[i] = i+1;
			colouring(colour,graph);
			System.out.println();
			for (int i=0; i<n; i++)
				System.out.print(colour[i]+" ");
			
			
			
			int[] sortedList = new int[n];
			int[] colour2 = new int[n];
			for (int i=0; i<n; i++){
				colour2[i] = ConnectionList[1][n-i-1];
				sortedList[i] = ConnectionList[1][n-i-1];
			}
			System.out.println();
			System.out.println();
			for (int i=0; i<n; i++)
					System.out.print(sortedList[i]+" ");
					
			colouring2(colour2,graph,sortedList);
			System.out.println();
			for (int i=0; i<n; i++)
				System.out.print(colour2[i]+" ");
			
			
			
			
			
			System.out.println();
			int sessions = Values(colour);
			int sessions2 = Values(colour2);
			
			System.out.println("Lower number is: "+mincount);
			System.out.println("Chromatic number is: "+sessions);
			System.out.println("Chromatic number 2 is: "+sessions2);
		}
		
		public static void fillMatrix(int[][] matrix, ColEdge e[], int n, int m){
			for (int i=0; i<m; i++){
				for (int j=0; j<n ;j++){
					matrix[e[i].u-1][e[i].v-1]=1;
					matrix[e[i].v-1][e[i].u-1]=1;
				}
			}
		}
									
		public static void colouring(int[] colour, int[][] graph){
			for (int i=1; i<graph.length; i++){
				int j = 0;
				boolean test = true;
				while (test && j<i){
					if (graph[i][j] == 0){
						int relative = colour[j];
						boolean ok = true;
						int l = 0;
						while (ok && l<i){
							if (colour[l] == relative)
								if (graph[i][l] == 1)
									ok = false;
							l++;
							}
						if (ok){
							colour[i]=relative;
							test = false;
						}
					}
					j++;
				}
			}
		}
		
		public static int Values(int[] colourArray){
			int result = 0;
			ArrayList<Integer> diff = new ArrayList<>();

			for(int i=0; i<colourArray.length; i++){
				if(!diff.contains(colourArray[i])){
					diff.add(colourArray[i]);
				}
			}
			if(diff.size()==1){
            result = 0;
            }
            else{
            	result = diff.size();
            } 
            return result;
        }
						
        public static void sorting(int[][] ConnectionList, int index){
        	if (index<ConnectionList[0].length){
        		int aux = ConnectionList[0][index];
        		int aux2 = ConnectionList[1][index];
        		int j = index;
        		boolean fin = false;
        		while (!fin && j>0){
        			if (ConnectionList[0][j-1]>aux){
        				ConnectionList[0][j] = ConnectionList[0][j-1];
        				ConnectionList[1][j] = ConnectionList[1][j-1];
        				j = j-1;
        			} else fin = true;
        		}
        		if (j<index){
        			ConnectionList[0][j] = aux;
        			ConnectionList[1][j] = aux2;
        		}
        		sorting(ConnectionList,index+1);
        	}
        }
        public static void colouring2(int[] colour, int[][] graph, int[] sortedList){
			for (int i=0; i<sortedList.length; i++){
				int j = 0;
				boolean test = true;
				while (test && j<i){
					if (graph[sortedList[i]][j] == 0){
						int relative = colour[j];
						boolean ok = true;
						int l = 0;
						while (ok && l<i){
							if (colour[l] == relative)
								if (graph[i][l] == 1)
									ok = false;
							l++;
							}
						if (ok){
							colour[i]=relative;
							test = false;
						}
					}
					j++;
				}
			}
		}
        	
}