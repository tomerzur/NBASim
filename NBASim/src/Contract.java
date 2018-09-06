import java.util.ArrayList;

public class Contract {

  int numOfYears;
  ArrayList<Integer> salariesEachYear; // salariesEachYear[0] = year 1 salary, salariesEachYear[1] = year 2 salary,
  //... salariesEachYear[n] = year n salary
  
  Contract(int numOfYears, int baseSalary, int percentIncrease) {
    this.numOfYears = numOfYears;
    this.createContract(baseSalary, percentIncrease);
  }
  
  private void createContract(int baseSalary, int percentIncrease) {
    int yearISalary = baseSalary;
    for (int i = 0; i < this.numOfYears; i++) {
      this.salariesEachYear.add(yearISalary);
      yearISalary = yearISalary + (yearISalary * percentIncrease);
    }
    
  }
}
