/* GraphMap<E> primo progetto (PR2B) Matteo Giorgi 517183, Novembre 2016 */

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Queue;
import java.util.Deque;
import java.util.List;
import java.util.ArrayDeque;
import java.util.NavigableSet;
import java.util.Collections;
import java.util.stream.Stream;
import java.lang.reflect.MalformedParametersException;


public class GraphMap<E extends Comparable<E>> implements Iterable<E>, Graph<E>
{
	/*
		OVERVIEW:= tipo modificabile degli insiemi di oggetti omogenei generici
				   di tipo E, gestiti come un grafo.

		AF:= <V,A> dove
			 V = st.keySet(),
			 A = {
					<x,y> | st.containsKey(x) &&
							st.containsKey(y) &&
							st.get(x).contains(y)
				 }

		IR:= st!=null &&
			 num_edge = Somma(degreeVertex(x)/2
							perogni x appartenente a st.keySet())
			 && 0<=num_edge<=N*(N-1)/2, con N=numVertex()
			 && perogni x appartenente a st.keySet() => x!=null
			 && perogni x appartenente a st.keySet(),
				perogni y appartenente a st.get(x) => y appartiene a st.keySet()
													  && y!=null
													  && y!=x
			 && perogni x appartenente a st.keySet(),
				perogni y,z appartenenti a st.get(x) => y!=z
			 && perogni x appartenente a st.keySet(),
				perogni y appartenente a st.get(x) => x appartiene a st.get(y)
	*/

	/***************************************************************************
								Variabili di classe.
	***************************************************************************/
	private TreeMap<E, TreeSet<E>> st;
	private int num_edge;


	/***************************************************************************
			Controlla la correttazza dell'invariante di rappresentazione
	***************************************************************************/
	private void repOk()
	{
		int somma = 0,
			num_vertex = 0;
		if(st==null) throw new RepInvariantException("infranto repInvariant");

		for(Map.Entry<E, TreeSet<E>> keyValue: st.entrySet()){
			somma += keyValue.getValue().size();
			num_vertex++;
			keyValue.getValue().forEach(friend->{
				if( !st.containsKey(friend) )
					throw new RepInvariantException("infranto repInvariant");
				if( !st.get(friend).contains(keyValue.getKey()) )
					throw new RepInvariantException("infranto repInvariant");
			});
		}

		if( somma < 0							||
			somma > num_vertex*(num_vertex-1) 	||
			num_edge != somma/2
		) throw new RepInvariantException("infranto repInvariant");
	}
	/*
		EFFECTS: se l'invariante di rappresentazione è infranto,
				 lancia RepInvariantException (unchecked)
	*/


