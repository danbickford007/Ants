
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import view.ColonyNodeView;


public class AntTest {
	
	public ColonyNodeView center = new ColonyNodeView();
	

	@Test
	public void findsNonHiddenNode() {
		Ant ant = setNodeRelationships();
		ColonyNodeView theNode = ant.checkAroundAntForOpenNode(ant.cnv);
		assertEquals(theNode, ant.cnv.above);
	}
	
	@Test
	public void returnsNullIfNoNodeFound() {
		Ant ant = setNodeRelationships();
		ant.cnv.above = null;
		ant.cnv.right = null;
		ant.cnv.below = null;
		ant.cnv.left = null;
		ColonyNodeView theNode = ant.checkAroundAntForOpenNode(ant.cnv);
		assertEquals(null, theNode);
	}
	
	@Test
	public void calculatesTotalAmountOfAntsByCategory() {
		Ant ant = setNodeRelationships();
		ArrayList<Ant> ants = new ArrayList<Ant>();
		ant.category = "Bala";
		ants.add(ant);
		int count = ant.totalCount(ant.cnv, ants, "Bala");
		assertEquals(1, count);
	}
	
	@Test
	public void movesToHighestPhermone() {
		Ant ant = setNodeRelationships();
		ant.moveByPhermone(center);
		assertEquals("12:11", ant.cnv.getID());
	}
	
	@Test
	public void antTakesFood() {
		Ant ant = setNodeRelationships();
		ant.category = "Forager";
		ant.cnv = ant.cnv.above;
		ant.takeFood();
		assertEquals(true, ant.hasFood);
	}
	
	@Test
	public void antRandomlyMovesToAnotherOpenNode() {
		Ant ant = setNodeRelationships();
		ColonyNodeView node = ant.cnv;
		ant.randomlyMoveAnt();
		boolean eq = false;
		if(ant.cnv.getID().equals(node.getID())){
			eq = true;
		}
		assertEquals(false, eq);
	}
	
	@Test
	public void antMovesAndAttacksBalaIfInReach() {
		Ant ant = setNodeRelationships();
		ColonyNodeView node = ant.cnv;
		Ant bala = new Ant(55);
		bala.category = "Bala";
		bala.cnv = node.above;
		ArrayList<Ant> ants = new ArrayList<Ant>();
		ants.add(ant);
		ants.add(bala);
		ArrayList<ColonyNodeView> colonies = new ArrayList<ColonyNodeView>();
		colonies.add(ant.cnv);
		colonies.add(bala.cnv);
		ant.moveByAttack(ants, colonies);
		assertEquals(false, ants.contains(bala));
	}
		
	public Ant setNodeRelationships(){
		center.setID("11:11");
		
		Ant ant = new Ant(1);
		
		ColonyNodeView node = new ColonyNodeView();
		node.pheromone = 1;
		node.discovered = true;
		node.setID("12:12");
		node.hidden = false;
		ant.cnv = node;
		
		ColonyNodeView node2 = new ColonyNodeView();
		node2.pheromone = 2;
		node2.discovered = true;
		node2.setID("13:12");
		node2.hidden = false;
		ant.cnv.right = node2;
		node2.left = ant.cnv;
		
		ColonyNodeView node3 = new ColonyNodeView();
		node3.pheromone = 3;
		node3.discovered = true;
		node3.setID("12:13");
		node3.hidden = false;
		ant.cnv.below = node3;
		node3.above = ant.cnv;
		
		ColonyNodeView node4 = new ColonyNodeView();
		node4.pheromone = 4;
		node4.discovered = true;
		node4.setID("11:12");
		node4.hidden = false;
		ant.cnv.left = node4;
		node4.right = ant.cnv;
		
		ColonyNodeView node5 = new ColonyNodeView();
		node5.pheromone = 5;
		node5.discovered = true;
		node5.setID("12:11");
		node5.hidden = false;
		node5.food = 50;
		ant.cnv.above = node5;
		node5.below = ant.cnv;
		
		return ant;
	}

}
