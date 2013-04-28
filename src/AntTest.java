import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import view.ColonyNodeView;


public class AntTest {

	@Test
	public void findsNonHiddenNode() {
		Ant ant = setNodeRelationships();
		ColonyNodeView theNode = ant.checkAroundAntForOpenNode(ant.cnv);
		assertEquals(theNode, ant.cnv.right);
	}
	
	@Test
	public void returnsNullIfNoNodeFound() {
		Ant ant = setNodeRelationships();
		ant.cnv.right = null;
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
	
	public Ant setNodeRelationships(){
		Ant ant = new Ant(1);
		ColonyNodeView node = new ColonyNodeView();
		node.setID("12:12");
		node.hidden = false;
		ant.cnv = node;
		ColonyNodeView node2 = new ColonyNodeView();
		node2.setID("12:13");
		node2.hidden = false;
		ant.cnv.right = node2;
		return ant;
	}

}
