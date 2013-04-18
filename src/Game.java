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
				System.out.print("ABOVE------>"+new_cnv.above.getID()+"\n");

				
			}
			if(cnv.getID().equals(below)){
				System.out.print("BELOW------>"+theID+"+\n");
				new_cnv.below = cnv;
				cnv.above = new_cnv;
				
				
			}
			
			if(cnv.getID().equals(left)){
				System.out.print("LEFT------>"+theID+"+\n");
				new_cnv.left = cnv;
				cnv.right = new_cnv;
				
				
			}
			
			if(cnv.getID().equals(right)){
				System.out.print("RIGHT------>"+theID+"+\n");
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
					
				}
			}
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
		ageAnts();
		try {
			if (initializedGame == false){
				this.createDefaultColonies();
				initializedGame = true;
			}
			if(gameOver == false){
			    Thread.sleep(1000);
			    this.loop(gameOver);
			}else{
				JOptionPane.showMessageDialog(this.colony, "GAME OVER."); 
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
			if(ant.age > 100){
				gameOver = ant.removeAnt(coloniesArray);
				deadAnts.add(ant);
			}
		}
		for(Ant ant : deadAnts){
			gameOver = ant.removeAnt(coloniesArray);
			antsArray.remove(ant.id);
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
						gameOver = ant.removeAnt(coloniesArray);
						deadAnts.add(ant);
					}else if(randNum == 1){
						gameOver = ant.removeAnt(coloniesArray);
						deadAnts.add(bala);
					}
				}
			}
		}
		for(Ant ant : deadAnts){
			antsArray.remove(ant.id);
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
		}
		
	}
	
	public void moveAnts(){
		for(Ant ant : antsArray){
			int rand = (int) (Math.random() * 4);
			if(rand == 0){
				if(ant.cnv.above != null){
					ant.cnv = ant.cnv.above;
					ant.showHide(ant.cnv, ant.cnv.below);
					System.out.print("\n.....ABOVE"+ant.cnv.getID());
				}
				
			}else if(rand == 1){
				if(ant.cnv.right != null){
					ant.cnv = ant.cnv.right;
					ant.showHide(ant.cnv, ant.cnv.left);

					System.out.print("\n.....RIGHT"+ant.cnv.getID());
					
				}
			}else if(rand == 2){
				if(ant.cnv.below != null){
					ant.cnv = ant.cnv.below;
					ant.showHide(ant.cnv, ant.cnv.above);
					System.out.print("\n.....BELOW"+ant.cnv.getID());
				}
			}else{
				if(ant.cnv.left != null){
					ant.cnv = ant.cnv.left;
					ant.showHide(ant.cnv, ant.cnv.right);
					System.out.print("\n.....LEFT");
				}
			}
			
		}
	}
	
	public void setTime(){
		int _time = Integer.parseInt(this.time);
		if(_time % 10 == 0){
			days = days + 1;
			createAnt(null);
		}
		int rand = (int) (Math.random() * 9);
		if(rand < 4){
			createAnt("Bala");
		}
		this.ageAnts();
		_time = _time + 1;
		this.gui.setTime(this.time = convertToString(_time));
	}
	
	public Ant createAnt(String category){
		Ant ant = new Ant((antsArray.size()));
		if(category == "Bala"){
			ant.category = "Bala";
		}else if(category == "Queen"){
			ant.category = "Queen";
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
