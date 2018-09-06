import java.util.ArrayList;
import java.util.Random;

public class Possession {

  int timeOfEvent; // in seconds
  GameStats statsSoFar;
  Lineup offensiveFive;
  Lineup defensiveFive;
  int quarterNumber;
  int timeLeftInQuarter;
  int timeLeftOnShotClock;
  String minTimeInQuarter;
  boolean homeTeamOnOffense;
  
  // These values are on a scale from 1 to 1000. 1 being this event almost never happens and 1000 being this event happens very often.
  // Ex: pgPostup will usually be near 1, shootingFoul is usually close to 1000
  double pgShoots3ptValue;
  double pgShootsMidrangeValue;
  double pgShootsLayupValue;
  double pgPostupValue;
  double sgShoots3ptValue;
  double sgShootsMidrangeValue;
  double sgShootsLayupValue;
  double sgPostupValue;
  double sfShoots3ptValue;
  double sfShootsMidrangeValue;
  double sfShootsLayupValue;
  double sfPostupValue;
  double pfShoots3ptValue;
  double pfShootsMidrangeValue;
  double pfShootsLayupValue;
  double pfPostupValue;
  double cShoots3ptValue;
  double cShootsMidrangeValue;
  double cShootsLayupValue;
  double cPostupValue;
  double shootingFoulValue;
  double nonShootingFoulValue;
  double shotClockViolationValue;
  double passOutOfBoundsValue;
  double offensiveFoulValue;
  double jumpBallValue;
  double stealValue;
  // There are 27 total values
  ArrayList<Double> values = new ArrayList<>();
  double totalValue;
  boolean endOfPossession;
  Random r = new Random();
  
  Possession(GameStats statsSoFar) {
    this.statsSoFar = statsSoFar;
    this.offensiveFive = this.statsSoFar.currentOffensiveFive;
    this.defensiveFive = this.statsSoFar.currentDefensiveFive;
    if (this.offensiveFive.team.sameTeam(this.statsSoFar.homeTeam)) {
      this.homeTeamOnOffense = true;
    }
    else {
      this.homeTeamOnOffense = false;
    }
    this.quarterNumber = this.statsSoFar.quarterNumber;
    this.timeLeftInQuarter = this.statsSoFar.secondsLeftInQuarter;
    this.timeLeftOnShotClock = 24;
    this.endOfPossession = false;
    while (!this.endOfPossession) {
      if (this.timeLeftInQuarter == 720) {
        this.checkForSubstitutions();
      }
      if (this.timeLeftInQuarter == 0) {
        this.endOfPossession = true;
      }
      else {
        this.setValues();
        this.setTimeOfEvent();
        this.checkForTimeout();
        this.chooseEvent();
      }
    }
  }

  void setValues() {
    this.setPgShoots3ptValue();
    this.values.add(this.pgShoots3ptValue);
    this.setSgShoots3ptValue();
    this.values.add(this.sgShoots3ptValue);
    this.setSfShoots3ptValue();
    this.values.add(this.sfShoots3ptValue);
    this.setPfShoots3ptValue();
    this.values.add(this.pfShoots3ptValue);
    this.setCShoots3ptValue();
    this.values.add(this.cShoots3ptValue);
    this.setPgShootsMidrangeValue();
    this.values.add(this.pgShootsMidrangeValue);
    this.setSgShootsMidrangeValue();
    this.values.add(this.sgShootsMidrangeValue);
    this.setSfShootsMidrangeValue();
    this.values.add(this.sfShootsMidrangeValue);
    this.setPfShootsMidrangeValue();
    this.values.add(this.pfShootsMidrangeValue);
    this.setCShootsMidrangeValue();
    this.values.add(this.cShootsMidrangeValue);
    this.setShootingFoulValue();
    this.values.add(this.shootingFoulValue);
    this.setPgShootsLayupValue();
    this.values.add(this.pgShootsLayupValue);
    this.setSgShootsLayupValue();
    this.values.add(this.sgShootsLayupValue);
    this.setSfShootsLayupValue();
    this.values.add(this.sfShootsLayupValue);
    this.setPfShootsLayupValue();
    this.values.add(this.pfShootsLayupValue);
    this.setCShootsLayupValue();
    this.values.add(this.cShootsLayupValue);
    this.setPgPostupValue();
    this.values.add(this.pgPostupValue);
    this.setSgPostupValue();
    this.values.add(this.sgPostupValue);
    this.setSfPostupValue();
    this.values.add(this.sfPostupValue);
    this.setPfPostupValue();
    this.values.add(this.pfPostupValue);
    this.setCPostupValue();
    this.values.add(this.cPostupValue);
    this.setShotClockViolationValue();
    this.values.add(this.shotClockViolationValue);
    this.setPassOutOfBoundsValue();
    this.values.add(this.passOutOfBoundsValue);
    this.setOffensiveFoulValue();
    this.values.add(this.offensiveFoulValue);
    this.setJumpBallValue();
    this.values.add(this.jumpBallValue);
    this.setStealValue();
    this.values.add(this.stealValue);
    this.setNonShootingFoulValue();
    this.values.add(this.nonShootingFoulValue);
  }
  
  void setTimeOfEvent() {
    this.timeOfEvent = r.nextInt(18) + 6;
    if (this.timeLeftInQuarter < this.timeOfEvent) {
      this.timeOfEvent = this.timeLeftInQuarter;
    }
    this.statsSoFar.secondsLeftInQuarter = this.statsSoFar.secondsLeftInQuarter - this.timeOfEvent;
    this.statsSoFar.timeSinceLastTimeout = this.statsSoFar.timeSinceLastTimeout + this.timeOfEvent;
    this.timeLeftInQuarter = this.timeLeftInQuarter - this.timeOfEvent;
    this.timeLeftOnShotClock = this.timeLeftOnShotClock - this.timeOfEvent;
    if (this.quarterNumber == 4 && this.timeLeftInQuarter < 180) {
      if (this.statsSoFar.homeTeamTimeoutsLeft > 2) {
        this.statsSoFar.homeTeamTimeoutsLeft = 2;
      }
      if (this.statsSoFar.awayTeamTimeoutsLeft > 2) {
        this.statsSoFar.awayTeamTimeoutsLeft = 2;
      }
    }
    this.minTimeInQuarter = this.updateMinLeftInQuarter(this.timeLeftInQuarter);
    this.statsSoFar.addStat(this.offensiveFive.pg, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.offensiveFive.sg, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.offensiveFive.sf, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.offensiveFive.pf, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.offensiveFive.c, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.defensiveFive.pg, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.defensiveFive.sg, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.defensiveFive.sf, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.defensiveFive.pf, "min", (double) this.timeOfEvent / 60);
    this.statsSoFar.addStat(this.defensiveFive.c, "min", (double) this.timeOfEvent / 60);
  }
  
  String updateMinLeftInQuarter(int timeLeftInQuarter) {
    int minInQuarter = timeLeftInQuarter / 60;
    int leftoverSeconds = timeLeftInQuarter % 60;
    String minTime = minInQuarter + ":";
    if (leftoverSeconds < 10) {
      return minTime + "0" + leftoverSeconds;
    }
    else {
      return minTime + leftoverSeconds;
    }
  }
  
