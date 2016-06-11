package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/10/2016.
 */
object Main {

  def main(args: Array[String]) {
    //    println(calculateStartingPosition(4633))
    //    println(calculateStartingPositionRecursive(4633))
    val race1 = race(RaceConfig(10, 5, RaceService.initializeTeam))
    println(race1)
  }

  def race(raceConfig: RaceConfig): RaceConfig = {
    var raceConf : RaceConfig = raceConfig
    while (RaceService.allTeamsCompletedRace(raceConf)) {
      raceConf = RaceService.updateTeams(raceConf, 2)
    }
    raceConf
  }


}
