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
	public Ant(int id) {
		if(id < 1){
			category = "Queen";
		}else{
			category = randomAnt();
		}
		System.out.print("\n???????????????????????????????"+category+"\n");
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
	
	public void showHide(ColonyNodeView moveTo, ColonyNodeView moveFrom){
		if(this.category == "Scout"){
			moveTo.showNode();
			moveTo.discovered = true;
			moveTo.showScoutIcon();
			moveFrom.hideScoutIcon();
			this.cnv = moveTo;
		}else if(this.category == "Forager"){
			if(moveTo.hidden == false){
				moveTo.showForagerIcon();
				moveFrom.hideForagerIcon();
				this.cnv = moveTo;
			}else{
				moveTo = checkAroundAntForOpenNode(moveFrom);
				if(moveTo != null){
					moveTo.showForagerIcon();
					moveFrom.hideForagerIcon();
					this.cnv = moveTo;
				}
			}
		}else if(this.category == "Soldier"){
			if(moveTo.hidden == false){
				moveTo.showSoldierIcon();
				moveFrom.hideSoldierIcon();
				this.cnv = moveTo;
			}else{
				moveTo = checkAroundAntForOpenNode(moveFrom);
				if(moveTo != null){
					moveTo.showSoldierIcon();
					moveFrom.hideSoldierIcon();
					this.cnv = moveTo;
				}
			}
		}else if(this.category == "Bala"){
				if(moveTo != null){
					moveTo.showNode();
					moveTo.showBalaIcon();
					moveFrom.hideBalaIcon();
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
		int randNum = (r.nextInt(40) + 1) / 10;
		if(randNum == 0){
			ant = "Forager";
		}else if(randNum == 1){
			ant = "Scout";
		}else if(randNum == 2){
			ant = "Soldier";
		}else if(randNum == 3){
			ant = "Bala";
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
