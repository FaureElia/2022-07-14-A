package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private List<String> boroughs;
	private List<NTA> NTAs;
	private Graph<NTA,DefaultWeightedEdge> grafo;
	
	public List<String> getBoroughs(){
		if (this.boroughs==null) {
			NYCDao dao=new NYCDao();
			this.boroughs=dao.getAllBoroughs();
		}
		return boroughs;
	}
	
	public void creagRafo(String borough) {
		NYCDao dao=new NYCDao();
		this.NTAs=dao.getNTAbyBorough(borough);
		System.out.println(this.NTAs);
		
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.NTAs);
		
		for(NTA n1:this.NTAs) {
			for(NTA n2: this.NTAs) {
				if (!n1.equals(n2)) {
					Set<String> unione=new HashSet<String>(n1.getSSIDs());
					unione.addAll(n2.getSSIDs());// se ci sono elementi duplicati 
					Graphs.addEdge(this.grafo,n1,n2,unione.size());
				}
			}
		}
		System.out.println("Vertici: "+this.grafo.vertexSet().size());
		System.out.println("Archi: "+this.grafo.edgeSet().size());
		
	}
	
	public List<Arco> analisiArchi(){
		double media=0;
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			media+=this.grafo.getEdgeWeight(e);
		}
		media=media/this.grafo.edgeSet().size();
		List<Arco> result=new ArrayList<Arco>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e)>media) {
				result.add(new Arco(this.grafo.getEdgeSource(e).getNTACode(),this.grafo.getEdgeSource(e).getNTACode(),(int)this.grafo.getEdgeWeight(e)));
			}
		}
		Collections.sort(result);
		return result;
		
		
	}

	public void simula(int durata, int prob) {
	Simulator sim=new Simulator(durata,prob,new ArrayList<>(this.grafo.vertexSet()),this.grafo);
	sim.initialize();
		
	}
	
}
