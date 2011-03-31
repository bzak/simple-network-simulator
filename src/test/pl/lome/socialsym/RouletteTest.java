package test.pl.lome.socialsym;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.lome.socialsym.Roulette;

public class RouletteTest {

	@Test
	public void testUpdate() {
		Roulette r = new Roulette();
		r.Update(1, 10);
		for (int i = 0; i < r.getWheelSize(); i++) {
			assertSame(r.getWheel().get(i), 1);
		}		
	}
	
	@Test
	public void testUpdate50x50() {
		Roulette r = new Roulette();
		r.Update(1, 10);
		r.Update(2, 10);
		int count1 = 0;
		int count2 = 0;
		
		for (int i = 0; i < r.getWheelSize(); i++) {
			int value = r.getWheel().get(i);
			if (value==1) count1++;
			if (value==2) count2++;
		}		
		
		assertEquals(count1, count2);
	}
	
	@Test
	public void testUpdate50x50more() {
		Roulette r = new Roulette();
		r.Update(1, 10);
		r.Update(2, 10);
		r.Update(3, 10);
		r.Update(4, 10);
		r.Update(5, 10);
		
		
		int count1 = 0;
		int count2 = 0;
		
		for (int i = 0; i < r.getWheelSize(); i++) {
			int value = r.getWheel().get(i);
			if (value==1) count1++;
			if (value==2) count2++;
		}		
		
		assertEquals(count1, count2);
	}
	
	
	@Test
	public void testUpdate50x50more2() {
		Roulette r = new Roulette();
		r.Update(1, 10);
		r.Update(2, 10);
		r.Update(3, 10);
		r.Update(4, 10);
		r.Update(5, 10);
		
		r.Update(1, 125);
		r.Update(2, 125);
		
		int count1 = 0;
		int count2 = 0;
		
		for (int i = 0; i < r.getWheelSize(); i++) {
			int value = r.getWheel().get(i);
			if (value==1) count1++;
			if (value==2) count2++;
		}		
		
		assertEquals(count1, count2);
	}
}
