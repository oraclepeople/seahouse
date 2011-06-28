package org.hf;
import java.util.EnumSet;

/**
 * Very good samples !!
 * 
 * @author cuscab
 *
 */
public final class EnumExamples {

	public static final void main(String... aArgs){
		log("Exercising enumerations...");
		exerEnumsMiscellaneous();
		exerMutableEnum();
		exerEnumRange();
		exerBitFlags();
		exerEnumToStringAndValueOf();
		log("Done.");
	}

	private static void log(Object aText){
		System.out.println(String.valueOf(aText));
	}

	/** Example 1 - simple list of enum constants.  */
	enum Quark {
		/*
		 * These are called "enum constants".
		 * An enum type has no instances other than those defined by its
		 * enum constants. They are implicitly "public static final".
		 * Each enum constant corresponds to a call to a constructor.
		 * When no args follow an enum constant, then the no-argument constructor
		 * is used to create the corresponding object.
		 */
		UP,
		DOWN,
		CHARM,
		STRANGE,
		BOTTOM,
		TOP
	}

	//does not compile, since Quark is "implicitly final":
	//  private static class Quarky extends Quark {}

	/**
	 * Example 2 - adding a constructor to an enum.
	 *
	 * If no constructor is added, then the usual default constructor
	 * is created by the system, and declarations of the
	 * enum constants will correspond to calling this default constructor.
	 */
	public enum Lepton {
		//each constant implicity calls a constructor :
		ELECTRON(-1, 1.0E-31),
		NEUTRINO(0, 0.0);

		/* 
		 * This constructor is private.
		 * Legal to declare a non-private constructor, but not legal
		 * to use such a constructor outside the enum.
		 * Can never use "new" with any enum, even inside the enum 
		 * class itself.
		 */
		private Lepton(int aCharge, double aMass){
			//cannot call super ctor here
			//calls to "this" ctors allowed
			fCharge = aCharge;
			fMass = aMass;
		}
		
		final int getCharge() {
			return fCharge;
		}
		
		final double getMass() {
			return fMass;
		}
		
		private final int fCharge;
		private final double fMass;
	}

	/**
	 * Example 3 - adding methods to an enum.
	 *
	 * Here, "static" may be left out, since enum types which are class
	 * members are implicitly static.
	 */
	static enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST; //note semicolon needed only when extending behavior

		//overrides and additions go here, below the enum constants

		public String toString(){
			/*
			 * Either name() or super.toString() may be called here.
			 * name() is final, and always returns the exact name as specified in
			 * declaration; toString() is not final, and is intended for presentation
			 * to the user. It seems best to call name() here.
			 */
			return "Direction: " + name();
		}

		/** An added method.  */
		public boolean isCold() {
			//only NORTH is 'cold'
			return  this == NORTH;
		}

	}

	/**
	 * Example 4 - adding a method which changes the state of enum constants.
	 */
	private enum Flavor {
		CHOCOLATE(100),
		VANILLA(120),
		STRAWBERRY(80);

		void setCalories(int aCalories){
			//changes the state of the enum 'constant'
			fCalories = aCalories;
		}

		int getCalories(){
			return fCalories;
		}

		private Flavor(int aCalories){
			fCalories = aCalories;
		}

		private int fCalories;

	}

	/*
	 * What follows are various methods which exercise the above enums. 
	 */

	private static void exerEnumsMiscellaneous(){

		//toString method by default uses the identifier
		log("toString: " + Quark.BOTTOM);

		//equals and == amount to the same thing
		if ( Quark.UP == Quark.UP ) {
			log("UP == UP");
		}

		if ( Quark.UP.equals(Quark.UP) ) {
			log("UP.equals(UP)");
		}

		//compareTo order is defined by order of appearance in the definition of
		//the enum
		if ( Quark.UP.compareTo(Quark.DOWN) < 0 ) {
			//this branch is chosen
			log("UP before DOWN");
		}
		else if ( Quark.UP.compareTo(Quark.DOWN) > 0 ) {
			log("Error !! DOWN before UP");
		}
		else {
			log("Error !! UP same as DOWN");
		}

		//values() returns Quark[], not List<Quark>
		log("Quark values : " + Quark.values());
		
		//the order of values matches the order of appearance :
		for ( Quark quark : Quark.values() ){
			log("   Item in Quark.values() : " + quark);
		}

		log("toString : " + Direction.NORTH);
		if ( Direction.EAST.isCold() ){
			log("East is cold");
		}
		else {
			log("East is not cold.");
		}

		log("Electron charge : " + Lepton.ELECTRON.getCharge());

		//parsing text into an enum constant :
		Lepton lepton = Enum.valueOf(Lepton.class, "ELECTRON");
		log("Lepton mass : " + lepton.getMass());

		//throws IllegalArgumentException if text is not known to enum type :
		try {
			Lepton anotherLepton = Enum.valueOf(Lepton.class, "Proton");
		}
		catch (IllegalArgumentException ex){
			log("Proton is not a Lepton.");
		}

		//More compact style for parsing test :
		Lepton thirdLepton = Lepton.valueOf("NEUTRINO");
		log("Neutrino charge : " + thirdLepton.getCharge() );
	}

	private static void exerMutableEnum(){
		Flavor.VANILLA.setCalories(75); //change the state of the enum "constant"
		log("Calories in Vanilla: " + Flavor.VANILLA.getCalories());
	}

	private static void exerEnumRange(){
		for (Direction direction : EnumSet.range(Direction.NORTH, Direction.SOUTH)){
			log("NORTH-SOUTH: " + direction);
		}
	}

	private static void exerBitFlags(){
		EnumSet<Direction> directions = EnumSet.of(Direction.EAST, Direction.NORTH);
		for(Direction direction : directions) {
			log(direction);
		}
	}

	/**
	 * The valueOf method uses name(), not toString(). There is no need
	 * to synchronize valueOf with toString.
	 */
	private static void exerEnumToStringAndValueOf(){
		Direction dir = Direction.valueOf("EAST"); //successful
		log("Direction toString : " + dir);
		try {
		  dir = Direction.valueOf("Direction: EAST"); //fails
		}catch (Exception e) {
			log("Exception !!! "  +  e.getMessage());
		}
	}
}

