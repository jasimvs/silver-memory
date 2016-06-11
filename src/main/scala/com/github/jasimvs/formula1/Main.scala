package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/10/2016.
 */
object Main {

  def main(args: Array[String]) {
    //    println(RaceConfig.calculateStartingPosition(4633))
    //    println(RaceConfig.calculateStartingPositionRecursive(4633))
    val race1 = race(RaceConfig(10, 5), new DefaultRaceService)
    println(race1)
  }

  def race(raceConfig: RaceConfig, raceService: RaceService): RaceConfig = {
    var raceConf : RaceConfig = raceConfig
    while (raceService.isAnyTeamYetToCompleteRace(raceConf)) {
      raceConf = raceService.updateTeams(raceConf, 2)
    }
    raceConf
  }


}
