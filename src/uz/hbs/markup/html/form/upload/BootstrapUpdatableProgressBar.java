package uz.hbs.markup.html.form.upload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

public abstract class BootstrapUpdatableProgressBar extends BootstrapProgressBar {
    private static final long serialVersionUID = 1L;
	private Duration updateInterval = Duration.seconds(5);
    private UpdateBehavior behavior;

    public BootstrapUpdatableProgressBar(String id) {
        super(id);

        commonInit();
    }

    public BootstrapUpdatableProgressBar(String id, IModel<Integer> model) {
        super(id, model);

        commonInit();
    }

    public Duration updateInterval() {
        return behavior.updateInterval();
    }

    public BootstrapUpdatableProgressBar updateInterval(Duration updateInterval) {
        behavior.updateInterval(updateInterval);
        return this;
    }

    private void commonInit() {
        setOutputMarkupId(true);
        active(true);
        striped(true);

        behavior = new UpdateBehavior(updateInterval) {
            private static final long serialVersionUID = 1L;

			@Override
            protected void onPostProcessTarget(AjaxRequestTarget target) {
                super.onPostProcessTarget(target);

                BootstrapUpdatableProgressBar.this.onPostProcessTarget(target);
            }
        };

        indicator().add(behavior);
    }

    protected void onPostProcessTarget(AjaxRequestTarget target) {
        value(newValue());

        if (complete()) {
            behavior.stop(target);

            onComplete(target);
        }
    }

    protected void onComplete(AjaxRequestTarget target) {
        active(false);
        striped(false);

        target.add(this);
    }

    protected abstract IModel<Integer> newValue();

    private abstract static class UpdateBehavior extends AjaxSelfUpdatingTimerBehavior {
        private static final long serialVersionUID = 1L;

		/**
         * Construct.
         *
         * @param updateInterval Duration between AJAX callbacks
         */
        private UpdateBehavior(Duration updateInterval) {
            super(updateInterval);
        }

        private void updateInterval(Duration duration) {
            setUpdateInterval(duration);
        }

        private Duration updateInterval() {
            return getUpdateInterval();
        }
    }
}
