import java.awt.List;
import java.util.ArrayList;

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
	
	public ColonyNodeView center;
	
	public static void main(String [ ] args)
	{
	      Game game = new Game();
	      game.gui = new AntSimGUI();
	      game.colony = new ColonyView(27, 27);
	      game.gui.initGUI(game.colony);
	      while(game.endGame == false){
	    	  game.loop();
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
			//System.out.print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n"+"__"+cnv.getID()+"__"+below+"__"+"\n");
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
			
//			if(new_cnv.above != null){
//				System.out.print(">>>>>THE ID<<<<<<<<\n"+theID+"\n");
//				System.out.print(">>>>>above<<<<<<<<\n"+new_cnv.above.getID().toString()+"\n");
//			}
//			if(new_cnv.below != null){
//				System.out.print(">>>>>THE ID<<<<<<<<\n"+theID+"\n");
//				System.out.print(">>>>>below<<<<<<<<\n"+new_cnv.below.getID().toString()+"\n");
//			}
//			if(new_cnv.left != null){
//				System.out.print(">>>>>THE ID<<<<<<<<\n"+theID+"\n");
//				System.out.print(">>>>>left<<<<<<<<\n"+new_cnv.left.getID().toString()+"\n");
//			}
//			if(new_cnv.right != null){
//				System.out.print(">>>>>THE ID<<<<<<<<\n"+theID+"\n");
//				System.out.print(">>>>>right<<<<<<<<\n"+new_cnv.right.getID().toString()+"\n");
//			}
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
	
	public void loop(){
		setGameStats();
		ageAnts();
		try {
			if (initializedGame == false){
				this.createDefaultColonies();
				initializedGame = true;
			}
		    Thread.sleep(1000);
		    this.loop();
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		    System.out.print("FAIL IN GAME LOOP");
		}
	}
	
	public void ageAnts(){
		for(Ant ant : antsArray){
			ant.age += 1;
		}
	}
	
	public void setGameStats(){
		
		//this.setNewColony();
		this.setTime();
		this.moveAnts();
		//this.showColonies();
		//this.cnv.setScoutCount(ants);
	}
	
	public void moveAnts(){
		for(Ant ant : antsArray){
			int rand = (int) (Math.random() * 4);
			if(rand == 0){
				if(ant.cnv.above != null){
					ant.cnv = ant.cnv.above;
					ant.showHide(ant.cnv, ant.cnv.below);
//					ant.cnv.below.hideQueenIcon();
//					ant.cnv.showQueenIcon();
//					ant.cnv.showNode();
					System.out.print("\n.....ABOVE"+ant.cnv.getID());
				}
//				else if (ant.category == "Scout"){
//					ant.cnv = ant.findHiddenNode(this, "above");
//				}
//				if(ant.cnv != null){
					
//				}
				
			}else if(rand == 1){
				if(ant.cnv.right != null){
					ant.cnv = ant.cnv.right;
					ant.showHide(ant.cnv, ant.cnv.left);
//					ant.cnv.left.hideQueenIcon();
//					ant.cnv.showQueenIcon();
//					ant.cnv.showNode();

					System.out.print("\n.....RIGHT"+ant.cnv.getID());
					
				}
//				else if (ant.category == "Scout"){
//					ant.cnv = ant.findHiddenNode(this, "right");
//				}
				//if(ant.cnv != null){
					
				//}
			}else if(rand == 2){
				if(ant.cnv.below != null){
					ant.cnv = ant.cnv.below;
					ant.showHide(ant.cnv, ant.cnv.above);
//					ant.cnv.above.hideQueenIcon();
//					ant.cnv.showQueenIcon();
//					ant.cnv.showNode();
					System.out.print("\n.....BELOW"+ant.cnv.getID());
				}
//				else if (ant.category == "Scout"){
//					ant.cnv = ant.findHiddenNode(this, "bottom");
//				}
//				if(ant.cnv != null){
					
//				}
			}else{
				if(ant.cnv.left != null){
					ant.cnv = ant.cnv.left;
					ant.showHide(ant.cnv, ant.cnv.right);
//					ant.cnv.right.hideQueenIcon();
//					ant.cnv.showQueenIcon();
//					ant.cnv.showNode();
					System.out.print("\n.....LEFT");
				}
//				else if (ant.category == "Scout"){
//					ant.cnv = ant.findHiddenNode(this, "left");
//				}
//				if(ant.cnv != null){
					
//				}
			}
			
		}
	}
	
	public void setTime(){
		int _time = Integer.parseInt(this.time);
		if(_time % 10 == 0){
			days = days + 1;
			Ant ant = new Ant((antsArray.size()));
			//ant.category = "Scout";
			ant.cnv = center;
			center.showQueenIcon();
			antsArray.add(ant);
			this.ants += 1;
		}
		this.ageAnts();
		_time = _time + 1;
		this.gui.setTime(this.time = convertToString(_time));
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
