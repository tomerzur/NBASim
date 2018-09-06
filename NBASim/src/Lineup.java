import java.util.ArrayList;

public class Lineup {

  Player pg;
  Player sg;
  Player sf;
  Player pf;
  Player c;
  Team team;
  
  ArrayList<Player> players = new ArrayList<>();
  
  Lineup(Player pg, Player sg, Player sf, Player pf, Player c) {
    this.pg = pg;
    this.sg = sg;
    this.sf = sf;
    this.pf = pf;
    this.c = c;
    this.team = this.pg.team;
    this.players.add(this.pg);
    this.players.add(this.sg);
    this.players.add(this.sf);
    this.players.add(this.pf);
    this.players.add(this.c);
  }

  // given a player in this lineup, returns the position of the player
  String getPosition(Player otherPlayer) {
    if (this.pg.samePlayer(otherPlayer)) {
      return "pg";
    }
    else if (this.sg.samePlayer(otherPlayer)) {
      return "sg";
    }
    else if (this.sf.samePlayer(otherPlayer)) {
      return "sf";
    }
    else if (this.pf.samePlayer(otherPlayer)) {
      return "pf";
    }
    else {
      return "c";
    }
  }
  
  Player getPlayer(String position) {
    if (position.equals("pg")) {
      return this.pg;
    }
    else if (position.equals("sg")) {
      return this.sg;
    }
    else if (position.equals("sf")) {
      return this.sf;
    }
    else if (position.equals("pf")) {
      return this.pf;
    }
    else {
      return this.c;
    }
  }

  boolean hasPlayer(Player player) {
    return player.samePlayer(this.pg) || player.samePlayer(this.sg) || player.samePlayer(this.sf) ||
        player.samePlayer(this.pf) || player.samePlayer(this.c);
  }

  boolean sameLineup(Lineup otherLineup) {
    return this.pg.samePlayer(otherLineup.pg) && this.sg.samePlayer(otherLineup.sg) && this.sf.samePlayer(otherLineup.sf) &&
        this.pf.samePlayer(otherLineup.pf) && this.c.samePlayer(otherLineup.c);
  }

  void substitute(Player player, String position) {
    if (position.equals("pg")) {
      this.pg = player;
      this.players.set(0, player);
    }
    else if (position.equals("sg")) {
      this.sg = player;
      this.players.set(1, player);
    }
    else if (position.equals("sf")) {
      this.sf = player;
      this.players.set(2, player);
    }
    else if (position.equals("pf")) {
      this.pf = player;
      this.players.set(3, player);
    }
    else {
      this.c = player;
      this.players.set(4, player);
    }
  }

  String lineupAsString() {
    return "PG - " + this.pg.firstName + " " + this.pg.lastName + 
        ", SG - " + this.sg.firstName + " " + this.sg.lastName + 
        ", SF - " + this.sf.firstName + " " + this.sf.lastName + 
        ", PF - " + this.pf.firstName + " " + this.pf.lastName + 
        ", C - "  + this.c.firstName + " " + this.c.lastName;
  }

  
}