  void chooseEvent() {
    this.totalValue = this.pgPostupValue + this.pgShoots3ptValue + this.pgShootsLayupValue + this.pgShootsMidrangeValue +
        this.sgPostupValue + this.sgShoots3ptValue + this.sgShootsLayupValue + this.sgShootsMidrangeValue +
        this.sfPostupValue + this.sfShoots3ptValue + this.sfShootsLayupValue + this.sfShootsMidrangeValue +
        this.pfPostupValue + this.pfShoots3ptValue + this.pfShootsLayupValue + this.pfShootsMidrangeValue + 
        this.cPostupValue + this.cShoots3ptValue + this.cShootsLayupValue + this.cShootsMidrangeValue + 
        this.shootingFoulValue + this.nonShootingFoulValue + this.shotClockViolationValue + this.passOutOfBoundsValue + 
        this.offensiveFoulValue + this.jumpBallValue + this.stealValue;
    double random = r.nextDouble() * totalValue;
    double currentWeight = 0;
    int counter = 0; // counter goes up to 26 (because there are 27 different values)
    for (double value : this.values) {
      currentWeight += value;
      if (currentWeight >= random) {
        if (counter == 0) {
          this.pgShoots3pt();
          break;
        }
        else if (counter == 1) {
          this.sgShoots3pt();
          break;
        }
        else if (counter == 2) {
          this.sfShoots3pt();
          break;
        }
        else if (counter == 3) {
          this.pfShoots3pt();
          break;
        }
        else if (counter == 4) {
          this.cShoots3pt();
          break;
        }
        else if (counter == 5) {
          this.pgShootsMidrange();
          break;
        }
        else if (counter == 6) {
          this.sgShootsMidrange();
          break;
        }
        else if (counter == 7) {
          this.sfShootsMidrange();
          break;
        }
        else if (counter == 8) {
          this.pfShootsMidrange();
          break;
        }
        else if (counter == 9) {
          this.cShootsMidrange();
          break;
        }
        else if (counter == 10) {
          this.shootingFoul();
          break;
        }
        else if (counter == 11) {
          this.pgShootsLayup();
          break;
        }
        else if (counter == 12) {
          this.sgShootsLayup();
          break;
        }
        else if (counter == 13) {
          this.sfShootsLayup();
          break;
        }
        else if (counter == 14) {
          this.pfShootsLayup();
          break;
        }
        else if (counter == 15) {
          this.cShootsLayup();
          break;
        }
        else if (counter == 16) {
          this.pgPostup();
          break;
        }
        else if (counter == 17) {
          this.sgPostup();
          break;
        }
        else if (counter == 18) {
          this.sfPostup();
          break;
        }
        else if (counter == 19) {
          this.pfPostup();
          break;
        }
        else if (counter == 20) {
          this.cPostup();
          break;
        }
        else if (counter == 21) {
          this.shotClockViolation();
          break;
        }
        else if (counter == 22) {
          this.passOutOfBounds();
          break;
        }
        else if (counter == 23) {
          this.offensiveFoul();
          break;
        }
        else if (counter == 24) {
          this.jumpBall();
          break;
        }
        else if (counter == 25) {
          this.steal();
          break;
        }
        else {
          this.nonShootingFoul();
          break;
        }
      }
      counter++;
    }
    
  }

  void setPgShoots3ptValue() {
    this.pgShoots3ptValue = offensiveFive.pg.threePtFrequency * 
        (425 + (((offensiveFive.pg.threePtOpen + offensiveFive.pg.threePtContested) / 2) - 75) * 20);
  }
  
  void setSgShoots3ptValue() {
    this.sgShoots3ptValue = offensiveFive.sg.threePtFrequency * 
        (425 + (((offensiveFive.sg.threePtOpen + offensiveFive.sg.threePtContested) / 2) - 75) * 20);
  }

  void setSfShoots3ptValue() {
    this.sfShoots3ptValue = offensiveFive.sf.threePtFrequency * 
        (425 + (((offensiveFive.sf.threePtOpen + offensiveFive.sf.threePtContested) / 2) - 75) * 20);
  }

  void setPfShoots3ptValue() {
    this.pfShoots3ptValue = offensiveFive.pf.threePtFrequency * 
        (425 + (((offensiveFive.pf.threePtOpen + offensiveFive.pf.threePtContested) / 2) - 75) * 20);
  }
  
  void setCShoots3ptValue() {
    this.pfShoots3ptValue = offensiveFive.pf.threePtFrequency * 
        (425 + (((offensiveFive.pf.threePtOpen + offensiveFive.pf.threePtContested) / 2) - 75) * 20);
  }
  
  void setPgShootsMidrangeValue() {
    this.pgShootsMidrangeValue = offensiveFive.pg.midrangeFrequency * 
        (425 + (((offensiveFive.pg.midrangeOpen + offensiveFive.pg.midrangeContested) / 2) - 75) * 20);
  }
  
  void setSgShootsMidrangeValue() {
    this.sgShootsMidrangeValue = offensiveFive.sg.midrangeFrequency * 
        (425 + (((offensiveFive.sg.midrangeOpen + offensiveFive.sg.midrangeContested) / 2) - 75) * 20);
  }
  
  void setSfShootsMidrangeValue() {
    this.sfShootsMidrangeValue = offensiveFive.sf.midrangeFrequency * 
        (425 + (((offensiveFive.sf.midrangeOpen + offensiveFive.sf.midrangeContested) / 2) - 75) * 20);
  }
  
  void setPfShootsMidrangeValue() {
    this.pfShootsMidrangeValue = offensiveFive.pf.midrangeFrequency * 
        (425 + (((offensiveFive.pf.midrangeOpen + offensiveFive.pf.midrangeContested) / 2) - 75) * 20);
  }
  
  void setCShootsMidrangeValue() {
    this.cShootsMidrangeValue = offensiveFive.c.midrangeFrequency * 
        (425 + (((offensiveFive.c.midrangeOpen + offensiveFive.c.midrangeContested) / 2) - 75) * 20);
  }
  
  void setShootingFoulValue() {
    this.shootingFoulValue = 1.2 * 1000 * offensiveFive.pg.freeThrowFrequency * offensiveFive.sg.freeThrowFrequency *
        offensiveFive.sf.freeThrowFrequency * offensiveFive.pf.freeThrowFrequency * offensiveFive.c.freeThrowFrequency * 15;
  }
  
  // from 500 to 1000, depending on how good the defense is
  void setNonShootingFoulValue() {
    this.nonShootingFoulValue = 1.2 * 1000 - ((defensiveFive.pg.perimeterDefense + defensiveFive.pg.interiorDefense / 2 + defensiveFive.sg.perimeterDefense +
        defensiveFive.sg.interiorDefense / 2 + defensiveFive.sf.interiorDefense / 4 * 3 + defensiveFive.sf.perimeterDefense / 4 * 3 + 
        defensiveFive.pf.perimeterDefense / 4 * 3 + defensiveFive.pf.interiorDefense / 4 * 3 + 
        defensiveFive.c.perimeterDefense / 2 + defensiveFive.c.interiorDefense) / 3 * 2);
    this.nonShootingFoulValue = this.nonShootingFoulValue / 8 * 1.3;
  }
  
  void setPgShootsLayupValue() {
    this.pgShootsLayupValue = (offensiveFive.pg.closeContested + offensiveFive.pg.closeOpen + offensiveFive.pg.layupContested) / 3
        * 10 * offensiveFive.pg.layupFrequency;
  }
  
  void setSgShootsLayupValue() {
    this.sgShootsLayupValue = (offensiveFive.sg.closeContested + offensiveFive.sg.closeOpen + offensiveFive.sg.layupContested) / 3
        * 10 * offensiveFive.sg.layupFrequency;
  }
  
