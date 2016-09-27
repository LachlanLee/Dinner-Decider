package dinnerdecider;

import java.util.Set;

/**
 * An object holding the name of the meal and the types it has
 * @author Lachlan
 *
 */
public class Meal {

	//Name of the meal
	private String name;
	//The meal types
	private Set<String> filters;
	
	public Meal(String name, Set<String> filters){
		this.name = name;
		this.filters = filters;
	}
	
	public String getName(){
		return name;
	}
	
	public Set<String> getFilters(){
		return filters;
	}
}
