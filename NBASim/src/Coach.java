// a coach, with integer ratings (ranging from 25 to 99) in various categories and a team
public class Coach {

  int playcalling;
  int ingameAdjustments;
  int offense;
  int defense;
  int adjustsToPlayers;
  int age;
  
  Team team;
  
  Coach(Team team) {
    this.team = team;
  }
  
  Coach() {
    
  }
}
