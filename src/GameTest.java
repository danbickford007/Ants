import static org.junit.Assert.*;

import org.junit.Test;

import view.AntSimGUI;
import view.ColonyNodeView;
import view.ColonyView;


public class GameTest {
	
	Game game = getGame();

	@Test
	public void createsMultipleAnts() {
		game.createMultipleAnts(5, "Scout");
		assertEquals(6, game.antsArray.size());
	}
	
	@Test
	public void createsDefaultAnts() {
		game.setUp();
		assertEquals(66, game.antsArray.size());
	}
	
	@Test
	public void agesAllAnts() {
		boolean incorrect = false;
		game.setUp();
		game.ageAnts();
		for(Ant ant : game.antsArray){
			if(ant.age != 1){
				incorrect = true;
			}
		}
		assertEquals(false, incorrect);
	}
	
	@Test
	public void incrementsTime() {
		game.setTime();
		assertEquals("1", game.time);
	}
	
	@Test
	public void queenEatsOneUnitPerTurn() {
		game.queenEatOneUnit();
		int food = 0;
		for(ColonyNodeView colony : game.coloniesArray){
			if(colony.getID().equals("11:11")){
				food = colony.food;
			}
		}
		assertEquals(999, food);
	}
	
	@Test
	public void decreasesAllPheromoneOneUnit(){
		ColonyNodeView cnv = new ColonyNodeView();
		for(ColonyNodeView colony : game.coloniesArray){
			if(colony.getID().equals("10:10")){
				cnv = colony;
			}
		}
		cnv.pheromone = 1000;
		game.decreaseAllPheromones();
		assertEquals(999, cnv.pheromone);
	}

	public Game getGame(){
		Game game = new Game();
		game.colony = new ColonyView(27, 27);
		game.presenter = new ColonyNodePresenter(game);
		game.presenter.createDefaultColonies();
		game.gui = new AntSimGUI();
		game.gui.initGUI(game.colony);
		return game;
	}
	
}
