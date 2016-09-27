package dinnerdecider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * A program that decides what to make for dinner, given a list of meals.
 * A map of the meals, and their associated number and meal types is built.
 * The user is then asked which types they want/don't want, and a filter list is built from that
 * The meals that have types in the filter list are then removed from the meal map
 * A meal is then picked from the map by randomly generating a number, within the correct bounds, and printed to the console 
 * @author Lachlan Lee
 *
 */
public class DinnerDecider {

	/**
	 * Creates the list of possible meals from the given text file
	 * @return map containing the number of the meal as the key, and a Meal object containing its name and filters
	 */
	private static List<Meal> createList(){
		List<Meal> dinnerList = new ArrayList<Meal>();
		
		//Start reading the meal file
		InputStream is = DinnerDecider.class.getResourceAsStream("DinnerList.txt");
		Scanner meals = new Scanner(is);
		
		//For each meal, get its number, name, and filters, then add to the map
		while(meals.hasNext()){
			String name = meals.next();
			
			//Read the meals filter list
			Scanner filters = new Scanner(meals.nextLine());
			
			Set<String> filterList = new HashSet<String>();
			
			//Add each filter to a list
			while(filters.hasNext()){
				filterList.add(filters.next());
			}
			
			//Add the meal to the map
			dinnerList.add(new Meal(name, filterList));
			
			filters.close();
		}
		
		meals.close();
		return dinnerList;
	}
	
	/**
	 * Format the meal names to replace the hyphens with white space
	 * @param meals - the map containing the meals
	 */
	private static void formatNames(List<Meal> meals){
		for(int i = 0; i < meals.size(); i++){
			//Replace all the hyphens in the name with white space
			String newName = meals.get(i).getName().replace('-', ' ');
			//Replace that meals entry with one with the new name
			meals.add(new Meal(newName, meals.get(i).getFilters()));
			meals.remove(meals.get(i));
		}
	}
	
	/**
	 * Create the filter list to allow the removal of meals that you cannot make
	 * @param meals - the map of meals
	 * @return a set of the meal types to filter out
	 */
	private static Set<String> getFilterList(List<Meal> meals){
		Set<String> filterList = new HashSet<String>();
		List<String> unfilter = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		
		//Add all the meal types to the filter list
		for(Meal m : meals){
			for(String filt : m.getFilters()){
				if(!filterList.contains(filt)){ filterList.add(filt);}
			}
		}
		
		//Get the list of types to not filter out
		for(String filter : filterList){
			System.out.print("Do you have/want " + filter + "? (y/n): ");
			if(input.next().equals("y")){ unfilter.add(filter); }
		}
		
		//Remove the types you do not want to filter from the filter list
		for(String unfilt : unfilter){
			filterList.remove(unfilt);
		}
		
		input.close();
		
		return filterList;
	}
	
	/**
	 * Filter out the unwanted meal types
	 * @param meals - map of meals
	 * @param filters - set of meal types to filter
	 */
	private static void filter(List<Meal> meals, Set<String> filters){
		List<Meal> toRemove = new ArrayList<Meal>();
		
		//Get the list of meal numbers to filter out
		for(Meal i : meals){
			for(String filt : i.getFilters()){
				if(filters.contains(filt)){ toRemove.add(i); }
			}
		}
		
		//Remove the filtered meals
		for(Meal i : toRemove){
			meals.remove(meals.indexOf(i));
		}
	}
	
	/**
	 * Decide which meal to make and print to the console
	 * @param meals - map of meals
	 */
	private static void decide(List<Meal> meals){
		Random rand = new Random();
		int mealNumber = 0;
		//Generate a random number between 1 and the number of meals, then print the name attached to that number in the map
		if(meals.size() > 1){
			mealNumber = rand.nextInt(meals.size()-1);
			System.out.println("Tonights Dinner is: " + meals.get(mealNumber).getName());
		}
		//If only one meal in the map, print that
		else if(meals.size() == 1){
			mealNumber = 1;
			System.out.println("Tonights Dinner is: " + meals.get(mealNumber).getName());
		}
		//If there are no meals in the map, print that to the console
		else{
			System.out.println("There are either no meals in your list, or they were all filtered out");
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Dinner Decider!");
		System.out.println("");
		
		//Create list of meals
		List<Meal> dinnerList = createList();
		//Create list of filters
		Set<String> filterList = getFilterList(dinnerList);
		
		System.out.println("");
		
		//Filter out unwanted meals
		filter(dinnerList, filterList);
		
		//Format the names for printing
		formatNames(dinnerList);
		
		//Decide on the meal
		decide(dinnerList);
	}

}
