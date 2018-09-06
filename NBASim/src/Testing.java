
public class Testing {

  public static void main(String[] args) {
    PlayerTeamData data = new PlayerTeamData();
    Game g = new Game(data.lakers, data.warriors);
    g.playGame();
    g.stats.printPlayByPlay();
    g.stats.printGameStats();
  }

}
