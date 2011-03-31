package pl.lome.socialsym;

import java.util.HashMap;
import java.util.Random;

import org.omg.CORBA.Object;

public class Roulette {
	private HashMap<Integer, Integer> wheel; /* random => slot */
	private HashMap<Integer, Integer> slotSize; /* slot => size */
	private int wheelSize;
	private Random random;
	
	public Roulette() {
		this.wheel = new HashMap<Integer, Integer>();
		this.slotSize = new HashMap<Integer, Integer>();
		this.wheelSize = 0;
		this.random = new Random();
	}
	
	public int RandomSlot() {
		int key = random.nextInt(wheelSize);
		return wheel.get(key);
	}
	
	public void Update(java.lang.Object slot, int size) throws NumberFormatException {
		this.Update(Integer.parseInt(slot.toString()), size);
	}
	
	public void Update(int slot, int size) {
		Integer oldSize = 0;
		if (slotSize.containsKey(slot)) {
			oldSize = slotSize.get(slot);
		}
		if (oldSize < size) {
			// add numbers to wheel
			for (int i = 0; i < size-oldSize; i++) {
				wheel.put(wheelSize, slot);
				wheelSize++;
			}
		}
		if (oldSize > size) {
			throw new RuntimeException("Not implemented");
		}
		slotSize.put(slot, size);
	}


	public HashMap<Integer, Integer> getWheel() {
		return wheel;
	}

	public int getWheelSize() {
		return wheelSize;
	}


}
