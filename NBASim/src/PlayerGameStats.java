
public class PlayerGameStats {

  double min;
  int fg;
  int fga;
  int threept;
  int threeptattempted;
  int ft;
  int fta;
  int orb;
  int drb;
  int ast;
  int stl;
  int blk;
  int tov;
  int pf;
  int pts;
  
  PlayerGameStats() {
    this.min = 0;
    this.fg = 0;
    this.fga = 0;
    this.threept = 0;
    this.threeptattempted = 0;
    this.ft = 0;
    this.fta = 0;
    this.orb = 0;
    this.drb = 0;
    this.ast = 0;
    this.stl = 0;
    this.blk = 0;
    this.tov = 0;
    this.pf = 0;
    this.pts = 0;
  }

  public void addStat(String statName, double amount) {
    if (statName.equals("min")) {
      this.min = this.min + amount;
    }
    else if (statName.equals("fg")) {
      this.fg = this.fg + (int) amount;
    }
    else if (statName.equals("fga")) {
      this.fga = this.fga + (int) amount;
    }
    else if (statName.equals("threept")) {
      this.threept = this.threept + (int) amount;
    }
    else if (statName.equals("threeptattempted")) {
      this.threeptattempted = this.threeptattempted + (int) amount;
    }
    else if (statName.equals("ft")) {
      this.ft = this.ft + (int) amount;
    }
    else if (statName.equals("fta")) {
      this.fta = this.fta + (int) amount;
    }
    else if (statName.equals("orb")) {
      this.orb = this.orb + (int) amount;
    }
    else if (statName.equals("drb")) {
      this.drb = this.drb + (int) amount;
    }
    else if (statName.equals("ast")) {
      this.ast = this.ast + (int) amount;
    }
    else if (statName.equals("stl")) {
      this.stl = this.stl + (int) amount;
    }
    else if (statName.equals("blk")) {
      this.blk = this.blk + (int) amount;
    }
    else if (statName.equals("tov")) {
      this.tov = this.tov + (int) amount;
    }
    else if (statName.equals("pf")) {
      this.pf = this.pf + (int) amount;
    }
    else if (statName.equals("pts")) {
      this.pts = this.pts + (int) amount;
    }
    else {
      System.out.println("given string is not a valid stat name");
    }
  }

  void printStats() {
    System.out.format("%1$-5.1f", this.min);
    System.out.format("%1$-5s", this.fg);
    System.out.format("%1$-5s", this.fga);
    System.out.format("%1$-5s", this.threept);
    System.out.format("%1$-5s", this.threeptattempted);
    System.out.format("%1$-5s", this.ft);
    System.out.format("%1$-5s", this.fta);
    System.out.format("%1$-5s", this.orb);
    System.out.format("%1$-5s", this.drb);
    System.out.format("%1$-5s", (this.orb + this.drb));
    System.out.format("%1$-5s", this.ast);
    System.out.format("%1$-5s", this.stl);
    System.out.format("%1$-5s", this.blk);
    System.out.format("%1$-5s", this.pts);
    System.out.format("%1$-5s", this.tov);
    System.out.format("%1$-5s", this.pf);
  }
  
  
  
}
