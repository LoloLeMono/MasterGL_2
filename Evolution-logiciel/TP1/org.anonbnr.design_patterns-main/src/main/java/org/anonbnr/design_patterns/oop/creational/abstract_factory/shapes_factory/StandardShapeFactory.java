package org.anonbnr.design_patterns.oop.creational.abstract_factory.shapes_factory;

/**
 * a StandardShapeFactory concrete class that plays the role of a specific 
 * ConcreteFactory in the Abstract Factory design pattern.
 * It defines a factory for the creation of standard shapes.
 * @author anonbnr
 */
public class StandardShapeFactory extends AbstractShapeFactory {

	
	public Shape createShape(ShapeType shapeType) {
		
		if (shapeType == StandardShapeType.RECTANGLE)
			return new Rectangle();
		
		else if (shapeType == StandardShapeType.TRIANGLE)
			return new Triangle();
		
		return null;
	}
}
