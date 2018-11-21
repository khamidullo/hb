package uz.hbs.actions.hotel.reservations.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import uz.hbs.beans.TourAgent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.CommonUtil;

public class TourAgentDetailPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TourAgentDetailPanel(String id, final long touragentuser_id) {
		super(id);
		TourAgent ta = new MyBatisHelper().selectOne("selectTourAgentDetailsById", touragentuser_id);
		add(new Label("name", ta.getName()));
		add(new Label("contact_person", CommonUtil.nvl(ta.getManagers().getLast_name()).concat(" ").concat(CommonUtil.nvl(ta.getManagers().getFirst_name())).concat(" ").concat(CommonUtil.nvl(ta.getManagers().getMiddle_name()))));
		add(new Label("contact_number_1", ta.getManagers().getContact_number()));
		add(new Label("contact_number_2", ta.getPrimary_phone()));
		add(new Label("contact_email", ta.getManagers().getContact_email()));
		add(new Label("address", ta.getAddresses().getAddress()));
	}
}
