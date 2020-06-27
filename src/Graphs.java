/* Graphs primo progetto (PR2B) Matteo Giorgi 517183, Novembre 2016 */

import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.Arrays;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.awt.Desktop;


/*******************************************************************************
			Classe di funzioni statiche per la gestione del grafo
*******************************************************************************/
public class Graphs
{
	/***************************************************************************
					Legge un file di testo e
					riempie le due collezioni passategli:
					l'insieme ordinato dei verici e
					la lista ordinata degli insiemi degli adiacenti 
	***************************************************************************/
	private static void fillSets(FileReader f,
								LinkedHashSet<String> vertexes,
								ArrayList<TreeSet<String>> adjoints)
								throws IOException
	{
		BufferedReader br = new BufferedReader(f);
		String inputLine;

		while( (inputLine=br.readLine())!=null ){
			if( inputLine.isEmpty() ) continue;
			String[] auxArray = inputLine.split("-|:|\\s");
			switch(auxArray.length){
				case 0:
					continue;
				case 1:
					vertexes.add(auxArray[0]);
					adjoints.add(new TreeSet<String>());
					break;
				default:
					vertexes.add(auxArray[0]);
					adjoints.add(
						new TreeSet<String>(Arrays.asList(
							Arrays.copyOfRange(auxArray, 1, auxArray.length))
						)
					);
			}
		}
		br.close();
	}


	/***************************************************************************
						Legge un file di testo e
						riempie il grafo vuoto passatogli
	***************************************************************************/
	public static void readAndFill(GraphMap<String> g, FileReader f)
									throws IOException
	{
		LinkedHashSet<String> vertexes = new LinkedHashSet<String>();
		ArrayList<TreeSet<String>> adjoints = new ArrayList<TreeSet<String>>();

		Graphs.fillSets(f, vertexes, adjoints);

		g.addSetVertex(vertexes);
		Iterator<String> i = vertexes.iterator();
		Iterator<TreeSet<String>> j = adjoints.iterator();
		while( j.hasNext() ){
			TreeSet<String> friends = j.next();
			String vrt = i.next();
			friends.forEach(v->{
				if(!g.existsEdge(v, vrt)) g.addEdge(v, vrt);
			});
		}
	}
	/*
		EFFECTS: invoca Graphs.fillSets() che splitta ogni riga di f in
				 vertice - insieme degli adiacenti riempiendo vertexes e
				 adjoints; poi scorre nelle due collezioni per riempire g.
				 In caso sopraggiunga un errore di I/O, rilancia una IOException
				 (checked) al chiamante.
	*/


	/***************************************************************************
				Legge un file di testo e restituisce un nuovo grafo
				usando il Costruttore2 di GraphMap<E>
	***************************************************************************/
	public static GraphMap<String> asGraph(FileReader f) throws IOException
	{
		LinkedHashSet<String> vertexes = new LinkedHashSet<String>();
		ArrayList<TreeSet<String>> adjoints = new ArrayList<TreeSet<String>>();
		
		Graphs.fillSets(f, vertexes, adjoints);

		return new GraphMap<String>(vertexes, adjoints);
	}
	/*
		EFFECTS: analogamente a readAndFill invoca Graphs.fillSets(), poi
				 restituisce un nuovo grafo usando il Costruttore2 di GraphMap.
				 In caso sopraggiunga un errore di I/O, rilancia una IOException
				 (checked) al chiamante.
	*/


