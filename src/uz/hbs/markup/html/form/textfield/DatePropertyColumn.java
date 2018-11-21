package uz.hbs.markup.html.form.textfield;

import java.util.Date;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import uz.hbs.MyWebApplication;
import uz.hbs.utils.FormatUtil;

public class DatePropertyColumn<T,S> extends PropertyColumn<T,S>{
    private static final long serialVersionUID = 1L;

    public DatePropertyColumn(IModel<String> displayModel, S sortProperty, String propertyExpression){
        super(displayModel, sortProperty, propertyExpression);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel){
        item.add(new Label(componentId, FormatUtil.toString(((IModel<Date>) rowModel).getObject(), MyWebApplication.DATE_FORMAT)));
    }
}