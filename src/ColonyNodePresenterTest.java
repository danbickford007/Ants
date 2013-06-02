import static org.junit.Assert.*;

import org.junit.Test;

import view.ColonyNodeView;
import view.ColonyView;


public class ColonyNodePresenterTest {

	public ColonyNodeView center = new ColonyNodeView();
	
	@Test
	public void linksNodeAtLeft() {
		Game game = getGame();
		game.presenter.createDefaultColonies();
		ColonyNodeView node = game.presenter.orientColony(11, 13);
		assertEquals("10:13", node.left.getID());
	}
	
	@Test
	public void linksNodeAtBelow() {
		Game game = getGame();
		game.presenter.createDefaultColonies();
		ColonyNodeView node = game.presenter.orientColony(11, 13);
		assertEquals("11:14", node.below.getID());
	}
	
	@Test
	public void linksNodeAtRight() {
		Game game = getGame();
		game.presenter.createDefaultColonies();
		ColonyNodeView node = game.presenter.orientColony(11, 13);
		assertEquals("12:13", node.right.getID());
	}
	
	@Test
	public void linksNodeAtAbove() {
		Game game = getGame();
		game.presenter.createDefaultColonies();
		ColonyNodeView node = game.presenter.orientColony(11, 13);
		assertEquals("11:12", node.above.getID());
	}
	
	@Test
	public void createsAllNeededColonies() {
		Game game = getGame();
		game.presenter.createDefaultColonies();
		boolean lt = false;
		boolean rt = false;
		boolean lb = false;
		boolean rb = false;
		for(ColonyNodeView node : game.coloniesArray){
			if(node.getID().equals("01:01")){
				lt = true;
			}
			if(node.getID().equals("01:27")){
				rt = true;
			}
			if(node.getID().equals("27:01")){
				lb = true;
			}
			if(node.getID().equals("27:27")){
				rb = true;
			}
		}
		assertEquals(true, lt);
		assertEquals(true, rt);
		assertEquals(true, lb);
		assertEquals(true, rb);
	}
	
	@Test
	public void movesAnAnt() {
		Game game = getGame();
		Ant ant = new Ant(0);
		game.presenter.createDefaultColonies();
		for(ColonyNodeView colony : game.coloniesArray){
			if(colony.getID().equals("11:11")){
				ant.cnv = colony;
			}
		}
		game.presenter.moveAnts();
		assertNotSame("11:11", ant.cnv.getID());
		
	}
	
	public Game getGame(){
		Game game = new Game();
		game.colony = new ColonyView(27, 27);
		game.presenter = new ColonyNodePresenter(game);
		game.presenter.createDefaultColonies();
		return game;

	}


}
