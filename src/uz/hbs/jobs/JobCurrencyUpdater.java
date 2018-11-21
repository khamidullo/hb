package uz.hbs.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uz.hbs.beans.Currencies;
import uz.hbs.db.MyBatisHelper;
import uz.hbs.utils.NumberUtil;

public class JobCurrencyUpdater implements Job {
	private static Logger logger = LoggerFactory.getLogger(JobCurrencyUpdater.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the date and time that it is running
		if (context != null) {
			JobKey jobKey = context.getJobDetail().getKey();
			logger.info("Executing: " + jobKey);
		}
		/*
		 http://cbu.uz/uzc/arkhiv-kursov-valyut/veb-masteram/
		 Маълумот стандартлашган XML  кўринишида қуйидаги таркибий тузилишда берилади:
		
		<CBU_Curr>  –  Энг юқори даражани белгиловчи калитли тег;
		<CcyNtry>  – Валютанинг сонли коди. Масалан: 840, 978, 643 ва бошқалар; 
		<Ccy>  – Валютанинг рамзли коди (альфа-3). Масалан: USD, EUR, RUB ва бошқалар; 
		<CcyNm_RU> – Валютанинг рус тилидаги номи;
		<CcyNm_UZ> – Валютанинг ўзбек (кириллица) тилидаги номи;
		<CcyNm_UZC> – Валютанинг ўзбек (лотин) тилидаги номи;
		<CcyNm_EN> – Валютанинг инглиз тилидаги номи;
		<CcyMnrUnts> – Курснинг каср қисми узунлиги;
		<Nominal> – Валютанинг бирликлар сони;
		<Rate> – Валюта курси;
		<Date> – Курснинг амал қилиш санаси;
		
		<?xml version="1.0" encoding="UTF-8"?>
		<CBU_Curr name="CBU Currency XML by ISO 4217">
			<CcyNtry ID="840">
				<Ccy>USD</Ccy>
		    	<CcyNm_RU>Доллар США</CcyNm_RU>
		    	<CcyNm_UZ>AQSh dollari</CcyNm_UZ>
		    	<CcyNm_UZC>АҚШ доллари</CcyNm_UZC>
		    	<CcyNm_EN>U.S. Dollar</CcyNm_EN>
		    	<CcyMnrUnts>2</CcyMnrUnts>
		    	<Nominal>1</Nominal>
		    	<Rate>2994.76</Rate>
		    	<date>06.09.2016</date>
			</CcyNtry>
		</CBU_Curr>
		 */

		try {
			URL url = new URL("http://cbu.uz/uzc/arkhiv-kursov-valyut/xml");
			URLConnection conn = url.openConnection();

			InputStream is = conn.getInputStream();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(is);
			Element root = doc.getRootElement();
			List<Element> children = root.getChildren();
			String date_act = null, rate = null;
			for (Element child : children) {
				if (child.getChildTextTrim("Ccy").equalsIgnoreCase("USD")) {
					date_act = child.getChildTextTrim("date");
					rate = child.getChildTextTrim("Rate");
				}
			}
			is.close();

			if (rate != null && !rate.equals("")) {
				if (NumberUtil.isNumber(rate)) {
					Map<String, Serializable> params = new HashMap<String, Serializable>();
					params.put("id", Currencies.ID_UZS);
					Currencies usd = new MyBatisHelper().selectOne("selectCurrencies", params);
					if (usd != null) {
						double value = Double.parseDouble(rate);
						if (usd.getValue() != value) {
							new MyBatisHelper().insert("insertCurrenciesHistory", usd);
							logger.info("Currency is backed up, " + usd.toString());

							usd.setValue(value);
							new MyBatisHelper().update("updateCurrencies", usd);
							logger.info("Currency is updated successfully, Rate=" + rate + ", DateAct=" + date_act);
						} else {
							logger.info("Currency is up to date, Rate=" + rate + ", DateAct=" + date_act);
						}
					} else {
						logger.error("Currency is not update, USD not found in DB, Rate=" + rate + ", DateAct=" + date_act);
					}
				} else {
					logger.error("Currency is not update, rate is not number, Rate=" + rate + ", DateAct=" + date_act);
				}
			} else {
				logger.error("Currency is not update, rate is empty, Rate=" + rate + ", DateAct=" + date_act);
			}
		} catch (IOException e) {
			logger.error("Error, >>>>>>>>>>>>>>>>>>>>>> Couldn't connect to http://cbu.uz/uzc/arkhiv-kursov-valyut/xml <<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	public static void main(String[] args) throws JobExecutionException {
		JobCurrencyUpdater instance = new JobCurrencyUpdater();
		instance.execute(null);
	}
}
