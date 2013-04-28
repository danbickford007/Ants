import java.util.ArrayList;
import java.util.Random;

import view.ColonyNodeView;
import view.ColonyView;


public class Ant {
	
	public int age = 0;
	public int id = 0;
	public boolean hasFood = false;
	public ColonyNodeView cnv;
	String category = "none";
	public ColonyNodeView lastNode = null;
	ArrayList<ColonyNodeView> nodeHistory = new ArrayList<ColonyNodeView>();
	
	public Ant(int id) {
		this.id = id;
		if(id < 1){
			this.category = "Queen";
		}else{
			this.category = randomAnt();
		}
		System.out.print("\nCATEGORY WITHIN INITIALIZATION"+category+"\n");

	}
	
	public void setColony(ColonyNodeView cnv){
		this.cnv = cnv;
	}
	
	public ColonyNodeView getColony(){
		return this.cnv;
	}
	
	public ColonyNodeView checkAroundAntForOpenNode(ColonyNodeView moveFrom){
		if(moveFrom.above != null && moveFrom.above.hidden == false){
			return moveFrom.above;
		}else if(moveFrom.right != null && moveFrom.right.hidden == false){
			return moveFrom.right;
		}else if(moveFrom.below != null && moveFrom.below.hidden == false){
			return moveFrom.below;
		}else if(moveFrom.left != null && moveFrom.left.hidden == false){
			return moveFrom.left;
		}
		return null;
	}
	
	public static int totalCount(ColonyNodeView cnv, ArrayList<Ant> ants, String category){
		int count = 0;
		for(Ant ant : ants){
			if(ant.category.equals(category) && cnv.getID().equals(ant.cnv.getID()) ){
				count += 1;
			}
		}
		return count;
	}
	
	public void moveByPhermone(ColonyNodeView center){
		if(this.cnv.getID().equals("11:11") && this.hasFood == true){
			this.cnv.food += 1;
			this.cnv.setFoodAmount(this.cnv.food);
			this.hasFood = false;
			if(this.cnv.foragers <= 1){
				this.cnv.hideForagerIcon();
			}
			this.cnv = center;
			this.cnv.showForagerIcon();
			while(this.nodeHistory.size() > 0){
				this.nodeHistory.remove(this.nodeHistory.size() - 1);
			}
			this.nodeHistory.add(this.cnv);
			
		}else if(this.nodeHistory.size() > 0 && this.hasFood == true){
			if(this.cnv.foragers <= 1){
				this.cnv.hideForagerIcon();
			}
			if(this.cnv.pheromone < 1000){
				this.cnv.setPheromoneLevel(this.cnv.pheromone += 10);
			}
			this.cnv = nodeHistory.get(this.nodeHistory.size() - 1);
			this.nodeHistory.remove(this.nodeHistory.size() - 1);
			this.cnv.showForagerIcon();
		}else{
			int above = 0;
			int left = 0;
			int right = 0;
			int bottom = 0;
			
			if(this.cnv.above != null && this.cnv.above.discovered == true){
				above = this.cnv.above.pheromone;
			}
			if(this.cnv.right != null && this.cnv.right.discovered == true){
				right = this.cnv.right.pheromone;
			}
			if(this.cnv.below != null && this.cnv.below.discovered == true){
				bottom = this.cnv.below.pheromone;
			}
			if(this.cnv.left != null && this.cnv.left.discovered == true){
				left = this.cnv.left.pheromone;
			}
			int highest = 0;
			ColonyNodeView highest_cnv = null;
			if(above > highest && this.cnv.above != this.lastNode){
				highest = above;
				highest_cnv = this.cnv.above;
			}
			if(right > highest && this.cnv.right != this.lastNode){
				highest = right;
				highest_cnv = this.cnv.right;
			}
			if(bottom > highest && this.cnv.below != this.lastNode){
				highest = bottom;
				highest_cnv = this.cnv.below;
			}
			if(left > highest && this.cnv.left != this.lastNode){
				highest = left;
				highest_cnv = this.cnv.left;
			}
			if(highest > 0){
				if(!this.cnv.getID().equals(highest_cnv.getID()))
				{
					this.nodeHistory.add(this.cnv);
				}
				if(this.cnv.foragers <= 1){
					this.cnv.hideForagerIcon();
				}
				this.lastNode = this.cnv;
				this.cnv = highest_cnv;
				highest_cnv.showForagerIcon();
				this.takeFood();
			}else{
				this.lastNode = this.cnv;
				ColonyNodeView old = this.cnv;
				this.randomlyMoveAnt();
				this.takeFood();
				if(!old.getID().equals(this.cnv.getID())){
					this.nodeHistory.add(this.cnv);
				}
			}
			

		}
		
	}
	
