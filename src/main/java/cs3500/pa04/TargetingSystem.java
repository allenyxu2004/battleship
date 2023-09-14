package cs3500.pa04;

import cs3500.pa03.model.board.BoardStatePlayer;
import cs3500.pa03.model.board.CellState;
import cs3500.pa03.model.coordinates.Coord;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TargetingSystem {

  private ArrayList<FocusShot> oneHit = new ArrayList<>();
  private ArrayList<FocusShot> diagonalMiss = new ArrayList<>();
  private ArrayList<FocusShot> oneMissOneHit = new ArrayList<>();
  private ArrayList<FocusShot> twoHit = new ArrayList<>();
  private ArrayList<FocusShot> threeHit = new ArrayList<>();
  private ArrayList<FocusShot> priorityShots = new ArrayList<>();
  private Deque<Coord> shotQueue;
  private CellState[][] opponentBoard;
  BoardStatePlayer player;

  public TargetingSystem(Deque<Coord> shotQueue, CellState[][] opponentBoard,
                         BoardStatePlayer player) {
    this.shotQueue = shotQueue;
    this.opponentBoard = opponentBoard;
    this.player = player;
  }

  public boolean isEmpty() {
    return this.diagonalMiss.isEmpty() && this.oneHit.isEmpty()
        && this.oneMissOneHit.isEmpty() && this.twoHit.isEmpty()
        && this.threeHit.isEmpty() && this.priorityShots.isEmpty();
  }

  public void fireShots() {
    AtomicInteger shotsSoFar = new AtomicInteger(0);
    for (FocusShot fs : priorityShots) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }

    for (FocusShot fs : threeHit) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }

    for (FocusShot fs : twoHit) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }

    for (FocusShot fs : oneMissOneHit) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }

    for (FocusShot fs : diagonalMiss) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }

    for (FocusShot fs : oneHit) {
      if (shotsSoFar.get() < player.getShipsLeft()) {
        fs.addToShotQueue(this.shotQueue, player.getShipsLeft(), shotsSoFar);
      }
    }
  }


  private boolean listContainsFocusShot(FocusShot focusShot, ArrayList<FocusShot> list) {

    for (FocusShot f : list) {
      for (Coord c : f.getShots()) {
        if (c.equals(focusShot.getInitialShot())) {
          return true;
        }
      }
    }
    return false;
  }

  public void sortSuccessfulHits(List<Coord> successfulHits) {

    for (Coord c : successfulHits) {
      FocusShot focusShot = new FocusShot(c, opponentBoard);
      if (focusShot.canConfirmShipDirection() || listContainsFocusShot(focusShot, priorityShots)) {
        if (listContainsFocusShot(focusShot, priorityShots)) {
        } else {
          focusShot.addNeighborsInDirection();
          priorityShots.add(focusShot);
        }
      } else if (focusShot.isThreeOrMoreHit() || listContainsFocusShot(focusShot, threeHit)) {
        if (listContainsFocusShot(focusShot, threeHit)) {
        } else {
          threeHit.add(focusShot);
        }
      } else if (focusShot.isTwoHit() || listContainsFocusShot(focusShot, twoHit)) {
        if (listContainsFocusShot(focusShot, twoHit)) {
        } else {
          twoHit.add(focusShot);
        }
      } else if (focusShot.isOneHitOneMiss()) {
        oneMissOneHit.add(focusShot);
      } else if (focusShot.isDiagonalMiss()) {
        diagonalMiss.add(focusShot);
      } else if (focusShot.isOneHit()) {
        oneHit.add(focusShot);
      }
    }
  }


  public void transferExceptions() {
    for (int i = 0; i < twoHit.size(); i++) {
      FocusShot focusShot = twoHit.get(i);
      for (FocusShot fs : focusShot.getExceptionFocusShots()) {
        fs.addNeighborsInDirection();
        priorityShots.add(fs);
      }
      if (focusShot.getExceptionFocusShots().size() > 0) {
        twoHit.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < oneMissOneHit.size(); i++) {
      FocusShot focusShot = oneMissOneHit.get(i);
      for (FocusShot fs : focusShot.getExceptionFocusShots()) {
        fs.addNeighborsInDirection();
        priorityShots.add(fs);
      }
      if (focusShot.getExceptionFocusShots().size() > 0) {
        oneMissOneHit.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < diagonalMiss.size(); i++) {
      FocusShot focusShot = diagonalMiss.get(i);
      for (FocusShot fs : focusShot.getExceptionFocusShots()) {
        fs.addNeighborsInDirection();
        priorityShots.add(fs);
      }
      if (focusShot.getExceptionFocusShots().size() > 0) {
        diagonalMiss.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < oneHit.size(); i++) {
      FocusShot focusShot = oneHit.get(i);
      for (FocusShot fs : focusShot.getExceptionFocusShots()) {
        fs.addNeighborsInDirection();
        priorityShots.add(fs);
      }
      if (focusShot.getExceptionFocusShots().size() > 0) {
        oneHit.remove(focusShot);
        i--;
      }
    }
  }

  public void filter() {
    for (int i = 0; i < priorityShots.size(); i++) {
      FocusShot focusShot = priorityShots.get(i);
      if (focusShot.completeFocusShot()) {
        priorityShots.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < threeHit.size(); i++) {
      FocusShot focusShot = threeHit.get(i);
      if (focusShot.completeFocusShot()) {
        threeHit.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < twoHit.size(); i++) {
      FocusShot focusShot = twoHit.get(i);
      if (focusShot.completeFocusShot()) {
        twoHit.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < oneMissOneHit.size(); i++) {
      FocusShot focusShot = oneMissOneHit.get(i);
      if (focusShot.completeFocusShot()) {
        oneMissOneHit.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < diagonalMiss.size(); i++) {
      FocusShot focusShot = diagonalMiss.get(i);
      if (focusShot.completeFocusShot()) {
        diagonalMiss.remove(focusShot);
        i--;
      }
    }

    for (int i = 0; i < oneHit.size(); i++) {
      FocusShot focusShot = oneHit.get(i);
      if (focusShot.completeFocusShot()) {
        oneHit.remove(focusShot);
        i--;
      }
    }
  }

  public void recalibrate() {
    for (FocusShot focusShot : priorityShots) {
      focusShot.recalibrateFocusShot();
    }

    for (FocusShot focusShot : threeHit) {
      focusShot.recalibrateFocusShot();
    }

    for (FocusShot focusShot : twoHit) {
      focusShot.recalibrateFocusShot();
    }

    for (FocusShot focusShot : oneMissOneHit) {
      focusShot.recalibrateFocusShot();
    }

    for (FocusShot focusShot : diagonalMiss) {
      focusShot.recalibrateFocusShot();
    }

    for (FocusShot focusShot : oneHit) {
      focusShot.recalibrateFocusShot();
    }
  }


  public void transferFocusShots() {
    transferOneHit();
    transferOneMissOneHit();
    transferDiagonalMiss();
    transferTwoHits();
    transferThreeHitToPriority();
  }


  // may slow down
  private void transferThreeHitToPriority() {
    for (int i = 0; i < threeHit.size(); i++) {
      FocusShot focusShot = threeHit.get(i);
      if (!listContainsFocusShot(focusShot, priorityShots)) {
        for (Coord c : focusShot.getShots()) {
          FocusShot shotElement = new FocusShot(c, opponentBoard);
          if (shotElement.canConfirmShipDirection()) {
            priorityShots.add(focusShot);
            threeHit.remove(focusShot);
            i--;
            break;
          }
        }
      } else {
        threeHit.remove(focusShot);
        i--;
      }
    }
  }

  private void transferTwoHits() {
    for (int i = 0; i < twoHit.size(); i++) {
      FocusShot focusShot = twoHit.get(i);
      if (!listContainsFocusShot(focusShot, threeHit)
          && !listContainsFocusShot(focusShot, priorityShots)) {

        for (Coord c : focusShot.getShots()) {
          FocusShot shotElement = new FocusShot(c, opponentBoard);
          if (shotElement.canConfirmShipDirection()) {
            priorityShots.add(focusShot);
            twoHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isThreeOrMoreHit()) {
            threeHit.add(focusShot);
            twoHit.remove(focusShot);
            i--;
            break;
          }
        }
      } else {
        twoHit.remove(focusShot);
        i--;
      }
    }
  }

  private void transferOneMissOneHit() {
    for (int i = 0; i < oneMissOneHit.size(); i++) {
      FocusShot focusShot = oneMissOneHit.get(i);
      if (!listContainsFocusShot(focusShot, priorityShots)
          && !listContainsFocusShot(focusShot, threeHit) &&
          !listContainsFocusShot(focusShot, twoHit) &&
          !listContainsFocusShot(focusShot, diagonalMiss)) {

        for (Coord c : focusShot.getShots()) {
          FocusShot shotElement = new FocusShot(c, this.opponentBoard);
          if (shotElement.canConfirmShipDirection()) {
            priorityShots.add(focusShot);
            oneMissOneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isThreeOrMoreHit()) {
            threeHit.add(focusShot);
            oneMissOneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isTwoHit()) {
            twoHit.add(focusShot);
            oneMissOneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isDiagonalMiss()) {
            diagonalMiss.add(focusShot);
            oneMissOneHit.remove(focusShot);
            i--;
            break;
          }
        }
      } else {
        oneMissOneHit.remove(focusShot);
        i--;
      }
    }
  }

  private void transferDiagonalMiss() {
    for (int i = 0; i < diagonalMiss.size(); i++) {
      FocusShot focusShot = diagonalMiss.get(i);
      if (!listContainsFocusShot(focusShot, priorityShots)
          && !listContainsFocusShot(focusShot, threeHit) &&
          !listContainsFocusShot(focusShot, twoHit)) {
        for (Coord c : focusShot.getShots()) {
          FocusShot shotElement = new FocusShot(c, this.opponentBoard);
          if (shotElement.canConfirmShipDirection()) {
            priorityShots.add(focusShot);
            diagonalMiss.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isThreeOrMoreHit()) {
            threeHit.add(focusShot);
            diagonalMiss.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isTwoHit()) {
            twoHit.add(focusShot);
            diagonalMiss.remove(focusShot);
            i--;
            break;
          }
        }
      } else {
        diagonalMiss.remove(focusShot);
        i--;
      }
    }
  }

  private void transferOneHit() {
    for (int i = 0; i < oneHit.size(); i++) {
      FocusShot focusShot = oneHit.get(i);
      if (!listContainsFocusShot(focusShot, priorityShots)
          && !listContainsFocusShot(focusShot, threeHit) &&
          !listContainsFocusShot(focusShot, twoHit) &&
          !listContainsFocusShot(focusShot, diagonalMiss) &&
          !listContainsFocusShot(focusShot, oneMissOneHit)) {

        for (Coord c : focusShot.getShots()) {
          FocusShot shotElement = new FocusShot(c, this.opponentBoard);
          if (shotElement.canConfirmShipDirection()) {
            priorityShots.add(focusShot);
            oneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isThreeOrMoreHit()) {
            threeHit.add(focusShot);
            oneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isTwoHit()) {
            twoHit.add(focusShot);
            oneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isDiagonalMiss()) {
            diagonalMiss.add(focusShot);
            oneHit.remove(focusShot);
            i--;
            break;
          } else if (shotElement.isOneHitOneMiss()) {
            oneMissOneHit.add(focusShot);
            oneHit.remove(focusShot);
            i--;
            break;
          }
        }
      } else {
        oneHit.remove(focusShot);
        i--;
      }
    }
  }


}
