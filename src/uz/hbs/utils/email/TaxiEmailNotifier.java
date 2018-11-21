package uz.hbs.utils.email;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.MyWebApplication;
import uz.hbs.beans.AdditionalServiceDetail;
import uz.hbs.beans.AdditionalServiceOrder;
import uz.hbs.beans.AdditionalServicePrice;
import uz.hbs.beans.Message;
import uz.hbs.beans.TourAgent;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.DateUtil;
import uz.hbs.utils.FormatUtil;

public class TaxiEmailNotifier {
	private static final Logger logger = LoggerFactory.getLogger(TaxiEmailNotifier.class);

	public static void send(final long additionalServiceOrderId) {
		prepareDataFromDB(additionalServiceOrderId, false);
	}
	
	public static void sendCancel(final long additionalServiceOrderId) {
		prepareDataFromDB(additionalServiceOrderId, true);
	}
	
	private static void prepareDataFromDB(final long additionalServiceOrderId, boolean isCancel) {
		TourAgent tourAgent = new MyBatisHelper().selectOne("selectGreenHallEmail", additionalServiceOrderId);

		AdditionalServicePrice price = new MyBatisHelper().selectOne("selectCurrentAdditionalServicePrice");

		AdditionalServiceOrder order = new MyBatisHelper().selectOne("selectAdditionalServiceOrderById", additionalServiceOrderId);

		List<AdditionalServiceDetail> list = new MyBatisHelper().selectList("selectAdditionalServiceDetailsByOrderId", additionalServiceOrderId);

		for (AdditionalServiceDetail detail : list) {
			if (detail.isTaxi_order()) {
				prepareHtml(order, tourAgent, price, detail, isCancel);
			}
		}
	}

	private static void prepareHtml(AdditionalServiceOrder order, final TourAgent tourAgent, final AdditionalServicePrice price,
			AdditionalServiceDetail detail, boolean isCancel) {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("taxi_name", "Сити Такси");
		attributes.put("order", order.getId() + "/" + tourAgent.getLicense_id());
		if (isCancel) {
			attributes.put("cancel_info", "<span style=\"font-size: 48px; font-weight: bold;\">ОТМЕНА</span>");
		}
		attributes.put("order_date_time", DateUtil.toString(order.getCreate_date(), MyWebApplication.DATE_TIME_FORMAT));
		attributes.put("tour_agency_phone", tourAgent.getPrimary_phone());
		attributes.put("tour_agency_fax", tourAgent.getSecondary_phone());
		attributes.put("tour_agency_address", tourAgent.getAddresses().getAddress());
		attributes.put("ta_name", tourAgent.getTourAgentName());
		attributes.put("tour_agency_email", tourAgent.getEmail());
		attributes.put("tour_agency", tourAgent.getName());
		attributes.put("cars_count", detail.getTaxi_order_car());
		
		if (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED) {
			if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT) {
				attributes.put("from", "Аэропорт,<br />Терминал-" + detail.getAir_terminal());
				attributes.put("numb", detail.getAir_numb());
				attributes.put("time", DateUtil.toString(detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED ? order.getDeparture_date() : order.getArrival_date(),
						MyWebApplication.DATE_FORMAT) + " " + (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED && detail.isTaxi_order() ? detail.getTaxi_order_time() :detail.getAir_time()));
			} else {
				attributes.put("from", "Вокзал Ж/Д");
				attributes.put("numb", detail.getTrain_numb());
				attributes.put("time", DateUtil.toString(
						detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED ? order.getDeparture_date() : order.getArrival_date(),
								MyWebApplication.DATE_FORMAT) + " " + (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED && detail.isTaxi_order() ? detail.getTaxi_order_time() :detail.getTrain_time()));
			}

			if (detail.getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS) {
				attributes.put("to", detail.getLocation_value());
			} else {
				String destination = new MyBatisHelper().selectOne("selectHotelNameByReserveId", order.getReservations_id());
				attributes.put("to", destination);
			}
		} else {
			if (detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT) {
				attributes.put("to", "Аэропорт,<br />Терминал-" + detail.getAir_terminal());
				attributes.put("numb", detail.getAir_numb());
				attributes.put("time", DateUtil.toString(detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED ? order.getDeparture_date() : order.getArrival_date(),
						MyWebApplication.DATE_FORMAT) + " " + (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED && detail.isTaxi_order() ? detail.getTaxi_order_time() :detail.getAir_time()));
			} else {
				attributes.put("to", "Вокзал Ж/Д");
				attributes.put("numb", detail.getTrain_numb());
				attributes.put("time", DateUtil.toString(
						detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED ? order.getDeparture_date() : order.getArrival_date(),
								MyWebApplication.DATE_FORMAT) + " " + (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED && detail.isTaxi_order() ? detail.getTaxi_order_time() :detail.getTrain_time()));
			}

			if (detail.getLocation_type() == AdditionalServiceDetail.LOCATION_TYPE_ADDRESS) {
				attributes.put("from", detail.getLocation_value());
			} else {
				String destination = new MyBatisHelper().selectOne("selectHotelNameByReserveId", order.getReservations_id());
				attributes.put("from", destination);
			}
		}

