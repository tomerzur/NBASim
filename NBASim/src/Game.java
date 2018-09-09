import java.util.ArrayList;
import java.util.Random;

// controls the playing out of a game. Adds on to the game stats throughout the simulation
public class Game {

  GameStats stats;
  
  Random r = new Random();
  
  Game(Team homeTeam, Team awayTeam) {
    this.stats = new GameStats(homeTeam, awayTeam);
  }
  
  // simulates a full game between the homeTeam and the awayTeam
  void playGame() {
    this.jumpBall();
    this.simQuarter(1);
    this.simQuarter(2);
    this.simQuarter(3);
    this.simQuarter(4);
    int overtimeNumber = 1;
    while (this.stats.homeTeamScore == this.stats.awayTeamScore) {
      this.simQuarter(overtimeNumber + 4);
    }
  }

  // simulates the jump ball that occurs at the beginning of a game
  void jumpBall() {
    Player player1 = this.stats.homeTeam.startingLineup.c;
    Player player2 = this.stats.awayTeam.startingLineup.c;
    ArrayList<Double> jumpBallValues = new ArrayList<>();
    jumpBallValues.add(100 + Math.pow(player1.height + player1.wingspan - player2.height - player2.wingspan, 2));
    if (Math.pow(player1.height + player1.wingspan - player2.height - player2.wingspan, 2) > 100) {
      jumpBallValues.add(0.0);
    }
    else {
      jumpBallValues.add(100 - Math.pow(player1.height + player1.wingspan - player2.height - player2.wingspan, 2));
    }
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    Player jumpBallWinner = this.weightedSelect(jumpBallValues, players);
    Team offensiveTeam = jumpBallWinner.team;
    Team defensiveTeam = player1.team;
    if (defensiveTeam.sameTeam(offensiveTeam)) {
      defensiveTeam = player2.team;
    }
    this.stats.currentOffensiveFive = offensiveTeam.startingLineup;
    this.stats.currentDefensiveFive = defensiveTeam.startingLineup;
    this.stats.addToPlayByPlay("12:00    Jump ball between " + player1.firstName + " " + player1.lastName +
        " and " + player2.firstName + " " + player2.lastName + ". " + jumpBallWinner.firstName + " " + jumpBallWinner.lastName + 
        " won the jump ball.");
  }

  // simulates a quarter of basketball
  // if the quarter number is greater than 4, then it represents an overtime, with 5 representing the 1st overtime, 6 representing the 2nd overtime, etc.
  // the quarterNumber given must be >= 1
  void simQuarter(int quarterNumber) {
    this.stats.quarterNumber = quarterNumber;
    if (quarterNumber <= 4) {
      this.stats.addToPlayByPlay("Quarter " + quarterNumber);
      this.stats.secondsLeftInQuarter = 720;
    }
    else {
      this.stats.addToPlayByPlay("Overtime" + (quarterNumber - 4));
      this.stats.secondsLeftInQuarter = 300;
    }
    if (quarterNumber == 4) {
      if (this.stats.homeTeamTimeoutsLeft > 4) {
        this.stats.homeTeamTimeoutsLeft = 4;
      }
      if (this.stats.awayTeamTimeoutsLeft > 4) {
        this.stats.awayTeamTimeoutsLeft = 4;
      }
    }
    if (quarterNumber > 4) {
      if (this.stats.homeTeamTimeoutsLeft > 2) {
        this.stats.homeTeamTimeoutsLeft = 2;
      }
      if (this.stats.awayTeamTimeoutsLeft > 2) {
        this.stats.awayTeamTimeoutsLeft = 2;
      }
    }
    while (this.stats.secondsLeftInQuarter > 0) {
      Possession currentPossession = new Possession(this.stats);
      Lineup oldDefensiveFive = this.stats.currentDefensiveFive;
      this.stats.currentDefensiveFive = this.stats.currentOffensiveFive;
      this.stats.currentOffensiveFive = oldDefensiveFive;
      
    }
    if (quarterNumber <= 4) {
      this.stats.addToPlayByPlay("0:00    Quarter " + quarterNumber + " has ended.");
    }
    else {
      this.stats.addToPlayByPlay("0:00    Overtime" + (quarterNumber - 4) + " has ended.");
    }
  }
  
  // Given two arrayLists of values and objects that are the same size (each value in the first arrayList has a corresponding 
  // object in the second arrayList), randomly chooses an object using the weighted values given in the first arrayList
  <E> E weightedSelect(ArrayList<Double> valueList, ArrayList<E> objects) {
    if (valueList.size() != objects.size()) {
      throw new IllegalArgumentException("two given arrayLists in weightedSelect must be the same size");
    }
    double totalWeight = 0;
    for (int i = 0; i < valueList.size(); i++) {
      totalWeight += valueList.get(i);
    }
    double rand = r.nextDouble() * totalWeight;
    double currentWeight = 0;
    for (int i = 0; i < valueList.size(); i++) {
      double currentValue = valueList.get(i);
      E currentObject = objects.get(i);
      currentWeight += currentValue;
      if (currentWeight >= rand) {
        return currentObject;
      }
    }
    throw new RuntimeException("error with weightedSelect method");
  }
}
