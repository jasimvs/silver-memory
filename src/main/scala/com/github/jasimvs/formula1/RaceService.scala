package main.scala.com.github.jasimvs.formula1

import com.github.jasimvs.formula1.StandardFormulas
import com.typesafe.scalalogging.LazyLogging

/**
 * Created by jsulaiman on 6/11/2016.
 */
trait RaceService {

  def race(raceConfig: RaceConfig): RaceConfig
}

class DefaultRaceService extends RaceService with LazyLogging {

  override def race(raceConfig: RaceConfig): RaceConfig = {
    var raceConf: RaceConfig = raceConfig
    while (isAnyTeamYetToCompleteRace(raceConf)) {
      raceConf = updateTeams(raceConf, 2)
      logTeamStatus(raceConf)
    }
    raceConf
  }

  private def logTeamStatus(raceConfig: RaceConfig) = {
    raceConfig.teams.map(team => logger.debug(s"Team ${team.teamNumber} # Time: ${team.raceTime} seconds # distance: ${team.trackPositionInMetres}# speed: ${team.getCurrentSpeedInKmph} kmph"))
  }

  def isAnyTeamYetToCompleteRace(raceConfig: RaceConfig): Boolean =
    !raceConfig.teams.filterNot(team => raceCompleted(team, raceConfig.trackLengthInKms)).isEmpty

  private def updateTeams(raceConfig: RaceConfig, timeInSeconds: Int): RaceConfig = {
    val teams = raceConfig.teams.map(team =>
      if (raceCompleted(team, raceConfig.trackLengthInKms)) {
        team
      } else {
        val index = raceConfig.teams.indexOf(team)
        val isLastPos = (index + 1 == raceConfig.teams.size)
        val activateHf = shouldActivateHf(raceConfig.teams, team, index)
        logger.debug(s"Updating team ${team.teamNumber} at position ${index + 1} with HF=$activateHf and last position=$isLastPos ")
        updateTeam(team, timeInSeconds, activateHf, isLastPos)
      }
    )
    raceConfig.copy(teams = teams.sortWith(teamSortAlgorithm(_, _, raceConfig.trackLengthInKms)))
  }

  def shouldActivateHf(teams: Seq[Team], team: Team, index: Int): Boolean = {
    val index = teams.indexOf(team)
    val carAheadIsTooClose = (index > 0 && team.trackPositionInMetres - teams(index - 1).trackPositionInMetres <= 10
      && team.trackPositionInMetres - teams(index - 1).trackPositionInMetres >= -10)
    val carBehindIsTooClose = (index < teams.size - 1 && team.trackPositionInMetres - teams(index + 1).trackPositionInMetres >= -10
      && team.trackPositionInMetres - teams(index + 1).trackPositionInMetres <= 10)
    if (carAheadIsTooClose)
      logger.debug(s"${team.trackPositionInMetres} - ${teams(index - 1).trackPositionInMetres} <= +/-10")
    if (carBehindIsTooClose)
      logger.debug(s"${team.trackPositionInMetres} - ${teams(index + 1).trackPositionInMetres} <= +/-10")
    carAheadIsTooClose || carBehindIsTooClose
  }

  private def updateTeam(team: Team, timeInSeconds: Int, activateHf: Boolean, lastPos: Boolean): Team = {
    val activateNitro: Boolean = lastPos && team.nitroAvailability > 0 && team.currentSpeed > 0
    val nitroFactor = if (activateNitro) team.car.nitroPower else 1
    val hfFactor = if (activateHf) team.car.handlingFactor else 1
    val distanceSpeedTuple = calculateDistanceTravelled(team.currentSpeed * nitroFactor * hfFactor,
      team.car.accelerationInMetresPerSecondSquare, timeInSeconds, team.car.topSpeedInMetresPerSecond)

    if (activateNitro) {
      logger.debug("Activated Nitro")
      team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1,
        currentSpeed = distanceSpeedTuple._2, raceTime = team.raceTime + timeInSeconds,
        nitroAvailability = team.nitroAvailability - 1)
    } else {
      team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1,
        currentSpeed = distanceSpeedTuple._2, raceTime = team.raceTime + timeInSeconds)
    }
  }

  def teamSortAlgorithm(team1: Team, team2: Team, trackLengthInKms: Float): Boolean = {
    val team1Completed = raceCompleted(team1, trackLengthInKms)
    val team2Completed = raceCompleted(team2, trackLengthInKms)

    if (team1Completed && team2Completed)
      team1.raceTime < team2.raceTime
    else if (team1Completed)
      true
    else if (team2Completed)
      false
    else
      team1.trackPositionInMetres > team2.trackPositionInMetres
  }

  def raceCompleted(team: Team, trackLengthInKms: Float) = team.trackPositionInMetres >= trackLengthInKms * 1000

  def calculateDistanceTravelled(u: Double, a: Int, t: Double, maxV: Double): (Double, Double) = {
    val v = StandardFormulas.calculateSpeedAfterTime(u, a, t)
    if (u >= maxV) {
      (StandardFormulas.calculateDistanceTravelled(maxV, 0, t), maxV)
    } else if (v > maxV) {
      val t1 = StandardFormulas.calculateTimeToReachSpeed(u, maxV, a)
      val s1 = StandardFormulas.calculateDistanceTravelled(u, a, t1)
      val s2 = StandardFormulas.calculateDistanceTravelled(maxV, 0, t - t1)
      (s1 + s2, maxV)
    } else {
      (StandardFormulas.calculateDistanceTravelled(u, a, t), v)
    }
  }
}