		attributes.put("tourist_name", detail.getGuest());
		attributes.put("guest_phone", detail.getPhone_number());
		if (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED
				&& detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT
				&& detail.getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {// если это встреча в аэропорту и был заказан
																											// зеленый коридор
			attributes.put("additional_info",
					"Клиента из аэропорта выведут сотрудники ЦУДО Узбектуризм (Зеленый коридор). Они подведут гостя к Вам самостоятельно, увидев табличку с его именем");
		} else if (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED
				&& detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT
				&& detail.getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_GREEN_HALL) {// если это проводы в аэропорт и был заказан
																											// зеленый коридор
			attributes.put("additional_info",
					"Клиента в аэропорту встречают сотрудники ЦУДО Узбектуризм (Зеленый коридор). Просьба по прибытию в аэропорт позвонить по номеру +99898 3030307, чтобы Клиента встретили.");
		} else if (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_ARRIVED
				&& detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT
				&& detail.getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {// если это встреча в аэропорту и был заказан
																										// СИП зал
			attributes.put("additional_info", "Клиента необходимо встречать возле КПП №2 (возле CIP-зала)");
		} else if (detail.getService_type() == AdditionalServiceDetail.SERVICE_TYPE_DEPARTED
				&& detail.getTransport_type() == AdditionalServiceDetail.TRANSPORT_TYPE_AIRPORT
				&& detail.getAir_service_type() == AdditionalServiceDetail.AIR_SERVICE_TYPE_VIP_HALL) {// если это проводы в аэропорт, был заказан СИП
																										// зал или у туриста Бизнес класс
			attributes.put("additional_info", "Просьба высадить Клиента у КПП №2 (возле CIP-зала)");
		}

		MyStringTemplateGroup templateGroup = new MyStringTemplateGroup("mailgroup");
		templateGroup.setFileCharEncoding("UTF-8");
		templateGroup.registerRenderer(Double.class, FormatUtil.getDoubleRenderer());
		templateGroup.registerRenderer(BigDecimal.class, FormatUtil.getBigDecimalRenderer());

		String creator_email = new MyBatisHelper().selectOne("selectCreatorEmail", order.getId());

		StringTemplate st = templateGroup.getInstanceOf("uz/hbs/utils/email/templates/reservation/Taxi.html", attributes);
		sendEmail(price.getEmail_send_request(), st.toString(), String.valueOf(order.getId()), creator_email);
	}

	private static void sendEmail(final String recipient, final String content, final String number, String creator_email) {
		new Thread() {
			@Override
			public void run() {
				String serverType = MyWebApplication.getConfigBundle().getString("server_type");
				if (serverType.equals("production")) {
					serverType = "";
				} else {
					serverType = "Тест. ";
				}

				Message message = new Message();
				message.setRecipient(recipient);
				if (creator_email != null) 
					message.setRecipient_cc(new String[]{ creator_email });
				message.setRecipient_bcc(new String[] { "request@hotelios.uz", "maqsudjon@gmail.com" });
				message.setSubject(serverType + "Заявка на такси №" + number);
				message.setContent(content);
				try {
					message.setAttachment(Base64
							.decode("iVBORw0KGgoAAAANSUhEUgAAAYIAAABLCAYAAAB9aoBBAAAACXBIWXMAACHWAAAh3wF45y65AAAkg0lEQVR4nO2dCWBU1bn4z3fu3MkkE7JQWQIBsgCBBMSFamlLa7X21f9rVV7V2vJvban2VZqWZZJJQlhUjCEhAbTUpVhbW+lrq9K+v/Y9LbZWW6u2BUQhAbKQCBg2yQYks9zz/b8zGVrEJDN31uicH0zuLPec+93lfN93tu9YmCKmHCiadJFmWO5AziqR4VFAWJyfMf5FePVVT7xlUygUiYkl3gIkCsgYtBUUpHmx/1EB7GpgzE5f5dEvj7T0Ht20a968By9VxkChUMQBZQhiRNOM3OsBXKuA8csZDHwH8j9ADhq4PrWzY+6BvLzV01tbD8ZXUoVCkWgoQxBldk2alJI+yvI1g7GVpPcnDLYPAOhUM7iV2cS4A0W590zbe/AV8FUiFAqFIvooQxBF9s+amKMZermB4puk7K3D7gwg78VnNcQrW2fkfvuo3f3s+B1HzsRGUoVCkcgoQxAF/nwts2Qdzp+NwtiMIC4HFsAI+JFNRcggDTn+oLdPv+pQXt6K7NbWLlU7UCgU0UQZggizax7TR72T83XGjJVkAHJCyYPSjWGIt/fbRP7B2XkV7K3WHREWU6FQKP6JMgQR5K2CiWNt3XoFIruNAWRAOJkNNBVdIwz8aXPRlBomUp6c2tjoClfGqqoq64kTJ4KqoYwUkpKSjJqamr6hfq+qWkjnNCau5zRmzJj+yspKb6D9Fi5cqE2ePNnW19cX9OOhaZqnvr7ezWJcM6RnxUbPiikdQdfhLF0HES2ZLoRk5F1dXWkWiycd3dpog/MpgJhLP2VR/doOjNvpslkA2FlEOEvfn+IAB1GIg3SzjtkRu951ubo2b948Ikfsbdu2TXvjjZfS3O7kVAAYZRhGPmdGDj0VGYyJUQwhHZk8T/AiEy5A6Eeg8+R4EgS8LQDeQcSTVq+39wxiz8aNG89QPu97jiwOhyNN1/WgC5HL5UKPx9MTrQv32GPF+ltv6WlU+M0UFNe99957erATjBX7Z+XM1gxWQxr8WnLpI2Jg6QJw2sxC5A8j68s9dFne5kk7WzvDyZMKzZdsuv6FSMgXK1B422hTOdhvUhF0d5+63qbzBbGV6r3QdX2ENi8H2m/8+PEFaHi+T/dgVLB5I8dXqJz+mIxB2I6AGXq7u6Wcc4LdnxQSdnd3r6a3rVEUy4d0aHp7O+eTjB/XOLtcGHohKb9cTZY9OKc63rs99zVKe8p9hfSYC2Ffqj15Z4XT+Vcv4gvr16/virbswVBaWppB8l39t9dem0t2jO6BdwpZ1xwyYvYBtSBVHZwbeuhLA/J78J8tgl+ByLPFbsOitdgY7FvhdDbKcyWl+Xp1dfU/+yAtmgZ1KIzrghXQqlu6bDbL7fT29cie+gAHGmxXWi3wMMmUHmwar+H9TXl5eRm9HdJrjBaNc+ZYra6eBWiwNXTtC/zKO6L45xyUu/rElc0z85z5ja2NofYbkHCXUIZfjbSM0YS8nJ1sCEPQ0NAA2RMmXBzvcwKGv2dBGAIr52MF4pdIKV0UdN4IYvTozJ+GI18okId5FTlXQesGmYC4n0XJEBQXF+s2my2HFOTNvV2dC+iiT6WDklfMNJ/yA9N18HGUxzgU+Cna3kFK852y0tLfMc4fp98ODFcLjQayBtbZ2TmdDNtt9DxdJxCyyQuyA3CfTgm1hYHuodSll8kXKQ2DrlkPGt4jdK6/OPzOO/Vbt251Wyjzj9ArO+hcEVMo66QQZQqGJDrjCSRTZtAiMTY6LS0trJaYUGjPz89wubu/LThU0sGjKgA94ynI4DqOmN1cMGUF7m//X9WJrIguYJhNoQkRlWJANaJsC+d3UqX/SyTXdBa6XnwfpChlXrLpReY7jQnjFipY20pKSn5QV1fXzGJQzioqKqb0dJ1apgF8iTz87AG5fH8jfSiNXpk+/Qr4xcmTJz9An92qjyAEqsixvnVGTp6bix9whE/RVymxOK6sbVAxuxg4PN5cmLvxkA0eCrepSKEYEkQMwcuOKKuXLMn06PqNVNteQa9cKgValA8pJ3lOpHP/rsZhQVnp8vWguX+xbt3mk9E4GBkAO/N4bhFezyry/CezAUUdc5QhCIEvz8y9RvjsAbt8oGEudvhnI8tmhZX9/TizeWb28qmNh0/EUgZFggDMbI0AhBa5GsEah2OMi/MNyHABeev2WJokOp4s19nIeA0atvmksL9bXV19PJLHKCsrSxZe70q6znf6m2/ihjIEJpD9Abq7eykCfps+jqEK45lg64ykvVPoz7DWnh54F/1xBy0QsC8i6NnNM/Pqpza2Pht0OoUiCCCEpiEL0yNy7PLykmv6BdSQZ36Zv+kmLtChbbT5Eno9eWVljpKamvoXI5GvrOm40Kii/L8dg1pOQJQhMA3s0kB8b+BtcCk8ArLoTtfS248Mvye+Jhiv17kIOAzxHF6yHFxYuoLdX6EwgelhoIKHXyNwOpfNZQK2UEa58W6a8iNr4ZdRUXtoRWnprfetX/9GOJnJkW69XV2LycjdFsg5jBXKEJhg5u7d0lvfbjZda8GUaYbGPBDAcnAGx09njH9ORSFVjASohioCPbPvJzyV4nQ6bgDGf0BvJ4WVURSga1EgAH/jdDq/VVtb+8dQ8+k5depzoPG7YQTUBM6hDIFCoRgcQf9M9oCJ0EcNQYXDMZMOWAsj0Aj8C8gBhusqKpbeXF29qd1s6mXLlk1gnDtYnDqFh0IZAoVCMSjAzfcRhMqKFSvyhMf9Y5DDN0c6iHOFYXnI4XDcVl9fb2qghq5r19M5fjrM4/chsP1UozhK1bYT9LkfOfOSzbYjAzt9HoUAcgj+FNo7qImLyhAMAS5kWvOunJs1w/PHvP1H3jNaQC4y8xsTE8e8XqGBFpQDAKOPH9e2Bdk2u2Bg/uD79m2emfs5YYij0w+0vzlIsi5ENO3JnCei3cxkKAkdr4c2IQ9zBaAHPrKQSPgubSMW3RWQnY5UXiMFeZHM9tOGMo9AThQzPJ5yensFhDFwnsSVZUHe0y7KZh9n2C7ke+CcvPhMKrhyIajppCzTaZscaie0P91nrRy+UVVVVR9sSA1f30B35zforekedf+5nUJkT9JN+W8q381CGF3+KA+yTxEpf8vp06eTaN9ki9eb6eF8PEecRvrqOjr/T6CcMzZEFGRlCIagdUfOZ+l2r/F42WsX/nZges6s2Rb2b8HmBQjjBMOUgE8dwlR3klg+uzAnqJFDrYz1N2PylvfHIBKXggWu3jVp0oJLDx06e/4vNevXV9OmOljZL6SspOSrVBK2mklD1/GH62rrVoR6zEhDBcVFZnxZTU3dE/GWZSQDMjpBlPtqZewluz35K0zgrf4hmyFBerKJ0v+WytqzqRkZ/yDlfHaw/WToBtpvPinGG0kx3gABB3AMiU7pl/X2dr5K7/8STIK+rq4CSjPHrPnxGQFgzzIwVtas27BnqFA6/lhX8iWNoZz30ESvP9PrMafTmUV64QYyjv8mkF0j4xadn1YZgkE4eOn0caLfXSmnnw/2O+hQSH743fQwBXn9ZHmCgF4A+VKzAbEweEnhsKFpcjr8ewwBIpfVj6vSRlnKm2fOrIpEsDqFIhpMHjMmmxRTKSmm1BCz6CG1+HP0ikdOezz7AsVA88cSeqaquPgPPfakJxhyJylacvrAtC4kOzkOBHx/6dKlb2zatClgjdDD2SdJb5g6Dvom9bHnuVcsrd6w8WBNzUazYvqora3toM3Dq5cs+ZXbar2CZF/G2b/OWRmCC9g/a+wow+VZT1f/E6Toe4faD5gvuFXQwfqCcQJAdiBB8CMJ6BEZel9gGv3+XYT+Fvr0eLB5KhSxBK2WRfTczwox+WFyju9KTc/8idmIp5WbN8saw4sOh2OHhbP1pNW/RmUv2UwesgZDevoGm65/gj4+H3B3wWaiyU5iOkavgezumg0bIrKE7T333y+baJ8n4/WK1cqvmNHb52t9UIbgPLbRTQJvytcR2AKI8YzhaEDnkUne0orWgil/zdvf3hRveRSK8ykrK5uNKL4SUusT4ikB/Ftnz/S9WF0Tetjr+vr6nlXFxU53sk32Yy0D84raSsq9uKJi0V+qqx8bss+JztUmhDeLAzd3uoiHMjIyI74eib8G888hsMoQnMelRbnzvAKpmspCraaOKPwRaqcLzje1FhR8M2///ohOkVcoQkX2DTAmFtAzmmc2LSnebnLTVtXW1P4+ErKs3by5m2oU93jc7kLK/boQOpE/KkTmXNq+NNQOVsLtMkKISQanglnnIlyUIfCzPz8/R6B4gEz85HjLEmkQ8GpDczt2zZu3Uk1WU4wEsrOz0xkat4YSXoG09DbgekSbO6uqqnorSkpWC87y6WOBqcSIY8njupGM21+2bt066JBbt9stz9P8aCHGJjgcjqRor0WhDAHxek5OMljFMqpfXhzO8LWRCj2kNnqiFo3qOipHNzwTb3kUClI8HzMYzDCdELGDGaK2unZ9xIb+nqO6rm5nRVnp44jsPjPpZA2C0nyxsLCwgrHBA/VZLBaP12OYXt+A6ibjdPBFODYd0cAMCW8I5JyAJjvcCYJ9y2z74AcKOfYf2abW2Xnv5Kk1kBVxRDYLGShuYiadLkR0A/BNh451RKu/CwXjPwM0bicNbLLJCvN6Tp26hN68b7i5JD8/v//AvoZQ5prYkcPqysrKNqq1RK2fL+ENQVNRzlxAVk6W1x5vWaINAssRXuFsnjnzjqmNjT3xlkeRmGRnZ49HFBebrXqT493BhXhuqOaXSOD1ek9aOHuBatF3MBOGytevwOEaNoQhWLRokafcWXLMP0nPzKnLnT9huN015eXlS9atW3fIRNqgSWhDIPsFOBo1TIaUTgD8y2jeyFh/e+OcOSv9QfQUipiC6C3ijJvuJKaUL1WtX//WfXV1kRfKj2yLLy8peYYB3kIlJsNk8o8O257P4a8gfBO+zPYVyLl9N4IwCspLS+/Rbbbn1q5d220yj2FJaEOgWY01dI0/FW85YopvuBt+2+rqkf0F/y/e4igSD0SWT89gutnBOYLxp4aaVRtJXIbxqhW04yRehpl0VOOeoOv6WHo7hNeu7WDMkIM1THca+2sRhXSMR9z9/c+Xly9btW7dxgNm8xmKhDQEu+bN01O7O/4TGXz5Q90vMARU7U0XXNzbNG1a+7Smpt3xlkeRWGgIHyPv2NQ8HTIeRzWLNljsrIizcePGd8tKShrk0Gsz6QBxAhjGBDaEIZDNOuWlJf9DWv2mUGUjfZVOf25Bw/L5MqfzKQ3xUXtG3+7Kys2DhtQIlhCmVZMYiElymbVwDjxk/oaRNBBLLXqDd1JOH7uYNkvpCIHPgWtJ7fn5tvO/cjHUR8K68fJe0MMn5XvPkFAXGJbAceR5Eeqe2j/n598wv6WlP4pijkRkXM3CMofjM+FmRI6a53R//+uBQhso/gUCFJov3diEiBEfKTQUZKb2MF8zqplEkG6w4ZecRM5+wQR+PoyQGv5DsTTKbZEA9rnerqS/OJ3OzemdnTsqt2yRzVKmlVMoNYJ0JmAroBGV9mUyAUl0GmnRsgMtMyZPQi8+RlcyP9C+JIKNvJCnPEy8Z+YiR5ZMclpGwDjTi7g4+wfPBYEuyNVKxwDRUX39BciuHp8kVryek1N9ZVub6aFtH1R8ERiBVZCxrAg3L1JOx+12+0x6eyoCoiUImG/a0QO+x+PxDBnyJeJwy9+ZMNcnTc+CnQcIYicEvGgBfIbK51fCku9fZNO1uRVQ3NSbkf5yudP5GGjadrPrK5s2BP4IgWOitYRcNJVr45ysVOHhy0gDFgQTVpH2SaLdBo2DMgKMgLwFcqbixYP8ElxsIxloC/H2jySLv9FHteaxIuosXbpUesKmFmr3h2A+VF9fH7PBDYZhvK0BM9We72vHR5Y13D4y6F1FheNe9MK1ZsO5Bzi21OVX09W6Ar3eXWWlpVv73e5fjx07tjuYOEwJ1Uegu23/FwEX+xS8YgCALIbawweK8j87fW/LvniLo/hwY9f1iYbJOF6k5LyCCTncOWbtsaQgXF7GZLu7OaPFZJPN8Nx3X11jhdNZSgaujs4t1DDYQ5FKXuB8cgTnJ1v1Rb1dXY9UVVX9cqiw3OdICEMg29JbZ0y5ioyAE5QReD/AxjMUq5pnZi+d2njY1IpLCoUZPACjuHl97gXksWsWIvoRvRZpCGD4Nv8LIcVuC2IfvOOOO3550ej0VCFYjb9mH3kArqC/hae7O79Y5nDUpI0e/Y+h4hYlhCFoK5oymyGUI8KUKLVofdDRAPFmxq37G+dkbZi5u+NDt9qWYmSgIaag+TJoaDy2hkDXdS8a3hBG4oigBtFs2bKlnzz1H3V3d9PlwFX01aBrn0SAVGRwI2gwr7e7u4oMwU9lXKULd0oIQ+AGq0sTnmlBNp0nJgBURsVlM8bO6mesI97SKD6kGCBs3HyEdzkhN+oROM+nr69P2Ky66UBvyHjQcpJSdi9cuHBLdvb4FhCwjsqg7I+M1nD2cXQN1xlu96VrHI6yuy9YazkhDMHxPU0tE2bkbEDAKtJ4AdvwEg30BShlbzLGV8D27TEtcIoEQw7EManqfIu5cIxO88kQWK1WjQHazLdiCVOj77Zu3So7wJ+rLClpMQAdpJ/kaKKo6CjZBEXG4Gv9GqQvW7bsexs3bnzn3G8JYQjmM+Y93M9+7EqBS+i+foMl4CSy4ZGLy/N78xtbVWexIqpwAPPNLYgaGiKmDhz3eCyoW+ym4yENdDCbpqqurqm4uHi53W7/FUexChleGkKIi4D4RxfdoFss7iVLliy+f2DFssQwBJLstra+psKcNRxhGgL7JAswzj5hQPQggx9Na2x9Ot6iKD78UHWzz6zSIeWlCcFjulgU2GwWZnhN10JQiK5Qj7nZv3xmaWnpLo2Jq0hF3Ukn/2nmG8QUUTTO8IakJL2hqqqqWnYgJ4whkExtaHunuTBnFd3mJ8hyTwqYAH0V2S524bA1QAspz/R4r10g202BQdcgvyQhQGog+eikBHlov+boWR8lEUcq8n7K9t/wm8GAnU1KSor/NPMPCLqun0TD3GVHZBYyBheR0uJm1yYOGY/HTtUXs4bAo3HLkXAPLeca0Oa3FRUV2w3DdQln2nfoKnySrsNk/zyu8AFIBsTynp4euc7BawllCEAqgIa2l5tn5tbT29rAi8+jtNBLKOV7Olbkwvb0p4IyNB08KpLQ+RwHZHdSDeeCEBG+WCaLWIAmMKp+viuYVjUtwYaMkgH1UCF/hAv2cviZMTcZgpiFPvigk5qa2tHT1en1N1EEBYAM48OmnjhxQirmmIxoMzifQV6zqSZk8gZOGwARC+9eXV0tn6tXyAD+nRT2JRzEAroOX6fvJkQif7oHKVSDWUz5v2E+1hBiL/1dzQSLTrAygMuokN4FLHrrBnusaT/WvT2T6By+P6wyByYMl+eVgpYjbed/faAoN5PjCAg2xMDjtdhfLNiz5z3DwZpm5M4NVFch8Y9y5HdMbWxpjKaEIxTyKvEf1evrtsVbkERDjpQpKy2VXvMUM+mAYZHVah3FYmQIAMWVIURPOMmY+2SkZZHXjDZ/W7hw4Y7x48dvsFosNwsUt5MyLoKAzmwg8Lrezs755kNMyDkhFr6zunp9+N7UIJSXlJCOZVEN4DVz9+7ThwtzNvYB3EDnMzWaxxqRIHo54M8sbngh3qIoEpJmZtIQEHk6ouwwjsXYZmkBBgndEgBkx3i/cSzy4gzgX5BH1t4fXOFwPC84v4F05RoIY5SRnNksAD+eUE1D55Pd0HaktSD3K4aGv6SaZ8AAdB8iBAK84NFda/Mb2hMt6qhiZPBXel1jKgVAshd90UBroiHQ+TidzkLS6rlm05FCbrrv/vtPVD/wQDTEeg/31de30GbDaqfzt/1MrOEDw05DaaoG8ry/kLCGQNI9evzutM6OjYJjfeKEnsBGel7K1exhRdzg2MAQZGe9uTIH7AsOh+Oh+vr6qC2zKleSLC93XkPu0lizTUPknb8ei4Vzzuee2trWZcuWLbfq+kmG4juy3d90JgCzE9oQXPrqqx6sqnqoeeuj8+ju3fphX6SGzrGLI797aqNajEYRPwyDNVgAD5ECMtUsS+WzSOf8KhbFlfWWLl2aYbPqt4TQ9u4CTfufqAgVALmQTnFxcXmqzeaii+Rk5vVYUkIbAglUVooDRXmrADGbFOWn4j0kNGogc9OJbbG44Zl4i6JIbDRNa2LC2M9M9s+Rt55Bz/GtpPRe8I+5jzjJyfpcJmCu2XTIcGfaoUNR6x8IhFwYaaXD8aCHwWepnH/UbPqENwSS6XtbDzYV5srAT0+y6AV/iicGAj7n1dPvmdq4W/ULKOJKTU1Nn9NZ8ixn7N/NpPPF+wd2U3pq8i9ZFGoFFRUVY9HwrGDmJ3B5AOGpFU884ancujXSYgXN2rq6I+VO55N0jS5hJvsLlCHwc7Th4KvjC/PXMzTuYmEuIzfSoJpOK6BWLUdLxVsWhUJiGPgscHY36faxJpPqlHZVRUlJa3Vd3Z5IybNw4UIrGYHvkrn5mOnEiO3Ite2B+geoJpNCnruMRRSVfgR5/LKyklYUzEXvlSEIBRmPqN3Ffuixsmn08VvsQ3Jt6Inr4QKd+ftaXou3LArFOYqKio41NTb+ip7PYmayOZbSXI4Am5YsWXLzuVg54UBGQJuclfUfiKyUJAm4nsD74PBCWlra3kC72e3Jt5c5HAfSRr/5QmVldII7ggAD5Vxsk3wolF2kmNLS0t8yI6dOMLyczOvlH/TOAoHYzxlseWdS27NMhZNTjCAWLVrkKS8p+TWVswVMrrtrAtlEhIifSbJaH6wsKVktA7aFKofD4Ugi1/lrCEy2BAS1lsD5kMY9qRn4UDChL6i+MI2MRnl395wt5eWX/6C6uvrdSI4ykgaNDjKbhDJ9HsoQXED+vrbmpsIcBz1pv2TDrByEgIJuYdBWnTwYLZjYPzCwPmvQ2Q73o4bsZV2H+vnbIxBTR6GIMIc6Ol6dNHHCb9lArcAUMuYOFaYvG8CnlJeXOEeNynxtqNW3hqKkpGSchfPvUJErpfzsZmUgY2SQJD+3d3QE72YBZHHGVjHD+FhZWcmDVVVVvzMr91CMHz8+hw5wC4Bpvd6vDMEg/Lqh7S+3FE5ZR2q5dLDfuWD/IA9iUbArLQHCeNqspNfo4ffE18lgPEzboGZWA3K3ZhiDLegtRWvVNf6dyW+1qlVmFCMSOVO2srLkAXqCP292KKkfGT7sY1ROt/V2d24rK1v+GGN6c1paWtdQHrpsp7fb7RNAiE9R6iVkBGaEGqaBDt6gWa0PVQ6sKWAuKbBrqbZ+xenuzj+VLl/+I+R8Z2Zm5olQgurJuQ8VFRUFILw1YiDshNkM3lSGYBAqyTO/0ab93NqPBVYLf9+Nmdp4UM7qawk2v9aCKdMMDZyBmkLJUzjckz7+v+T8BvNSn5+ROCVQu2dyQ2tbWPl8eLFQSbypzOmcEclMAb17R2V85FdRiJA5p6eraw3JG7HQK/SsvZ2anv5ozKJ5DkFbW0frxKys9cBwQyheORsoVGNo859M8JuAidfIKOwpKyl5m/I7QQr2JHq9qbJTmqrP40hHzgA05tL7AnoGuGmleQ7EPgG8Zl1VVajNUrKBIIPkuFGzaJ+juv3r3Z2dL5Q5HH8cbm3hC1njcIwpL3feCIiLBbKLIZQTAvZ7ZQiGoGhna+fhnJySv7UdMmvt447VZXl8cl6elzW2joDAeCMPGfkSBV5PpfmLkcxXMHi6oaHh15HM088skrUohD7AISGl8WpHR8dPmC8A3+BQtTLqS3zLWsHChQt/mj0x60r6eBsLY1KnjJtDm3+ny3QdKTdSpGgACoNxn8LX5AI356KehnliHgT+kKZpvw0rl3+RQvJ+hkSaD5yX9XZ3HSsrLd1Nt2Ynyd7OkR816JicC7l6iE77pNO7QuDsYheDTwOyTDqjpJDOCbGDbvN2ZQiGQS5mE28ZQkF2erOWoCssCYnfc4qsnsOoTUaEkDy9YcAoTZw0ODdtreRyjU6ns4qhmEanKReNCks2f8x+63mf37MNG8SXuMVS4w8THTH8RkoGkEsjUadRve0mX8chCsHl+hmyR0Lj8rw0HrHHAV5jFssOZQgUCkXE4CEYAkltbW3rCofjm4LD02QG5kRargghg8+/7EW2uK66+nisDuo3bKZHAgUCETupZnNvLRk0ZQgUCkUECX0AjIyoWVZWtoRqBg+Sv1sYQaEiAlm4RsHYnXV1dc3xliUCeOTQ8tT09DflB2UITPDWxIn25DT9hwjwcfoY9MQTGUScM8wM5CrRQ/a51O6jLU2FwUfABYbHgMG2/IaD1UEnUiiCAEJooeEitBrBOWpqal6qWLr0/whdfxKAXcZGRiBIcp7xWU3gspqB8M8faOhcBOmNJ/o8xtpqf6e0MgQmmH3kyJmm9JxKupQOKiS301ejgk0bTPMx5ZlOm/Tg8pP3k71Die7z6Gnbg5VDoYgm3gisKVW9aVN7+fLlX0eL5S56zG+KZ1RgKmT9VC6f0pOw4t576w/HS45IIdc5p83vdIS71m3a9M+QM8oQmGRaQ9uRP197rTPrcMsr5MPXUu0gB3yj8WKHb81dYP+LmnBO3/P2/lgeW6GIBes2bNhXXFz8Nbvd9neqKi9mAHmxPL7PawbWREagdlRGxhP+5SI/4GAX4/DomTN999Zs3tx9/i/KEITA/O2+OCFP758x5V1yVVaSMbgqZl4LYi89nD+19kP15NY2NVlMET3Mtw0hN8JrGjofGVqZjMED6Sn6S160lFH21zLfiJrojWr1G4AeqsE/zYDfn5aetjdScy2oBt9Decvw2bLjN2YRbOic3HQr9yNnd3NueY6u6/tGOylDEAYF+9r/1J6f/4bHiuuA4VcRgm8qMos//EQ7IF+Tt691Kwwz/luhiBehjhoaCmkMaPOPqqqqL3d3d5PDJRaygfDVkQ4XL1tb20lhPic83i1Hjh/f7V8jOGJ4hbjHArATOLteznUgezYmkvkPggw5/wYH/ni/x/OkXMBmqB2VIQiTKS0tXftnzSrV8MxuurkylvnESC9ugwP9zX/WAFbvamz9a358jUAfeRhHzSWB3uiIMgACnqaLZFKmqNAVaAcUwg0cjvnbauMGKbx3s7KyAintLpP3WngBIjb7+Xz8XvkfKyoqXieZNtOhPknXkowCTCGXVzpgyf5hlsEiy9QZSnuGavR76Hr8N/Maf0Bdb6/duDEq84fq6+vl8pxPL1269HmrFaYLtHxUA9/azZeSHDL8TDLdkCST5+HD/zzJ/Puo1nECEF4BIZ6kLxrouAH7NiyksXZQIQp6IQZSdqfJ+z1lVtCg8xfiJLkVv6fSHfSaAKQI3sjMzIyo9TZDwZ49UtE93FSYsxcR1tONuJxFqKmIrncvldqnAG1luY2NJyKRZzi4vN7tKZr2STNpDIsWdqjgoSCvTZSV3fmoZqQ8Ha1jBIsb4LSUZ7h90GLZZRXiC4YQcR0NY7hd/aRchy0z0O9ay3V9k5l8vQyj2qHqn8S1S76Ki4sfTk1NzSUleBmiUQQCJzOQcb3wIiozdmRgg4EFWkgfYj/9JtMeo+86hGCHaPuGAL6ntrb2IIvSGgGDsWmgk3an//XIqlWr0r39/XMEx3yq908kSSbR9+NIoDGka21kqGyAaCP5baQPyNBiH6dzoff9CKyT9jlCd+sg49iObqOZWa1719XWmDJmln6394E0xIeCTdBntWJaakbUFjg543Y3JGvJ30l2u4P2qj0pKe7FixfHuzMHpzW0vXygKO9WeijXkkW+nm5cyE1FOPBgHmGANYDJP5va2Bi1BbvN4H+IR9ICN1hT85B0TKLmnEQSuToXbdriLUcwVD/wgJw0FbOJU2bxNxsdkK+qqirOOjpsLl1PNlJSkrjLZfHqhgZU/5Lt/h7OjSTD69VGib6kpLN9K1b8yBXrheaHYu3atbLj9mX/yxcam3OeTHInJSUZmmEk0cvQdENo587FYvV6XS7N8Lg9bovFcra+vs4VjgyWTecNIRoJ+G9uV7zlCBW57OX+WWPv1Az7DmS4mix1uummIqrmyciG5AUsydvX/rLqD1AohsffdHTW/wpi/y3RFSgM/E1IYSl2s6g+gihQsOd476558zaP6joqZyCupOrb3KCHmKIvBPVPBDc2Tt97SC0no1Aooo4yBFHCH0r6mcYZua06w/vo/XUswDqiZCxOMGQ/Rki+a/rexph6BAqFInFRhiDKzNx3cO9bl0/8qq0vaSVDcRsAZF24j39o6Fv0piwvM+uPEO56BAqFQmECZQhiwOwdR868npNzz0es4hVm0R4nxZ/5z36DgVnCf+KCLc/d37YXWNuI6MBSKBSJgzIEMeLKgbUNnm0qnHI1IF89sCiKb3z9/VaX9oCcjxBnERUKRYKiDEGMmdbQvnt/bu73LMnsbUR802NN3zq1cXe8h74qFIoE5v8D7nUBQq5CdKwAAAAASUVORK5CYII="
									.getBytes("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					logger.error("Exception", e);
				}
				EmailUtil.sendHtmlEmail(message);
			};
		}.start();
	}

	public static void main(String[] args) {
		sendCancel(32l);
	}
}