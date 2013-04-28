import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import view.AntSimGUI;
import view.ColonyNodeView;
import view.ColonyView;


public class Game {
	
	public String time = "1";
	private int ants = 0;
	private int days = 0;
	public boolean endGame = false;
	
	public AntSimGUI gui;
	public ColonyView colony;
	public ColonyNodeView cnv;
	public static ArrayList<ColonyNodeView> coloniesArray = new ArrayList<ColonyNodeView>();
	public static ArrayList<Ant> antsArray = new ArrayList<Ant>();
	private boolean initializedGame = false;
	public boolean gameOver = false;
	
	public ColonyNodeView center;
	
	public static void main(String [ ] args)
	{
	      Game game = new Game();
	      game.gui = new AntSimGUI();
	      game.colony = new ColonyView(27, 27);
	      game.gui.initGUI(game.colony);
	      while(game.endGame == false){
	    	  game.loop(false);
	      }
	}
	
	public ColonyNodeView orientColony(int x, int y){
		String the_x;
		String the_y;
		if(x < 10){
			the_x = "0"+x;
		}else{
			the_x = x+"";
		}
		if(y < 10){
			the_y = "0"+y;
		}else{
			the_y = y+"";
		}

		String theID = the_x + ":" + the_y;
		
		ColonyNodeView new_cnv = this.createColony(x,y);
		new_cnv.setID(theID);
		coloniesArray.add(new_cnv);
		int top = y - 1;

		String above;
		if(top < 10){
			above = the_x + ":" + ("0" + top);
		}else{
			above = the_x + ":" + top;
		}
		int bottom = y + 1;
		
		String below;
		if(bottom < 10){
			below = the_x + ":" + ("0"+bottom);
		}else{
			below = the_x + ":" + bottom;
		}
		
		
		int _left = x - 1;
		String left;
		if(_left < 10){
			left = ("0" + _left) + ":" + the_y;
		}else{
			left = _left + ":" + the_y;
		}
		
		int _right = x + 1;
		String right;
		if(_right < 10){
			right = ("0"+_right) + ":" + the_y;
		}else{
			right = _right + ":" + the_y;
		}
		

		for(ColonyNodeView cnv : coloniesArray){
			if(cnv.getID().equals(above)){
				new_cnv.above = cnv;
				cnv.below = new_cnv;
			}
			if(cnv.getID().equals(below)){
				new_cnv.below = cnv;
				cnv.above = new_cnv;
			}
			
			if(cnv.getID().equals(left)){
				new_cnv.left = cnv;
				cnv.right = new_cnv;
			}
			
			if(cnv.getID().equals(right)){
				new_cnv.right = cnv;
				cnv.left = new_cnv;
			}
		}
		return new_cnv;
	}
	
	public void createDefaultColonies(){
		for(int i=1; i<=27; i++){
			for(int x = 1; x<=27; x++){
				ColonyNodeView c = this.orientColony(i, x);
				if(i == 11 && x == 11){
					center = c;
					center.showNode();
					center.setQueen(true);
					createAnt("Queen");
					center.setFoodAmount(center.food = 1000);
				}
				if((i == 10 && x == 11) || (i == 12 && x == 11) || 
						(i == 10 && x == 12) || (i == 11 && x == 12) || (i == 12 && x == 12) || 
						(i == 10 && x == 10) || (i == 11 && x == 10) || (i == 12 && x == 10)){
					c.showNode();
					c.discovered = true;
				}
				c.pheromone = 0;
				c.setPheromoneLevel(0);
			}
		}
	}
	
	public void createMultipleAnts(int howMany, String type){
		for(int i = 0; i < howMany; i++){
			createAnt(type);
		}
	}
	
	public ColonyNodeView createColony(Integer pos1, Integer pos2){
		ColonyNodeView new_cnv = new ColonyNodeView();
	    //coloniesArray.add(new_cnv);
	    this.colony.addColonyNodeView(new_cnv, pos1, pos2);
	    return new_cnv;
	}
	
	public void loop(boolean gameOver){
		setGameStats();
		try {
			if (initializedGame == false){
				this.createDefaultColonies();
				initializedGame = true;
				this.createMultipleAnts(10, "Soldier");
				this.createMultipleAnts(50, "Forager");
				this.createMultipleAnts(4, "Scout");
			}
			if(this.gameOver == false){
			    Thread.sleep(1000);
			    this.loop(gameOver);
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
			if(ant.age > 365){
				//gameOver = ant.removeAnt(coloniesArray);
				deadAnts.add(ant);
			}
		}
		for(Ant ant : deadAnts){
			gameOver = ant.removeAnt(coloniesArray);
			antsArray.remove(ant);
		}
	}
	
	public void battle(){
		ArrayList<Ant> balas = new ArrayList<Ant>();
		for(Ant ant : antsArray){
			if(ant.category == "Bala"){
				balas.add(ant);
			}
		}
		ArrayList<Ant> deadAnts = new ArrayList<Ant>();
		for(Ant ant : antsArray){
			for(Ant bala : balas){
				if(ant.cnv.getID().equals(bala.cnv.getID()) && ant.category != "Bala"){
					Random r = new Random();
					int randNum = (r.nextInt(10) + 1) / 10;
					if(randNum == 0){
						this.gameOver = ant.removeAnt(coloniesArray);
						deadAnts.add(ant);
					}
				}
			}
		}
		for(Ant ant : deadAnts){
			antsArray.remove(ant);
		}
	}
	
	public void setGameStats(){
		battle();
		this.setTime();
		this.moveAnts();
		this.setStats();
		
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
	
	public void removeIfNoAnts(){
		
	}
	
	public void moveAnts(){
		for(Ant ant : antsArray){
			if(ant.category == "Forager"){
				ant.moveByPhermone(center);
			}else if(ant.category == "Soldier"){
				if(ant.moveByAttack(antsArray, coloniesArray) == false){
					ant.randomlyMoveAnt();
				}
			}else{
				ant.randomlyMoveAnt();
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
		System.out.print("\n???????????????????????????????"+ant.category+"\n");
		return ant;
	}
	
	
	public String convertToString(int i) {
	    return "" + i;
	}
	
	public void showColonies(){
		for(ColonyNodeView i : coloniesArray){
			i.showNode();
			i.setScoutCount(ants);
		}
	}
	
	public void setNewColony(){
		ColonyNodeView new_cnv = new ColonyNodeView();
		coloniesArray.add(new_cnv);
		this.colony.addColonyNodeView(new_cnv, (2 + ants), (3 + ants));
		//new_cnv.showNode();
		
	}



}
