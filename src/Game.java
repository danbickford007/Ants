import java.awt.List;	
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import view.AntSimGUI;
import view.ColonyNodeView;
import view.ColonyView;
import view.SimulationEvent;
import view.SimulationEventListener;


public class Game implements SimulationEventListener{
	
	public String time = "1";
	int ants = 0;
	private int days = 0;
	public boolean endGame = false;
	
	public AntSimGUI gui;
	public ColonyView colony;
	public ColonyNodeView cnv;
	public ArrayList<ColonyNodeView> coloniesArray = new ArrayList<ColonyNodeView>();
	public ArrayList<Ant> antsArray = new ArrayList<Ant>();
	private boolean initializedGame = false;
	public boolean gameOver = false;
	public SimulationEvent eventHandler;
	public boolean step = false;
	public boolean pressed = false;
	
	public ColonyNodeView center;
	ColonyNodePresenter presenter;
	
	public static void main(String [ ] args)
	{
	      Game game = new Game();
	      game.gui = new AntSimGUI();
	      game.eventHandler = new SimulationEvent(game, 1);
	      game.gui.addSimulationEventListener(game);
	      game.colony = new ColonyView(27, 27);
	      game.gui.initGUI(game.colony);
	      game.presenter = new ColonyNodePresenter(game);
	      //while(game.endGame == false){
	    	  game.loop(false, false);
	      //}
	    	  
	}
	
	public void createMultipleAnts(int howMany, String type){
		for(int i = 0; i < howMany; i++){
			createAnt(type);
		}
	}
	
	public void setUp(){
		presenter.createDefaultColonies();
		initializedGame = true;
		this.createMultipleAnts(10, "Soldier");
		this.createMultipleAnts(50, "Forager");
		this.createMultipleAnts(4, "Scout");
	}
	
	
	
	public void loop(boolean gameOver, boolean initializedGame){
		simulationEventOccurred(this.eventHandler);
		this.gameOver = gameOver;
		this.initializedGame = initializedGame;
		setGameStats();
		System.out.println("+++"+this.gameOver+"::"+initializedGame+"::"+step+"::"+pressed);
		try {
			if(this.gameOver == false){
				if (this.initializedGame == true){
					if(this.step == false){
						Thread.sleep(1000);
						this.loop(this.gameOver, this.initializedGame);
					}else{
						while(this.step == true){
							this.pressed = false;
							Thread.sleep(500);
							simulationEventOccurred(this.eventHandler);
							if(this.pressed == true || this.step == false){
								this.loop(this.gameOver, this.initializedGame);
							}
						}
					}		
				}else{
					while(this.initializedGame == false){
						simulationEventOccurred(this.eventHandler);
						if(this.initializedGame == true){
							this.loop(this.gameOver, this.initializedGame);

						}
					}
				}
			}else{
				JOptionPane.showMessageDialog(this.colony, "GAME OVER."); 
				System.exit(0);
			}
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		    System.out.print("FAIL IN GAME LOOP");
		}
	}
	
	public void ageAnts(){
		ArrayList<Ant> deadAnts = new ArrayList<Ant>();
		for(Ant ant : antsArray){
			ant.age += 1;
			if(ant.age > 365 && ant.category != "Queen"){
				//gameOver = ant.removeAnt(coloniesArray);
				deadAnts.add(ant);
			}
		}
		for(Ant ant : deadAnts){
			gameOver = ant.removeAnt(coloniesArray);
			antsArray.remove(ant);
		}
	}
	
	public void setGameStats(){
		this.gameOver = presenter.moveAndBattle(antsArray, coloniesArray);
		//presenter.moveAnts();
		//this.gameOver = presenter.battle(antsArray, coloniesArray);
		setTime();
		setStats();
		
	}
	
	public void setStats(){
		int soldiers = 0;
		int balas = 0;
		int scouts = 0;
		int foragers = 0;
		for(ColonyNodeView cnv : coloniesArray){
			soldiers = 0;
			balas = 0;
			scouts = 0;
			foragers = 0;
			soldiers += Ant.totalCount(cnv, antsArray, "Soldier");
			balas += Ant.totalCount(cnv, antsArray, "Bala");
			foragers += Ant.totalCount(cnv, antsArray, "Forager");
			scouts += Ant.totalCount(cnv, antsArray, "Scout");
			cnv.setSoldierCount(soldiers);
			cnv.setBalaCount(balas);
			cnv.setForagerCount(foragers);
			cnv.setScoutCount(scouts);
			if(scouts < 1){
				cnv.hideScoutIcon();
			}
			if(balas < 1){
				cnv.hideBalaIcon();
			}
			if(soldiers < 1){
				cnv.hideSoldierIcon();
			}
			if(foragers < 1){
				cnv.hideForagerIcon();
			}
		}
		
	}
	
	public void setTime(){
		int _time = Integer.parseInt(this.time);
		if(_time % 10 == 0){
			days = days + 1;
			createAnt(null);
			decreaseAllPheromones();
			ageAnts();
		}
		int rand = (int) (Math.random() * 9);
		if(rand < 4){
			createAnt("Bala");
		}
		this.ageAnts();
		queenEatOneUnit();
		_time = _time + 1;
		this.gui.setTime(this.time = convertToString(_time));
	}
	
	public void queenEatOneUnit(){
		if(center != null){
			center.setFoodAmount(center.food -= 1);
			if(center.food < 1){
				this.gameOver = true;
			}
		}
	}
	
	public void decreaseAllPheromones(){
		for(ColonyNodeView cnv : coloniesArray){
			if(cnv.pheromone > 0){
				if(cnv.pheromone > 9){
					cnv.setPheromoneLevel(cnv.pheromone -= 10);
				}else{
					cnv.setPheromoneLevel(0);
					cnv.pheromone = 0;
				}
			}
		}
	}
	
	public Ant createAnt(String category){
		Ant ant = new Ant((antsArray.size()));
		if(category == "Bala"){
			ant.category = "Bala";
		}else if(category == "Queen"){
			ant.category = "Queen";
		}else if(category == "Soldier"){
			ant.category = "Soldier";
		}
		if(ant.category == "Bala"){
			for(ColonyNodeView node : coloniesArray){
				if(node.getID().equals("01:01")){
					node.showNode();
					node.showBalaIcon();
					ant.cnv = node;
					antsArray.add(ant);
					this.ants += 1;
					
					
				}
			}
		}else{
			if(ant.category == "Queen"){
				center.showQueenIcon();
			}else if(ant.category == "Scout"){
				center.showScoutIcon();
			}else if(ant.category == "Forager"){
				center.showForagerIcon();
				ant.nodeHistory.add(center);
			}else if(ant.category == "Soldier"){
				center.showSoldierIcon();
			}
			ant.cnv = center;
			antsArray.add(ant);
			this.ants += 1;
		}
		return ant;
	}
	
	
	public String convertToString(int i) {
	    return "" + i;
	}

	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {
		if(simEvent.getEventType() == 6){
			this.step = true;
			this.pressed = true;
		}else if(simEvent.getEventType() == 5){
			this.step = false;
		}else if(simEvent.getEventType() == 0){
			this.initializedGame = true;
			this.setUp();
		}
		System.out.println(this.step+"><><><><><><<>"+this.pressed+"<><><><><><><"+simEvent.getEventType());
	}
	
	



}
