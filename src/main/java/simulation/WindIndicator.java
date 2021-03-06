/*
this class has one task:
measures force and direction of wind
for simulation purposes it counts APPARENT wind in a yacht's reference system, receiving:
Wind.speedAtWaterLevel, Wind.windGradient, Wind.direction, Yacht.speed (outer.speed), Yacht.direction Sail.height..
(windGradient is counted by a separate function)
*/

package simulation;

import lombok.Data;
import yacht.Yacht;

import static java.lang.Math.*;

@Data //@Getter , @Setter , @ToString , @EqualsAndHashCode and @RequiredArgsConstructor
public class WindIndicator {

    private double speed;           //in m/s
    private double direction;       //in degrees (-180 - +180)

    /* The following function counts apparent wind and returns its force and direction as following:
            >>wind from starboard: (+0 - +180)
            >>wind from port: (-0 - -180) degrees
         */
    public Wind measureWind(Wind trueWind, Yacht yacht, double height) {
        double windSpeed = trueWind.getSpeed() * Simulation.windGradient(height);
        double angleBetweenVectorsInDegrees = abs(trueWind.getDirection() - yacht.getFollowedCourseAzimuth());    // yachtDirection == inducedWindDirection
        double apparentWindSpeed = sqrt(pow(windSpeed, 2) + pow(yacht.getVelocity(), 2) + 2 * windSpeed * yacht.getVelocity() * cos(toRadians(angleBetweenVectorsInDegrees)));
        double apparentWindDirectionCountingFromBow = toDegrees(atan2(windSpeed * sin(toRadians(angleBetweenVectorsInDegrees)),
                yacht.getVelocity() + windSpeed * cos(toRadians(angleBetweenVectorsInDegrees))));
        if (apparentWindSpeed < 0) {
            apparentWindSpeed *= -1;
            apparentWindDirectionCountingFromBow += 180;
        }
        if (apparentWindDirectionCountingFromBow > 180)
            apparentWindDirectionCountingFromBow -= 360;

        setSpeed(apparentWindSpeed);
        setDirection(apparentWindDirectionCountingFromBow);

        return new Wind(apparentWindSpeed, apparentWindDirectionCountingFromBow);
    }
}
