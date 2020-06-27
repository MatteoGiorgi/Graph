/* Graph<E> primo progetto (PR2B) Matteo Giorgi 517183, Novembre 2016 */

import java.util.Set;

public interface Graph<E>
{
	/*
		OVERVIEW: tipo modificabile degli insiemi di oggetti omogenei generici
				  di tipo E, gestiti come un grafo
		TYPICAL ELEMENT: <V,A> dove V={x | (x istanceof E)} e
									A={<x,y> | (x!=y)&&(x,y appartenenti a V)}
						 con V insieme dei vertici insieme finito e
						 	 A insieme degli archi insieme finito.
	*/


	/***************************************************************************
						Controlla l'esistenza di un vertice.
	***************************************************************************/
	public boolean
	existsVertex(E v);
	/*
		EFFECTS: restituisce true se il vertice v è presente nel grafo,
				 false altrimenti.
				 Se v==null lancia NullPointerException,
				 sottoclasse di RuntimeException.
	*/


	/***************************************************************************
						Controlla l'esistenza di un arco.
	***************************************************************************/
	public boolean
	existsEdge(E v, E w);
	/*
		EFFECTS: restituisce true se l'arco <v,w> è presente nel grafo,
				 false altrimenti.
				 Se v==null o w==null lancia NullPointerException (unchecked).
				 Se v e/o w non sono presenti nel grafo, lancia
				 IllegalArgumentException sottoclasse di RuntimeException.
	*/


	/***************************************************************************
						Restituisce il numero dei vertici.
	***************************************************************************/
	public int
	numVertex();
	/*
		EFFECTS: restituisce la cardinalità dell'insieme dei vertici del grafo.
	*/


	/***************************************************************************
						Restituisce il numero degli archi.
	***************************************************************************/
	public int
	numEdge();
	/*
		EFFECTS: restituisce la cardinalità dell'insieme degli archi del grafo.
	*/


	/***************************************************************************
						Aggiunge un vertice al grafo.
	***************************************************************************/
	public void
	addVertex(E v);
	/*
		MODIFIES: insieme V dei vertici
		EFFECTS: se V è l'insieme dei vertici prima dell'invocazione,
				 V'=V unito {v} sarà l'insieme dei vertici dopo l'invocazione.
				 Se v=null, lancia NullPointerException,
				 se prima dell'invocazione v appartiene a V,
				 lancia MalformedParametersException,
				 entrambe sottoclassi di RuntimeException.
	*/


	/***************************************************************************
						Rimuove un vertice dal grafo.
	***************************************************************************/
	public void
	removeVertex(E v);
	/*
		MODIFIES: insieme V dei vertici e insieme A degli archi
		EFFECTS: se V è l'insieme dei vertici prima dell'invocazione e
					A è l'insieme degli archi prima dell'invocazione,
				 V'=V\{v} sarà l'insieme dei vertici dopo l'invocazione e
				 A'=A\{<v,x> | <v,x> appartiene a A} sarà l'insieme degli archi
				 									 dopo l'invocazione
				 Se v=null, lancia NullPointerException,
				 sottoclasse di RuntimeException.
				 Se prima dell'invocazione v non appartiene a V,
				 lancia MalformedParametersException
				 sottoclasse di RuntimeException.
	*/


	/***************************************************************************
					Aggiunge un arco tra due vertici del grafo.
	***************************************************************************/
	public void
	addEdge(E v, E w);
	/*
		MODIFIES: insieme V dei vertici e insieme A degli archi
		EFFECTS: se A è l'insieme degli archi prima dell'invocazione e
				 se V è l'insieme dei vertici prima dell'invocazione,
				 A'=A unito {<v,w>} sarà l'insieme degli archi
				 					dopo l'invocazione e
				 V'=V unito {v} unito {w} sarà l'insieme dei vertici
				 						  dopo l'invocazione
				 Se v=null o w=null lancia NullPointerException,
				 sottoclasse di RuntimeException.
				 Se prima dell'invocazione <v, w> appartiene ad A,
				 lancia MalformedParametersException
				 sottoclasse di RuntimeException.
	*/


	/***************************************************************************
							Rimuove un arco dal grafo.
	***************************************************************************/
	public void
	removeEdge(E v, E w);
	/*
		MODIFIES: insieme A degli archi
		EFFECTS: se A è l'insieme degli archi prima dell'invocazione,
				 A'=A\{<v,w>} sarà l'insieme degli archi dopo l'invocazione.
				 Se v=null o w=null lancia NullPointerException,
				 sottoclasse di RuntimeException.
				 Se prima dell'invocazione <v, w> non appartiene ad A,
				 lancia MalformedParametersException
				 sottoclasse di RuntimeException.
	*/


	/***************************************************************************
			Restituisce il grado del vertice (numero archi del vertice).
	***************************************************************************/
	public int
	degreeVertex(E v);
	/*
		EFFECTS: restituisce il grado del vertice v
				 (numero di archi uscenti da v).
				 Se v=null lancia NullPointerException
				 sottoclasse di RuntimeException.
				 Se v non appartiene a V lancia IllegalArgumentException
				 sottoclasse di RuntimeException.
	*/


	/***************************************************************************
					Restituisce l'elenco dei vertici del grafo.
	***************************************************************************/
	public Set<E>
	listVertex();
	/*
		EFFECTS: restituisce un insieme contenente i vertici del grafo.
	*/


	/***************************************************************************
				Restituisce l'elenco degli adiacenti di un vertice.
	***************************************************************************/
	public Set<E>
	adjacentVertex(E v);
	/*
		EFFECTS: restituisce un insieme contenente gli adiacenti a v.
	*/


	/***************************************************************************
		Restituische la distanza tra due vertici,
		-1 se i due vertici non appartengono al medesimo sottografo connesso.
	***************************************************************************/
	public Integer
	distanceInBetween(E v, E w);
	/*
		EFFECTS: restituisce la profondità del vertice w nel MinimumSpanningTree
				 di radice v del grafo
	*/


	/***************************************************************************
					Restituisce il diametro del grafo:
					il maggiore dei minimi percorsi possibili.
	***************************************************************************/
	public int
	graphDiameter();
	/*
		EFFECTS: fra le altezze di tutti i MinimumSpanningTree del grafo,
				 ne restituisce quella maggiore
	*/


	/***************************************************************************
				Restituisce un array di Object contenente i nodi
				in ordine crescente di comparazione (compareTo).
	***************************************************************************/
	public Object[]
	toArray();
	/*
		EFFECTS: restituisce un arrai contenebte tutti gli elementi v tali che
				 v appartiene a V.
	*/


	/***************************************************************************
			Override equals di Object:
			controlla l'uguaglianza tra due grafi.
			Due grafi sono uguali se:
			hanno lo stesso numero di vertici,
			ogni vertice contenuto in uno è contenuo anche nell'altro e
			l'insieme degli adiacenti di un vertice in un grafo è uguale
			all'insieme degli adiacenti del medesimo vertice nell'altro
	***************************************************************************/
	public boolean
	equals(Graph<E> h);
	/*
		EFFECTS: dato
				 V=insieme dei vertici di questo grafo,
				 a=insieme degli archi di questo grafo,	
				 Vh=insieme dei vertici di h,
				 Ah=insieme degli archi di h,
				 restituisce true se V=Vh e A=Ah.
	*/
}