  void setSfShootsLayupValue() {
    this.sfShootsLayupValue = (offensiveFive.sf.closeContested + offensiveFive.sf.closeOpen + offensiveFive.sf.layupContested) / 3
        * 10 * offensiveFive.sf.layupFrequency;
  }
  
  void setPfShootsLayupValue() {
    this.pfShootsLayupValue = (offensiveFive.pf.closeContested + offensiveFive.pf.closeOpen + offensiveFive.pf.layupContested) / 3
        * 10 * offensiveFive.pf.layupFrequency;
  }
  
  void setCShootsLayupValue() {
    this.cShootsLayupValue = (offensiveFive.c.closeContested + offensiveFive.c.closeOpen + offensiveFive.c.layupContested) / 3
        * 10 * offensiveFive.c.layupFrequency;
  }
  
  void setPgPostupValue() {
    if (offensiveFive.pg.postupFrequency < 205) {
      this.pgPostupValue = 5;
    }
    else {
      this.pgPostupValue = 1000 * offensiveFive.pg.postupFrequency - 200;
    }
  }
  
  void setSgPostupValue() {
    if (offensiveFive.sg.postupFrequency < 205) {
      this.sgPostupValue = 5;
    }
    else {
      this.sgPostupValue = 1000 * offensiveFive.sg.postupFrequency - 200;
    }
  }
  
  void setSfPostupValue() {
    if (offensiveFive.sf.postupFrequency < 205) {
      this.sfPostupValue = 5;
    }
    else {
      this.sfPostupValue = 1000 * offensiveFive.sf.postupFrequency - 200;
    }
  }
  
  void setPfPostupValue() {
    if (offensiveFive.pf.postupFrequency < 205) {
      this.pfPostupValue = 5;
    }
    else {
      this.pfPostupValue = 1000 * offensiveFive.pf.postupFrequency - 200;
    }
  }
  
  void setCPostupValue() {
    if (offensiveFive.c.postupFrequency < 205) {
      this.cPostupValue = 5;
    }
    else {
      this.cPostupValue = 1000 * offensiveFive.c.postupFrequency - 200;
    }
  }
  
  void setShotClockViolationValue() {
    this.shotClockViolationValue = 10;
    // both out of 10
    double offensiveScoringAbility = (offensiveFive.pg.scoringAbility + offensiveFive.sg.scoringAbility + offensiveFive.sf.scoringAbility + 
        offensiveFive.pf.scoringAbility + offensiveFive.c.scoringAbility) / 50;
    double shutdownDefense = (defensiveFive.pg.perimeterDefense + defensiveFive.pg.interiorDefense / 2 + defensiveFive.sg.perimeterDefense +
        defensiveFive.sg.interiorDefense / 2 + defensiveFive.sf.interiorDefense / 4 * 3 + defensiveFive.sf.perimeterDefense / 4 * 3 + 
        defensiveFive.pf.perimeterDefense / 4 * 3 + defensiveFive.pf.interiorDefense / 4 * 3 + 
        defensiveFive.c.perimeterDefense / 2 + defensiveFive.c.interiorDefense) / 75;
    this.shotClockViolationValue = this.shotClockViolationValue + shutdownDefense - offensiveScoringAbility;
    
  }
  
  void setPassOutOfBoundsValue() {
    this.passOutOfBoundsValue = 25;
    // both out of 10
    double offensivePassingAbility = (offensiveFive.pg.passingAccuracy + offensiveFive.sg.passingAccuracy + offensiveFive.sf.passingAccuracy + 
        offensiveFive.pf.passingAccuracy + offensiveFive.c.passingAccuracy) / 50;
    double passingDefense = (defensiveFive.pg.perimeterDefense + defensiveFive.sg.perimeterDefense +
        defensiveFive.sf.perimeterDefense + defensiveFive.pf.perimeterDefense + defensiveFive.c.perimeterDefense) / 50;
    this.passOutOfBoundsValue = this.passOutOfBoundsValue + passingDefense - offensivePassingAbility;
  }
  
  void setOffensiveFoulValue() {
    this.offensiveFoulValue = 13;
  }
  
  void setJumpBallValue() {
    this.jumpBallValue = 10;
  }
  
  void setStealValue() {
    this.stealValue = 30;
    // both out of 30
    double offensiveBallSecurity = (offensiveFive.pg.passingAccuracy + offensiveFive.pg.ballhandling + offensiveFive.sg.passingAccuracy
        + offensiveFive.sg.ballhandling + offensiveFive.sf.passingAccuracy + offensiveFive.sf.ballhandling +
        offensiveFive.pf.passingAccuracy + offensiveFive.pf.ballhandling + offensiveFive.c.passingAccuracy +
        offensiveFive.c.ballhandling) / 100 * 3;
    double defenseStealAbility = (defensiveFive.pg.steal + defensiveFive.sg.steal +
        defensiveFive.sf.steal + defensiveFive.pf.steal + defensiveFive.c.steal) / 50 * 3;
    this.passOutOfBoundsValue = this.passOutOfBoundsValue + defenseStealAbility - offensiveBallSecurity;
  }


  // each possible outcome of the possession is represented with a method
  
