package Types_of_Ducks;

import Abstract_Classes.Duck;
import FlyingType.FlyWithWings;
import Quacking.Quack;

public class MallardDuck extends Duck{
	
	public MallardDuck(){
		quackBehavior = new Quack();
		flyBehavior = new FlyWithWings();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		System.out.println("I'm a real Mallard Duck");
	}

}