	public void takeFood(){
		if(this.cnv.food > 0 && this.category == "Forager" && !this.cnv.getID().equals("11:11") && this.hasFood != true){
			this.cnv.food -= 1;
			this.cnv.setFoodAmount(this.cnv.food);
			this.hasFood = true;
			this.nodeHistory.add(this.cnv);
			if(this.cnv.pheromone < 1000){
				this.cnv.setPheromoneLevel(this.cnv.pheromone += 10);
			}
		}
	}
	
	public void randomlyMoveAnt(){
		int rand = (int) (Math.random() * 4);
		if(rand == 0){
			if(this.cnv.above != null){
				this.cnv = this.cnv.above;
				this.showHide(this.cnv, this.cnv.below);
				this.takeFood();
				//System.out.print("\n.....ABOVE"+this.cnv.getID());
			}
			
		}else if(rand == 1){
			if(this.cnv.right != null){
				this.cnv = this.cnv.right;
				this.showHide(this.cnv, this.cnv.left);
				this.takeFood();
				//System.out.print("\n.....RIGHT"+this.cnv.getID());
				
			}
		}else if(rand == 2){
			if(this.cnv.below != null){
				this.cnv = this.cnv.below;
				this.showHide(this.cnv, this.cnv.above);
				this.takeFood();
				//System.out.print("\n.....BELOW"+this.cnv.getID());
			}
		}else{
			if(this.cnv.left != null){
				this.cnv = this.cnv.left;
				this.showHide(this.cnv, this.cnv.right);
				this.takeFood();
				//System.out.print("\n.....LEFT");
			}
		}
	}
	
	public boolean moveByAttack(ArrayList<Ant> ants, ArrayList<ColonyNodeView> colonies){
		boolean moved = false;
		ArrayList<Ant> balas = new ArrayList<Ant>();
		for(Ant ant : ants){
			if(ant.category == "Bala"){
				balas.add(ant);
			}
		}
		Random r = new Random();
		int randNum = (r.nextInt(10) + 1) / 10;
		for(Ant bala : balas){
			if(this.cnv.above != null && bala.cnv.getID().equals(this.cnv.above.getID())){
				if(randNum == 0){
					this.cnv = bala.cnv;
					bala.removeAnt(colonies);
					this.cnv.hideBalaIcon();
					moved = true;
				}
			}else if(this.cnv.left != null && bala.cnv.getID().equals(this.cnv.left.getID())){
				if(randNum == 0){
					this.cnv = bala.cnv;
					bala.removeAnt(colonies);
					this.cnv.hideBalaIcon();
					moved = true;
				}
			}else if(this.cnv.below != null && bala.cnv.getID().equals(this.cnv.below.getID())){
				if(randNum == 0){
					this.cnv = bala.cnv;
					bala.removeAnt(colonies);
					this.cnv.hideBalaIcon();
					moved = true;
				}
			}else if(this.cnv.right != null && bala.cnv.getID().equals(this.cnv.right.getID())){
				if(randNum == 0){
					this.cnv = bala.cnv;
					bala.removeAnt(colonies);
					this.cnv.hideBalaIcon();
					moved = true;
				}
			}
		}
		return moved;
	}
	
