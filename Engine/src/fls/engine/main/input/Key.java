package fls.engine.main.input;

public class Key{
	
	public int presses,absorbs;
	public boolean down,clicked;
	public final int key;
	
	public Key(Input i, int k){
		i.keys.add(this);
		this.key = k;
	}
	
	public void toggle(boolean pressed){
		if(pressed != down){
			down = pressed;
		}
		
		if(pressed)presses ++;
	}
	
	public void tick(){
		if(absorbs < presses){
			absorbs++;
			clicked = true;
		}else{
			clicked = false;
		}
	}
}