	/***************************************************************************
							Costruttore1:
							istanzia un GraphMap vuoto.
	***************************************************************************/
	public GraphMap()
	{
		this.st = new TreeMap<E, TreeSet<E>>();
		this.num_edge = 0;
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: alloca un Treemap<E, TreeSet<E>> vuoto e setta num_edge a 0.
	*/


	/***************************************************************************
		Costruttore2:
		istanzia un GraphMap con vertici ed archi passati per argomento;
		il costruttore associa ad ogni elemento dell'insieme dei vertici
		il corrispettivo insieme di vertici adiacenti
		nell'ordine di scorrimento dell'iteratore.
	***************************************************************************/
	public GraphMap(Set<E> vertexes, List<TreeSet<E>> adjoints)
	{
		this();

		if( vertexes==null || adjoints==null )
			throw new NullPointerException("passato oggetto nullo");
		if( vertexes.size()!=adjoints.size() )
			throw new MalformedParametersException("errato numero vertici");

		Iterator<E> i = vertexes.iterator();
		Iterator<TreeSet<E>> j = adjoints.iterator();
		while(i.hasNext()){
			TreeSet<E> neighbours = j.next();
			num_edge += neighbours.size();
			st.put(i.next(), neighbours);
		}
		num_edge /= 2;
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: alloca un Treemap<E. TreeSet<E>>
				 tale che st.keySet()==vertexes && st.values()==adjoints,
				 rispettando l'ordine di iterazione
	*/


	/***************************************************************************
			Override iterator di Iterable<E>:
			restituisce un iteratore sugli elementi di tipo E del grafo.
	***************************************************************************/
	public Iterator<E> iterator()
	{
		return st.keySet().iterator();
	}
	/*
		EFFECTS: restituisce st.keySet().iterator(),
				 iteratore  sull'insieme dei vertici del grafo
				 secondo l'ordine crescente di comparazione (compareTo).
	*/


	/***************************************************************************
						Controlla l'esistenza di un vertice.
	***************************************************************************/
	public boolean existsVertex(E v)
	{
		if( v==null ) throw new NullPointerException("passato oggetto nullo");
		return st.containsKey(v);
	}
	/*
		EFFECTS: restituisce true se st.containsKey(v)==true, false altrimenti.
				 Se v==null lancia NullPointerException, (unchecked).
	*/


	/***************************************************************************
						Controlla l'esistenza di un arco.
	***************************************************************************/
	public boolean existsEdge(E v, E w)
	{
		checkVertex(v);
		checkVertex(w);
		return st.get(v).contains(w) && st.get(w).contains(v);
	}
	/*
		EFFECTS: restituisce true se
				 st.get(v).contains(w)==true && st.get(w).contains(v)==true,
				 false altrimenti.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.get(v)==null || st.get(w)==null lancia
				 IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
				Lancia una eccezione in caso il vertice non esista.
	***************************************************************************/
	private void checkVertex(E v)
	{
		if( !existsVertex(v) )
			throw new IllegalArgumentException(v+" non e' un vertice");
	}


	/***************************************************************************
						Restituisce il numero dei vertici.
	***************************************************************************/
	public int numVertex()
	{
		return st.size();
	}
	/*
		EFFECTS: restituisce st.size(),
				 ovvero il numero di coppie presenti nella mappa.
	*/


	/***************************************************************************
						Restituisce il numero degli archi.
	***************************************************************************/
	public int numEdge()
	{
		return num_edge;
	}
	/*
		EFFECTS: restituisce il contatore num_edge,
				 pari alla metà della somma delle cardinalità
				 dei valori associati alle chiavi.
	*/


	/***************************************************************************
						Aggiunge un vertice al grafo.
	***************************************************************************/
	public void addVertex(E v)
	{
		if( existsVertex(v) )
			throw new MalformedParametersException(v+" e' gia' presente");
		st.put(v, new TreeSet<E>());
		repOk();
	}
	/*
		MODIFIES: st.keySet(), st.values()
		EFFECTS: aggiunge la coppia <v, {}> alla mappa.
				 Se v==null lancia NullPointerException (unchecked).
				 Se prima dell'invocazione st.containsKey(v)==true, lancia
				 MalformedParametersException (unchecked).
	*/


	/***************************************************************************
					Aggiunge un insieme di vertici al grafo.
	***************************************************************************/
	public void addSetVertex(Set<? extends E> vertexes)
	{
		if(vertexes==null)
			throw new NullPointerException("passato insieme nullo");
		vertexes.forEach(v->{
			addVertex(v);
		});
		repOk();
	}
	/*
		MODIFIES: st.keySet(), st.values()
		EFFECTS: perogni v appartenente a vertexes aggiunge la coppia <v,{}>
				 alla mappa.
				 Se vertexes==null lancia NullPointerException (unchecked)
				 Perogni v appartenente a vertexes,
				 se v==null lancia NullPointerException,
				 se prima dell'invocazione st.containsKey(v)==true lancia
				 MalformedParametersException (unchecked).
	*/


	/***************************************************************************
			Aggiunge un vertice al grafo e lo collega a un insieme di
			vertici preesistenti.
	***************************************************************************/
	public void addAttachedVertex(E v, Set<? extends E> neighbours)
	{
		if(neighbours==null)
			throw new NullPointerException("passato insieme nullo");
		addVertex(v);
		neighbours.forEach(w->{
			addEdge(v, w);
		});
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: aggiunge la coppia <v, neighbours> alla mappa,
				 perogni w appartenente a neighbours esegue st.get(w).add(v) e
				 incrementa il contatore num_edge.
				 Se v==null || neighbours==null
				 lancia NullPointerException (unchecked).
				 Se prima dell'invocazione st.containsKey(v)==true, lancia
				 MalformedParametersException (unchecked).
				 Perogni w appartenente a vertexes,
				 se w==null lancia NullPointerException (unchecked),
				 se st.containsKey(w)==false lancia MalformedParametersException
				 (unchecked).
	*/


	/***************************************************************************
						Rimuove un vertice dal grafo.
	***************************************************************************/
	public void removeVertex(E v)
	{
		checkVertex(v);
		for(E neighbour: st.get(v)){
			st.get(neighbour).remove(v);
			num_edge--;
		}
		st.remove(v);
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: perogni neighbour appartenente a st.get(v)
				 esegue st.get(neighbour).remove(v),
				 poi rimuove la coppia <v, st.get(v)> dalla mappa e decrementa
				 il contatore num_edge.
				 Se v==null lancia NullPointerException (unchecked),
				 se prima dell'invocazione st.contains(v)==true lancia
				 MalformedParametersException (unchecked).
	*/


	/***************************************************************************
					Rimuove un insieme di vertici dal grafo.
	***************************************************************************/
	public void removeSetVertex(Set<? extends E> vertexes)
	{
		if(vertexes==null)
			throw new NullPointerException("passato insieme nullo");
		vertexes.forEach(v->{
			removeVertex(v);
		});
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: Perogni v appartenente a vertexes,
				 perogni neighbour appartenente a st.get(v)
				 esegue st.get(neighbour).remove(v),
				 poi rimuove la coppia <v, st.get(v)> dalla mappa e decrementa
				 il contatore num_edge.
				 Se vertexes==null lancia NullPointerException (unchecked).
				 Perogni v appartenente a vertexes se v==null lancia
				 NullPointerException (unchecked),
				 se prima dell'invocazione st.containsKey(v)==false lancia
				 MalformedParametersException (unchecked).
	*/


	/***************************************************************************
							Rimuove tutti i vertici.
	***************************************************************************/
	public void removeAllVertex()
	{
		this.st = new TreeMap<E, TreeSet<E>>();
		this.num_edge = 0;
		repOk();
	}
	/*
		MODIFIES: this
		EFFECTS: alloca un nuovo TreeMap<E, TreeSet<E>> vuoto da assegnare a st
				 e azzera il contatore num_edge.
	*/


	/***************************************************************************
					Aggiunge un arco tra due vertici del grafo.
	***************************************************************************/
	public void addEdge(E v, E w)
	{
		if( existsEdge(v, w) )
			throw new MalformedParametersException( "<"+v+", "+w+">"+
													" è già presente" );
		st.get(v).add(w);
		st.get(w).add(v);
		num_edge++;
		repOk();
	}
	/*
		MODIFIES: st.values(), num_edge
		EFFECTS: aggiunge v all'insieme st.get(w),
				 aggiunge w all'insieme st.get(v) e
				 incrementa il contatore degli archi num_edge.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false || st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked).
				 Se prima dell'invocazione
				 st.get(w).contains(v)==true && st.get(v).contains(w)==true
				 lancia MalformedParametersException (unchecked).
	*/


	/***************************************************************************
			Aggiunge un insieme di archi ad un vertice preesistente.
	***************************************************************************/
	public void addSetEdge(E v, Set<? extends E> neighbours)
	/* questo non significa che Set<?> sia sottotipo di Set<E> */
	{
		if(neighbours==null)
			throw new NullPointerException("passato insieme nullo");
		neighbours.forEach(w->{
			addEdge(v, w);
		});
		repOk();
	}
	/*
		MODIFIES: st.values(), num_edge
		EFFECTS: perogni w appartenente a neighbours esegue st.get(w).add(v),
				 st.get(v).add(w) e incrementa il contatore num_edge.
				 Se v==null || neighbours==null
				 lancia NullPointerException (unchecked).
				 Se prima dell'invocazione st.containsKey(v)==false, lancia
				 IllegalArgumentException (unchecked).
				 Perogni w appartenente a vertexes,
				 se w==null lancia NullPointerException (unchecked),
				 se st.containsKey(w)==false lancia MalformedParametersException
				 (unchecked).
	*/


	/***************************************************************************
							Rimuove un arco dal grafo.
	***************************************************************************/
	public void removeEdge(E v, E w)
	{
		checkVertex(v);
		checkVertex(w);
		if( !st.get(v).remove(w) )
			throw new MalformedParametersException( "<"+v+", "+w+">"+
													" non è presente" );
		num_edge--;
		st.get(w).remove(v);
		repOk();
	}
	/*
		MODIFIES: st.values(), num_edge
		EFFECTS: esegue st.get(v).remove(w), st.get(w).remove(v) e
				 decrementa il contatore num_edge.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false || st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked).
				 Se prima dell'invocazione st.get(v).contains(w)==false
				 lancia MalformedParametersException (unchecked).
	*/


	/***************************************************************************
			Rimuove un insieme di archi da un vertice preesistente.
	***************************************************************************/
	public void removeSetEdge(E v, Set<? extends E> neighbours)
	{
		if(neighbours==null)
			throw new NullPointerException("passato insieme nullo");
		neighbours.forEach(w->{
			removeEdge(v, w);
		});
		repOk();
	}
	/*
		MODIFIES: st.values(), num_edge
		EFFECTS: perogni w appartenente a neighbours esegue
				 st.get(v).remove(w), st.get(w).remove(v) e
				 decrementa il contatore num_edge.
				 Se v==null || neighbours==null
				 lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false
				 lancia IllegalArgumentException (unchecked).
				 Perogni w appartenente a neighbours
				 se w==null lancia NullPointerException,
				 se st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked),
				 se prima dell'invocazione st.get(v).contains(w)==false
				 lancia MalformedParametersException (unchecked).
	*/


	/***************************************************************************
						Rimuove tutti gli archi di un vertice.
	***************************************************************************/
	public void isolateVertex(E v)
	{
		checkVertex(v);
		st.get(v).forEach(friend->{
			st.get(friend).remove(v);
			num_edge--;
		});
		st.put(v, new TreeSet<E>());
		repOk();
	}
	/*
		MODIFIES: st.values(), num_edge
		EFFECTS: Perogni w appartenente a st.get(v) esegue st.get(w).remove(v),
				 st.get(v).remove(w) e decrementa il contatore num_edge .
				 Se v==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false
				 lancia IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
			Restituisce il grado del vertice (numero archi del vertice).
	***************************************************************************/
	public int degreeVertex(E v)
	{
		checkVertex(v);
		return st.get(v).size();
	}
	/*
		EFFECTS: restituisce st.get.size()
				 pari alla cardinalità dell'insieme deli adiacenti a v.
				 Se v==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false
				 lancia IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
					Restituisce l'elenco dei vertici del grafo.
	***************************************************************************/
	public NavigableSet<E> listVertex()
	{
		return Collections.unmodifiableNavigableSet(st.navigableKeySet());
	}
	/*
		EFFECTS: restituisce un insieme navigabile non modificabile
				 dei vertici del grafo.
	*/


	/***************************************************************************
				Restituisce l'elenco degli adiacenti di un vertice.
	***************************************************************************/
	public NavigableSet<E> adjacentVertex(E v)
	{
		checkVertex(v);
		return Collections.unmodifiableNavigableSet(st.get(v));
	}
	/*
		EFFECTS: restituisce un insieme navigabile non modificabile
				 degli adiacenti di un vertice.
	*/


	/***************************************************************************
		Classe privata di oggetti che tengono traccia di:
		distanza e percorso di ciascun vertice da una radice scelta del grafo.
		E' da intendersi come una efficiente rappresentazione di un
		MinimunSpanningTree costruito con l'algoritmo di ricerca BFS. 
	***************************************************************************/
	private class Path{
		Map<E, Integer> distMap = new TreeMap<E, Integer>();
		Map<E, E> prevMap = new TreeMap<E, E>();

		/* Costruttore:
		   istanzia un Path partendo dalla radice passata come primo elemento
		   dei variadic-arguments vrt; se i variadic dovessero essere due,
		   viene costruito un Path parziale da utilizzare per info sullo
		   specifico elemento passato come secondo argomento dei variadic. */
		Path(TreeMap<E, TreeSet<E>> st, Comparable ... vrt)
		{
			@SuppressWarnings("unchecked")
			E[] _vrt = (E[]) vrt;

			Queue<E> queue = new ArrayDeque<E>();
			E head = null;
			queue.add(_vrt[0]);
			distMap.put(_vrt[0], 0);
			if( _vrt.length>1 && _vrt[0].compareTo(_vrt[1])==0 )
				return;
			while( (head=queue.poll())!=null )
				for(E neighbour: st.get(head))
					if(!distMap.containsKey(neighbour)){
						distMap.put(neighbour, distMap.get(head)+1);
						prevMap.put(neighbour, head);
						if( _vrt.length>1 && _vrt[1].compareTo(neighbour)==0 )
							return;
						queue.add(neighbour);
					}
		}
	}


	/***************************************************************************
		Restituisce la distanza tra due vertici,
		-1 se i due vertici non appartengono al medesimo sottografo connesso.
	***************************************************************************/
	public Integer distanceInBetween(E v, E w)
	{
		checkVertex(v);
		checkVertex(w);
		Path p = new Path(this.st, v, w);
		return p.distMap.getOrDefault(w, -1);
	}
	/*
		EFFECTS: restituisce il valore associato a w
				 nella mappa delle distanze creata usando la radice v,
				 -1 se w non è mappato.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false || st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
			Restituisce il percorso (elenco dei vertici intermedi)
			tra due vertici.
	***************************************************************************/
	public Iterable<E> pathInBetween(E v, E w)
	{
		checkVertex(v);
		checkVertex(w);
		Path p = new Path(this.st, v, w);
		Deque<E> stackPath = new ArrayDeque<E>();
		while( (w=p.prevMap.get(w))!=null && w.compareTo(v)!=0 )
			stackPath.addFirst(w);
		return stackPath;
	}
	/*
		EFFECTS: restituisce una Deque<E> contenente i vertici intermedi
				 nel percorso tra v e w.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false || st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
					Restituisce il diametro del grafo:
					il maggiore dei minimi percorsi possibili.
	***************************************************************************/
	public int graphDiameter()
	{
		int max=0;
		for( Map.Entry<E, TreeSet<E>> keyValue: st.entrySet() ){
			Path p = new Path(this.st, keyValue.getKey());
			final int aux = max;
			max = p.distMap.values().stream()
									.max(Integer::compareTo)
									/* .max(i->Integer.compareTo(i)) */
									.filter(tempMax->tempMax>aux)
									.orElse(max);
		}
		return max;
	}
	/*
		EFFECTS: perogni v appartenente a st.keySet() calcola il cammino minimo
				 maggiore usando la mappa delle distanze e
				 restituisce il più grande di quelli trovati.
	*/


	/***************************************************************************
			Dati due nodi, restituisce uno Stream dell'intersezione
			degli insiemi dei loro vicini.
	***************************************************************************/
	public Stream<E> commonNeighbours(E v, E w)
	{
		checkVertex(v);
		checkVertex(w);
		return st.get(v).stream()
						.filter(x->st.get(w).contains(x));
	}
	/*
		EFFECTS: restituisce uno Stream<E> contenete tutti i vertici x tali che
				 st.get(v).contains(x)==true && st.get(w).contains(x)==true.
				 Se v==null || w==null lancia NullPointerException (unchecked).
				 Se st.containsKey(v)==false || st.containsKey(w)==false
				 lancia IllegalArgumentException (unchecked).
	*/


	/***************************************************************************
				Restituisce un array di Object contenente i nodi
				in ordine crescente di comparazione (compareTo).
	***************************************************************************/
	public Object[] toArray()
	{
		return listVertex().toArray();
	}
	/*
		EFFECTS: restituisce un Object[] contenete tutti gli oggetti v tali che
				 st.containsKey(v)==true. 
	*/


	/***************************************************************************
			Override toString di Object:
			restituisce una stringa formata dalla concatenazione delle
			rappresentazioni in striga dei vertici del grafo
			in ordine crescente di comparazione (compareTo).
	***************************************************************************/
	public String toString()
	{
		return st.keySet().toString();
	}
	/*
		EFFECTS: restituisce una String s tala che s = v_1 + v_2 + ... + v_n
				 con v_1, v_2, ..., v_n apparteneti a st.keySet() e
				 n = numVertex().
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
	public boolean equals(Graph<E> h)
	{
		if( !listVertex().equals(h.listVertex()) ) return false;
		for(E x: listVertex())
			if( !adjacentVertex(x).equals(h.adjacentVertex(x)) ) return false;
		return true;
	}
	/*
		EFFECTS: restituisce true se
				 ( listVertex().equals(h.listVertex()) ) &&
				 ( perogni x appartenente a listVertex(),
				 adjacentVertex(x).equals(h.adjacentVertex(x)) ),
				 false altrimenti.
	*/
}


/*******************************************************************************
	Eccezione runtime per la gestione dell'invariante di rappresentazione
*******************************************************************************/
class RepInvariantException extends RuntimeException
{
	public RepInvariantException()
	{
		super();
	}
	
	public RepInvariantException(String str)
	{
		super(str);
	}
}
