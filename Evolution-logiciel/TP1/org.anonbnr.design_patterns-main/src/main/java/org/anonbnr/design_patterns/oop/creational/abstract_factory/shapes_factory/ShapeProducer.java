package org.anonbnr.design_patterns.oop.creational.abstract_factory.shapes_factory;

import org.anonbnr.design_patterns.oop.creational.singleton.singleton_registry.Singleton;

/**
 * a ShapeProducer concrete class is a factory that creates 
 * concrete shape factories that are then used to create 
 * corresponding concrete products.
 * @author anonbnr
 */
public class ShapeProducer {
	/* METHODS */
	public static AbstractShapeFactory createFactory(ShapeFactoryType type) {
		if (type == ShapeFactoryType.STANDARD)
			return (StandardShapeFactory) Singleton.getInstance(StandardShapeFactory.class);
		
		else if (type == ShapeFactoryType.ROUNDED)
			return (RoundedShapeFactory) Singleton.getInstance(RoundedShapeFactory.class);
		
		return null;
	}
}
