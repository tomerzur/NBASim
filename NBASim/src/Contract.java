import java.util.ArrayList;

// represents a player's contract, with an arrayList representing a player's salary in each year of his contract
public class Contract {

  int numOfYears;
  ArrayList<Integer> salariesEachYear; // salariesEachYear[0] = year 1 salary, salariesEachYear[1] = year 2 salary,
  //... salariesEachYear[n] = year n salary
  
  // creates a contract for a given amount of years and salary and percentage increase
  Contract(int numOfYears, int baseSalary, int percentIncrease) {
    this.numOfYears = numOfYears;
    this.createContract(baseSalary, percentIncrease);
  }
  
  // creates an empty contract
  Contract() {
    
  }
  
  // fills in this.salariesEachYear given a base salary (the year 1 salary) and a percentage by which the value of the contract increases each year
  private void createContract(int baseSalary, int percentIncrease) {
    int yearISalary = baseSalary;
    for (int i = 0; i < this.numOfYears; i++) {
      this.salariesEachYear.add(yearISalary);
      yearISalary = yearISalary + (yearISalary * percentIncrease);
    }
    
  }
}