	/***************************************************************************
				Dato un grafo ed un file di scrittura già aperto
				scrive un file html usando delle librerie javascript
				fornite 
	***************************************************************************/
	private static void createHtml(GraphMap<String> g,
									String name,
									FileWriter f)
									throws IOException
	{
		BufferedWriter bw = new BufferedWriter(f);
		String lastChunk =
			"}};sys.graft(data);</script>"+
			"<section id=\"info\"><p id='info_0' class='info'>GRAFO "+
			name+"</p>"+
			"</body></html>",
			   middleChunk =
			"},edges:{",
			   firstChunk =
			"<html><head>"+
			"<script language=\"javascript\" type=\"text/javascript\" "+
			"src=\"jquery.min.js\"></script>"+
			"<script language=\"javascript\" type=\"text/javascript\" "+
			"src=\"arbor.js\" ></script>"+
			"<script language=\"javascript\" type=\"text/javascript\" "+
			"src=\"graphics.js\" ></script>"+
			"<script language=\"javascript\" type=\"text/javascript\" "+
			"src=\"renderer.js\" ></script>"+
			"</head><body>"+
			"<canvas id=\"viewport\" width=\"1200\" height=\"600\"></canvas>"+
			"<script language=\"javascript\" type=\"text/javascript\">"+
			"var sys = arbor.ParticleSystem(30, 600, 0.6, true);"+
			"sys.parameters({gravity:true});"+
			"sys.renderer = Renderer(\"#viewport\");"+
			"var data = {nodes:{";
		bw.write(firstChunk);

		for(Iterator<String> j=g.iterator(); j.hasNext();){
			String v = j.next().toString(),
				   line = v+":{'color':'blue','shape':'dot','label':'"+v+"'}";
			bw.write(line);
			if(j.hasNext()) bw.write(",");
		}
		bw.write(middleChunk);

		for(Iterator<String> j=g.iterator(); j.hasNext();){
			String v = j.next().toString(),
				   line = v+":{";
			for( Iterator<String> i=g.adjacentVertex(v).iterator();
				 i.hasNext(); ){
				line = line+i.next()+":{}";
				if(i.hasNext()) line = line+",";
			}
			bw.write(line+"}");
			if(j.hasNext()) bw.write(",");
		}
		bw.write(lastChunk);

		bw.close();
	}


	/***************************************************************************
			Dato un grafo lancia una istanza del browser predefinito
			per visualizzarlo graficamente
	***************************************************************************/
	public static void drawGraph(GraphMap<String> g, String name)
								throws IOException
	{
		File htmlFile = new File("GraphTest"+name+".html");
		htmlFile.createNewFile();
		htmlFile.deleteOnExit();
		Graphs.createHtml(g, name, new FileWriter(htmlFile));
		Desktop desktop = Desktop.isDesktopSupported()
							? Desktop.getDesktop()
							: null;
		if( desktop!=null && desktop.isSupported(Desktop.Action.BROWSE) )
			desktop.browse(htmlFile.toURI());
	}
	/*
		EFFECTS: invoca Graphs.createHtml() che crea un file html usando name,
				 poi, se il s.o. lo supporta, lancia la pagina creata in
				 una nuova finestra del browser predefinito .
				 In caso sopraggiunga un errore di I/O, rilancia una IOException
				 (checked) al chiamante.
	*/


	/***************************************************************************
				Dato un grafo ne stampa le informazioni essenziali
	***************************************************************************/
	public static <E extends Comparable<E>> void printInfo(GraphMap<E> g)
	{
		System.out.println(
			"Il grafo ha:\nnumero vertici = "+g.numVertex()+
						  "\nNumero archi = "+g.numEdge()+
						  "\nDiametro = "+g.graphDiameter()+
						  "\nInsieme (ordinato) dei vertici ="
		);
		Iterator<E> i = g.iterator();
		for(int cnt=g.numVertex(); cnt>=4; cnt-=4)
			System.out.printf( "%-10s %-10s %-10s %-10s\n",
								i.next().toString(),
								i.next().toString(),
								i.next().toString(),
								i.next().toString()
			);
		while( i.hasNext() ) System.out.printf("%-10s ", i.next().toString());
		System.out.printf("\n\n");
	}
	/*
		EFFECTS: stampa sulla shell le informazioni sul grafo g,
				 usando i metodi di GraphMap
	*/


	/***************************************************************************
					Il processo chiamante attende un secondo
	***************************************************************************/
	public static void waitOneSec(int n)
	{
		try{ Thread.sleep(n*1000); }
		catch(InterruptedException exc){ exc.printStackTrace(); }
	}
	/*
		EFFECTS: causa la sospensione per n secondi del thread chiamante senza
				 che esso perda la priorita' su alcun monitor.
				 Se un qualsiasi altro thread interrompe il thread corrente,
				 lancia una InterruptedException (checked) immediatamente
				 catturata e gestita con una semplice stampa in shell delle
				 info relative.
	*/
}