  void playerShoots3pt(Player offensivePlayer, Player defensivePlayer) {
    this.statsSoFar.addStat(offensivePlayer, "fga", 1);
    this.statsSoFar.addStat(offensivePlayer, "threeptattempted", 1);
    String make = "make";
    String miss = "miss";
    String block = "block";
    double makingShotValue = Math.pow(offensivePlayer.threePtContested + offensivePlayer.threePtOpen, 2);
    double missingShotValue = Math.pow(defensivePlayer.perimeterDefense * 2 + 55, 2);
    double blockValue = Math.pow(defensivePlayer.block, 3) / 10000;
    ArrayList<String> outcomes = new ArrayList<>();
    outcomes.add(make);
    outcomes.add(miss);
    outcomes.add(block);
    ArrayList<Double> values = new ArrayList<>();
    values.add(makingShotValue);
    values.add(missingShotValue);
    values.add(blockValue);
    String outcome = this.weightedSelect(values, outcomes);
    if (outcome.equals(make)) {
      this.statsSoFar.addStat(offensivePlayer, "fg", 1);
      this.statsSoFar.addStat(offensivePlayer, "threept", 1);
      this.statsSoFar.addStat(offensivePlayer, "pts", 3);
      this.updateScore(3);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a three pointer.");
      this.checkForAssist(offensivePlayer);
      this.endOfPossession = true;
    }
    else if (outcome.equals(miss)) {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a three pointer.");
      Player rebounder = this.chooseRebounder(offensivePlayer, "threept", false);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
    else { // outcome.equals(block)
      this.statsSoFar.addStat(defensivePlayer, "blk", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a three pointer "
          + "(blocked by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ").");
      Player rebounder = this.chooseRebounder(offensivePlayer, "threept", true);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
  }

  void playerShootsMidrange(Player offensivePlayer, Player defensivePlayer) {
    this.statsSoFar.addStat(offensivePlayer, "fga", 1);
    String make = "make";
    String miss = "miss";
    String block = "block";
    double makingShotValue = Math.pow(offensivePlayer.midrangeContested + offensivePlayer.midrangeOpen, 1.7);
    double missingShotValue = Math.pow(defensivePlayer.perimeterDefense * 2 + 45, 1.7);
    double blockValue = Math.pow(defensivePlayer.block, 2.5) / 10000;
    ArrayList<String> outcomes = new ArrayList<>();
    outcomes.add(make);
    outcomes.add(miss);
    outcomes.add(block);
    ArrayList<Double> values = new ArrayList<>();
    values.add(makingShotValue);
    values.add(missingShotValue);
    values.add(blockValue);
    String outcome = this.weightedSelect(values, outcomes);
    if (outcome.equals(make)) {
      this.statsSoFar.addStat(offensivePlayer, "fg", 1);
      this.statsSoFar.addStat(offensivePlayer, "pts", 2);
      this.updateScore(2);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a midrange shot.");
      this.checkForAssist(offensivePlayer);
      this.endOfPossession = true;
    }
    else if (outcome.equals(miss)) {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a midrange shot.");
      Player rebounder = this.chooseRebounder(offensivePlayer, "midrange", false);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
    else { // outcome.equals(block)
      this.statsSoFar.addStat(defensivePlayer, "blk", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a midrange shot "
          + "(blocked by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ").");
      Player rebounder = this.chooseRebounder(offensivePlayer, "midrange", true);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
  }
  
  void playerShootsLayup(Player offensivePlayer, Player defensivePlayer) {
    this.statsSoFar.addStat(offensivePlayer, "fga", 1);
    String make = "make";
    String miss = "miss";
    String block = "block";
    double makingShotValue = (offensivePlayer.layupContested + offensivePlayer.closeContested + offensivePlayer.closeOpen) / 3 * 1.3;
    double missingShotValue = defensivePlayer.interiorDefense;
    double blockValue = defensivePlayer.block / 9;
    ArrayList<String> outcomes = new ArrayList<>();
    outcomes.add(make);
    outcomes.add(miss);
    outcomes.add(block);
    ArrayList<Double> values = new ArrayList<>();
    values.add(makingShotValue);
    values.add(missingShotValue);
    values.add(blockValue);
    String outcome = this.weightedSelect(values, outcomes);
    if (outcome.equals(make)) {
      this.statsSoFar.addStat(offensivePlayer, "fg", 1);
      this.statsSoFar.addStat(offensivePlayer, "pts", 2);
      this.updateScore(2);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a layup/dunk.");
      this.checkForAssist(offensivePlayer);
      this.endOfPossession = true;
    }
    else if (outcome.equals(miss)) {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a layup/dunk.");
      Player rebounder = this.chooseRebounder(offensivePlayer, "layup", false);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
    else { // outcome.equals(block)
      this.statsSoFar.addStat(defensivePlayer, "blk", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a layup/dunk "
          + "(blocked by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ").");
      Player rebounder = this.chooseRebounder(offensivePlayer, "layup", true);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
  }
  
  void playerPostsUp(Player offensivePlayer, Player defensivePlayer) {
    this.statsSoFar.addStat(offensivePlayer, "fga", 1);
    String make = "make";
    String miss = "miss";
    String block = "block";
    double makingShotValue = Math.pow(offensivePlayer.postupScoring, 1.2);
    double missingShotValue = Math.pow(defensivePlayer.interiorDefense, 1.2);
    double blockValue = Math.pow(defensivePlayer.block, 1.2) / 10;
    ArrayList<String> outcomes = new ArrayList<>();
    outcomes.add(make);
    outcomes.add(miss);
    outcomes.add(block);
    ArrayList<Double> values = new ArrayList<>();
    values.add(makingShotValue);
    values.add(missingShotValue);
    values.add(blockValue);
    String outcome = this.weightedSelect(values, outcomes);
    if (outcome.equals(make)) {
      this.statsSoFar.addStat(offensivePlayer, "fg", 1);
      this.statsSoFar.addStat(offensivePlayer, "pts", 2);
      this.updateScore(2);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a postup shot.");
      this.checkForAssist(offensivePlayer);
      this.endOfPossession = true;
    }
    else if (outcome.equals(miss)) {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a postup shot.");
      Player rebounder = this.chooseRebounder(offensivePlayer, "postup", false);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
    else { // outcome.equals(block)
      this.statsSoFar.addStat(defensivePlayer, "blk", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " missed a postup shot "
          + "(blocked by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ").");
      Player rebounder = this.chooseRebounder(offensivePlayer, "postup", true);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else { // rebounder is in defensive five
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an defensive rebound.");
        this.endOfPossession = true;
      }
    }
  }
  
  void pgShoots3pt() {
    this.playerShoots3pt(this.offensiveFive.pg, this.defensiveFive.pg);
  }
  
  void pgShootsMidrange() {
    this.playerShootsMidrange(this.offensiveFive.pg, this.defensiveFive.pg);
  }
  
  void pgShootsLayup() {
    this.playerShootsLayup(this.offensiveFive.pg, this.defensiveFive.pg);
  }
  
  void pgPostup() {
    this.playerPostsUp(this.offensiveFive.pg, this.defensiveFive.pg);
  }
  
  void sgShoots3pt() {
    this.playerShoots3pt(this.offensiveFive.sg, this.defensiveFive.sg);
  }
  
  void sgShootsMidrange() {
    this.playerShootsMidrange(this.offensiveFive.sg, this.defensiveFive.sg);
  }
  
  void sgShootsLayup() {
    this.playerShootsLayup(this.offensiveFive.sg, this.defensiveFive.sg);
  }
  
  void sgPostup() {
    this.playerPostsUp(this.offensiveFive.sg, this.defensiveFive.sg);
  }
  
  void sfShoots3pt() {
    this.playerShoots3pt(this.offensiveFive.sf, this.defensiveFive.sf);
  }
  
  void sfShootsMidrange() {
    this.playerShootsMidrange(this.offensiveFive.sf, this.defensiveFive.sf);
  }
  
  void sfShootsLayup() {
    this.playerShootsLayup(this.offensiveFive.sf, this.defensiveFive.sf);
  }
  
  void sfPostup() {
    this.playerPostsUp(this.offensiveFive.sf, this.defensiveFive.sf);
  }
  
  void pfShoots3pt() {
    this.playerShoots3pt(this.offensiveFive.pf, this.defensiveFive.pf);
  }
  
  void pfShootsMidrange() {
    this.playerShootsMidrange(this.offensiveFive.pf, this.defensiveFive.pf);
  }
  
  void pfShootsLayup() {
    this.playerShootsLayup(this.offensiveFive.pf, this.defensiveFive.pf);
  }
  
  void pfPostup() {
    this.playerPostsUp(this.offensiveFive.pf, this.defensiveFive.pf);
  }
  
  void cShoots3pt() {
    this.playerShoots3pt(this.offensiveFive.c, this.defensiveFive.c);
  }
  
  void cShootsMidrange() {
    this.playerShootsMidrange(this.offensiveFive.c, this.defensiveFive.c);
  }
  
  void cShootsLayup() {
    this.playerShootsLayup(this.offensiveFive.c, this.defensiveFive.c);
  }
  
  void cPostup() {
    this.playerPostsUp(this.offensiveFive.c, this.defensiveFive.c);
  }
  
  void shootingFoul() {
    ArrayList<Player> offensivePlayers = new ArrayList<>();
    offensivePlayers.add(this.offensiveFive.pg);
    offensivePlayers.add(this.offensiveFive.sg);
    offensivePlayers.add(this.offensiveFive.sf);
    offensivePlayers.add(this.offensiveFive.pf);
    offensivePlayers.add(this.offensiveFive.c);
    ArrayList<Double> offLikelihoods = new ArrayList<>();
    for (Player p: offensivePlayers) {
      Double offLikelihood = 2 * p.freeThrowFrequency * (p.hasBallFrequency + 2);
      offLikelihoods.add(offLikelihood);
    }
    Player offensivePlayer = this.weightedSelect(offLikelihoods, offensivePlayers);
    ArrayList<Player> defensivePlayers = new ArrayList<>();
    defensivePlayers.add(this.defensiveFive.pg);
    defensivePlayers.add(this.defensiveFive.sg);
    defensivePlayers.add(this.defensiveFive.sf);
    defensivePlayers.add(this.defensiveFive.pf);
    defensivePlayers.add(this.defensiveFive.c);
    ArrayList<Double> defLikelihoods = new ArrayList<>();
    String offensivePosition = this.offensiveFive.getPosition(offensivePlayer);
    for (Player p: defensivePlayers) {
      String defensivePosition = this.defensiveFive.getPosition(p);
      Double defLikelihood = 0.0;
      if (offensivePosition.equals(defensivePosition)) {
        defLikelihood = (150.0 - p.feelForGame) * 2;
      }
      else {
        defLikelihood = 150.0 - p.feelForGame;
      }
      defLikelihoods.add(defLikelihood);
    }
    Player defensivePlayer = this.weightedSelect(defLikelihoods, defensivePlayers);
    this.statsSoFar.addStat(defensivePlayer, "pf", 1);
    this.checkForFoulOut(defensivePlayer);
    
    double chanceFouledOn3pt = offensivePlayer.threePtFrequency / 10;
    double rand3 = r.nextDouble();
    if (rand3 < chanceFouledOn3pt) { // player fouled on three pointer
      double chanceMaking3pt = offensivePlayer.threePtContested /1000;
      double rand4 = r.nextDouble();
      if (rand4 < chanceMaking3pt) { // player makes three pointer
        this.statsSoFar.addStat(offensivePlayer, "threept", 1);
        this.statsSoFar.addStat(offensivePlayer, "threeptattempted", 1);
        this.statsSoFar.addStat(offensivePlayer, "fg", 1);
        this.statsSoFar.addStat(offensivePlayer, "fga", 1);
        this.statsSoFar.addStat(offensivePlayer, "pts", 3);
        this.updateScore(3);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a three pointer "
            + "and was fouled by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ".");
        this.checkForSubstitutions(offensivePlayer);
        this.shootLastFt(offensivePlayer);
      }
      else { // player misses three pointer
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " was fouled by "
            + defensivePlayer.firstName + " " + defensivePlayer.lastName + " while shooting a three pointer.");
        this.shootFtNoRebound(offensivePlayer);
        this.checkForSubstitutions(offensivePlayer);
        this.shootFtNoRebound(offensivePlayer);
        this.shootLastFt(offensivePlayer);
      }
    }
    else { // player fouled on two pointer
      double chanceMaking2pt = offensivePlayer.layupContested + offensivePlayer.closeContested + offensivePlayer.midrangeContested / 1000;
      double rand4 = r.nextDouble();
      if (rand4 < chanceMaking2pt) { // player makes two pointer
        this.statsSoFar.addStat(offensivePlayer, "fg", 1);
        this.statsSoFar.addStat(offensivePlayer, "fga", 1);
        this.statsSoFar.addStat(offensivePlayer, "pts", 2);
        this.updateScore(2);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " made a two pointer "
            + "and was fouled by " + defensivePlayer.firstName + " " + defensivePlayer.lastName + ".");
        this.checkForSubstitutions(offensivePlayer);
        this.shootLastFt(offensivePlayer);
      }
      else { // player misses two pointer
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " was fouled by "
            + defensivePlayer.firstName + " " + defensivePlayer.lastName + " while shooting a two pointer.");
        this.shootFtNoRebound(offensivePlayer);
        this.checkForSubstitutions(offensivePlayer);
        this.shootLastFt(offensivePlayer);
      }
    }
  }

  void shootFtNoRebound(Player ftShooter) {
    this.statsSoFar.addStat(ftShooter, "fta", 1);
    int rand1 = r.nextInt(100);
    if (rand1 < ftShooter.freeThrow) { // player makes free throw
      this.statsSoFar.addStat(ftShooter, "pts", 1);
      this.updateScore(1);
      this.statsSoFar.addStat(ftShooter, "ft", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + ftShooter.firstName + " " + ftShooter.lastName + " made a free throw.");
    }
    else {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + ftShooter.firstName + " " + ftShooter.lastName + " missed a free throw.");
    }
  }
  
  void shootLastFt(Player ftShooter) {
    this.statsSoFar.addStat(ftShooter, "fta", 1);
    int rand2 = r.nextInt(100);
    if (rand2 < ftShooter.freeThrow) { // player makes free throw
      this.statsSoFar.addStat(ftShooter, "pts", 1);
      this.updateScore(1);
      this.statsSoFar.addStat(ftShooter, "ft", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + ftShooter.firstName + " " + ftShooter.lastName + " made a free throw.");
      this.endOfPossession = true;
    }
    else { // player misses free throw
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + ftShooter.firstName + " " + ftShooter.lastName + " missed a free throw.");
      Player rebounder = this.chooseRebounder(ftShooter, "ft", false);
      if (this.offensiveFive.hasPlayer(rebounder)) {
        this.statsSoFar.addStat(rebounder, "orb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got an offensive rebound.");
      }
      else {
        this.statsSoFar.addStat(rebounder, "drb", 1);
        this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + rebounder.firstName + " " + rebounder.lastName + " got a defensive rebound.");
        this.endOfPossession = true;
      }
    }
  }

  void nonShootingFoul() {
    ArrayList<Player> defensivePlayers = new ArrayList<>();
    defensivePlayers.add(this.defensiveFive.pg);
    defensivePlayers.add(this.defensiveFive.sg);
    defensivePlayers.add(this.defensiveFive.sf);
    defensivePlayers.add(this.defensiveFive.pf);
    defensivePlayers.add(this.defensiveFive.c);
    ArrayList<Double> defLikelihoods = new ArrayList<>();
    for (Player p: defensivePlayers) {
      Double defLikelihood = 200.0 - p.feelForGame;
      defLikelihoods.add(defLikelihood);
    }
    Player defensivePlayer = this.weightedSelect(defLikelihoods, defensivePlayers);
    this.statsSoFar.addStat(defensivePlayer, "pf", 1);
    this.checkForFoulOut(defensivePlayer);
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + defensivePlayer.firstName + " " + defensivePlayer.lastName +
        " committed a non-shooting foul.");
    if (this.timeLeftOnShotClock < 14) {
      this.timeLeftOnShotClock = 14;
    }
    this.checkForSubstitutions();
  }
  
