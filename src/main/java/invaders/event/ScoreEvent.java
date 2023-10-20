package invaders.event;

import javafx.event.Event;
import javafx.event.EventType;


/**
 * @Description :
 * addListener to update data
*/
public class ScoreEvent extends Event {
    public static final EventType<ScoreEvent> SCORE_CHANGED = new EventType<>(ANY, "SCORE_CHANGED");

    private int scoreChange;

    public ScoreEvent(int scoreChange) {
        super(SCORE_CHANGED);
        this.scoreChange = scoreChange;
    }

    public int getScoreChange() {
        return scoreChange;
    }
}
