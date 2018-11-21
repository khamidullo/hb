package uz.hbs.markup.html.form.label.feedback;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class FeedbackLabel  extends Label {
    private static final long serialVersionUID = 1L;
	/** Field component holds a reference to the {@link Component} this FeedbackLabel belongs to */
    private FormComponent<?> component;
    /** Field text holds a model of the text to be shown in the FeedbackLabel */
    private IModel<Serializable> text = null;

    /**
     * Call this constructor if you just want to display the FeedbackMessage of the component
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the FeedbackMessage for.
     */
    public FeedbackLabel(String id, FormComponent<?> component) {
        super(id);
        this.component = component;
    }

    /**
     * Call this constructor if you want to display a custom text
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the custom text for.
     * @param text The custom text to show when the FormComponent has a FeedbackMessage
     */
    public FeedbackLabel(String id, FormComponent<?> component, String text) {
        this(id, component, new Model<Serializable>(text));
    }

    /**
     * Call this constructor if you want to display a custom model (for easy i18n)
     * @param id The non-null id of this component
     * @param component The {@link FormComponent} to show the custom model for.
     * @param iModel The custom model to show when the {@link FormComponent} has a FeedbackMessage
     */
    public FeedbackLabel(String id, FormComponent<?> component, IModel<Serializable> iModel) {
        super(id);
        this.component = component;
        this.text = iModel;
    }

    /**
     * Set the content of this FeedbackLabel, depending on if the component has a FeedbackMessage.
     *
     * The HTML class attribute will be filled with the error level of the feedback message. That way, you can easily
     * style different messages differently. Examples:
     *
     * class = "feedbacklabel INFO"
     * class = "feedbacklabel ERROR"
     * class = "feedbacklabel DEBUG"
     * class = "feedbacklabel FATAL"
     *
     *
     * @see Component
     */
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        this.setDefaultModel(null);
        
        if(component.getFeedbackMessages() != null && component.getFeedbackMessages().first() != null){
            if(this.text != null){
                this.setDefaultModel(text);
            } else {
                this.setDefaultModel(new Model<Serializable>(component.getFeedbackMessages().first().getMessage()));
            }

            this.add(new AttributeModifier("class", new Model<Serializable>("feedbacklabel " + component.getFeedbackMessages().first().getLevelAsString())));
        } else {
            this.setDefaultModel(null);
        }
    }
}