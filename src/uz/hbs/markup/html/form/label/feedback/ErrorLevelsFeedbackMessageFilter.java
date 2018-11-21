package uz.hbs.markup.html.form.label.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

public class ErrorLevelsFeedbackMessageFilter implements IFeedbackMessageFilter{
	private static final long serialVersionUID = 1L;
	/** The minimum error level */
	private int[] filteredErrorLevels;

	/**
	 * Constructor
	 *
	 * @param filteredErrorLevels The FeedbackMessages that have thes error levels will
     *                            not be shown.
	 */
	public ErrorLevelsFeedbackMessageFilter(int[] filteredErrorLevels){
		this.filteredErrorLevels = filteredErrorLevels;
	}

	/**
     * Method accept, only accept FeedbackMessages that are not in the list of error levels to filter.
     *
     * @param message of type FeedbackMessage
     * @return boolean
     */
    public boolean accept(FeedbackMessage message){
        for (int errorLevel : filteredErrorLevels) {
            if (message.getLevel() == errorLevel) {
                return false;
            }
        }
        return true;
	}
}