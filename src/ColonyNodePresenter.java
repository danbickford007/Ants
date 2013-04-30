

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import view.ColonyNodeView;
import view.ColonyView;

public class ColonyNodePresenter {
	
	public Game game;


	public ColonyNodePresenter(Game game){
		this.game = game;
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
		this.game.coloniesArray.add(new_cnv);
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
		

		for(ColonyNodeView cnv : this.game.coloniesArray){
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
				ColonyNodeView c = orientColony(i, x);
				if(i == 11 && x == 11){
					game.center = c;
					game.center.showNode();
					game.center.setQueen(true);
					game.createAnt("Queen");
					game.center.setFoodAmount(game.center.food = 1000);
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
	
	
	
	public ColonyNodeView createColony(Integer pos1, Integer pos2){
		ColonyNodeView new_cnv = new ColonyNodeView();
	    //coloniesArray.add(new_cnv);
	    this.game.colony.addColonyNodeView(new_cnv, pos1, pos2);
	    return new_cnv;
	}
	
	public void showColonies(){
		for(ColonyNodeView i : game.coloniesArray){
			i.showNode();
			i.setScoutCount(game.ants);
		}
	}
	
	public void setNewColony(){
		ColonyNodeView new_cnv = new ColonyNodeView();
		game.coloniesArray.add(new_cnv);
		game.colony.addColonyNodeView(new_cnv, (2 + game.ants), (3 + game.ants));
		
	}
	
	public boolean moveAndBattle(ArrayList<Ant> antsArray, ArrayList<ColonyNodeView> coloniesArray){
		this.moveAnts();
		boolean gameOver = this.battle(antsArray, coloniesArray);
		return gameOver;
	}
	
	public void moveAnts(){
		for(Ant ant : game.antsArray){
			if(ant.category != "Queen"){
				if(ant.category == "Forager"){
					ant.moveByPhermone(game.center);
				}else if(ant.category == "Soldier"){
					if(ant.moveByAttack(game.antsArray, game.coloniesArray) == false){
						ant.randomlyMoveAnt();
					}
				}else{
					ant.randomlyMoveAnt();
				}
			}
		}
	}
	
	public boolean battle(ArrayList<Ant> antsArray, ArrayList<ColonyNodeView> coloniesArray){
		boolean gameOver = false;
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
					int randNum = (r.nextInt(20)) / 10;
					System.out.println("_______________>"+randNum+":::"+ant.category);
					if(randNum == 0){
						System.out.println("KILLED==========="+ant.category);
						gameOver = ant.removeAnt(coloniesArray);
						deadAnts.add(ant);
						if(ant.category == "Queen"){
							System.out.println("BALA KILLED QUEEN!!!!"+ant.cnv.getID()+"::::::::::::"+gameOver);
							ant.cnv.showBalaIcon();
						}
						return gameOver;
					}
				}
			}
		}
		for(Ant ant : deadAnts){
			antsArray.remove(ant);
		}
		return gameOver;
	}
	
	
}
