package Abstract_Classes;
import Interfaces.FlyBehavior;
import Interfaces.QuackBehavior;


public abstract class Duck {
	public FlyBehavior flyBehavior;
	public QuackBehavior quackBehavior;

	public Duck(){
		
	}
	
	public abstract void display();
	
	public void performFly(){
		flyBehavior.fly();
	}
	
	public void performQuack(){
		quackBehavior.quack();
	}
	
	public void swim(){
		System.out.println("All ducks floar, even decoys!");
	}
}
