package com.horia.chapter13;

import java.awt.Shape;

import com.horia.chapter6.BaseGameEntity;

public class VectorEntity extends BaseGameEntity {
	// variables
	private Shape shape;

	// accessor methods
	public Shape getShape() {
		return shape;
	}

	// mutator methods
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	// default constructor
	VectorEntity() {
		setShape(null);
	}
}