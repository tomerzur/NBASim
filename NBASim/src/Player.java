// a player has all sorts of data, such as attributes, biometrics, and position stored about them
public class Player {

  String firstName;
  String lastName;
  int height; // in inches
  int wingspan; // in  inches
  int weight; // in lbs
  Birthday birthday;
  int yearsOfExperience; // 0 = rookie, 1 = sophomore, etc.
  
  Team team;
  Contract contract;
  String primaryPosition; // PG, SG, SF, PF, or C
  String secondaryPosition; // PG, SG, SF, PF, C, or None
  
  // these ratings range from 25 (worst possible) to 99 (best possible)
  int threePtOpen;
  int threePtContested;
  int midrangeOpen;
  int midrangeContested;
  int closeOpen;
  int closeContested;
  int layupContested;
  int block;
  int steal;
  int perimeterDefense;
  int interiorDefense;
  int offensiveRebound;
  int defensiveRebound;
  int scoringAbility;
  int speed;
  int strength;
  int switchability;
  int passingAccuracy;
  int passingVision;
  int feelForGame;
  int freeThrow;
  int ballhandling;
  int takeCharge;
  int postupScoring;
  
  // frequencies range from 0 to 1 with 1 being very frequently, 0 being never
  double threePtFrequency;
  double midrangeFrequency;
  double layupFrequency;
  double postupFrequency;
  double freeThrowFrequency;
  double hasBallFrequency; // how much time a player has the ball for per possession
  
  int fatigueLevel; // 0 = not tired at all, 99 is max tiredness
  
  Player(String firstName, String lastName, Team team, String pos1, String pos2) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.team = team;
    this.primaryPosition = pos1;
    this.secondaryPosition = pos2;
    this.fatigueLevel = 0;
  }
  
  // creates a player without assigning them a team
  Player(String firstName, String lastName, String pos1, String pos2) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.primaryPosition = pos1;
    this.secondaryPosition = pos2;
    this.fatigueLevel = 0;
  }
  
  // used as a placeholder for a player value that represents null
  Player() {
    this.firstName = "null player";
  }

  // returns whether a given player is the same player as this player
  boolean samePlayer(Player otherPlayer) {
    return this.firstName.equals(otherPlayer.firstName) && this.lastName.equals(otherPlayer.lastName) 
        && this.height == otherPlayer.height && this.weight == otherPlayer.weight;
  }
  
  
}
