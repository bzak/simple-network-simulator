package pl.lome.socialsym;

import java.awt.Color;
import java.util.Random;

import com.tinkerpop.blueprints.pgm.Vertex;

public class Attitude {
	private Vertex v;
	private String propertyName;
	
	public Attitude(Vertex v) {
		this.v = v;
		this.propertyName = "value";
	}
	
	public Attitude(Vertex v, String property) {
		this.v = v;
		this.propertyName = property;
	}

	public double getValue() {
		Object value = v.getProperty(propertyName);
		if (value != null) {
			return Double.parseDouble(v.getProperty(propertyName).toString());
		}
		return 0;
	}

	public void setValue(double value) {
		v.setProperty(propertyName, value);
		String hex = Integer.toHexString(new Color((float) value, 0.5F, 0.5F)
				.getRGB());
		hex = hex.substring(2, hex.length());
		//v.setProperty("color", hex);
	}

	public boolean influence(Vertex influenced, double weight) {
		return influenceWeightedAverage(influenced, weight);
	}
	
	private boolean influence(Vertex influenced) {
		//return influenceCopying(influenced);
		return influenceAverage(influenced);
	}

	/**
	 * Influence attitude of another vertex
	 * 
	 * @param influenced
	 * @return true if value has changed
	 */
	public boolean influenceWeightedAverage(Vertex influenced, double weight) {
		Attitude influencedAttitude = new Attitude(influenced);
		// if (getValue() != influencedAttitude.getValue()) {
		if (getValue() > influencedAttitude.getValue()) {
			double newValue = (getValue()*weight + influencedAttitude.getValue()*(1-weight));
			influencedAttitude.setValue(newValue);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Influence attitude of another vertex
	 * 
	 * @param influenced
	 * @return true if value has changed
	 */
	public boolean influenceAverage(Vertex influenced) {
		Attitude influencedAttitude = new Attitude(influenced);
		if (getValue() != influencedAttitude.getValue()) {
			double newValue = (getValue() + influencedAttitude.getValue()) / 2;
			influencedAttitude.setValue(newValue);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Homophily and Contagion Are Generically Confounded in Observational
	 * Social Network Studies Cosma Rohilla Shalizi Department of Statistics,
	 * Carnegie Mellon University Santa Fe Institute Andrew C. Thomas Department
	 * of Statistics, Carnegie Mellon University
	 * 
	 * @param influenced
	 * @return
	 */
	public boolean influenceCopying(Vertex influenced) {
		Attitude influencedAttitude = new Attitude(influenced);
		if (getValue() != influencedAttitude.getValue()) {

			double newValue = getValue();
			if (new Random().nextDouble() > 0.95) {
				// with very low probability revert the value
				newValue = 1 - getValue();
			}
			influencedAttitude.setValue(newValue);
			return true;
		} else {
			return false;
		}
	}

	public void infect(double weight) {
		//this.setValue(1);
		
		double newValue = (1*weight + getValue()*(1-weight));
		this.setValue(newValue);
		
		//this.setValue(Math.max(0.99, Math.abs(new Random().nextGaussian())));
	}

	public void initialValue() {
		this.setValue(0);
	}
}
