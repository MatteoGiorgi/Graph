/* SocialNetTest primo progetto (PR2B) Matteo Giorgi 517183, Novembre 2016 */

import java.util.Scanner;
import java.util.HashSet;
import java.io.FileReader;
import java.io.IOException;


/*******************************************************************************
					Classe per il test della rete sociale
*******************************************************************************/
public class SocialNetTest
{
	static Scanner sc = new Scanner(System.in);

	public static void
	main(String[] args) throws IOException
	{

		/* Costruisco i grafi alpha e beta */
		GraphMap<String> alpha = Graphs.asGraph(new FileReader("friends.txt"));
		GraphMap<String> beta = new GraphMap<String>();
		Graphs.readAndFill(beta, new FileReader("friends.txt"));

		/* Disegno i grafi alpha e beta */
		Graphs.drawGraph(alpha, "alpha");
		Graphs.drawGraph(beta, "beta");
		Graphs.waitOneSec(2);
		
		/* Stampo le info di alpha e beta*/
		System.out.print("\n\"alpha\", "); Graphs.printInfo(alpha);
		System.out.print("\"beta\", "); Graphs.printInfo(beta);

		/* Mostro che alpha e beta sono uguali */
		System.out.println("I grafi \"alpha\" e \"beta\" sono uguali: "+
							alpha.equals(beta));

		/* Richiedo operazione */
		String name = null;
		for(;;){
			System.out.print("Scegliere il grafo da elaborare (alpha/beta),"+
								" 0 per terminare: ");
			switch( name=sc.next() ){
				case "alpha": break;
				case "beta": break;
				case "0": System.exit(0);
				default:
					System.out.println("Inserire \"alpha\", \"beta\","+
										" 0 per terminare: ");
					continue;
			}
			System.out.println(
				"\nScegliere l'operazione da effettuare sul grafo \""+name+"\""+
				"\n0.Nessuna operazione"+
				"\n1.Info su singolo vertice"+
				"\n2.Info su una coppia di vertici"+
				"\n3.Rimozione vertice"+
				"\n4.Rimozione insieme di vertici"+
				"\n5.Inserimento vertice"+
				"\n6.Inserimento insieme di vertici"+
				"\n7.Inserimento vertice già collegato"+
				"\n8.Isola vertice"+
				"\n9.Rimozione arco"+
				"\n10.Rimozione insieme di archi"+
				"\n11.Inserimento arco"+
				"\n12.Inserimento insieme di archi"+
				"\n13.Svuota grafo   "
			);

			/* Eseguo operazione e ridisegno il grafo */
			int intIn = -1;
			SocialNetTest.operation(name.equals("alpha")?alpha:beta,
									intIn=sc.nextInt());
			if(intIn>2){
				Graphs.drawGraph(name.equals("alpha")?alpha:beta, name);
				Graphs.waitOneSec(2);

				/* Stampo le info di alpha e beta e li confronto */
				System.out.print("\n\"alpha\", "); Graphs.printInfo(alpha);
				System.out.print("\"beta\", "); Graphs.printInfo(beta);
				System.out.println("I grafi \"alpha\" e \"beta\" sono uguali: "+
									alpha.equals(beta));
			}
		}
	}


	private static void operation(GraphMap<String> g, int op)
	{
		switch(op){

			/* Info su un singolo vertice */
			case 1:
				System.out.print("\nInserire un vertice:   ");
				String aux = sc.next();
				System.out.print("\nGrado = "+g.degreeVertex(aux)+
								 "\nInsieme degli amici = ");
				g.adjacentVertex(aux).forEach(s->System.out.print(s+" "));
				System.out.println("\n");
				break;

			/* Info su una coppia di vertice */
			case 2:
				System.out.print("\nInserire due vertici:   ");
				String x = sc.next(),
					   y = sc.next();
				System.out.print(
					"\nDistanza = "+g.distanceInBetween(x, y)+
					"\nPercorso = ");
				g.pathInBetween(x, y).forEach(s->System.out.print(s+" "));
				System.out.print("\nAmici comuni = ");
				g.commonNeighbours(x, y).forEach(k->System.out
														  .print(k+" "));
				System.out.println("\n");
				break;

			/* Rimozione vertice */
			case 3:
				System.out.print("\nVertice da rimuovere:   ");
				g.removeVertex(sc.next());
				break;

			/* Rimozione insieme di vertici */
			case 4:
				System.out.print(
					"\nVertici da rimuovere (terminare elenco con //):   "
				);
				HashSet<String> hh = new HashSet<String>();
				for(
					String auxx=null;
					!(auxx=sc.next()).equals("//");
					hh.add(auxx)
				);
				g.removeSetVertex(hh);
				break;

			/* Inserimento vertice */
			case 5:
				System.out.print("\nVertice da aggiungere:   ");
				g.addVertex(sc.next());
				break;

			/* Inserimento insieme di vertici */
			case 6:
				System.out.print(
					"\nVertici da aggiungere (terminare elenco con //):   "
				);
				HashSet<String> sh = new HashSet<String>();
				for(
					String oux=null;
					!(oux=sc.next()).equals("//");
					sh.add(oux)
				);
				g.addSetVertex(sh);
				break;

			/* Inserimento vertice già collegato */
			case 7:
				System.out.print("\nVertice da aggiungere:   ");
				String v = sc.next();
				System.out.print(
					"\nAmici da collegare (terminare elenco con //):   "
				);
				HashSet<String> hs = new HashSet<String>();
				for(
					String eux=null;
					!(eux=sc.next()).equals("//");
					hs.add(eux)
				);
				g.addAttachedVertex(v, hs);
				break;

			/* Isola vertice */
			case 8:
				System.out.print("\nVertice da isolare:   ");
				g.isolateVertex(sc.next());
				break;

			/* Rimozione arco */
			case 9:
				System.out.print("\nVertici da scollegare:   ");
				g.removeEdge(sc.next(), sc.next());
				break;

			/* Rimozione insieme di archi */
			case 10:
				System.out.print("\nVertice da modificare:   ");
				String n = sc.next();
				System.out.print(
					"\nArchi da rimuovere (terminare elenco con //):   "
				);
				HashSet<String> shh = new HashSet<String>();
				for(
					String iux=null;
					!(iux=sc.next()).equals("//");
					shh.add(iux)
				);
				g.removeSetEdge(n, shh);
				break;

			/* Inserimento arco */
			case 11:
				System.out.print("\nVertici da collegare:   ");
				g.addEdge(sc.next(), sc.next());
				break;

			/* Inserimento insieme di archi */
			case 12:
				System.out.print("\nVertice da modificare:   ");
				String w = sc.next();
				System.out.print(
					"\nAmici da collegare (terminare elenco con //):   "
				);
				HashSet<String> ss = new HashSet<String>();
				for(
					String uux=null;
					!(uux=sc.next()).equals("//");
					ss.add(uux)
				);
				g.addSetEdge(w, ss);
				break;

			/* Svuota grafo */
			case 13:
				g.removeAllVertex();
				break;

			default:
				System.out.println();
				break;
		}/* switch */
	}/* operation */
}/* class */