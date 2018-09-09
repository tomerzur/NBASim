// use this class to test that a game runs as it should
// prints out a play-by-play log of the whole game and a box score
public class Testing {

  public static void main(String[] args) {
    PlayerTeamData data = new PlayerTeamData();
    Game g = new Game(data.lakers, data.warriors);
    g.playGame();
    g.stats.printPlayByPlay();
    g.stats.printGameStats();
  }

}
