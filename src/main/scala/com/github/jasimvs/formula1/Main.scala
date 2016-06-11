package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/10/2016.
 */
object Main {

  def main(args: Array[String]) {
    //    println(RaceConfig.calculateStartingPosition(4633))
    //    println(RaceConfig.calculateStartingPositionRecursive(4633))
    println("Welcome to Formula 1 racer!")
    println("Once you enter the track length and number of teams, we will declare the winner! Enter 0 to exit.")
    var flag = true;
    while (flag) {
      val raceService: RaceService = new DefaultRaceService()
      try {
        println("Please enter track length (in kms): ")
        val trackLength = scala.io.StdIn.readInt()
        println("Please enter number of teams: ")
        val noOfTeams = scala.io.StdIn.readInt()
        if (trackLength > 0 && noOfTeams > 0) {
          val race1 = raceService.race(RaceConfig(trackLength, noOfTeams))
          printResult(race1)
        } else {
          println("Exiting...")
          flag = false
        }
      } catch {
        case nfe: NumberFormatException =>
          println("Exiting...")
          flag = false
      }
    }
  }

  def printResult(raceConfig: RaceConfig) = {
    raceConfig.teams.map(team => println(s"Team ${team.teamNumber} # Time: ${team.raceTime} seconds # Final speed: ${team.getCurrentSpeedInKmph} kmph"))
  }

}
