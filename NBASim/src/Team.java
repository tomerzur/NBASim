import java.util.ArrayList;

public class Team {
  String name;
  String location;
  Coach coach;
  ArrayList<Player> players;
  Lineup startingLineup;
  Rotation rotation;
  
  Team(String location, String name) {
    this.name = name;
    this.location = location;
    this.players = new ArrayList<>();
  }
  
  void addPlayer(Player p) {
    this.players.add(p);
    p.team = this;
  }
  
  void addCoach(Coach c) {
    this.coach = c;
    c.team = this;
  }

  boolean sameTeam(Team otherTeam) {
    // TODO Auto-generated method stub
    return this.name.equals(otherTeam.name);
  }
}
