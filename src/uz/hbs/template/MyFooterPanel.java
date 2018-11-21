package uz.hbs.template;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.Home;
import uz.hbs.beans.User;
import uz.hbs.session.MySession;
import uz.hbs.utils.DateUtil;

public class MyFooterPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public MyFooterPanel(String id, boolean isExceptionPage) {
		super(id);
		
		User user = ((MySession)getSession()).getUser();
		String userType = "admin";
		if (user != null) {
			if (user.getType()!= null) {
				if (user.getType().getId() == User.TYPE_TOURAGENT_USER) {
					userType = "ta";
				} else if (user.getType().getId() == User.TYPE_HOTEL_USER) {
					userType = "hotel";
				}
			}
		}
		
		WebMarkupContainer linksContainer = new WebMarkupContainer("linksContainer");
		linksContainer.setVisible(!isExceptionPage);
		add(linksContainer);
		
		WebMarkupContainer policyContainer = new WebMarkupContainer("policyContainer");
		ExternalLink policyLink = new ExternalLink("policyLink", "privacy_policy.pdf");
		policyLink.setContextRelative(true);
		policyContainer.add(policyLink);
		linksContainer.add(policyContainer);

		WebMarkupContainer instructionContainer = new WebMarkupContainer("instructionContainer");
		ExternalLink instructionLink = new ExternalLink("instructionLink", "instruction_" + userType + ".pdf");
		instructionLink.setContextRelative(true);
		instructionContainer.add(instructionLink);
		linksContainer.add(instructionContainer);
		
		WebMarkupContainer contractContainer = new WebMarkupContainer("contractContainer");
		ExternalLink contactLink = new ExternalLink("contractLink", "contract_" + userType + ".pdf");
		contactLink.setContextRelative(true);
		contractContainer.add(contactLink);
		linksContainer.add(contractContainer);
		
		//Bu link hamma pagelarda Wicket Ajax library yuklanish uchun qo'yilgan, bu panel esa hamma pageda qatnashadi
		add(new AjaxLink<String>("blank") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(Home.class);
			}
		});
		
		String copyrightYear = new StringResourceModel("copyright_year", null).getString();
		String currentYear = DateUtil.toString(new Date(), "yyyy");
		Label copyRight = new Label("copyright", new StringResourceModel("signin.copyright", null,
				new Object[] { (currentYear.equals(copyrightYear) ? currentYear :  copyrightYear + "-" + currentYear) + " <a href=\"http://hotelios.uz/\" style=\"color: #ffffff\">Hotelios</a>" }));
		copyRight.setEscapeModelStrings(false);
		add(copyRight);
	}
}
