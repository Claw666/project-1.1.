import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}
		
public class ReadGraphGreedy
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
			
			//making an array with the degree of every vertices
			int[] degree = new int[n];
			for (int i=0; i<m; i++){
				degree[e[i].u-1]++;
				degree[e[i].v-1]++;
			}
			
			// making the adjacency matrix
			int [][] adjacencyMatrix = new int [n][n];
			for(int d = 0; d<m; d++){
				adjacencyMatrix[e[d].v-1][e[d].u-1] = 1;
				adjacencyMatrix[e[d].u-1][e[d].v-1] = 1;
			}
			//print the matrix
			for (int r = 0; r < adjacencyMatrix.length; r++){
				for(int c=0; c<adjacencyMatrix[0].length; c++){		
					int value = adjacencyMatrix[r][c];
					System.out.print("| " + value + "|");				
		}
		System.out.println("");					
	}		
			greedy(e,n,m,degree,adjacencyMatrix);				
		}	
			
		
		
		public static void greedy(ColEdge [] e, int vertices, int edges,int [] degree, int[][]adjacencyMatrix){
		    
		    Map<Integer,Integer> result = new HashMap<>(); // vertix, color
		    
		    //set color = 0 for all vertices
		    for(int n = 0; n<vertices; n++)
		    	result.put(n,0);
		    	//little sidenote: HasSet's are ordere set's
		    	//and there's a possibility that assignedvertices is not used.... but i didnt want to delet it just no to be sure :p
		        Set<Integer> assignedvertices = new HashSet<>(); // vertices of which de colour has been set
		    	Set<Integer> useableColors = new HashSet<>(); 
		    	
			int [] colorArray = new int[vertices];
		    	

			result.put(0,1); // giving the first color to the first node
			assignedvertices.add(0);
			colorArray[0]=1;
			
			for(int v =1; v<vertices; v++){
				int[] neighbourghcolours = new int [degree[v]+1]; // the amount of neighbouring colours can be max equal to the degree of the vertice
				int place = 0;
				
				for(int c=1; c<=vertices;c++)
				useableColors.add(c); // reset the usable colours
			
				for(int j = 0; j<vertices;j++){
					
					/* now we start searching for all the neighbouring vertices
					if we found one, we put their colour value in the neighbourghcolours array
					*/
					
					if(adjacencyMatrix[v][j]==1){
						neighbourghcolours[place]=result.get(j);
						place++;
					}
					
				}
				
				/* after collecting all neighbouring colours we start searching
				the lowest available colour*/
				
				for( int i=0; i<degree[v]+1;i++){
					if(useableColors.contains(neighbourghcolours[i])){
							useableColors.remove(neighbourghcolours[i]); //remove the neighbouringcolours from the available colors
					}
				for(int s=1; s<=vertices;s++){ //vertices = the max amount of colors that can be used
					//s excludes the 'color' 0 by stating at 1
					
					if(useableColors.contains(s)){
					result.put(v,s);
					break; //break when a colour is found
					}
				}
				
						
			}
			//here the program moves to the next vertice, if there is one
		}
		/*after assigning all vertices to a colour we
		 put the colours in an array such that we can count the differen amount of used colours
		 */
		 
		for(int i=0;i<vertices;i++)
			colorArray[i]=result.get(i);
		int amount = 0;
			ArrayList<Integer> diff = new ArrayList<>();

			for(int i=0; i<colorArray.length; i++){
				if(!diff.contains(colorArray[i])){
					diff.add(colorArray[i]);
				}
			}
			if(diff.size()==1){
            amount = 0;
            }
            else{
            	amount = diff.size();
            } 
		
		System.out.print("the colouring pattern is: ");
		System.out.println(result);
		System.out.println("Different amount of colors: " + amount);
	}
}
	