  void shotClockViolation() {
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + "The " + this.offensiveFive.team.name + " committed a shot clock violation.");
    this.checkForSubstitutions();
    this.endOfPossession = true;
  }
  
  void passOutOfBounds() {
    ArrayList<Player> offensivePlayers = new ArrayList<>();
    offensivePlayers.add(this.offensiveFive.pg);
    offensivePlayers.add(this.offensiveFive.sg);
    offensivePlayers.add(this.offensiveFive.sf);
    offensivePlayers.add(this.offensiveFive.pf);
    offensivePlayers.add(this.offensiveFive.c);
    ArrayList<Double> offLikelihoods = new ArrayList<>();
    for (Player p: offensivePlayers) {
      Double offLikelihood = (135 - p.passingAccuracy) * p.hasBallFrequency;
      offLikelihoods.add(offLikelihood);
    }
    Player offensivePlayer = this.weightedSelect(offLikelihoods, offensivePlayers);
    this.statsSoFar.addStat(offensivePlayer, "tov", 1);
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName + " passed the ball out of bounds.");
    this.checkForSubstitutions();
    this.endOfPossession = true;
  }
  
  void offensiveFoul() {
    ArrayList<Player> offensivePlayers = new ArrayList<>();
    offensivePlayers.add(this.offensiveFive.pg);
    offensivePlayers.add(this.offensiveFive.sg);
    offensivePlayers.add(this.offensiveFive.sf);
    offensivePlayers.add(this.offensiveFive.pf);
    offensivePlayers.add(this.offensiveFive.c);
    ArrayList<Double> offLikelihoods = new ArrayList<>();
    for (Player p: offensivePlayers) {
      Double offLikelihood = (135 - p.feelForGame) * p.hasBallFrequency;
      offLikelihoods.add(offLikelihood);
    }
    ArrayList<Player> defensivePlayers = new ArrayList<>();
    defensivePlayers.add(this.defensiveFive.pg);
    defensivePlayers.add(this.defensiveFive.sg);
    defensivePlayers.add(this.defensiveFive.sf);
    defensivePlayers.add(this.defensiveFive.pf);
    defensivePlayers.add(this.defensiveFive.c);
    ArrayList<Double> defLikelihoods = new ArrayList<>();
    for (Player p: defensivePlayers) {
      Double defLikelihood = 2.0 * p.takeCharge;
      defLikelihoods.add(defLikelihood);
    }
    Player offensivePlayer = this.weightedSelect(offLikelihoods, offensivePlayers);
    Player defensivePlayer = this.weightedSelect(defLikelihoods, defensivePlayers);
    this.statsSoFar.addStat(offensivePlayer, "tov", 1);
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + defensivePlayer.firstName + " " + defensivePlayer.lastName +
        " drew an offensive foul on " + offensivePlayer.firstName + " " + offensivePlayer.lastName + ".");
    this.checkForSubstitutions();
    this.endOfPossession = true;
  }
  
  void jumpBall() {
    Player offensivePlayer;
    Player defensivePlayer;
    int rand = r.nextInt(5);
    if (rand == 0) {
      offensivePlayer = this.offensiveFive.pg;
      int rand1 = r.nextInt(2);
      if (rand1 == 0) {
        defensivePlayer = this.defensiveFive.pg;
      }
      else {
        int rand2 = r.nextInt(4);
        if (rand2 == 0) {
          defensivePlayer = this.defensiveFive.sg;
        }
        else if (rand2 == 1) {
          defensivePlayer = this.defensiveFive.sf;
        }
        else if (rand2 == 2) {
          defensivePlayer = this.defensiveFive.pf;
        }
        else {
          defensivePlayer = this.defensiveFive.c;
        }
      }
    }
    else if (rand == 1) {
      offensivePlayer = this.offensiveFive.sg;
      int rand1 = r.nextInt(2);
      if (rand1 == 0) {
        defensivePlayer = this.defensiveFive.sg;
      }
      else {
        int rand2 = r.nextInt(4);
        if (rand2 == 0) {
          defensivePlayer = this.defensiveFive.pg;
        }
        else if (rand2 == 1) {
          defensivePlayer = this.defensiveFive.sf;
        }
        else if (rand2 == 2) {
          defensivePlayer = this.defensiveFive.pf;
        }
        else {
          defensivePlayer = this.defensiveFive.c;
        }
      }
    }
    else if (rand == 2) {
      offensivePlayer = this.offensiveFive.sf;
      int rand1 = r.nextInt(2);
      if (rand1 == 0) {
        defensivePlayer = this.defensiveFive.sf;
      }
      else {
        int rand2 = r.nextInt(4);
        if (rand2 == 0) {
          defensivePlayer = this.defensiveFive.sg;
        }
        else if (rand2 == 1) {
          defensivePlayer = this.defensiveFive.pg;
        }
        else if (rand2 == 2) {
          defensivePlayer = this.defensiveFive.pf;
        }
        else {
          defensivePlayer = this.defensiveFive.c;
        }
      }
    }
    else if (rand == 3) {
      offensivePlayer = this.offensiveFive.pf;
      int rand1 = r.nextInt(2);
      if (rand1 == 0) {
        defensivePlayer = this.defensiveFive.pf;
      }
      else {
        int rand2 = r.nextInt(4);
        if (rand2 == 0) {
          defensivePlayer = this.defensiveFive.sg;
        }
        else if (rand2 == 1) {
          defensivePlayer = this.defensiveFive.sf;
        }
        else if (rand2 == 2) {
          defensivePlayer = this.defensiveFive.pg;
        }
        else {
          defensivePlayer = this.defensiveFive.c;
        }
      }
    }
    else {
      offensivePlayer = this.offensiveFive.c;
      int rand1 = r.nextInt(2);
      if (rand1 == 0) {
        defensivePlayer = this.defensiveFive.c;
      }
      else {
        int rand2 = r.nextInt(4);
        if (rand2 == 0) {
          defensivePlayer = this.defensiveFive.sg;
        }
        else if (rand2 == 1) {
          defensivePlayer = this.defensiveFive.sf;
        }
        else if (rand2 == 2) {
          defensivePlayer = this.defensiveFive.pf;
        }
        else {
          defensivePlayer = this.defensiveFive.pg;
        }
      }
    }
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + "Jump ball between " + defensivePlayer.firstName + " " + defensivePlayer.lastName +
        " and " + offensivePlayer.firstName + " " + offensivePlayer.lastName + ".");
    this.checkForSubstitutions(offensivePlayer, defensivePlayer);
    ArrayList<Double> jumpBallValues = new ArrayList<>();
    jumpBallValues.add(100 + Math.pow(offensivePlayer.height + offensivePlayer.wingspan - defensivePlayer.height - defensivePlayer.wingspan, 2));
    if (Math.pow(offensivePlayer.height + offensivePlayer.wingspan - defensivePlayer.height - defensivePlayer.wingspan, 2) > 100) {
      jumpBallValues.add(0.0);
    }
    else {
      jumpBallValues.add(100 - Math.pow(offensivePlayer.height + offensivePlayer.wingspan - defensivePlayer.height - defensivePlayer.wingspan, 2));
    }
    ArrayList<Player> players = new ArrayList<>();
    players.add(offensivePlayer);
    players.add(defensivePlayer);
    Player jumpBallWinner = this.weightedSelect(jumpBallValues, players);
    if (jumpBallWinner.samePlayer(defensivePlayer)) {
      this.statsSoFar.addStat(offensivePlayer, "tov", 1);
      this.endOfPossession = true;
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + defensivePlayer.firstName + " " + defensivePlayer.lastName +
          " wins the jump ball.");
    }
    else {
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + offensivePlayer.firstName + " " + offensivePlayer.lastName +
          " wins the jump ball.");
    }
  }
  
  void steal() {
    ArrayList<Player> offensivePlayers = new ArrayList<>();
    offensivePlayers.add(this.offensiveFive.pg);
    offensivePlayers.add(this.offensiveFive.sg);
    offensivePlayers.add(this.offensiveFive.sf);
    offensivePlayers.add(this.offensiveFive.pf);
    offensivePlayers.add(this.offensiveFive.c);
    ArrayList<Double> tovLikelihoods = new ArrayList<>();
    for (Player p: offensivePlayers) {
      Double tovLikelihood = (415 - p.ballhandling - p.passingAccuracy - p.feelForGame) * p.hasBallFrequency;
      tovLikelihoods.add(tovLikelihood);
    }
    ArrayList<Player> defensivePlayers = new ArrayList<>();
    defensivePlayers.add(this.defensiveFive.pg);
    defensivePlayers.add(this.defensiveFive.sg);
    defensivePlayers.add(this.defensiveFive.sf);
    defensivePlayers.add(this.defensiveFive.pf);
    defensivePlayers.add(this.defensiveFive.c);
    ArrayList<Double> stlLikelihoods = new ArrayList<>();
    for (Player p: defensivePlayers) {
      Double stlLikelihood = 2.0 * p.steal;
      stlLikelihoods.add(stlLikelihood);
    }
    Player offensivePlayer = this.weightedSelect(tovLikelihoods, offensivePlayers);
    Player defensivePlayer = this.weightedSelect(stlLikelihoods, defensivePlayers);
    this.statsSoFar.addStat(offensivePlayer, "tov", 1);
    this.statsSoFar.addStat(defensivePlayer, "stl", 1);
    this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + defensivePlayer.firstName + " " + defensivePlayer.lastName +
        " stole the ball from " + offensivePlayer.firstName + " " + offensivePlayer.lastName + ".");
    this.endOfPossession = true;
  }
  
  // determines which player gets the rebound off of a missed shot
  // typeOfShot should always be either "ft", "threept", "midrange", "postup", or "layup"
  Player chooseRebounder(Player shooter, String typeOfShot, boolean blockedShot) {
    double offReboundFrequency = 0.0;
    if (typeOfShot.equals("ft")) {
      offReboundFrequency = 0.14;
    }
    else if (typeOfShot.equals("threept") || typeOfShot.equals("midrange")) {
      offReboundFrequency = 0.29;
    }
    else if (typeOfShot.equals("postup")) {
      offReboundFrequency = 0.31;
    }
    else if (typeOfShot.equals("layup")) {
      offReboundFrequency = 0.34;
    }
    String position = shooter.primaryPosition;
    Player shootersDefender = defensiveFive.getPlayer(position);
    ArrayList<Player> players = new ArrayList<>();
    players.add(this.offensiveFive.pg);
    players.add(this.offensiveFive.sg);
    players.add(this.offensiveFive.sf);
    players.add(this.offensiveFive.pf);
    players.add(this.offensiveFive.c);
    players.add(this.defensiveFive.pg);
    players.add(this.defensiveFive.sg);
    players.add(this.defensiveFive.sf);
    players.add(this.defensiveFive.pf);
    players.add(this.defensiveFive.c);
    ArrayList<Double> values = new ArrayList<>();
    int count = 0;
    boolean isOffensivePlayer = true;
    for (Player p: players) {
      if (count < 5) {
        isOffensivePlayer = true;
      }
      else {
        isOffensivePlayer = false;
      }
      Double value = 0.0;
      if (p.primaryPosition.equals("pg") && isOffensivePlayer) {
        value = 0.5 * p.offensiveRebound * Math.pow(offReboundFrequency, 1.5);
      }
      else if (p.primaryPosition.equals("pg") && !isOffensivePlayer) {
        value = 0.5 * p.defensiveRebound;
      }
      else if (p.primaryPosition.equals("sg") && isOffensivePlayer) {
        value = 0.55 * p.offensiveRebound * Math.pow(offReboundFrequency, 1.5);
      }
      else if (p.primaryPosition.equals("sg") && !isOffensivePlayer) {
        value = 0.55 * p.defensiveRebound;
      }
      else if (p.primaryPosition.equals("sf") && isOffensivePlayer) {
        value = 0.68 * p.offensiveRebound * Math.pow(offReboundFrequency, 1.5);
      }
      else if (p.primaryPosition.equals("sf") && !isOffensivePlayer) {
        value = 0.68 * p.defensiveRebound;
      }
      else if (p.primaryPosition.equals("pf") && isOffensivePlayer) {
        value = 0.86 * p.offensiveRebound * Math.pow(offReboundFrequency, 1.5);
      }
      else if (p.primaryPosition.equals("pf") && !isOffensivePlayer) {
        value = 0.86 * p.defensiveRebound;
      }
      else if (p.primaryPosition.equals("c") && isOffensivePlayer) {
        value = p.offensiveRebound * Math.pow(offReboundFrequency, 1.5);
      }
      else { // p.primaryPosition.equals("c") && !isOffensivePlayer
        value = (double) p.defensiveRebound;
      }
      if (p.samePlayer(shooter)) {
        if (blockedShot) {
          value = value * 3;
        }
        else {
          value = value * 0.75;
        }
      }
      if (p.samePlayer(shootersDefender)) {
        if (blockedShot) {
          value = value * 3;
        }
        else {
          value = value * 0.85;
        }
      }
      values.add(value);
      count++;
    }
    Player rebounder = this.weightedSelect(values, players);
    return rebounder;
  }
  
  void updateScore(int pts) {
    if (this.homeTeamOnOffense) {
      this.statsSoFar.homeTeamScore = this.statsSoFar.homeTeamScore + pts;
    }
    else {
      this.statsSoFar.awayTeamScore = this.statsSoFar.awayTeamScore + pts;
    }
  }
  
  void checkForAssist(Player shooter) {
    ArrayList<Double> values = new ArrayList<>();
    ArrayList<String> assisters = new ArrayList<>();
    assisters.add("pg");
    assisters.add("sg");
    assisters.add("sf");
    assisters.add("pf");
    assisters.add("c");
    assisters.add("no assist");
    for (String position: assisters) {
      if (position.equals("no assist")) {
        double value = 1500.0;
        for (Player p : this.offensiveFive.players) {
          value = value * (1 + ((200 - p.passingVision) / 100)) / 2 * (1 + ((200 - p.feelForGame) / 100)) / 2;
        }
        values.add(value);
      }
      else {
        Player currentPlayer = this.offensiveFive.getPlayer(position);
        if (currentPlayer.samePlayer(shooter)) {
          values.add(0.0);
        }
        else {
          Double value = Math.pow(currentPlayer.passingVision, 3) * currentPlayer.hasBallFrequency / 1000; // value is from 0 to 100
          values.add(value);
        }
      }
    }
    String positionOfAssister = this.weightedSelect(values, assisters);
    if (positionOfAssister.equals("no assist")) {
      // do nothing
    }
    else {
      Player assister = this.offensiveFive.getPlayer(positionOfAssister);
      this.statsSoFar.addStat(assister, "ast", 1);
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + assister.firstName + " " + assister.lastName + " got an assist.");
    }
  }
  
  void checkForSubstitutions(Player...unsubbablePlayers) {
    Lineup oldOffensiveFive = this.offensiveFive;
    Lineup oldDefensiveFive = this.defensiveFive;
    if (this.homeTeamOnOffense) {
      this.offensiveFive = this.statsSoFar.homeTeamGameRotation.getLineup(this.quarterNumber, this.timeLeftInQuarter);
      this.defensiveFive = this.statsSoFar.awayTeamGameRotation.getLineup(this.quarterNumber, this.timeLeftInQuarter);
    }
    else {
      this.offensiveFive = this.statsSoFar.awayTeamGameRotation.getLineup(this.quarterNumber, this.timeLeftInQuarter);
      this.defensiveFive = this.statsSoFar.homeTeamGameRotation.getLineup(this.quarterNumber, this.timeLeftInQuarter);
    }
    if (oldOffensiveFive.sameLineup(this.offensiveFive) && oldDefensiveFive.sameLineup(this.defensiveFive)) {
      // no substitutions were made
    }
    else {
      for (Player unsubbable: unsubbablePlayers) {
        if (oldOffensiveFive.hasPlayer(unsubbable)) {
          String position = oldOffensiveFive.getPosition(unsubbable);
          this.offensiveFive.substitute(unsubbable, position);
        }
        else { // oldDefensiveFive.hasPlayer(unsubbable)
          String position = oldDefensiveFive.getPosition(unsubbable);
          this.defensiveFive.substitute(unsubbable, position);
        }
      }
      this.statsSoFar.currentOffensiveFive = this.offensiveFive;
      this.statsSoFar.currentDefensiveFive = this.defensiveFive;
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   Substitutions were made: The " + this.offensiveFive.team.name +
          " lineup is: " + this.offensiveFive.lineupAsString() + ". The " + this.defensiveFive.team.name + " lineup is : "
          + this.defensiveFive.lineupAsString() + ".");
    }
  }
  
  void checkForTimeout() {
    int timeSinceLastTimeout = this.statsSoFar.timeSinceLastTimeout;
    int offensiveTimeoutsLeft = 0;
    if (this.homeTeamOnOffense) {
      offensiveTimeoutsLeft = this.statsSoFar.homeTeamTimeoutsLeft;
    }
    else {
      offensiveTimeoutsLeft = this.statsSoFar.awayTeamTimeoutsLeft;
    }
    double timeoutProbability = 0.0;
    int quarterNum = this.quarterNumber;
    if (quarterNum == 1) {
      timeoutProbability = 0.000000008 * Math.pow(timeSinceLastTimeout, 1.8) * Math.pow(offensiveTimeoutsLeft, 4);
    }
    else if (quarterNum == 2) {
      timeoutProbability = 0.00000003 * Math.pow(timeSinceLastTimeout, 1.8) * Math.pow(offensiveTimeoutsLeft, 4);
    }
    else if (quarterNum == 3) {
      timeoutProbability = 0.00000006 * Math.pow(timeSinceLastTimeout, 1.8) * Math.pow(offensiveTimeoutsLeft, 4);
    }
    else if (quarterNum == 4) {
      if (this.timeLeftInQuarter > 120) {
        timeoutProbability = 0.00000013 * Math.pow(timeSinceLastTimeout, 1.8) * Math.pow(offensiveTimeoutsLeft, 4);
      }
      else if (this.timeLeftInQuarter < 30) {
        timeoutProbability = 0.8 * offensiveTimeoutsLeft;
      }
      else {
        timeoutProbability = 0.3 * offensiveTimeoutsLeft;
      }
    }
    else { // overtime
      if (this.timeLeftInQuarter > 120) {
        timeoutProbability = 0.04 * offensiveTimeoutsLeft;
      }
      else if (this.timeLeftInQuarter < 30) {
        timeoutProbability = 0.8 * offensiveTimeoutsLeft;
      }
      else {
        timeoutProbability = 0.3 * offensiveTimeoutsLeft;
      }
    }
    if (timeoutProbability > 1) {
      timeoutProbability = 1;
    }
    ArrayList<Double> values = new ArrayList<>();
    values.add(timeoutProbability);
    values.add(1 - timeoutProbability);
    ArrayList<Boolean> yesOrNo = new ArrayList<>();
    yesOrNo.add(true);
    yesOrNo.add(false);
    boolean timeoutCalled = this.weightedSelect(values, yesOrNo);
    if (timeoutCalled) {
      if (this.homeTeamOnOffense) {
        this.statsSoFar.homeTeamTimeoutsLeft--;
      }
      else {
        this.statsSoFar.awayTeamTimeoutsLeft--;
      }
      this.statsSoFar.timeSinceLastTimeout = 0;
      this.statsSoFar.addToPlayByPlay(this.updateMinLeftInQuarter(this.timeLeftInQuarter + this.timeOfEvent) + "   The " + this.offensiveFive.team.name + " called timeout.");
    }
  }
  
  void checkForFoulOut(Player p) {
    if (this.statsSoFar.playersStats.get(p).pf >= 6) {
      if (this.homeTeamOnOffense) {
        this.statsSoFar.fouledOutAwayPlayers.add(p);
      }
      else {
        this.statsSoFar.fouledOutHomePlayers.add(p);
      }
      for (int i = (this.quarterNumber - 1) * 4; i < 16; i++) {
        if (this.homeTeamOnOffense) {
          Lineup ithLineup = this.statsSoFar.awayTeamGameRotation.gameLineups[i];
          if (ithLineup.hasPlayer(p)) {
            String position = ithLineup.getPosition(p);
            Player replacementPlayer = new Player();
            for (Player replacement: p.team.players) {
              if (!ithLineup.hasPlayer(replacement)) {
                if (p.primaryPosition.equals(replacement.primaryPosition)) {
                  replacementPlayer = replacement;
                  break;
                }
              }
            }
            if (replacementPlayer.firstName.equals("null player")) {
              for (Player replacement: p.team.players) {
                if (!ithLineup.hasPlayer(replacement)) {
                  if (p.primaryPosition.equals(replacement.secondaryPosition)) {
                    replacementPlayer = replacement;
                    break;
                  }
                }
              }
            }
            if (replacementPlayer.firstName.equals("null player")) {
              for (Player replacement: p.team.players) {
                if (!ithLineup.hasPlayer(replacement)) {
                  replacementPlayer = replacement;
                  break;
                }
              }
            }
            ithLineup.substitute(replacementPlayer, position);
          }
          this.statsSoFar.awayTeamGameRotation.gameLineups[i] = ithLineup;
        }
        else { // awayTeam on offense
          Lineup ithLineup = this.statsSoFar.homeTeamGameRotation.gameLineups[i];
          if (ithLineup.hasPlayer(p)) {
            String position = ithLineup.getPosition(p);
            Player replacementPlayer = new Player();
            for (Player replacement: p.team.players) {
              if (!ithLineup.hasPlayer(replacement)) {
                if (p.primaryPosition.equals(replacement.primaryPosition)) {
                  replacementPlayer = replacement;
                  break;
                }
              }
            }
            if (replacementPlayer.firstName.equals("null player")) {
              for (Player replacement: p.team.players) {
                if (!ithLineup.hasPlayer(replacement)) {
                  if (p.primaryPosition.equals(replacement.secondaryPosition)) {
                    replacementPlayer = replacement;
                    break;
                  }
                }
              }
            }
            if (replacementPlayer.firstName.equals("null player")) {
              for (Player replacement: p.team.players) {
                if (!ithLineup.hasPlayer(replacement)) {
                  replacementPlayer = replacement;
                  break;
                }
              }
            }
            ithLineup.substitute(replacementPlayer, position);
          }
          this.statsSoFar.homeTeamGameRotation.gameLineups[i] = ithLineup;
        }
      }
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   " + p.firstName + " " + p.lastName + " has fouled out.");
      this.statsSoFar.addToPlayByPlay(this.minTimeInQuarter + "   Substitutions were made: The " + this.offensiveFive.team.name +
          " lineup is: " + this.offensiveFive.lineupAsString() + ". The " + this.defensiveFive.team.name + " lineup is : "
          + this.defensiveFive.lineupAsString() + ".");
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