	public void showHide(ColonyNodeView moveTo, ColonyNodeView moveFrom){
		if(this.category == "Scout"){
			moveTo.showNode();
			moveTo.discovered = true;
			moveTo.showScoutIcon();
			System.out.println("++++++++++++"+moveFrom.scouts);
			if(moveFrom.scouts <= 1){
				moveFrom.hideScoutIcon();
			}
			this.cnv = moveTo;
		}else if(this.category == "Forager"){
			if(moveTo.hidden == false){
				moveTo.showForagerIcon();
				//if(moveFrom.foragers <= 1){
					moveFrom.hideForagerIcon();
				//}
				this.cnv = moveTo;
			}else{
				moveTo = checkAroundAntForOpenNode(moveFrom);
				if(moveTo != null){
					moveTo.showForagerIcon();
					//if(moveFrom.foragers <= 1){
						moveFrom.hideForagerIcon();
					//}
					this.cnv = moveTo;
				}
			}
		}else if(this.category == "Soldier"){
			if(moveTo.hidden == false){
				moveTo.showSoldierIcon();
				//if(moveFrom.soldiers <= 1){
					moveFrom.hideSoldierIcon();
				//}
				this.cnv = moveTo;
			}else{
				moveTo = checkAroundAntForOpenNode(moveFrom);
				if(moveTo != null){
					moveTo.showSoldierIcon();
					//if(moveFrom.soldiers <= 1){
						moveFrom.hideSoldierIcon();
					//}
					this.cnv = moveTo;
				}
			}
		}else if(this.category == "Bala"){
				if(moveTo != null){
					moveTo.showNode();
					moveTo.showBalaIcon();
					//if(moveFrom.balas <= 1){
						moveFrom.hideBalaIcon();
					//}
					if(moveFrom.discovered == false){
						moveFrom.hideNode();
					}
					this.cnv = moveTo;
				}
		}
	}
	
	
	public String randomAnt(){
		String ant = "";
		Random r = new Random();
		int randNum = (r.nextInt(30) + 1) / 10;
		if(randNum == 0 || randNum == 1){
			ant = "Forager";
		}else if(randNum == 2){
			ant = "Scout";
		}else if(randNum == 3){
			ant = "Soldier";
		}else{
			System.out.print("! ! ! ! ! ! !! ! ! ! ! ! !OUT OF RANGE RANDOM_ANT");
		}
		return ant;
	}
	
	public ColonyNodeView findHiddenNode(Game game, String position){
		String x = this.cnv.getID().split(":")[0];
		String y = this.cnv.getID().split(":")[1];
		
		int _y = Integer.parseInt(y);
		int _x = Integer.parseInt(x);
		int top;
		String above;
		boolean topFirst = false;
		if(position == "above"){
			top = _y - 1;
			above = cleanPosition(top);
		}else if(position == "right"){
			top = _x + 1;
			above = cleanPosition(top);
			topFirst = true;
		}else if(position == "bottom"){
			top = _y + 1;
			above = cleanPosition(top);
		}else{
			top = _x - 1;
			above = cleanPosition(top);
			topFirst = true;
		}

		
		int _above = Integer.parseInt(above);
		ColonyNodeView cnv;
		if(topFirst == true){
			cnv = game.orientColony(_above, _y);
		}else{
			cnv = game.orientColony(_x, _above);
		}
		return cnv;
	}
	
	public String cleanPosition(int top){
		String above;
		if(top < 10){
			above = ("0"+top);
		}else{
			above = top+"";
		}
		return above;
	}
	
	public boolean removeAnt(ArrayList<ColonyNodeView> colonies){
		boolean gameOver = false;
		for(ColonyNodeView cnv : colonies){
			if(cnv.getID().equals(this.cnv.getID())){
				if(this.category == "Queen"){
					gameOver = true;
					System.out.println("QUEEN HAS BEEN KILLED!!!!!");
				}else if(this.category == "Bala"){
					this.cnv.hideBalaIcon();
					if(this.cnv.discovered == false){
						this.cnv.hideNode();
					}
				}else if(this.category == "Scout"){
					this.cnv.hideScoutIcon();
				}else if(this.category == "Soldier"){
					this.cnv.hideSoldierIcon();
				}else if(this.category == "Forager"){
					this.cnv.hideForagerIcon();
				}
			}
		}
		return gameOver;
	}


}
