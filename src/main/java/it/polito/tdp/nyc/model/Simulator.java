package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.model.Event.Type;

public class Simulator {
	//stato del sistema
	private Map<NTA, Integer> filePerNTA;
	private Graph<NTA, DefaultWeightedEdge> grafo;
	private List<NTA> listaNTA;
	//parametri di input
	private final int probShare;
	private  final int durata;
	private final int termine;
	// coda
	PriorityQueue<Event> queue;
	//output
	private Map <NTA,Integer>numTotShare;
	
	
	public Simulator(int durata, int prob, List<NTA> ntaList, Graph<NTA, DefaultWeightedEdge> grafo) {
		this.probShare=prob;
		this.durata=durata;
		this.listaNTA=ntaList;//lista serve per estrarre casualmente i vertici del grafo!
		this.termine=100;
		this.grafo=grafo;
		
	}

	public void initialize() {
		this.filePerNTA=new HashMap<NTA,Integer>();
		this.numTotShare=new HashMap<NTA,Integer>();
		for (NTA nta: listaNTA) {
			this.filePerNTA.put(nta, 0);
			this.numTotShare.put(nta, 0);
		}
		this.queue=new PriorityQueue<Event>();
		
		//creo eventi iniziali!
		
		int giorno=1;
		while(giorno<=100) {
			if(Math.random()<=this.probShare) {
				//condivido
				double ntaRandom=Math.random();
				
				NTA nta=this.listaNTA.get((int)ntaRandom*this.listaNTA.size());
				this.queue.add(new Event(giorno,Type.pubblicazione, nta,this.durata));
			}
			giorno++;
		}
		this.run();
		
	}

	private void run() {
		while(this.queue.isEmpty()) {
			Event e=this.queue.poll();
			if (e.getGiorno()>100) {
				break;
			}
			Type tipo=e.getTipo();
			int giorno=e.getGiorno();
			int duration=e.getDurata();
			switch (tipo){
			case pubblicazione:
				int numeroFile=this.filePerNTA.get(e.getNta());
				this.filePerNTA.put(e.getNta(), numeroFile+1);
				this.numTotShare.put(e.getNta(), numeroFile+1);
				this.queue.add(new Event(giorno+duration,Type.rimozione,e.getNta(),-1));
				if(duration/2>0) {
				NTA scelto=ntaScelto(e.getNta());
				//potrei anche non trovare nta!!
				if(scelto!=null) {
					this.queue.add(new Event(giorno+1,Type.pubblicazione,ntaScelto(e.getNta()),(int)e.getDurata()/2));
					}
				}
				break;
			case rimozione:
				this.filePerNTA.put(e.getNta(), this.filePerNTA.get(e.getNta())-1);
				break;
				
			}
		}
	}
	
	private NTA ntaScelto(NTA nta) {
		int pesoMax=-1;
		NTA selezionato=null;
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(nta)) {
			NTA vicino=Graphs.getOppositeVertex(this.grafo, e, nta);
			int peso=(int)this.grafo.getEdgeWeight(e);
			if(peso>pesoMax && this.filePerNTA.get(vicino)==0) {
				pesoMax=peso;
				selezionato=vicino;	
			}
		}
		
//		List<NTA> adiacenti=new ArrayList<>(Graphs.neighborListOf(this.grafo, nta));
//		for (NTA cand: adiacenti) {
//			if(this.filePerNTA.get(cand)>0) {
//				adiacenti.remove(cand);
//			}
//		}
//		NTA selezionato=null;
//		int pesoMax=-1;
//		for(NTA n:adiacenti) {
//			DefaultWeightedEdge e=this.grafo.getEdge(nta, n);
//			if(this.grafo.getEdgeWeight(e)>pesoMax) {
//				selezionato=n;
//				pesoMax=(int)this.grafo.getEdgeWeight(e);
//			}
			
	//	}
		
		
		return selezionato;
	}

}
