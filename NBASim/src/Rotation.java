import java.util.ArrayList;

public class Rotation {

  // Each rotation consists of 16 lineups; 4 lineups per quarter (for 12:00 to 9:00, 9:00 to 6:00, 6:00 to 3:00, and 3:00 to 0:00)
  // the lineups are arranged chronologically
  Lineup[] gameLineups = new Lineup[16];
  
  Rotation(Lineup startingLineup) {
    this.gameLineups[0] = startingLineup;
  }
  
  Lineup getLineup(int quarterNumber, int timeInQuarter) {
    if (quarterNumber > 4) {
      quarterNumber = 4;
    }
    int lineupNumber = (quarterNumber - 1) * 4;
    if (timeInQuarter < 180) {
      lineupNumber = lineupNumber + 3;
    }
    else if (timeInQuarter < 360) {
      lineupNumber = lineupNumber + 2;
    }
    else if (timeInQuarter < 540) {
      lineupNumber = lineupNumber + 1;
    }
    else {
      // lineupNumber = lineupNumber
    }
    return this.gameLineups[lineupNumber];
  }
  
  
}
