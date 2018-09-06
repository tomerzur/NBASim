import java.util.ArrayList;
import java.util.HashMap;

public class GameStats {

  Lineup currentOffensiveFive;
  Lineup currentDefensiveFive;
  
  Team homeTeam;
  Team awayTeam;
  
  int quarterNumber;
  int secondsLeftInQuarter;
  int homeTeamTimeoutsLeft;
  int awayTeamTimeoutsLeft;
  int timeSinceLastTimeout;
  int homeTeamScore;
  int awayTeamScore;
  ArrayList<Player> fouledOutHomePlayers = new ArrayList<>();
  ArrayList<Player> fouledOutAwayPlayers = new ArrayList<>();
  Rotation homeTeamGameRotation;
  Rotation awayTeamGameRotation;
  
  ArrayList<String> playByPlay = new ArrayList<>();
  
  HashMap<Player, PlayerGameStats> playersStats = new HashMap<>();
  
  GameStats(Team homeTeam, Team awayTeam) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.homeTeamGameRotation = this.homeTeam.rotation;
    this.awayTeamGameRotation = this.awayTeam.rotation;
    this.homeTeamTimeoutsLeft = 7;
    this.awayTeamTimeoutsLeft = 7;
    for (int i = 0; i < this.homeTeam.players.size(); i++) {
      Player currentPlayer = this.homeTeam.players.get(i);
      this.playersStats.put(currentPlayer, new PlayerGameStats());
    }
    for (int i = 0; i < this.awayTeam.players.size(); i++) {
      Player currentPlayer = this.awayTeam.players.get(i);
      this.playersStats.put(currentPlayer, new PlayerGameStats());
    }
    this.quarterNumber = 1;
    this.secondsLeftInQuarter = 720;
  }
  
  void addStat(Player player, String statName, double amount) {
    PlayerGameStats givenPlayerStats = this.playersStats.get(player);
    givenPlayerStats.addStat(statName, amount);
  }

  void addToPlayByPlay(String play) {
    this.playByPlay.add(this.homeTeam.name + " " + this.homeTeamScore + " - " + this.awayTeamScore + " "
  + this.awayTeam.name + "   " + play);
  }
  
  void printPlayByPlay() {
    for (String s: this.playByPlay) {
      System.out.println(s);
    }
  }
  
  void printGameStats() {
    System.out.println("Box Score:");
    System.out.println(this.homeTeam.location + " " + this.homeTeam.name);
    System.out.format("%1$-25s", "Name");
    System.out.format("%1$-5s", "MIN");
    System.out.format("%1$-5s", "FG");
    System.out.format("%1$-5s", "FGA");
    System.out.format("%1$-5s", "3P");
    System.out.format("%1$-5s", "3PA");
    System.out.format("%1$-5s", "FT");
    System.out.format("%1$-5s", "FTA");
    System.out.format("%1$-5s", "ORB");
    System.out.format("%1$-5s", "DRB");
    System.out.format("%1$-5s", "REB");
    System.out.format("%1$-5s", "AST");
    System.out.format("%1$-5s", "STL");
    System.out.format("%1$-5s", "BLK");
    System.out.format("%1$-5s", "PTS");
    System.out.format("%1$-5s", "TOV");
    System.out.format("%1$-5s", "PF");
    for (int i = 0; i < this.homeTeam.players.size(); i++) {
      System.out.println();
      Player currentPlayer = this.homeTeam.players.get(i);
      System.out.format("%1$-25s", (currentPlayer.firstName + " " + currentPlayer.lastName));
      this.playersStats.get(currentPlayer).printStats();
    }
    System.out.println();
    System.out.println();
    System.out.println(this.awayTeam.location + " " + this.awayTeam.name);
    System.out.format("%1$-25s", "Name");
    System.out.format("%1$-5s", "MIN");
    System.out.format("%1$-5s", "FG");
    System.out.format("%1$-5s", "FGA");
    System.out.format("%1$-5s", "3P");
    System.out.format("%1$-5s", "3PA");
    System.out.format("%1$-5s", "FT");
    System.out.format("%1$-5s", "FTA");
    System.out.format("%1$-5s", "ORB");
    System.out.format("%1$-5s", "DRB");
    System.out.format("%1$-5s", "REB");
    System.out.format("%1$-5s", "AST");
    System.out.format("%1$-5s", "STL");
    System.out.format("%1$-5s", "BLK");
    System.out.format("%1$-5s", "PTS");
    System.out.format("%1$-5s", "TOV");
    System.out.format("%1$-5s", "PF");
    for (int i = 0; i < this.awayTeam.players.size(); i++) {
      System.out.println();
      Player currentPlayer = this.awayTeam.players.get(i);
      System.out.format("%1$-25s", (currentPlayer.firstName + " " + currentPlayer.lastName));
      this.playersStats.get(currentPlayer).printStats();
    }
  }
  
}
