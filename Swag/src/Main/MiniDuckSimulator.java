package Main;

import Types_of_Ducks.MallardDuck;
import Abstract_Classes.Duck;

public class MiniDuckSimulator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Duck mallard = new MallardDuck();
		mallard.performQuack();
		mallard.performFly();
		mallard.display();
	}

}
